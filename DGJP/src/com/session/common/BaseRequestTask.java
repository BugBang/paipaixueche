package com.session.common;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;
import com.session.common.utils.CryptoUtil;
import com.session.common.utils.HttpUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.MD5Util;
import com.session.common.utils.SharedPreferencesUtil;
import com.session.common.utils.ToastUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.Constants;
import com.session.dgjp.enity.Account;
import com.session.dgjp.login.LoginActivity;
import com.session.dgjp.request.LoginRequestData;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 请求基类 */
public abstract class BaseRequestTask extends AsyncTask<Void, Integer, String> {
	
	public static final String KEY = "SessionTech123456";
	protected final static int CODE_SUCCESS = 0;
	protected final static int CODE_SESSION_ABATE = 4;
	protected final static int CODE_RE_INIT = 7;
	/** init中，初次init或者正在重连 */
	protected final static String ResponseInit = "{\"msg\":\"正在重新初始化网络请求\",\"code\":7}";
	/** 还没有进行初始化 */
	protected final static String ResponseNotInit = "{\"msg\":\"还没有初始化网络请求\",\"code\":8}";
	public final static String KEY_SESSIONID = "sessionId";
	public final static String KEY_CHANNEL = "channel";
	public final static String KEY_VERSION_APP = "appVersion";
	public final static String KEY_VERSION_PRT = "protocolVersion";
	public final static String KEY_TIME_STAMP = "timeStamp";
	public final static String KEY_AUTH_STRING = "authString";
	protected final static String KEY_CODE = "code";
	protected final static String KEY_MSG = "msg";
	protected final static String KEY_DATA = "data";
	protected static boolean mLock;
	protected static String mUrlInit;
	protected static String mChannel;
	protected static String mAppVersion;
	protected static String mProtocolVersion = Constants.VERSION_PROTOCOL;
	protected static String mSessionId;
	public static void resetSessionId() {
		BaseRequestTask.mSessionId = null;
	}
	/** 日志Tag */
	protected final String TAG = this.getClass().getName();
	/** 等待进度框 */
	protected ProgressDialog mProgressDialog;
	/** 请求地址 */
	protected String mUrl;
	/** 请求参数 */
	protected List<NameValuePair> mParams;

	/**
	 * 请求的响应
	 * @param code 状态码
	 * @param msg 状态描述
	 * @param response 返回的data属性，如果无法解释会返回整个网络请求返回
	 */
	protected abstract void onResponse(int code, String msg, String response);
	
	/**
	 * 设置全局的超时时间
	 * @param timeout_connect 建立连接超时时间
	 * @param timeout_so 连接传输数据超时时间
	 */
	public static void setTimeout(int timeout_connect, int timeout_so) {
		HttpUtil.setTimeout(timeout_connect, timeout_so);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (null != mProgressDialog && !mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		if (mLock)// 正在请求token时直接返回
			return ResponseInit;
		if (null == mSessionId)// 没有token时直接返回
			return ResponseNotInit;
		return HttpUtil.post(mUrl, mParams);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
        super.onPostExecute(result);
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
		// ToastUtil.showShort(BaseApplication.getInstance(), result);
		try {
            Logger.i("com.session.common.result解析前 = "+result);
            JSONObject jobj = new JSONObject(result);
			int code = jobj.optInt(KEY_CODE);
			String msg = jobj.optString(KEY_MSG);
            if (code == CODE_RE_INIT || code == CODE_SESSION_ABATE) {
				new InitRequestTask() {
					@Override
					protected void onResponse(int code, String msg, String response) {
						if (code == CODE_SUCCESS) {
							String phone = SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_ACCOUNT, null);
							String password = SharedPreferencesUtil.getString(SharedPreferencesUtil.KEY_PASSWORD, null);
							if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
								login(phone, password);
							} else {
								BaseRequestTask.this.onResponse(CODE_SESSION_ABATE, null, null);
							}
						} else {
							BaseRequestTask.this.onResponse(code, msg, response);
						}
					}
				}.init(AppInstance.getInstance());
				return;
			}
			if (jobj.isNull(KEY_DATA)) {
				onResponse(code, msg, null);
			} else {
				String data = jobj.optString(KEY_DATA);
				if (!TextUtils.isEmpty(data)) {
					String str_data = CryptoUtil.decrypt(data);
					onResponse(code, msg, str_data);
                    Logger.json(str_data);
				} else {
					onResponse(code, msg, null);
				}
			}
		} catch (JSONException e) {
            e.printStackTrace();
			onResponse(99, "返回结果无法解释", result);
		} catch (Exception e) {
			e.printStackTrace();
			onResponse(999, "未知错误", result);
		}
	}
	
	/** 登录 */
	private void login(String phone, String password) {
		LoginRequestData requestData = new LoginRequestData();
		requestData.setAccount(phone);
		requestData.setPassword(password);
		final String data = new Gson().toJson(requestData);
		new BaseRequestTask() {

			@Override
			protected void onResponse(int code, String msg, String response) {
				try {
					switch (code) {
					case CODE_SUCCESS:
						Account account = new Gson().fromJson(response, Account.class);
						if (account != null) {
							AppInstance.getInstance().setAccount(account);
							BaseRequestTask.this.onResponse(CODE_SESSION_ABATE, null, null);
						} else {
							gotoLogin();
						}
						break;
					default:
						gotoLogin();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.request(Constants.URL_LOGIN, data, null, true);
	}
	
	/** 进入登录页 */
	private void gotoLogin() {
		AppInstance instance = AppInstance.getInstance();
		ToastUtil.showShort(instance, "验证码失效，请重新登录");
		Intent intent = new Intent(instance, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		instance.startActivity(intent);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
		onResponse(99, "Task cancelled", null);
	}

	/**
	 * 执行请求
	 * @param url 请求地址
	 * @param data data参数
	 * @param dialog 等待进度框
	 * @param isSerial 是否串行，ture时串行执行请求，false并行执行
	 */
	public void request(final String url, final String data, final ProgressDialog dialog, final boolean isSerial) {
		mProgressDialog = dialog;
		if (TextUtils.isEmpty(url)) {
			onResponse(404, "请求地址为空", null);
			return;
		}
		if (TextUtils.isEmpty(mSessionId) && !data.equals("InitRequestTask")) {
			if (null != mProgressDialog && !mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
			new InitRequestTask() {

				@Override
				protected void onResponse(int code, String msg, String response) {
					if (null != BaseRequestTask.this.mProgressDialog && BaseRequestTask.this.mProgressDialog.isShowing()) {
						BaseRequestTask.this.mProgressDialog.cancel();
					}
					if (code == 0) {
						BaseRequestTask.this.request(url, data, dialog, isSerial);
					} else {
						BaseRequestTask.this.onResponse(code, msg, response);
					}
				}
			}.init(AppInstance.getInstance());
			return;
		}
		mUrl = url;
		if (null == mParams)
			mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair(KEY_CHANNEL, mChannel));
		mParams.add(new BasicNameValuePair(KEY_VERSION_APP, mAppVersion));
		mParams.add(new BasicNameValuePair(KEY_VERSION_PRT, mProtocolVersion));
		String timestamp = getTimeStamp();
		mParams.add(new BasicNameValuePair(KEY_TIME_STAMP, timestamp));
		String params_data = "";
		if (null != data && !data.equals("InitRequestTask")) {
			params_data = CryptoUtil.encrypt(data);
			mParams.add(new BasicNameValuePair(KEY_DATA, params_data));
		}
		if (null != mSessionId) {
			mParams.add(new BasicNameValuePair(KEY_SESSIONID, mSessionId));
		}
		mParams.add(new BasicNameValuePair(KEY_AUTH_STRING, MD5Util.encode(params_data + timestamp + KEY)));
		if (isSerial) {
			executeOnExecutor(SERIAL_EXECUTOR);
		} else {
			executeOnExecutor(THREAD_POOL_EXECUTOR);
		}
        LogUtil.e("com.session.common.url == ",url);
        LogUtil.e("com.session.common.params == ",URLEncodedUtils.format(mParams,"utf-8"));
    }

	public void request(String url, List<NameValuePair> params, ProgressDialog dialog) {
		if (TextUtils.isEmpty(url)) {
			onResponse(404, "请求地址为空", null);
			return;
		}
		mUrl = url;
		mProgressDialog = dialog;
		mParams = params;
		executeOnExecutor(THREAD_POOL_EXECUTOR);
	}

	/** 获取yyyyMMddHHmmss格式的当前时间 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeStamp() {
		String time = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			time = df.format(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 获取对象实例的参数键值对列表
	 * @param obj 需要获取参数键值对列表的实例对象
	 * @return 参数键值对列表
	 */
	public static List<NameValuePair> getParams(Object obj) {
		if (null == obj) {
			return null;
		}
		return getParams(obj, obj.getClass());
	}

	/**
	 * 递归获取参数列表
	 * @param obj 需要获取参数键值对列表的实例对象
	 * @param cls 实例对象的Class文件，应用反射机制
	 * @return 参数键值对列表
	 */
	private static List<NameValuePair> getParams(Object obj, Class<?> cls) {
		if (Object.class.getName().equals(cls.getName())) {// 防止同名不同包，判断完整的类名
			return null;
		}
		List<NameValuePair> listSuper = getParams(obj, cls.getSuperclass());
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (null != listSuper && !listSuper.isEmpty()) {
			list.addAll(listSuper);
		}
		try {
			Field[] fields = cls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);// 设置访问权限
				if (Modifier.isTransient(field.getModifiers())) {// 是否有transient修饰符，有的话忽略该属性
					continue;
				}
				String name = field.getName();// 属性名称
				if (name.equalsIgnoreCase("this$0") || name.equalsIgnoreCase("serialVersionUID")) {
					continue;// 内部类有this$0这个默认属性指向外部类，serialVersionUID不需要
				}
				if (field.isAnnotationPresent(FieldAlias.class)) {// 注解别名，Annotation是自定义的FieldAlias
					FieldAlias alias = field.getAnnotation(FieldAlias.class);
					name = alias.value();
				}
				if (field.isAnnotationPresent(SerializedName.class)) {// 注解别名，Annotation是Gson包的SerializedName
					SerializedName alias = field.getAnnotation(SerializedName.class);
					name = alias.value();
				}
				Object value = field.get(obj);// 获取属性值，需要从源对象中获取
				BasicNameValuePair nvp = new BasicNameValuePair(name, null == value ? "" : value.toString());
				list.add(nvp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
