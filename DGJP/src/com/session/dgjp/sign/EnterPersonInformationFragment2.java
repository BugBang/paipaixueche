package com.session.dgjp.sign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
import com.session.dgjp.enity.SignOrder;
import com.session.dgjp.enity.SignStudent;
import com.session.dgjp.request.GetOrderPayStatusRequestData;
import com.session.dgjp.request.OperateOrderRequestData;
import com.session.dgjp.request.UpDataSignInfoRequestData;
import com.session.dgjp.view.SignPayDetailDialog;
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
    private int mDriveTypeHight;
    public static final int C1 = 1;
    public static final int C2 = 2;

    public static final int WE_CHAT_PAY = 3;
    public static final int ALI_PAY = 4;
    public static final int BALANCE_PAY = 5;

    public int mCurrentPayType = -1;
    public int mCurrentDriveType = -1;
    private boolean isShowDriveType;
    private Gson mGson;

    private SignOrder mSignOrder;
    private String order;

    private Double mSubjectOne;
    private Double mSubjectTwo;
    private Double mSubjectThree;
    private Double mSubjectFour;//科目四
    private Double mDocumentsFee;//证件工本费

    private Double mManagementFee;//场地管理费（东莞财政）
    private Double mCommissionFee;//学习资料及代办手续费（代办机构）

    private Double mExaminationFee;//体检费
    private Double mResidencePermit;//居住证
    private Double mOneSize;//驾驶证1寸数码照30元
    private Double mDigitalPhotosFee;//居住证数码照

    public static EnterPersonInformationFragment2 newInstance() {
        return new EnterPersonInformationFragment2();
    }

    @Override
    protected int getContentRes() {
        return R.layout.enter_person_information_fragment2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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
        getData();
    }

    private void getData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "加载中...", false);
        UpDataSignInfoRequestData requestData = new UpDataSignInfoRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setFristPlayType("Frist");
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                switch (code) {
                    case BaseRequestTask.CODE_SUCCESS:
                        parseInfoData(response);
                        break;
                    case BaseRequestTask.CODE_SESSION_ABATE:
                        getData();
                        break;
                    default:
                        break;
                }
            }
        }.request(Constants.URL_GET_STUDENT_INFO, data, progressDialog, true);
    }

    private void parseInfoData(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        SignStudent signStudent = mGson.fromJson(response, SignStudent.class);
        if (signStudent != null) {
            String cityname = signStudent.getList().get(0).getCityname();
            String classname = signStudent.getList().get(0).getClassname();
            String dsname = signStudent.getList().get(0).getDsname();

            if (TextUtils.isEmpty(cityname)) {
                mTvCity.setText("未知");
            } else {
                mTvCity.setText(cityname);
            }
            if (TextUtils.isEmpty(dsname)) {
                mTvSchool.setText("未知");
            } else {
                mTvSchool.setText(dsname);
            }
            if (TextUtils.isEmpty(classname)) {
                mTvClass.setText("未知");
            } else {
                mTvClass.setText(classname);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (payType != null && account != null && mCurrentPayType != -1 && mCurrentDriveType != -1 && order != null) {
            getOrderPayStatus();
        }
    }

    private void getOrderPayStatus() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等...", false);
        GetOrderPayStatusRequestData requestData = new GetOrderPayStatusRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setId(order);
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
                                    addFragment(R.id.content, SignPaySuccessFragment.newInstance(), null);
                                } else {
                                    toastLong("支付失败,请重新尝试");
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
        }.request(Constants.URL_GET_ORDER_PAY_STATUS, data, progressDialog, true);
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
                    upData();
                    setDriveTypeState();
                }
                break;
            case R.id.iv_c2:
                if (mCurrentDriveType != C2) {
                    mCurrentDriveType = C2;
                    upData();
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
                if(mTotalFee == null){
                    toastLong("发生错误,请稍候重试");
                    return;
                }

                if (mCurrentDriveType == -1) {
                    toastLong("请选择报考驾驶证类别");
                    return;
                }
                if (mCurrentPayType == -1) {
                    toastLong("请选择支付方式");
                    return;
                }
                if (mTotalFee == 0) {
                    toastLong("结算金额为0.00元，请使用余额支付。");
                    return;
                }
                if (mCurrentPayType == BALANCE_PAY && mTotalFee > 0) {
                    toastLong("余额不足,请选择其他支付方式");
                    return;
                }
                if (mCurrentPayType == WE_CHAT_PAY) {
                    payByWeChat();
                } else if (mCurrentPayType == ALI_PAY) {
                    payByAlipay();
                }
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
        if (mSignOrder == null) {
            toastLong("发生错误,请稍候重试");
            return;
        }
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍候...", false);
        OperateOrderRequestData requestData = new OperateOrderRequestData();
        requestData.setAccount(account.getAccount());
        requestData.setId(mSignOrder.getOrderId());
        requestData.setOperateType(OperateOrderRequestData.PAYMENT);
        requestData.setPayType(payType);
        requestData.setFee(mTotalFee*100);
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
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
                                    req.extData = mSignOrder.getOrderId();
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

    private void upData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "加载中...", false);
        UpDataSignInfoRequestData requestData = new UpDataSignInfoRequestData();
        if (mCurrentDriveType != -1) {
            requestData.setDriverType(mCurrentDriveType);
        }
        requestData.setFristPlayType("Frist");
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                switch (code) {
                    case BaseRequestTask.CODE_SUCCESS:
                        parseData(response);
                        break;
                    case BaseRequestTask.CODE_SESSION_ABATE:
                        upData();
                        break;
                    default:
                        break;
                }
            }
        }.request(Constants.URL_UPDATA_STUDENT_INFO, data, progressDialog, true);
    }

    private Double mTestFee;
    private Double mTotalFee;

    private void parseData(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        mSignOrder = mGson.fromJson(response, SignOrder.class);
        order = mSignOrder.getOrderId();

        mSubjectOne = mSignOrder.getCostList().get(0).getSubjectOne();
        mSubjectTwo = mSignOrder.getCostList().get(0).getSubjectTwo();
        mSubjectThree = mSignOrder.getCostList().get(0).getSubjectThree();
        mDocumentsFee = mSignOrder.getCostList().get(0).getDocumentsFee();

        mManagementFee = mSignOrder.getCostList().get(0).getManagementFee();
        mCommissionFee = mSignOrder.getCostList().get(0).getCommissionFee();

        mExaminationFee = mSignOrder.getCostList().get(0).getExaminationFee();
        mResidencePermit = mSignOrder.getCostList().get(0).getResidencePermit();
        mOneSize = mSignOrder.getCostList().get(0).getOneSize();
        mDigitalPhotosFee = mSignOrder.getCostList().get(0).getDigitalPhotosFee();

        mTestFee = mSubjectOne + mSubjectTwo + mSubjectThree + mDocumentsFee;
        mTotalFee = mTestFee + mManagementFee + mCommissionFee;
        showPayDetailDialog();
    }

    private void showPayDetailDialog() {
        final SignPayDetailDialog signPayDetailDialog = new SignPayDetailDialog(act);
        signPayDetailDialog.setCancelable(false);
        signPayDetailDialog.setTotal("¥" + String.valueOf(mTotalFee));
        signPayDetailDialog.setTestTotal("¥" + String.valueOf(mTestFee));
        signPayDetailDialog.setFeeTest1("¥" + String.valueOf(mSubjectOne));
        signPayDetailDialog.setFeeTest2("¥" + String.valueOf(mSubjectTwo));
        signPayDetailDialog.setFeeTest3("¥" + String.valueOf(mSubjectThree));
        signPayDetailDialog.setFeePapers("¥" + String.valueOf(mDocumentsFee));
        signPayDetailDialog.setFeeSpace("¥" + String.valueOf(mManagementFee));
        signPayDetailDialog.setFeeData("¥" + String.valueOf(mCommissionFee));

        signPayDetailDialog.setFeePhysical("¥" + String.valueOf(mExaminationFee));
        signPayDetailDialog.setFeeResidence("¥" + String.valueOf(mResidencePermit));
        signPayDetailDialog.setFeePhoto("¥" + String.valueOf(mOneSize));
        signPayDetailDialog.setResidencePhoto("¥" + String.valueOf(mDigitalPhotosFee));

        signPayDetailDialog.setOnPhysicalOnclickListener(new SignPayDetailDialog.onPhysicalOnclickListener() {
            @Override
            public void onPhysicalClick() {
                mTotalFee = mTotalFee + mExaminationFee;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }

            @Override
            public void onPhysicalUnClick() {
                mTotalFee = mTotalFee - mExaminationFee;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }
        });
        signPayDetailDialog.setOnResidenceOnclickListener(new SignPayDetailDialog.onResidenceOnclickListener() {
            @Override
            public void onResidenceClick() {
                mTotalFee = mTotalFee + mResidencePermit;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }

            @Override
            public void onResidenceUnClick() {
                mTotalFee = mTotalFee - mResidencePermit;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }
        });
        signPayDetailDialog.setOnPhotoOnclickListener(new SignPayDetailDialog.onPhotoOnclickListener() {
            @Override
            public void onPhotoClick() {
                mTotalFee = mTotalFee + mOneSize;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }

            @Override
            public void onPhotoUnClick() {
                mTotalFee = mTotalFee - mOneSize;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }
        });
        signPayDetailDialog.setOnResidencePhotoOnclickListener(new SignPayDetailDialog.onResidencePhotoOnclickListener() {
            @Override
            public void onResidencePhotoClick() {
                mTotalFee = mTotalFee + mDigitalPhotosFee;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }

            @Override
            public void onResidenceUnPhotoClick() {
                mTotalFee = mTotalFee - mDigitalPhotosFee;
                signPayDetailDialog.upDataTotalFee("¥" + String.valueOf(mTotalFee));
            }
        });
        signPayDetailDialog.setOnConfirmOnclickListener(new SignPayDetailDialog.onConfirmOnclickListener() {
            @Override
            public void onConfirmClick() {
                signPayDetailDialog.dismiss();
                mTvNeedPay.setText(String.format("¥%s", String.valueOf(mTotalFee)));
            }
        });

        signPayDetailDialog.show();
        WindowManager windowManager = act.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = signPayDetailDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.95); //设置宽度
        signPayDetailDialog.getWindow().setAttributes(lp);
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
