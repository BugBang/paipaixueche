package com.session.dgjx.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.dgjx.AppInstance;
import com.session.dgjx.Constants;
import com.session.dgjx.R;
import com.session.dgjx.enity.Account;
import com.session.dgjx.request.CommonRequestData;
import com.session.dgjx.view.CircleTransformation;
import com.squareup.picasso.Picasso;

/** 个人资料 */
public class PersonalInfoActivity extends BaseActivity {

	private ImageView ivHead;
	private RatingBar ratingBar;
	private TextView tvEval, tvName, tvPhone, tvOrderTimes, tvSchool;

	@Override
	protected void init(Bundle savedInstanceState) {
		setContentView(R.layout.act_personal_info);
		initTitle(R.string.personal_info);
		ivHead = (ImageView) findViewById(R.id.ivHead);
		ratingBar = (RatingBar) findViewById(R.id.rating_bar);
		tvEval = (TextView) findViewById(R.id.tvEval);
		tvName = (TextView) findViewById(R.id.tvName);
		tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvOrderTimes = (TextView) findViewById(R.id.tvOrderTimes);
		tvSchool = (TextView) findViewById(R.id.tvSchool);
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
		tvOrderTimes.setText(String.valueOf(account.getOrderTimes()));
		tvSchool.setText(account.getBranchSchoolName());
		if (!TextUtils.isEmpty(account.getPhotoUrl())) {
			Picasso.with(this).load(account.getPhotoUrl()).placeholder(R.drawable.img_def_head).error(R.drawable.img_def_head).transform(new CircleTransformation()).into(ivHead);
		} else {
			Picasso.with(this).load(R.drawable.img_def_head).transform(new CircleTransformation()).into(ivHead);
		}
		ratingBar.setRating(account.getEval());
		tvEval.setText(getString(R.string.eval, account.getEval()));
	}

}
