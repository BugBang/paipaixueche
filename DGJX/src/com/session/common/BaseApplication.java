package com.session.common;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.session.common.receivers.NetworkReceiver;
import com.session.common.utils.AppUtil;
import com.session.common.utils.LogUtil;

/** App实例 */
public class BaseApplication extends Application {
	protected final String TAG = this.getClass().getSimpleName();
	private NetworkReceiver mReceiver;
	protected static Application mApp;
	
	public static Application getInstance() {
		return mApp;
	}

	@Override
	public void onCreate() {
		boolean debug = AppUtil.isDebuggable(this);
		LogUtil.setDebuggable(debug);
		if (debug)
			LogUtil.setLevel(LogUtil.DEBUG);

		super.onCreate();
		mApp = this;
		LogUtil.d(TAG, "onCreate");
		registerNetworkReceiver();
	}

	/** 遍历所有Activity并finish，可以以此退出程序 */
	public void exit() {
		LogUtil.d(TAG, "exit");
		unregisterNetworkReceiver();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onTerminate() {
		LogUtil.d(TAG, "onTerminate");
		exit();
		super.onTerminate();
	}

	/** 注册网络状态监听广播 */
	private void registerNetworkReceiver() {
		mReceiver = new NetworkReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(mReceiver, filter);
	}

	/** 关闭网络状态监听广播 */
	private void unregisterNetworkReceiver() {
		if (null == mReceiver) {
			return;
		}
		try {
			unregisterReceiver(mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
