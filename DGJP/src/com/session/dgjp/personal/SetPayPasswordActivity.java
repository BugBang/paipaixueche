package com.session.dgjp.personal;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.MD5Util;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.request.SetPayPasswordRequestData;

/** 设置支付密码 */
public class SetPayPasswordActivity extends BaseActivity {

	private EditText etNewPwd, etConfirmNewPwd;
	private String newPassword, confirmPassword;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_set_pay_password);
		initTitle(R.string.set_password);
		etNewPwd = (EditText) findViewById(R.id.etNewPwd);
		etConfirmNewPwd = (EditText) findViewById(R.id.etConfirmNewPwd);
		findViewById(R.id.btnSave).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnSave: // 保存
			setPayPassword();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	/** 设置支付密码 */
	private void setPayPassword() {
		if (!check()) {
			return;
		}
		ProgressDialog progressDialog = buildProcessDialog(null, "正在设置支付密码...", false);
		SetPayPasswordRequestData requestData = new SetPayPasswordRequestData();
		requestData.setAccount(account.getAccount());
		requestData.setPayPassword(MD5Util.encode(newPassword + BaseRequestTask.KEY));
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("设置支付密码成功");
						account.setIsSetPayPwd("Y");
						finish();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						setPayPassword();
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
		}.request(Constants.URL_SET_PAY_PASSWORD, data, progressDialog, true);
	}

	/** 校验 */
	private boolean check() {
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
		return true;
	}

}
