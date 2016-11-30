package com.session.common.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

/**
 * ApplicationUtil
 * @author JimChen
 */
public final class AppUtil {
	private AppUtil() {
		// 工具类不需要实例
	}

	/**
	 * 判断应用是否可以Debug
	 * @param ctx 待判断应用的上下文
	 * @return 应用是否可以Debug
	 */
	public static boolean isDebuggable(Context ctx) {
		try {
			ApplicationInfo info = ctx.getApplicationInfo();
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取应用名称
	 * @param ctx 应用上下文
	 * @return 应用名
	 */
	public static String getAppName(Context ctx) {
		String label = null;
		try {
			ctx = ctx.getApplicationContext();
			PackageManager pm = ctx.getPackageManager();
			ApplicationInfo info = ctx.getApplicationInfo();
			label = (String) pm.getApplicationLabel(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return label;
	}
	

	/** 获取App的图标，优先使用Manifest配置Application标签的logo属性，次选icon属性 */
	public static Drawable getAppIcon(Context ctx) {
		Drawable icon = null;
		try {
			ctx = ctx.getApplicationContext();
			PackageManager pm = ctx.getPackageManager();
			ApplicationInfo info = ctx.getApplicationInfo();
			if (0 != info.logo)
				icon = pm.getApplicationLogo(info);
			if (null != icon)
				return icon;
			if (0 != info.icon)
				icon = pm.getApplicationIcon(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return icon;
	}

	/**
	 * 获取应用版本编码
	 * @param ctx 应用上下文
	 * @return 应用版本编码
	 */
	public static int getVersionCode(Context ctx) {
		try {
			ctx = ctx.getApplicationContext();
			PackageManager pm = ctx.getPackageManager();
			PackageInfo packInfo = pm.getPackageInfo(ctx.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取应用版本代号
	 * @param ctx 应用上下文
	 * @return 应用版本代号
	 */
	public static String getVersionName(Context ctx) {
		try {
			ctx = ctx.getApplicationContext();
			PackageManager pm = ctx.getPackageManager();
			PackageInfo packInfo = pm.getPackageInfo(ctx.getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SD卡是否存在
	 * @return SD卡是否存在
	 */
	public static boolean isExistedSD() {
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 检查网络是否已经连接
	 * @param context
	 * @return true 网络可用，false 网络不可用
	 */
	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		boolean isConnect = false;
		if (info != null && info.isAvailable()) {
			isConnect = true;
		} else {
			isConnect = false;
		}
		return isConnect;
	}

	/**
	 * 判断服务Service是否在后台运行
	 * @param ctx 上下文
	 * @param className 类名
	 * @return 是否运行
	 */
	public boolean isServiceRunning(Context ctx, String className) {
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(35);
		for (ActivityManager.RunningServiceInfo info : serviceList) {
			if (info.service.getClassName().equals(className) == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取Activity包含的meta-data
	 * @param act 包含meta-data的Activity
	 * @param key meta-data的key
	 * @return key对应的value
	 */
	public static String getActMetaData(Activity act, String key) {
		try {
			ActivityInfo info = act.getPackageManager().getActivityInfo(act.getComponentName(),
					PackageManager.GET_META_DATA);
			String value = null;
			if (null != info && null != info.metaData)
				value = info.metaData.getString(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取Application包含的meta-data
	 * @param ctx 上下文
	 * @param key meta-data的key
	 * @return key对应的value
	 */
	public static String getAppMetaData(Context ctx, String key) {
		try {
			ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
					PackageManager.GET_META_DATA);
			String value = null;
			if (null != info && null != info.metaData)
				value = info.metaData.getString(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 获取渠道，先获取Application渠道对应的meta-data值，如果没有，按照渠道文件放META-INF文件夹处理 */
	public static String getChannel(Context context) {
		String channel = getAppMetaData(context, "channel");
		if (null != channel) {
			return channel;
		}
		final String start_flag = "META-INF/channel_";
		ApplicationInfo appinfo = context.getApplicationInfo();
		String sourceDir = appinfo.sourceDir;
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(sourceDir);
			Enumeration<?> entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (entryName.contains(start_flag)) {
					channel = entryName.replace(start_flag, "");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (channel == null || channel.length() <= 0) {
			channel = "official";// 读不到渠道号就默认是官方渠道
		}
		return channel;
	}

	// 封装在SDK内不需要这个方法
	// /**
	// * 检查系统是否已经安装了flash插件
	// * @return true 安装，false 未安装
	// */
	// public static boolean checkFlash(Context ctx) {
	// PackageManager pm = ctx.getPackageManager();
	// List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_SERVICES);
	// for (PackageInfo info : infoList) {
	// if ("com.adobe.flashplayer".equals(info.packageName)) {
	// return true;
	// }
	// }
	// return false;
	// }

}
