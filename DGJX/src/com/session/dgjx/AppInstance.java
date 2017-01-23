package com.session.dgjx;

import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.session.common.BaseApplication;
import com.session.common.ExtraMap;
import com.session.common.utils.AppUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.TextUtil;
import com.session.dgjx.enity.Account;
import com.umeng.analytics.MobclickAgent;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class AppInstance extends BaseApplication{

	private Account account;
	private SharedPreferences sp;
	//private AMapLocationClient locationClient;
	//private int locationTimes = 2;// 定位失败后重复定位2次
	//private AMapLocation location;// 高德定位结果
	private ExtraMap extraMap = new ExtraMap();

	public static AppInstance getInstance() {
		return (AppInstance) mApp;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		Set<String> tags = new HashSet<String>();
		if (account != null && !TextUtil.isEmpty(account.getAccount())) {
			String jpushSuffix;
			if(Constants.URL_IP.equals(Constants.RELEASE_URL))
			{
				jpushSuffix = "_official";
			}else {
				jpushSuffix = "_test";
			}
			
			tags.add("dgjx" + jpushSuffix);
			//如果服务停止，则先启动服务
			if(JPushInterface.isPushStopped(this))
			{
				JPushInterface.resumePush(this);
			}
			JPushInterface.setAliasAndTags(this, SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_ACCOUNT, ""), tags, new TagAliasCallback() {

				@Override
				public void gotResult(int code, String alias, Set<String> tags) {
					LogUtil.e(TAG, "jpush set alias and tag: " + code + "," + alias + "," + tags.toString());
				}
			});
		}else {
			JPushInterface.setAliasAndTags(this, "", tags, new TagAliasCallback() {
				
				@Override
				public void gotResult(int code, String alias, Set<String> tags) {
					LogUtil.e(TAG, "jpush set alias and tag: " + code + "," + alias + "," + tags.toString());
				}
			});
		}
	}

	public SharedPreferences getSp() {
		if (null == sp) {
			sp = getSharedPreferences("DGJX", MODE_PRIVATE);
		}
		return sp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		boolean isDebug = AppUtil.isDebuggable(this);
        Logger.init("BAO");
		JPushInterface.setDebugMode(isDebug);
		JPushInterface.init(this);
		MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		/*locationClient = new AMapLocationClient(this);
		locationClient.setLocationListener(this);
		AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
		locationClientOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		locationClientOption.setGpsFirst(true);
		locationClientOption.setOnceLocation(true);
		locationClientOption.setNeedAddress(false);
		locationClientOption.setWifiActiveScan(true);
		locationClientOption.setHttpTimeOut(3000);// 默认超时3秒
		locationClient.startLocation();*/
	}

	/*@Override
	public void onLocationChanged(AMapLocation location) {
		if (location.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
			locationTimes = 3;
			locationClient.stopLocation();
			locationClient.onDestroy();
			LogUtil.e(TAG, "locate success: " + location.getLongitude() + "," + location.getLatitude());
			this.location = location;
		} else {
			// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
			LogUtil.e(TAG, "locationg error code:" + location.getErrorCode() + ", errInfo:" + location.getErrorInfo());
			locationTimes--;
			if (locationTimes < 1) {
				locationClient.stopLocation();
				locationClient.onDestroy();
				this.location = location;
				location.setLongitude(-1);
				location.setLatitude(-1);
			}
		}
	}*/

	/*public AMapLocation getLocation() {
		return location;
	}*/
	
	public ExtraMap getExtraMap()
	{
		return extraMap;
	}

}
