package com.session.common.receivers;

import com.session.common.utils.HttpUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.ToastUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络监听广播，在App打开时开始监听，在App关闭时注销，将网络状态的变化保存到HttpUtil的变量里，不用每次请求网络操作时再做判断，需要有监听网络状态的权限
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * @author JimChen
 */
public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
			// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cm.getActiveNetworkInfo();
				if (null != info) {
					LogUtil.d("NetworkState", info.toString());
					HttpUtil.setConnect(info.isAvailable());
					HttpUtil.setWifi(ConnectivityManager.TYPE_WIFI == info.getType());
				} else {
					LogUtil.d("NetworkState", "网络断开");
					HttpUtil.setConnect(false);
				}
				if (HttpUtil.isConnect()) {
					if (!HttpUtil.isWifi()) {
						// ToastUtil.showShort(context, "您现在正在使用移动网络，请注意网络资费");
					}
				} else {
					ToastUtil.showShort(context, "您的网络连接已经断开");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
