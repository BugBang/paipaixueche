package com.session.common;

import android.content.Context;

import com.session.common.utils.AppUtil;
import com.session.common.utils.CryptoUtil;
import com.session.common.utils.HttpUtil;
import com.session.common.utils.LogUtil;
import com.session.dgjp.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class InitRequestTask extends BaseRequestTask {
	
	protected static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	protected final static String KEY_TOKEN = "token";
	public final static String KEY_RAND = "rand";
	protected final static int LENGTH_RAND = 6;
	private String mRand;

	/**
	 * 初始化网络公共参数，请求token
	 * @param url 请求token接口的地址
	 * @param channel 渠道号
	 * @param appversion 版本号
	 * @param protocolversion 后台通信协议版本
	 */
	public synchronized void init(String url, String channel, String appversion, String protocolversion) {
		if (mLock) {
			return;
		}
		mLock = true;
		mSessionId = null;
		CryptoUtil.setToken("");
		mChannel = channel;
		mAppVersion = appversion;
		mProtocolVersion = protocolversion;
		mRand = getRandomString();// "888888";
		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair(KEY_RAND, mRand));
		request(url, "InitRequestTask", null, true);
	}
	
	public static String getRandomString()
	{
		StringBuilder sb = new StringBuilder(LENGTH_RAND + 1);
		Random ra = new Random();
		ra.setSeed(System.currentTimeMillis());
		for (int i = 0; i < LENGTH_RAND; i++) {
			sb.append(CHARS[ra.nextInt(CHARS.length)]);
		}
		return sb.toString();
	}

	/**
	 * 初始化网络公共参数，请求token
	 * @param ctx 应用上下文
	 */
	public void init(Context ctx) {
		if (null == ctx)
			return;
		init(Constants.URL_INIT, "ANDROID", AppUtil.getVersionName(ctx), Constants.VERSION_PROTOCOL);
	}

	@Override
	protected String doInBackground(Void... params) {
		return HttpUtil.post(mUrl, mParams);
	}

	@Override
	protected void onPostExecute(String result) {
		try {
			mLock = false;
			JSONObject jobj = new JSONObject(result);
			int code = jobj.optInt(KEY_CODE);
			String msg = jobj.optString(KEY_MSG);
			String data = jobj.optString(KEY_DATA);
			if (0 != code) {
				LogUtil.w(TAG, KEY_CODE + "=" + code + "," + KEY_MSG + "=" + msg + "," + KEY_DATA + "=" + data);
				onResponse(code, msg, null);
			} else if (null == data || data.isEmpty()) {
				LogUtil.w(TAG, KEY_CODE + "=" + code + "," + KEY_MSG + "=" + msg + "," + KEY_DATA + "=" + result);
				onResponse(code, msg, null);
			} else {
				String str_data = CryptoUtil.decrypt(mRand, data);
				JSONObject jdata = new JSONObject(str_data);
				if (jdata.isNull(KEY_SESSIONID))
					return;
				mSessionId = jdata.optString(KEY_SESSIONID);
				if (jdata.isNull(KEY_TOKEN))
					return;
				String mToken = jdata.optString(KEY_TOKEN);
				CryptoUtil.setToken(mToken);
				LogUtil.w(TAG, KEY_CODE + "=" + code + "," + KEY_MSG + "=" + msg + "," + KEY_DATA + "=" + str_data);
				onResponse(code, msg, null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onResponse(99, "返回结果无法解释", result);
		} catch (Exception e) {
			e.printStackTrace();
			onResponse(999, "未知错误", result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		mLock = false;
	}

	@Override
	protected void onResponse(int code, String msg, String response) {
	}

}
