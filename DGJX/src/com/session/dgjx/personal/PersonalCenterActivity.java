package com.session.dgjx.personal;

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
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.common.BaseRequestTask;
import com.session.common.utils.AppUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.UpdateUtil;
import com.session.common.utils.UpdateUtil.OnVersionInfoListener;
import com.session.common.utils.VersionInfo;
import com.session.dgjx.AppInstance;
import com.session.dgjx.BWeb;
import com.session.dgjx.Constants;
import com.session.dgjx.R;
import com.session.dgjx.enity.Coach;
import com.session.dgjx.login.LoginActivity;
import com.session.dgjx.message.MessageActivity;
import com.session.dgjx.request.CommonRequestData;
import com.session.dgjx.request.GetPersonalDataRequestData;
import com.session.dgjx.view.CircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity {

    private ImageView ivHead;
    private TextView tvName, tvPhone ,tvMoney;
    private LinearLayout llVersion;
    private Button btnLogout;
    private Gson mGson;
    private boolean mIsCheck;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_personal_center);
        //		initTitle(R.string.personal_center,false);
        ivHead = (ImageView) findViewById(R.id.ivHead);
        tvName = (TextView) findViewById(R.id.tvName);
        tvMoney = (TextView) findViewById(R.id.tv_income);
        tvName.setText(account.getName());
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvPhone.setText(account.getPhone());
        if (!TextUtils.isEmpty(account.getPhotoUrl())) {
            Picasso.with(ctx).load(account.getPhotoUrl()).placeholder(R.drawable.img_def_head).error(R.drawable.img_def_head).transform(new CircleTransformation()).into(ivHead);
        } else {
            Picasso.with(ctx).load(R.drawable.img_def_head).transform(new CircleTransformation()).into(ivHead);
        }
        findViewById(R.id.llPersonInfo).setOnClickListener(this);
        llVersion = (LinearLayout) findViewById(R.id.llVersion);
        LinearLayout llComeIn = (LinearLayout) findViewById(R.id.ll_come_in);
        LinearLayout llQA = (LinearLayout) findViewById(R.id.ll_faq);
        LinearLayout llMsg = (LinearLayout) findViewById(R.id.ll_msg);
        LinearLayout llOnLine = (LinearLayout) findViewById(R.id.ll_online_student);
        llVersion.setOnClickListener(this);
        llComeIn.setOnClickListener(this);
        llQA.setOnClickListener(this);
        llMsg.setOnClickListener(this);
        llOnLine.setOnClickListener(this);
        findViewById(R.id.llAbout).setOnClickListener(this);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        getPersonalData();
        getLocalEase();
    }

    private String mEaseName;
    private void getLocalEase() {
        mEaseName = SharedPreferencesUtil.getString(SharedPreferencesUtil.EASE_USER_NAME, "");
    }


    private void getPersonalData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等...", false);
        GetPersonalDataRequestData requestData = new GetPersonalDataRequestData();
        requestData.setAccount(account.getAccount());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {
            @Override
            protected void onResponse(int code, String msg, String response) {
                $log(response);
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            parseCoachData(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getPersonalData();
                            break;
                        default:
                            toastShort(msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShort("网络异常，请稍后重试");
                } finally {
                }
            }
        }.request(Constants.URL_GET_ACCOUNT_DATA, data, progressDialog, true);
    }

    private void parseCoachData(String respone) {
        if (mGson == null) {
            mGson = new Gson();
        }
        Coach coach = mGson.fromJson(respone, Coach.class);
        if (coach!=null){
            tvMoney.setText(String.format("%s", coach.getList().get(0).getMoney()));
            Picasso.with(ctx).load(Constants.IMG_URL+coach.getList().get(0).getPhotoUrl())
                    .placeholder(R.drawable.img_def_head)
                    .error(R.drawable.img_def_head)
                    .transform(new CircleTransformation())
                    .into(ivHead);
        }
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
            case R.id.llPersonInfo: // 个人资料
                startActivity(new Intent(ctx, PersonalInfoActivity.class));
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
            case R.id.ll_faq: // 常见问题
                Intent intent = new Intent(ctx, BWeb.class);
                intent.putExtra(BWeb.TITLE,"常见问题");
                intent.putExtra(BWeb.URL,"http://www.papaxueche.com/cheet/answer.html");
                startActivity(intent);
                break;
            case R.id.ll_come_in:
                startActivity(new Intent(ctx,ComeInActivity.class));
                break;
            case R.id.ll_online_student:
                mIsCheck = true;
                toChat();
                break;
            case R.id.ll_msg:
                startActivity(new Intent(ctx,MessageActivity.class));
                break;
            case R.id.btnLogout: // 退出登录
                BaseDialog dialog = new BaseDialog.Builder(ctx).setTitle("提示").setMessage("您确定要退出登录吗？")
                        .setPositiveButton("确定", new OnClickListener() {

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

    private void toChat() {
        if (ChatClient.getInstance().isLoggedInBefore()) {
            Intent intent = new IntentBuilder(PersonalCenterActivity.this)
                    .setServiceIMNumber("kefuchannelimid_148313")
                    .setVisitorInfo(ContentFactory.createVisitorInfo(null)
                            .nickName(account.getName())
                            .phone(account.getPhone()))
                    .build();
            startActivity(intent);
        } else {
            toastLong("登录中...请稍等");
            getEaseAccount();
        }
    }

    private void getEaseAccount() {
        final String name = account.getPhone();
        final String pwd = "123456";
        CreateEase(name, pwd);

        if (!mEaseName.equals(name)) {
            SharedPreferencesUtil.saveString(SharedPreferencesUtil.EASE_USER_NAME, name);
            SharedPreferencesUtil.saveString(SharedPreferencesUtil.EASE_USER_PASSWORD, pwd);
            ChatClient.getInstance().logout(true, new Callback() {
                @Override
                public void onSuccess() {
                    LoginEase(name, pwd);
                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }

    private void CreateEase(final String name, final String pwd) {
        ChatClient.getInstance().createAccount(name, pwd, new Callback() {
            @Override
            public void onSuccess() {
                LoginEase(name, pwd);
            }

            @Override
            public void onError(int i, String s) {
                LoginEase(name, pwd);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void LoginEase(String name, String passWord) {
        ChatClient.getInstance().login(name, passWord, new Callback() {
            @Override
            public void onSuccess() {
                if (mIsCheck) {
                    mIsCheck = false;
                    toChat();
                }
            }

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }

}
