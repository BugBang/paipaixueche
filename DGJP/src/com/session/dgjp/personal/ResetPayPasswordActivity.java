package com.session.dgjp.personal;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.MD5Util;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.request.GetVerifyCodeRequestData;
import com.session.dgjp.request.ResetPayPasswordRequestData;

/** 重置支付密码 */
public class ResetPayPasswordActivity extends BaseActivity {

	private EditText etVerifyCode, etIdCard, etNewPwd, etConfirmNewPwd;
	private String verifyCode, idCard, newPassword, confirmPassword;
	private final String REGEX = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
	private Button btnCaptcha;
	private final int TOTAL_TIME = 120;
	private int totalTime;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_reset_pay_password);
		initTitle(R.string.reset_pay_pwd);
		etVerifyCode = (EditText) findViewById(R.id.etVerifyCode);
		etIdCard = (EditText) findViewById(R.id.etIdCard);
		etNewPwd = (EditText) findViewById(R.id.etNewPwd);
		etConfirmNewPwd = (EditText) findViewById(R.id.etConfirmNewPwd);
		btnCaptcha = (Button) findViewById(R.id.btnCaptcha);
		btnCaptcha.setOnClickListener(this);
		findViewById(R.id.btnSave).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnCaptcha: // 获取验证码
			getVerifyCode();
			break;
		case R.id.btnSave: // 保存
			resetPayPassword();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	/** 获取验证码 */
	private void getVerifyCode() {
		btnCaptcha.setEnabled(false);
		ProgressDialog progressDialog = buildProcessDialog(null, "正在获取验证码...", false);
		GetVerifyCodeRequestData requestData = new GetVerifyCodeRequestData();
		requestData.setAccount(account.getPhone());
		requestData.setAuthType(GetVerifyCodeRequestData.RESET_PAY_PWD);
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("验证码发送到" + account.getPhone().substring(7, 11) + "手机，请注意查收！");
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

	/** 重置支付密码 */
	private void resetPayPassword() {
		if (!check()) {
			return;
		}
		ProgressDialog progressDialog = buildProcessDialog(null, "正在重置支付密码...", false);
		ResetPayPasswordRequestData requestData = new ResetPayPasswordRequestData();
		requestData.setAccount(account.getAccount());
		requestData.setModifyType(ResetPayPasswordRequestData.RESET);
		requestData.setVerifyCode(verifyCode);
		requestData.setIdcard(idCard);
		requestData.setNewPayPassword(MD5Util.encode(newPassword + BaseRequestTask.KEY));
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("重置支付密码成功");
						finish();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						resetPayPassword();
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
		}.request(Constants.URL_RESET_PAY_PASSWORD, data, progressDialog, true);
	}

	/** 校验 */
	private boolean check() {
		verifyCode = etVerifyCode.getText().toString().trim();
		idCard = etIdCard.getText().toString().trim();
		newPassword = etNewPwd.getText().toString().trim();
		confirmPassword = etConfirmNewPwd.getText().toString().trim();
		if (TextUtils.isEmpty(newPassword) || newPassword.length() != 6) {
			toastShort("请输入6位数字的新密码");
			etNewPwd.requestFocus();
			return false;
		}
		if (TextUtils.isEmpty(confirmPassword) || confirmPassword.length() != 6) {
			toastShort("请再次输入新密码");
			etConfirmNewPwd.requestFocus();
			return false;
		}
		if (!newPassword.equals(confirmPassword)) {
			toastShort("两次输入的新密码不一致");
			return false;
		}
		if (TextUtils.isEmpty(idCard)) {
			toastShort("请输入身份证号码");
			etIdCard.requestFocus();
			return false;
		} else {
			boolean matches = idCard.matches(REGEX);
			if (!matches) {
				toastShort("身份证号码格式有误，请检查");
				etIdCard.requestFocus();
				return false;
			}
		}
		if (TextUtils.isEmpty(verifyCode)) {
			toastShort("请输入验证码");
			etVerifyCode.requestFocus();
			return false;
		}
		return true;
	}

}
