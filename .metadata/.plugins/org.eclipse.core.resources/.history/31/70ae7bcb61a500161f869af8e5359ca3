package com.session.dgjp.login;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.Constants;
import com.session.dgjp.HomeActivity;
import com.session.dgjp.MainActivity;
import com.session.dgjp.R;
import com.session.dgjp.enity.Account;
import com.session.dgjp.receiver.JpushReceiver;
import com.session.dgjp.request.LoginRequestData;

/** 欢迎 */
public class WelcomActivity extends BaseActivity {
	
	private int[] ids = new int[1];
	private ViewPager layoutPager;
	private ArrayList<View> mListView = new ArrayList<View>();
	private boolean isFinish;
	private String phone, password;

	@Override
	protected void init(Bundle savedInstanceState) {
		JPushInterface.stopPush(this);
		setContentView(R.layout.act_welcome);
		ids[0] = R.drawable.img_welcome_1;
		layoutPager = (ViewPager) findViewById(R.id.layoutPager);
		for (int i = 0; i < ids.length; i++) {
			// final int index = i;
			ImageView it = (ImageView) View.inflate(ctx, R.layout.it_splash, null);
			it.setImageResource(ids[i]);
			/*it.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点击进入下一个
					gotoNext(index + 1);
				}
			});*/
			mListView.add(it);
		}
		SplashAdapter adapter = new SplashAdapter(mListView);
		layoutPager.setAdapter(adapter);
		phone = SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_ACCOUNT, null);
		password = SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_PASSWORD, null);
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) { // 自动登录
			login();
		} else { // 倒计时自动跳转
			new CountDownTimer(2000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					if (isFinish)
						this.cancel();
				}

				@Override
				public void onFinish() {
					if (!isFinish)
						gotoLogin();
				}
			}.start();
		}
	}

	/** 登录 */
	private void login() {
		LoginRequestData requestData = new LoginRequestData();
		requestData.setAccount(phone);
		requestData.setPassword(password);
		final String data = new Gson().toJson(requestData);
		new BaseRequestTask() {
			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case BaseRequestTask.CODE_SUCCESS:
						Account account = new Gson().fromJson(response, Account.class);
						if (account != null) {
							SharedPreferencesUtil.saveString(SharedPreferencesUtil.KEY_ACCOUNT_ACCOUNT, account.getAccount());
							AppInstance.getInstance().setAccount(account);
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									gotoHome();
								}
							}, 1000);
						} else {
							gotoLogin();
						}
						break;
					case BaseRequestTask.CODE_SESSION_ABATE:
						login();
						break;
					default:
						gotoLogin();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					gotoLogin();
				}
			}
		}.request(Constants.URL_LOGIN, data, null, true);
	}

	/** 进入下一页 */
	/*private void gotoNext(int index) {
		if (index < mListView.size()) {
			layoutPager.setCurrentItem(index);
		} else {
			gotoLogin();
		}
	}*/
	
	/** 进入主页 */
	private void gotoHome() {
//		Intent in = new Intent(ctx, HomeActivity.class);
		Intent in = new Intent(ctx, MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
		finish();
	}

	/** 进入登录页 */
	private void gotoLogin() {
		Intent in = new Intent(ctx, LoginActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
		isFinish = true;
		finish();
	}

	public class SplashAdapter extends PagerAdapter {

		private ArrayList<View> listView;

		SplashAdapter(ArrayList<View> listView) {
			this.listView = listView;
		}

		@Override
		public int getCount() {
			return null == listView ? 0 : listView.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (position < 0 || position >= listView.size()) {
				return;
			}
			container.removeView(listView.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (position < 0 || position >= listView.size()) {
				return null;
			}
			View view = listView.get(position);
			container.addView(view);
			return view;
		}

	}


}
