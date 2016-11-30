package com.session.dgjp.sign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.session.common.BaseFragment;
import com.session.common.BaseRequestTask;
import com.session.common.utils.AnimUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.request.OperateOrderRequestData;
import com.session.dgjp.request.SignPersonMsgRequsetData;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by user on 2016-11-17.
 */
public class EnterPersonInformationFragment2 extends BaseFragment {


    private ImageView mIvBack, mIvWecharpay, mIvAlipay, mIvBalancepay, mIvC1, mIvC2, mIvDriveTypeArr;
    private TextView mTvTitle, mTvCity, mTvSchool, mTvClass, mTvNeedPay, mTvDriveType;
    private Button mBtPayNow;
    private LinearLayout mLlItemDriveType, mLlDriveType;
    private String mName, mAccount, mIdCard, mQqNum, mEmail, mPhone, mAddress;
    private int mDriveTypeHight;
    public static final int C1 = 0;
    public static final int C2 = 1;

    public static final int WE_CHAT_PAY = 3;
    public static final int ALI_PAY = 4;
    public static final int BALANCE_PAY = 5;

    public int mCurrentPayType = -1;
    public int mCurrentDriveType = -1;
    private boolean isShowDriveType;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case PAY_SUCCESS://支付成功
//                    toast((String) msg.obj, R.string.operation_success, Toast.LENGTH_SHORT);
//
//                    break;
//                case PAY_FAIL: {//支付失败
//                    BaseResponse baseResponse = (BaseResponse) msg.obj;
//                    if (baseResponse != null) {
//                        if (baseResponse.toLogin()) {
//                            toLogin();
//                        }
//                        toast(baseResponse.getMsg(), R.string.pay_fail, Toast.LENGTH_SHORT);
//                    } else {
//                        toast(R.string.pay_fail, Toast.LENGTH_SHORT);
//                    }
//                    break;
//                }
                case 0: {
//                    @SuppressWarnings("unchecked")
//                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//                    String resultStatus = payResult.getResultStatus();
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        // toastShort("支付成功");
//                    } else if (TextUtils.equals(resultStatus, "6001")) {
//                        toastShort("支付取消");
//                    } else {
//                        toastShort("支付失败");
//                    }
                    break;
                }
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public static EnterPersonInformationFragment2 newInstance() {
        return new EnterPersonInformationFragment2();
    }

    @Override
    protected int getContentRes() {
        return R.layout.enter_person_information_fragment2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments!=null){
            mName = arguments.getString(EnterPersonInformationFragment1.Name);
            mAccount = arguments.getString(EnterPersonInformationFragment1.ACCOUNT);
            mIdCard = arguments.getString(EnterPersonInformationFragment1.IDCARD);
            mQqNum = arguments.getString(EnterPersonInformationFragment1.QQ);
            mEmail = arguments.getString(EnterPersonInformationFragment1.EMAIL);
            mPhone = arguments.getString(EnterPersonInformationFragment1.PHONE);
            mAddress = arguments.getString(EnterPersonInformationFragment1.ADDRESS);

        }
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mTvCity = (TextView) view.findViewById(R.id.tv_city);
        mIvC1 = (ImageView) view.findViewById(R.id.iv_c1);
        mIvC2 = (ImageView) view.findViewById(R.id.iv_c2);
        mTvSchool = (TextView) view.findViewById(R.id.tv_school);
        mTvClass = (TextView) view.findViewById(R.id.tv_class);
        mIvWecharpay = (ImageView) view.findViewById(R.id.iv_wecharpay);
        mIvDriveTypeArr = (ImageView) view.findViewById(R.id.iv_drive_type_arr);
        mIvAlipay = (ImageView) view.findViewById(R.id.iv_alipay);
        mIvBalancepay = (ImageView) view.findViewById(R.id.iv_balancepay);
        mTvNeedPay = (TextView) view.findViewById(R.id.tv_need_pay);
        mBtPayNow = (Button) view.findViewById(R.id.bt_pay_now);
        mTvDriveType = (TextView) view.findViewById(R.id.tv_drive_type);
        mLlItemDriveType = (LinearLayout) view.findViewById(R.id.ll_item_drive_type);
        mLlDriveType = (LinearLayout) view.findViewById(R.id.ll_drive_type);

        mIvBack.setOnClickListener(this);
        mIvC1.setOnClickListener(this);
        mIvC2.setOnClickListener(this);
        mIvWecharpay.setOnClickListener(this);
        mIvAlipay.setOnClickListener(this);
        mIvBalancepay.setOnClickListener(this);
        mBtPayNow.setOnClickListener(this);
        mLlItemDriveType.setOnClickListener(this);
        initView();
    }

    private void initView() {
        mLlDriveType.post(new Runnable() {
            @Override
            public void run() {
                mDriveTypeHight = mLlDriveType.getMeasuredHeight();
                AnimUtil.ValueAnimator(mDriveTypeHight, 0, mLlDriveType, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                removeFragment();
                break;
            case R.id.iv_c1:
                if (mCurrentDriveType != C1) {
                    mCurrentDriveType = C1;
                    setDriveTypeState();
                }
                break;
            case R.id.iv_c2:
                if (mCurrentDriveType != C2) {
                    mCurrentDriveType = C2;
                    setDriveTypeState();
                }
                break;
            case R.id.iv_wecharpay:
                if (mCurrentPayType != WE_CHAT_PAY) {
                    mCurrentPayType = WE_CHAT_PAY;
                    setPayTypeState();
                }
                break;
            case R.id.iv_alipay:
                if (mCurrentPayType != ALI_PAY) {
                    mCurrentPayType = ALI_PAY;
                    setPayTypeState();
                }
                break;
            case R.id.iv_balancepay:
                if (mCurrentPayType != BALANCE_PAY) {
                    mCurrentPayType = BALANCE_PAY;
                    setPayTypeState();
                }
                break;
            case R.id.bt_pay_now:
//                payNow();
                operateOrder("WEIXIN");
                break;
            case R.id.ll_item_drive_type:
                setDriveTypeSpaceState();
                break;
        }
    }

    private String payType;
    private IWXAPI api;

    /**
     * 微信支付
     */
    private void payByWeChat() {
        api = WXAPIFactory.createWXAPI(act, null);
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
    /**
     * 操作订单
     */
    private void operateOrder(final String payType) {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍候...", false);
        OperateOrderRequestData requestData = new OperateOrderRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setId("1");
        requestData.setOperateType(OperateOrderRequestData.PAYMENT);
        requestData.setPayType(payType);
        requestData.setFee(1000);
//        Coupon coupon = couponAdapter.getSelectedCoupon();
//        if (coupon != null) {
//            requestData.setStudentCouponId(coupon.getId());
//        }
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
//                                    req.extData = order.getId();
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
                                            PayTask alipay = new PayTask(act);
                                            Map<String, String> result = alipay.payV2(orderInfo, true);
                                            Message msg = new Message();
                                            msg.what = 0;
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



        private void payNow() {
            ProgressDialog progressDialog = buildProcessDialog(null, "........", false);
            SignPersonMsgRequsetData requestData = new SignPersonMsgRequsetData();
            requestData.setName(mName);
            requestData.setAccount(mAccount);
            requestData.setIdcard(mIdCard);
            requestData.setQq(mQqNum);
            requestData.setEmail(mEmail);
            requestData.setPhone(mPhone);
            requestData.setAddress(mAddress);


            String data = new Gson().toJson(requestData);
            new BaseRequestTask() {
                @Override
                protected void onResponse(int code, String msg, String response) {
                    try {
                        switch (code) {
                            case BaseRequestTask.CODE_SUCCESS:
                                toastLong("CODE_SUCCESS");
                                logI("CODE_SUCCESS");
                                logI("response="+response);
                                break;
                            case BaseRequestTask.CODE_SESSION_ABATE:
                                toastLong("CODE_SESSION_ABATE");
                                logI("CODE_SESSION_ABATE");
                                logI("response="+response);
                                break;
                            default:
                                logI("default="+msg);
                                toastShort(msg);
                                logI("response="+response);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toastShort("网络异常，请稍后重试");
                    } finally {

                    }
                }
            }.request(Constants.URL_SIGN_PERSON_MAS, data, progressDialog, true);
        }

    private void setDriveTypeSpaceState() {
        if (!isShowDriveType) {
            isShowDriveType = !isShowDriveType;
            AnimUtil.ValueAnimator(0, mDriveTypeHight, mLlDriveType, 500);
            AnimUtil.RotationAnimator(0f, 180f, 0, 0, 0, 0, mIvDriveTypeArr, 500);
        } else {
            isShowDriveType = !isShowDriveType;
            AnimUtil.ValueAnimator(mDriveTypeHight, 0, mLlDriveType, 500);
            AnimUtil.RotationAnimator(180f, 0f, 0, 0, 0, 0, mIvDriveTypeArr, 500);
        }
    }

    private void setDriveTypeState() {
        if (mCurrentDriveType == C1) {
            mIvC1.setBackgroundResource(R.drawable.type_circle_select);
            mIvC2.setBackgroundResource(R.drawable.type_circle_normal);
            mTvDriveType.setText("C1 手动档");
        } else {
            mIvC1.setBackgroundResource(R.drawable.type_circle_normal);
            mIvC2.setBackgroundResource(R.drawable.type_circle_select);
            mTvDriveType.setText("C2 自动档");
        }
    }

    private void setPayTypeState() {
        if (mCurrentPayType == WE_CHAT_PAY) {
            mIvWecharpay.setBackgroundResource(R.drawable.type_circle_select);
            mIvAlipay.setBackgroundResource(R.drawable.type_circle_normal);
            mIvBalancepay.setBackgroundResource(R.drawable.type_circle_normal);
        } else if (mCurrentPayType == ALI_PAY) {
            mIvWecharpay.setBackgroundResource(R.drawable.type_circle_normal);
            mIvAlipay.setBackgroundResource(R.drawable.type_circle_select);
            mIvBalancepay.setBackgroundResource(R.drawable.type_circle_normal);
        } else {
            mIvWecharpay.setBackgroundResource(R.drawable.type_circle_normal);
            mIvAlipay.setBackgroundResource(R.drawable.type_circle_normal);
            mIvBalancepay.setBackgroundResource(R.drawable.type_circle_select);
        }
    }
}
