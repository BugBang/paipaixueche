package com.session.dgjp.order;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.common.BaseRequest;
import com.session.common.BaseRequestTask;
import com.session.common.BaseResponse;
import com.session.common.BaseResponseData;
import com.session.common.utils.DateUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.NumberUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.HomeActivity;
import com.session.dgjp.R;
import com.session.dgjp.enity.Coupon;
import com.session.dgjp.enity.Order;
import com.session.dgjp.enity.PayType;
import com.session.dgjp.personal.SetPayPasswordActivity;
import com.session.dgjp.request.CouponListRequestData;
import com.session.dgjp.request.GetOrderPayStatusRequestData;
import com.session.dgjp.request.OperateOrderRequestData;
import com.session.dgjp.request.PayTypeListRequestData;
import com.session.dgjp.response.CouponListResponseData;
import com.session.dgjp.response.PayTypeListResponseData;
import com.session.dgjp.trainer.PayPasswordVerificationDialog;
import com.session.dgjp.trainer.PayPasswordVerificationDialog.VerificationListener;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderPaymentActivity extends BaseActivity implements VerificationListener, OnItemClickListener {
    private PayPasswordVerificationDialog payPasswordVerificationDialog;
    private Order order;
    private PayTypeAdapter payTypeAdapter;
    private ListView couponListView;
    private CouponAdapter couponAdapter;
    private CountDownTimer countDownTimer;
    private String yuanStr;
    private Resources resources;
    private TextView couponTv, preferentialAmountTv, settlementAmountTv;
    private final static int GET_DATA_FAIL = 1;
    private final static int GET_PAY_TYPE_SUCCESS = 2;
    private final static int GET_PAY_TYPE_FAIL = 3;
    private final static int PAY_SUCCESS = 4;
    private final static int PAY_FAIL = 5;
    private final static int GET_COUPON_LIST_SUCCESS = 6;
    private final static int GET_COUPON_LIST_FAIL = 7;
    private final static int ALIPAY_FLAG = 8;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PAY_TYPE_SUCCESS:
                    PayType defaultPayType = null;// 默认的支付方式
                    // 在集合中查找余额支付，找到就将其设为默认
                    List<PayType> payTypes = (List<PayType>) msg.obj;
                    for (int i = 0; i < payTypes.size(); i++) {
                        PayType item = payTypes.get(i);
                        if (PayType.YUE.equals(item.getId())) {
                            defaultPayType = item;
                            break;
                        }
                    }
                    if (defaultPayType == null) {
                        defaultPayType = new PayType();
                        defaultPayType.setId(PayType.YUE);
                    }
                    payTypeAdapter.setList(payTypes);
                    payTypeAdapter.setSelectedItem(defaultPayType);
                    payTypeAdapter.notifyDataSetChanged();
                    break;
                case GET_DATA_FAIL:
                case GET_COUPON_LIST_FAIL:
                case GET_PAY_TYPE_FAIL: {
                    BaseResponse baseResponse = (BaseResponse) msg.obj;
                    if (baseResponse != null) {
                        if (baseResponse.toLogin()) {
                            toLogin();
                        } else {
                            finish();
                        }
                        toast(baseResponse.getMsg(), R.string.query_datas_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.query_datas_fail, Toast.LENGTH_SHORT);
                        finish();
                    }
                    break;
                }

                case PAY_SUCCESS:
                    toast((String) msg.obj, R.string.operation_success, Toast.LENGTH_SHORT);
                    toOrderDetail();
                    break;
                case PAY_FAIL: {
                    BaseResponse baseResponse = (BaseResponse) msg.obj;
                    if (baseResponse != null) {
                        if (baseResponse.toLogin()) {
                            toLogin();
                        }
                        toast(baseResponse.getMsg(), R.string.pay_fail, Toast.LENGTH_SHORT);
                    } else {
                        toast(R.string.pay_fail, Toast.LENGTH_SHORT);
                    }
                    break;
                }
                case GET_COUPON_LIST_SUCCESS: {
                    List<Coupon> coupons = (List<Coupon>) msg.obj;
                    if (coupons != null && !coupons.isEmpty()) {
                        couponTv.setText(getString(R.string.useable_coupon, coupons.size()));
                        couponTv.setVisibility(View.VISIBLE);
                        couponListView.setVisibility(View.VISIBLE);
                        //设置listview的高度
                        couponAdapter.setList(coupons);
                        int size = 0;
                        size = couponAdapter.getCount() < 4 ? couponAdapter.getCount() : 3;
                        couponListView.getLayoutParams().height = (resources.getDimensionPixelSize(R.dimen.view_39) + resources.getDimensionPixelSize(R.dimen.divider)) * size;
                        couponAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case ALIPAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // toastShort("支付成功");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        toastShort("支付取消");
                    } else {
                        toastShort("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.order_payment_activity);
        order = (Order) getIntent().getSerializableExtra(Order.class.getName());
        initTitle(R.string.order_payment, true);
        ListView payTypeListView = (ListView) findViewById(R.id.list_view);
        payTypeAdapter = new PayTypeAdapter(this, null);
        payTypeListView.setAdapter(payTypeAdapter);
        payTypeListView.setOnItemClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        int originalFee = order.getOriginalFee();
        int preferentialFee = order.getPreferentialFee();

        TextView originalFeeTv = (TextView) findViewById(R.id.original_fee);
        originalFeeTv.setText(getString(R.string.formated_money, originalFee / 100.0));
        TextView preferentialFeeTv = (TextView) findViewById(R.id.preferential_fee);
        resources = getResources();
        SpannableString ss1 = new SpannableString(NumberUtil.getMoneyFormat(preferentialFee / 100f));
        ForegroundColorSpan fcs1 = new ForegroundColorSpan(resources.getColor(R.color.red));
        ss1.setSpan(fcs1, 0, ss1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        preferentialFeeTv.append(ss1);
        yuanStr = getString(R.string.yuan);
        SpannableString ss2 = new SpannableString(yuanStr);
        ForegroundColorSpan fcs2 = new ForegroundColorSpan(resources.getColor(R.color.black_333333));
        ss2.setSpan(fcs2, 0, ss2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        preferentialFeeTv.append(ss2);
        preferentialAmountTv = (TextView) findViewById(R.id.preferential_amount);
        settlementAmountTv = (TextView) findViewById(R.id.settlement_amount);
        refreshSettlementAmount(0);
        couponTv = (TextView) findViewById(R.id.coupon);
        couponListView = (ListView) findViewById(R.id.coupon_list_view);
        couponAdapter = new CouponAdapter(this, null);
        couponListView.setAdapter(couponAdapter);
        couponListView.setOnItemClickListener(this);
        final TextView countDownTv = (TextView) findViewById(R.id.count_down);
        countDownTimer = new CountDownTimer(order.getRemainTime() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                /*countDownTv.setText("");
				SpannableString ss1 = new SpannableString("你还有 ");
				ForegroundColorSpan fcs1 = new ForegroundColorSpan(resources.getColor(R.color.gray_888888));
				ss1.setSpan(fcs1, 0, ss1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				countDownTv.append(ss1);
				SpannableString ss2 = new SpannableString(DateUtil.LOCAL_SIMPLE_TIME_SDF.format(new Date(millisUntilFinished)));
				ForegroundColorSpan fcs2 = new ForegroundColorSpan(resources.getColor(R.color.red));
				ss2.setSpan(fcs2, 0, ss2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				countDownTv.append(ss2);
				SpannableString ss3 = new SpannableString(" 进行支付");
				ForegroundColorSpan fcs3 = new ForegroundColorSpan(resources.getColor(R.color.gray_888888));
				ss3.setSpan(fcs3, 0, ss3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				countDownTv.append(ss3);*/
                countDownTv.setText(DateUtil.NETWORK_SIMPLE_MIN_SEC_SDF.format(new Date(millisUntilFinished)));
                order.setRemainTime((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                toast("支付超时，该订单已取消", Toast.LENGTH_SHORT);
                finish();
            }
        };
        countDownTimer.start();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
            case R.id.ivTitleLeft:
                onBackPressed();
                break;
            case R.id.confirm:
                PayType payType = payTypeAdapter.getSelectedItem();
                if (payType == null) {
                    toast(R.string.please_select_pay_type, Toast.LENGTH_SHORT);
                    return;
                }

                if (order.getFee() == 0 && !PayType.YUE.equals(payType.getId())) {
                    toast(R.string.please_select_yue, Toast.LENGTH_SHORT);
                    List<PayType> payTypes = payTypeAdapter.getList();
                    for (int i = 0; i < payTypes.size(); i++) {
                        PayType item = payTypes.get(i);
                        if (PayType.YUE.equals(item.getId())) {
                            payTypeAdapter.setSelectedItem(item);
                            payTypeAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    return;
                }

                if (PayType.YUE.equals(payType.getId())) {
                    if (order.getFee() > payType.getBalance()) {
                        toast(R.string.balance_insufficient, Toast.LENGTH_SHORT);
                        return;
                    }
                    if ("N".equals(account.getIsSetPayPwd())) {
                        //用户没有设置支付密码，跳转到支付密码设置界面
                        toast(R.string.set_password_hint, Toast.LENGTH_SHORT);
                        startActivity(new Intent(ctx, SetPayPasswordActivity.class));
                        return;
                    }
                    if (payPasswordVerificationDialog == null) {
                        payPasswordVerificationDialog = new PayPasswordVerificationDialog(this);
                        payPasswordVerificationDialog.setVerificationListener(this);
                    }
                    payPasswordVerificationDialog.setFee(order.getFee());
                    payPasswordVerificationDialog.show();
                } else if (PayType.ALIPAY.equals(payType.getId())) {
                    payByAlipay();
                } else if (PayType.WEIXIN.equals(payType.getId())) {
                    payByWeChat();
                } else {

                }
                break;

            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (payPasswordVerificationDialog != null && payPasswordVerificationDialog.isShowing()) {
            payPasswordVerificationDialog.dismiss();
        }
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onVerifySuccess() {
        pay();
    }

    private void pay() {
        progressDialog.setMessage(getText(R.string.paying));
        progressDialog.show();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OperateOrderRequestData requestData = new OperateOrderRequestData();
                    requestData.setFee(order.getFee());
                    requestData.setPayType(payTypeAdapter.getSelectedItem().getId());
                    requestData.setOperateType(OperateOrderRequestData.PAYMENT);
                    requestData.setId(order.getId());
                    Coupon coupon = couponAdapter.getSelectedCoupon();
                    if (coupon != null) {
                        requestData.setStudentCouponId(coupon.getId());
                    }
                    BaseRequest baseRequest = new BaseRequest(requestData);
                    BaseResponse<BaseResponseData> baseResponse = baseRequest.sendRequest(BaseResponseData.class);
                    Message msg = Message.obtain();
                    if (baseResponse.getCode() == BaseResponse.CODE_SUCCESS) {
                        msg.what = PAY_SUCCESS;
                        msg.obj = baseResponse.getMsg();
                    } else {
                        msg.what = PAY_FAIL;
                        msg.obj = baseResponse;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(PAY_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onVerifyFail() {

    }

    private void getData() {
        progressDialog.setMessage(getText(R.string.querying));
        progressDialog.show();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取支付类型
                    PayTypeListRequestData requestData = new PayTypeListRequestData();
                    BaseRequest request = new BaseRequest(requestData);
                    BaseResponse<PayTypeListResponseData> baseResponse = request.sendRequest(PayTypeListResponseData.class);
                    Message msg = Message.obtain();
                    if (baseResponse.getCode() == BaseResponse.CODE_SUCCESS) {
                        msg.what = GET_PAY_TYPE_SUCCESS;
                        msg.obj = baseResponse.getResponseData().getList();
                    } else {
                        msg.what = GET_PAY_TYPE_FAIL;
                        msg.obj = baseResponse;
                    }
                    handler.sendMessage(msg);
                    //获取可用的优惠券
                    CouponListRequestData couponListRequestData = new CouponListRequestData();
                    couponListRequestData.setBranchSchoolId(order.getTrainer().getBranchSchool().getId());
                    couponListRequestData.setIsUseable(Coupon.USEABLE_Y);
                    couponListRequestData.setPreferentialFee(order.getPreferentialFee());
                    request.setRequestData(couponListRequestData);
                    BaseResponse<CouponListResponseData> couponResponse = request.sendRequest(CouponListResponseData.class);
                    msg = Message.obtain();
                    if (couponResponse.getCode() == BaseResponse.CODE_SUCCESS) {
                        msg.what = GET_COUPON_LIST_SUCCESS;
                        msg.obj = couponResponse.getResponseData().getList();
                    } else {
                        msg.what = GET_COUPON_LIST_FAIL;
                        msg.obj = couponResponse;
                    }
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    handler.sendEmptyMessage(GET_DATA_FAIL);
                    LogUtil.e(TAG, e.toString(), e);
                } finally {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.list_view) {
            PayType item = payTypeAdapter.getItem(position);
            // 如果当前item处于非选中状态，这将原来处于选中状态的item设非选中状态，当前item设为选中状态
            // 如果当前item处于选中状态，则不做任何修改
            if (!item.equals(payTypeAdapter.getSelectedItem())) {
                payTypeAdapter.setSelectedItem(item);
                payTypeAdapter.notifyDataSetChanged();
            }
        } else {
            Coupon coupon = couponAdapter.getItem(position);
            if (coupon.equals(couponAdapter.getSelectedCoupon())) {
                //点击已选中优惠券
                couponAdapter.setSelectedCoupon(null);
                refreshSettlementAmount(0);
            } else {
                couponAdapter.setSelectedCoupon(coupon);
                refreshSettlementAmount(coupon.getDiscountPrice());
            }
            couponAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        BaseDialog dialog = new BaseDialog.Builder(this).setTitle("确认订单").setMessage("您确定要取消本次支付吗？取消支付后可以在我的预约订单中再次进行支付，超过20分钟未进行支付则自动取消该订单。").setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                OrderPaymentActivity.super.onBackPressed();
            }
        }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();
        dialog.show();
    }

    private String payType;
    private IWXAPI api;

    /**
     * 微信支付
     */
    private void payByWeChat() {
        api = WXAPIFactory.createWXAPI(ctx, null);
        api.registerApp(Constants.APPID);
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (isPaySupported) {
            payType = "WEIXIN";
            operateOrder(payType);
        } else {
            toastShort("无法进行支付，请安装最新版本的微信");
        }
    }

    /**
     * 支付宝支付
     */
    private void payByAlipay() {
        payType = "ALIPAY";
        operateOrder(payType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (payType != null && account != null && order != null) {
            getOrderPayStatus();
        }
    }

    /**
     * 查询订单支付状态
     */
    private void getOrderPayStatus() {
        GetOrderPayStatusRequestData requestData = new GetOrderPayStatusRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setId(order.getId());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {

            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            JSONObject json = new JSONObject(response);
                            if (json != null && json.has("status")) {
                                String status = json.optString("status");
                                if ("S".equals(status)) {
                                    toastShort("订单已支付成功");
                                    toOrderDetail();
                                }
                            }
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getOrderPayStatus();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.request(Constants.URL_GET_ORDER_PAY_STATUS, data, null, true);
    }

    /**
     * 操作订单
     */
    private void operateOrder(final String payType) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍候...", false);
        OperateOrderRequestData requestData = new OperateOrderRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setId(order.getId());
        requestData.setOperateType(OperateOrderRequestData.PAYMENT);
        requestData.setPayType(payType);
        requestData.setFee(order.getFee());
        Coupon coupon = couponAdapter.getSelectedCoupon();
        if (coupon != null) {
            requestData.setStudentCouponId(coupon.getId());
        }
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                logI("response="+response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            JSONObject json = new JSONObject(response);
                            if (json != null && json.has("desc")) {
                                if ("WEIXIN".equals(payType)) { // 微信支付
                                    JSONObject desc = json.optJSONObject("desc");
                                    PayReq req = new PayReq();
                                    req.appId = desc.getString("appid");
                                    req.partnerId = desc.getString("partnerid");
                                    req.prepayId = desc.getString("prepayid");
                                    req.nonceStr = desc.getString("noncestr");
                                    req.timeStamp = desc.getString("timestamp");
                                    req.packageValue = desc.getString("package");
                                    req.sign = desc.getString("sign");
                                    req.extData = order.getId();
                                    boolean sendReq = api.sendReq(req);
                                    if (sendReq) {
                                        toastShort("请稍候...");
                                    } else {
                                        toastShort("无法进行支付，请检查你的网络或稍后重试");
                                    }
                                } else if ("ALIPAY".equals(payType)) { // 支付宝支付
                                    final String orderInfo = json.optString("desc");
                                    Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            PayTask alipay = new PayTask(OrderPaymentActivity.this);
                                            Map<String, String> result = alipay.payV2(orderInfo, true);
                                            Message msg = new Message();
                                            msg.what = ALIPAY_FLAG;
                                            msg.obj = result;
                                            handler.sendMessage(msg);
                                        }
                                    };
                                    Thread payThread = new Thread(payRunnable);
                                    payThread.start();
                                }
                            }
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            operateOrder(payType);
                            break;
                        default:
                            toastShort(msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("网络异常，请稍后重试");
                }
            }
        }.request(Constants.URL_OPERATE_ORDER, data, progressDialog, true);
    }

    /**
     * 支付成功后返回主页
     *
     * @author YJ Liang
     * 2016  下午3:09:38
     */
    private void toOrderDetail() {
        LocalBroadcastManager.getInstance(OrderPaymentActivity.this).sendBroadcast(new Intent(HomeActivity.ACTION_ORDER_SUCCESS));
        Intent intent = new Intent(this, PaidOrderDetailActivity.class);
        intent.putExtra(Order.ID, order.getId());
        intent.putExtra(PaidOrderDetailActivity.NEW_ORDER, true);
        startActivity(intent);
        finish();
    }


    private void refreshSettlementAmount(int discountPrice) {
        int fee = order.getPreferentialFee() - discountPrice;
        if (fee < 0) {
            fee = 0;
        }
        order.setFee(fee);
        preferentialAmountTv.setText(getString(R.string.formated_money, (order.getOriginalFee() - fee) / 100.0));
        settlementAmountTv.setText("");
        SpannableString ss1 = new SpannableString(NumberUtil.getMoneyFormat(fee / 100f));
        ForegroundColorSpan fcs1 = new ForegroundColorSpan(resources.getColor(R.color.red));
        ss1.setSpan(fcs1, 0, ss1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        settlementAmountTv.append(ss1);
        SpannableString ss2 = new SpannableString(yuanStr);
        ForegroundColorSpan fcs2 = new ForegroundColorSpan(resources.getColor(R.color.black_333333));
        ss2.setSpan(fcs2, 0, ss2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        settlementAmountTv.append(ss2);
    }
}
