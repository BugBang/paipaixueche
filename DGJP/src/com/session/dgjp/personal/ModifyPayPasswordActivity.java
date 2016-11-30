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
import com.session.dgjp.request.ResetPayPasswordRequestData;

/** 修改支付密码 */
public class ModifyPayPasswordActivity extends BaseActivity {

	private EditText etOldPwd, etNewPwd, etConfirmNewPwd;
	private String oldPassword, newPassword, confirmPassword;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_modify_pay_password);
		initTitle(R.string.modify_password);
		etOldPwd = (EditText) findViewById(R.id.etOldPwd);
		etNewPwd = (EditText) findViewById(R.id.etNewPwd);
		etConfirmNewPwd = (EditText) findViewById(R.id.etConfirmNewPwd);
		findViewById(R.id.btnSave).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnSave: // 保存
			modifyPayPassword();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	/** 修改支付密码 */
	private void modifyPayPassword() {
		if (!check()) {
			return;
		}
		ProgressDialog progressDialog = buildProcessDialog(null, "正在修改支付密码...", false);
		ResetPayPasswordRequestData requestData = new ResetPayPasswordRequestData();
		requestData.setAccount(account.getAccount());
		requestData.setModifyType(ResetPayPasswordRequestData.MODIFY);
		requestData.setOldPayPassword(MD5Util.encode(oldPassword + BaseRequestTask.KEY));
		requestData.setNewPayPassword(MD5Util.encode(newPassword + BaseRequestTask.KEY));
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						toastShort("修改支付密码成功");
						finish();
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						modifyPayPassword();
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
		oldPassword = etOldPwd.getText().toString().trim();
		newPassword = etNewPwd.getText().toString().trim();
		confirmPassword = etConfirmNewPwd.getText().toString().trim();
		if (TextUtils.isEmpty(oldPassword) || oldPassword.length() != 6) {
			toastShort("请输入原密码");
			etOldPwd.requestFocus();
			return false;
		}
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
