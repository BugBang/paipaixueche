package com.session.dgjp.personal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.common.BaseRequestTask;
import com.session.common.utils.AppUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.UpdateUtil;
import com.session.common.utils.UpdateUtil.OnVersionInfoListener;
import com.session.common.utils.VersionInfo;
import com.session.dgjp.AppInstance;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.helper.BranchSchoolHelper;
import com.session.dgjp.helper.LabelHelper;
import com.session.dgjp.login.LoginActivity;
import com.session.dgjp.order.OrderListActivity;
import com.session.dgjp.request.CommonRequestData;
import com.session.dgjp.training.TrainingListActivity;
import com.session.dgjp.view.CircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity {

    private ImageView ivHead;
    private TextView tvName, tvPhone;
    private LinearLayout llVersion, mLlMyAddress, mLlMyOrder , mLlMyBook;
    private Button btnLogout;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_personal_center);
        initTitle(R.string.personal_center);
        ivHead = (ImageView) findViewById(R.id.ivHead);
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(account.getName());
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvPhone.setText(account.getPhone());

        mLlMyAddress = (LinearLayout) findViewById(R.id.ll_address);
        mLlMyOrder = (LinearLayout) findViewById(R.id.ll_my_training);
        mLlMyBook = (LinearLayout) findViewById(R.id.ll_my_book);
        mLlMyAddress.setOnClickListener(this);
        mLlMyOrder.setOnClickListener(this);
        mLlMyBook.setOnClickListener(this);
        // try {
        // Bitmap bm = BitmapFactory.decodeResource(getResources(),
        // R.drawable.img_def_head);
        // CircleDrawable head = new CircleDrawable(bm);
        // ivHead.setImageDrawable(head);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        if (!TextUtils.isEmpty(account.getPhotoUrl())) {
            Picasso.with(ctx).load(account.getPhotoUrl()).placeholder(R.drawable.personal_user_photo).error(R.drawable.personal_user_photo).transform(new CircleTransformation()).into(ivHead);
        } else {
            Picasso.with(ctx).load(R.drawable.personal_user_photo).transform(new CircleTransformation()).into(ivHead);
        }
        //		findViewById(R.id.llPersonInfo).setOnClickListener(this);
        findViewById(R.id.llModifyPayPwd).setOnClickListener(this);
        findViewById(R.id.llResetPayPwd).setOnClickListener(this);
        findViewById(R.id.llMyWallet).setOnClickListener(this);
        findViewById(R.id.llMyCoupon).setOnClickListener(this);
        llVersion = (LinearLayout) findViewById(R.id.llVersion);
        llVersion.setOnClickListener(this);
        findViewById(R.id.llAbout).setOnClickListener(this);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    /**
     * 版本检测
     */
    private void checkVersion() {
        UpdateUtil.checkUpdate(new OnVersionInfoListener() {
            @Override
            public void onVersionInfo(VersionInfo info) {
                int code = AppUtil.getVersionCode(getApplicationContext());
                if (null != info) {
                    if (code < info.getCode()) {
                        UpdateUtil.showUpdate(ctx, "版本更新", 0);
                    } else {
                        toastShort("当前版本已是最新");
                    }
                } else {
                    toastShort("无法检测到版本信息");
                }
                llVersion.setEnabled(true);
            }
        });
    }

    /**
     * 退出登录
     */
    private void logout() {
        btnLogout.setEnabled(false);
        ProgressDialog progressDialog = buildProcessDialog(null, "正在退出登录...", false);
        CommonRequestData requestData = new CommonRequestData();
        requestData.setAccount(account.getAccount());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {

            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT, "");
                            SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_PASSWORD, "");
                            SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT_ACCOUNT, "");
                            AppInstance.getInstance().setAccount(null);
                            LabelHelper.setLabels(null);
                            BranchSchoolHelper.setBranchSchools(null);
                            //PayTypeHelper.clearCache();
                            gotoLogin();
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            logout();
                            break;
                        default:
                            toastShort(msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("网络异常，请稍后重试");
                } finally {
                    btnLogout.setEnabled(true);
                }
            }
        }.request(Constants.URL_LOGOUT, data, progressDialog, true);
    }

    /**
     * 进入登录页
     */
    private void gotoLogin() {
        Intent in = new Intent(this, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //		case R.id.llPersonInfo: // 个人资料
            //			startActivity(new Intent(ctx, PersonalInfoActivity.class));
            //			break;
            case R.id.llModifyPayPwd: // 修改支付密码
                if ("N".equals(account.getIsSetPayPwd())) {
                    startActivity(new Intent(ctx, SetPayPasswordActivity.class));
                } else {
                    startActivity(new Intent(ctx, ModifyPayPasswordActivity.class));
                }
                break;
            case R.id.llResetPayPwd: // 重置支付密码
                if ("N".equals(account.getIsSetPayPwd())) {
                    startActivity(new Intent(ctx, SetPayPasswordActivity.class));
                } else {
                    startActivity(new Intent(ctx, ResetPayPasswordActivity.class));
                }
                break;
            case R.id.ll_my_training: // 我的订单
                startActivity(new Intent(ctx, TrainingListActivity.class));
                break;
            case R.id.ll_my_book: // 我的预约
                startActivity(new Intent(ctx, OrderListActivity.class));
                break;
            case R.id.llMyWallet: // 我的钱包
                startActivity(new Intent(ctx, MyWalletActivity.class));
                break;
            case R.id.llMyCoupon: // 我的优惠券
                startActivity(new Intent(ctx, MyCouponActivity.class));
                break;
            case R.id.llVersion: // 版本更新
                if (llVersion.isEnabled()) {
                    llVersion.setEnabled(false);
                    checkVersion();
                }
                break;
            case R.id.llAbout: // 关于
                startActivity(new Intent(ctx, AboutAppActivity.class));
                break;
            case R.id.ll_address: // 上车地址
                startActivity(new Intent(ctx, MyAddressActivity.class));
                break;
            case R.id.btnLogout: // 退出登录
                BaseDialog dialog = new BaseDialog.Builder(ctx).setTitle("提示").setMessage("您确定要退出登录吗？").setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        logout();
                    }
                }).setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
                dialog.show();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

}
