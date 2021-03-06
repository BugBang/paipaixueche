package com.session.dgjx.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.CryptoUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.dgjx.AppInstance;
import com.session.dgjx.Constants;
import com.session.dgjx.MainActivity;
import com.session.dgjx.R;
import com.session.dgjx.enity.Account;
import com.session.dgjx.request.GetVerifyCodeRequestData;
import com.session.dgjx.request.LoginRequestData;

/** 登录 */
public class LoginActivity extends BaseActivity {

	private EditText etPhone, etVerifyCode;
	private String phone, verifyCode;
	private Button btnCaptcha, btnLogin;
	private final int TOTAL_TIME = 120;
	private int totalTime;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_login);
		initTitle(R.string.login, false);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etVerifyCode = (EditText) findViewById(R.id.etCaptcha);
		String phone = SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_ACCOUNT, null);
		String password = SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_PASSWORD, null);
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
			etPhone.setText(phone);
		}
		btnCaptcha = (Button) findViewById(R.id.btnCaptcha);
		btnCaptcha.setOnClickListener(this);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		BaseRequestTask.resetSessionId();
		CryptoUtil.setToken("");
	}

	/** 获取验证码 */
	private void getVerifyCode() {
		btnCaptcha.setEnabled(false);
		ProgressDialog progressDialog = buildProcessDialog(null, "正在获取验证码...", false);
		GetVerifyCodeRequestData requestData = new GetVerifyCodeRequestData();
		requestData.setAccount(phone);
		requestData.setAuthType(GetVerifyCodeRequestData.LOGIN);
		final String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("验证码发送到" + phone.substring(7, 11) + "手机，请注意查收！");
						totalTime = TOTAL_TIME;
						new CountDownTimer(TOTAL_TIME * 1000, 1000) {
							
							@Override
							public void onTick(long millisUntilFinished) {
								btnCaptcha.setText("获取验证码" + "(" + (--totalTime) + ")");
								btnCaptcha.setBackgroundResource(R.drawable.btn_white_enable);
								btnCaptcha.setTextColor(getResources().getColor(R.color.text_hint));
							}
							
							@Override
							public void onFinish() {
								btnCaptcha.setText("获取验证码");
								btnCaptcha.setBackgroundResource(R.drawable.btn_white);
								btnCaptcha.setTextColor(getResources().getColor(R.color.text_blue));
								btnCaptcha.setEnabled(true);
								totalTime = TOTAL_TIME;
							}
						}.start();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						getVerifyCode();
						break;
					default:
						toastShort(msg);
						btnCaptcha.setEnabled(true);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
					btnCaptcha.setEnabled(true);
				}
			}
		}.request(Constants.URL_GET_VERIFY_CODE, data, progressDialog, true);
	}

	/** 登录 */
	private void login() {
		btnLogin.setEnabled(false);
		ProgressDialog progressDialog = buildProcessDialog(null, "正在登录...", false);
		LoginRequestData requestData = new LoginRequestData();
		requestData.setAccount(phone);
		requestData.setPassword(verifyCode);
		final String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						Account account = new Gson().fromJson(response, Account.class);
						if (account != null) {
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT, phone);
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_PASSWORD, verifyCode);
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT_ACCOUNT, account.getAccount());
							AppInstance.getInstance().setAccount(account);
							gotoHome();
						} else {
							toastShort("登录失败，请稍后重试");
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						login();
						break;
					default:
						toastShort(msg);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					toastShort("网络异常，请稍后重试");
				} finally {
					btnLogin.setEnabled(true);
				}
			}
		}.request(Constants.URL_LOGIN, data, progressDialog, true);
	}

	/** 进入主页 */
	private void gotoHome() {
//		Intent in = new Intent(ctx, HomeActivity.class);
		Intent in = new Intent(ctx, MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCaptcha: // 获取验证码
			if (checkPhone()) {
				getVerifyCode();
			}
			break;
		case R.id.btnLogin: // 登录
			if (!checkPhone()) {
				return;
			}
			if (!checkVerifyCode()) {
				return;
			}
			login();
			break;
		default:
			super.onClick(v);
		}
	}

	/** 校验手机号码 */
	private boolean checkPhone() {
		phone = etPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			toastShort(R.string.toast_input_phone);
			etPhone.requestFocus();
			return false;
		} else if (phone.length() != 11) {
			toastShort(R.string.toast_input_right_phone);
			etPhone.requestFocus();
			return false;
		}
		return true;
	}

	/** 校验验证码 */
	private boolean checkVerifyCode() {
		verifyCode = etVerifyCode.getText().toString().trim();
		if (TextUtils.isEmpty(verifyCode)) {
			toastShort(R.string.toast_input_captcha);
			etVerifyCode.requestFocus();
			return false;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			//toastShort(R.string.toast_press_exit);
			toast(getString(R.string.toast_press_exit, getString(R.string.app_name)), Toast.LENGTH_SHORT, R.dimen.text_medium_xx);
			mExitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
		}
	}

}
