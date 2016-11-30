package com.session.dgjp.personal;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.dgjp.AppInstance;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.Account;
import com.session.dgjp.request.CommonRequestData;
import com.session.dgjp.view.CircleTransformation;
import com.squareup.picasso.Picasso;

/** 个人资料 */
public class PersonalInfoActivity extends BaseActivity {

	private ImageView ivHead;
	private TextView tvName, tvPhone, tvAddress, tvSchool, tvTime, tvBalance;
	private EditText etId;
	private boolean isShowId;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_personal_info);
		initTitle(R.string.personal_info);
		ivHead = (ImageView) findViewById(R.id.ivHead);
		tvName = (TextView) findViewById(R.id.tvName);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvSchool = (TextView) findViewById(R.id.tvSchool);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvBalance = (TextView) findViewById(R.id.tvBalance);
//		try {
//			Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_def_head);
//			CircleDrawable head = new CircleDrawable(bm);
//			ivHead.setImageDrawable(head);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		etId = (EditText) findViewById(R.id.etId);
		showOrHideId();
		findViewById(R.id.ivShowId).setOnClickListener(this);
		// 先填充现有的用户信息，然后再去查询最新的用户信息
		fillInfo();
		getAccountInfo();
	}

	/** 获取个人信息 */
	private void getAccountInfo() {
		CommonRequestData requestData = new CommonRequestData();
		requestData.setAccount(account.getAccount());
		String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						Account account = new Gson().fromJson(response, Account.class);
						if (account != null) {
							AppInstance.getInstance().setAccount(account);
							fillInfo();
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						getAccountInfo();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.request(Constants.URL_GET_ACCOUNT_INFO, data, null, true);
	}

	/** 填充信息 */
	private void fillInfo() {
		tvName.setText(account.getName());
		tvPhone.setText(account.getPhone());
		tvAddress.setText(account.getAddress());
		tvSchool.setText(account.getBranchSchoolName());
		String time = account.getApplyTime();
		if (time != null && time.length() >= 10) {
			tvTime.setText(time.substring(0, 10));
		}
		// tvBalance.setText("" + account.getBalance() / 100);
		double balance = (double) (account.getBalance() / 100);
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		tvBalance.setText(decimalFormat.format(balance));
		if (!TextUtils.isEmpty(account.getPhotoUrl())) {
			Picasso.with(this).load(account.getPhotoUrl()).placeholder(R.drawable.img_def_head).error(R.drawable.img_def_head).transform(new CircleTransformation()).into(ivHead);
		} else {
			Picasso.with(this).load(R.drawable.img_def_head).transform(new CircleTransformation()).into(ivHead);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivShowId:
			isShowId = !isShowId;
			showOrHideId();
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	private void showOrHideId() {
		if (isShowId) {
			etId.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		} else {
			etId.setTransformationMethod(new IdPasswordTransformationMethod());
		}
	}

	protected class IdPasswordTransformationMethod extends PasswordTransformationMethod {

		@Override
		public CharSequence getTransformation(CharSequence source, View view) {
			return new PasswordCharSequence(source);
		}

		private class PasswordCharSequence implements CharSequence {
			private CharSequence mSource;

			public PasswordCharSequence(CharSequence source) {
				mSource = source;
			}

			public char charAt(int index) {
				if (index > 5 && index < 14)
					return '*';
				return mSource.charAt(index);
			}

			public int length() {
				if (null == mSource)
					return 0;
				return mSource.length();
			}

			public CharSequence subSequence(int start, int end) {
				return mSource.subSequence(start, end);
			}
		}
	}

}
