package com.session.common.utils;

import java.util.Map;

import android.content.SharedPreferences;

import com.session.dgjx.AppInstance;

/** SharedPreferences相关的工具类 */
public final class SharedPreferencesUtil {
	/** 是否记住密码的Key，值为布尔型 */
	public final static String KEY_REMEMBER_PASSWORD = "remember_password";
	/** 登录输入的账户，值为字符串 */
	public final static String KEY_ACCOUNT = "account";
	/** 用户真正的账号*/
	public final static String KEY_ACCOUNT_ACCOUNT = "account_account";
	/** 登录输入的密码，值为字符串 */
	public final static String KEY_PASSWORD = "password";
	/**自动登录*/
	public final static String KEY_AUTO_LOGIN = "auto_login";
	
	/**
	 * 获取SharedPreferences所有的键值对
	 */
	public static Map<String, ?> getAllKeyValue() {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return null;
		}
		return sp.getAll();
	}

	/** 保存字符串 */
	public static void saveString(String key, String value) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return;
		}
		sp.edit().putString(key, value).commit();
	}

	/** 保存布尔值 */
	public static void saveBoolean(String key, boolean value) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return;
		}
		sp.edit().putBoolean(key, value).commit();
	}

	/** 保存整型 */
	public static void saveInt(String key, int value) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return;
		}
		sp.edit().putInt(key, value).commit();
	}

	/** 保存浮点型 */
	public static void saveFloat(String key, float value) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return;
		}
		sp.edit().putFloat(key, value).commit();
	}

	/** 保存长整型 */
	public static void saveLong(String key, long value) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return;
		}
		sp.edit().putLong(key, value).commit();
	}

	/** 读取字符串 */
	public static String getString(String key, String defValue) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return null;
		}
		return sp.getString(key, defValue);
	}

	/** 读取布尔值 */
	public static boolean getBoolean(String key, boolean defValue) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return defValue;
		}
		return sp.getBoolean(key, defValue);
	}

	/** 读取整型 */
	public static int getInt(String key, int defValue) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return defValue;
		}
		return sp.getInt(key, defValue);
	}

	/** 读取浮点型 */
	public static float getFloat(String key, float defValue) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return defValue;
		}
		return sp.getFloat(key, defValue);
	}

	/** 读取长整型 */
	public static long getLong(String key, long defValue) {
		SharedPreferences sp = AppInstance.getInstance().getSp();
		if (null == sp) {
			return defValue;
		}
		return sp.getLong(key, defValue);
	}

	/**
	 * 获取SharedPreferences所有的键值对
	 */
	public static Map<String, ?> getAllKeyValue(SharedPreferences sp) {
		if (null == sp) {
			return null;
		}
		return sp.getAll();
	}

	/** 保存字符串 */
	public static void saveString(SharedPreferences sp, String key, String value) {
		if (null == sp) {
			return;
		}
		sp.edit().putString(key, value).commit();
	}

	/** 保存布尔值 */
	public static void saveBoolean(SharedPreferences sp, String key, boolean value) {
		if (null == sp) {
			return;
		}
		sp.edit().putBoolean(key, value).commit();
	}

	/** 保存整型 */
	public static void saveInt(SharedPreferences sp, String key, int value) {
		if (null == sp) {
			return;
		}
		sp.edit().putInt(key, value).commit();
	}

	/** 保存浮点型 */
	public static void saveFloat(SharedPreferences sp, String key, float value) {
		if (null == sp) {
			return;
		}
		sp.edit().putFloat(key, value).commit();
	}

	/** 保存长整型 */
	public static void saveLong(SharedPreferences sp, String key, long value) {
		if (null == sp) {
			return;
		}
		sp.edit().putLong(key, value).commit();
	}

	/** 读取字符串 */
	public static String getString(SharedPreferences sp, String key, String defValue) {
		if (null == sp) {
			return null;
		}
		return sp.getString(key, defValue);
	}

	/** 读取布尔值 */
	public static boolean getBoolean(SharedPreferences sp, String key, boolean defValue) {
		if (null == sp) {
			return defValue;
		}
		return sp.getBoolean(key, defValue);
	}

	/** 读取整型 */
	public static int getInt(SharedPreferences sp, String key, int defValue) {
		if (null == sp) {
			return defValue;
		}
		return sp.getInt(key, defValue);
	}

	/** 读取浮点型 */
	public static float getFloat(SharedPreferences sp, String key, float defValue) {
		if (null == sp) {
			return defValue;
		}
		return sp.getFloat(key, defValue);
	}

	/** 读取长整型 */
	public static long getLong(SharedPreferences sp, String key, long defValue) {
		if (null == sp) {
			return defValue;
		}
		return sp.getLong(key, defValue);
	}

}
