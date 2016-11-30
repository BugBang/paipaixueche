package com.session.dgjx.login;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseDialog;
import com.session.dgjx.HomeActivity;
import com.session.dgjx.R;

import java.util.Random;

public class RegisterActivity extends BaseActivity {
	private EditText etPhone, etCaptcha;
	private String mCaptcha;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_register);
		initTitle(R.string.register, false);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etCaptcha = (EditText) findViewById(R.id.etCaptcha);
		findViewById(R.id.btnCaptcha).setOnClickListener(this);
		findViewById(R.id.btnRegister).setOnClickListener(this);
	}

	private void getCaptcha() {
		String phone = etPhone.getText().toString();
		if (phone.isEmpty()) {
			toastShort(R.string.toast_input_phone);
			etPhone.requestFocus();
			return;
		} else if (phone.length() != 11) {
			toastShort(R.string.toast_input_right_phone);
			etPhone.requestFocus();
			return;
		}
		StringBuilder sb = new StringBuilder(6);
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(10));
		}
		mCaptcha = sb.toString();
		etCaptcha.setText(mCaptcha);
	}

	private void register() {
		String phone = etPhone.getText().toString();
		if (phone.isEmpty()) {
			toastShort(R.string.toast_input_phone);
			etPhone.requestFocus();
			return;
		} else if (phone.length() != 11) {
			toastShort(R.string.toast_input_right_phone);
			etPhone.requestFocus();
			return;
		}
		if (null == mCaptcha || mCaptcha.isEmpty()) {
			toastShort(R.string.toast_get_captcha);
			etCaptcha.requestFocus();
			return;
		}
		String captcha = etCaptcha.getText().toString();
		if (captcha.isEmpty()) {
			toastShort(R.string.toast_input_captcha);
			etCaptcha.requestFocus();
			return;
		} else if (!captcha.equals(mCaptcha)) {
			toastShort(R.string.toast_input_right_phone);
			etPhone.requestFocus();
			return;
		}
		BaseDialog dialog = new BaseDialog.Builder(this)
				.setTitle("注册成功！")
				.setMessage("欢迎使用学员版快捷学车助手")
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							login();
							break;
						}
						dialog.cancel();
					}
				})
				.create();
		dialog.show();
	}

	private void login() {
		gotoHome();
	}

	private void gotoHome() {
		Intent in = new Intent(this, HomeActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCaptcha:
			getCaptcha();
			break;
		case R.id.btnRegister:
			register();
			break;
		default:
			super.onClick(v);
		}
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
