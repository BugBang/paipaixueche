package com.session.common.utils;

/** 日记工具类 */
public final class LogUtil {
	private LogUtil() {
		// 工具类不需要实例
	}

	/** 是否打印日记 */
	private static boolean Debuggable = true;
	/** 日志Level，只打印高于Level的日志 */
	private static int Level = 0;

	/**
	 * Priority constant for the println method; use Log.v.
	 */
	public static final int VERBOSE = android.util.Log.VERBOSE;

	/**
	 * Priority constant for the println method; use Log.d.
	 */
	public static final int DEBUG = android.util.Log.DEBUG;

	/**
	 * Priority constant for the println method; use Log.i.
	 */
	public static final int INFO = android.util.Log.INFO;

	/**
	 * Priority constant for the println method; use Log.w.
	 */
	public static final int WARN = android.util.Log.WARN;

	/**
	 * Priority constant for the println method; use Log.e.
	 */
	public static final int ERROR = android.util.Log.ERROR;

	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = android.util.Log.ASSERT;

	/**
	 * 设置是否打印日记
	 * @param debuggable true表示打印日志到logcat，false则不打印
	 */
	public static void setDebuggable(boolean debuggable) {
		LogUtil.Debuggable = debuggable;
	}

	/**
	 * 设置打印日记的等级
	 * @param level 日记的级别{@link #VERBOSE}，{@link #DEBUG}，{@link #INFO}，{@link #WARN}，{@link #ERROR}，{@link #ASSERT}
	 */
	public static void setLevel(int level) {
		LogUtil.Level = level;
	}

	/**
	 * Send a VERBOSE log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 */
	public static void v(String tag, String msg) {
		if (Debuggable && Level <= VERBOSE)
			android.util.Log.v(tag, msg);
	}

	/**
	 * Send a VERBOSE log message and log the exception.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void v(String tag, String msg, Throwable tr) {
		if (Debuggable && Level <= VERBOSE)
			android.util.Log.v(tag, msg, tr);
	}

	/**
	 * Send a DEBUG log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 */
	public static void d(String tag, String msg) {
		if (Debuggable && Level <= DEBUG)
			android.util.Log.d(tag, msg);
	}

	/**
	 * Send a DEBUG log message and log the exception.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if (Debuggable && Level <= DEBUG)
			android.util.Log.d(tag, msg, tr);
	}

	/**
	 * Send an INFO log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 */
	public static void i(String tag, String msg) {
		if (Debuggable && Level <= INFO)
			android.util.Log.i(tag, msg);
	}

	/**
	 * Send a INFO log message and log the exception.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void i(String tag, String msg, Throwable tr) {
		if (Debuggable && Level <= INFO)
			android.util.Log.i(tag, msg, tr);
	}

	/**
	 * Send a WARN log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 */
	public static void w(String tag, String msg) {
		if (Debuggable && Level <= WARN)
			android.util.Log.w(tag, msg);
	}

	/**
	 * Send a WARN log message and log the exception.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void w(String tag, String msg, Throwable tr) {
		if (Debuggable && Level <= WARN)
			android.util.Log.w(tag, msg, tr);
	}

	/**
	 * Send a WARN log message and log the exception.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param tr An exception to log
	 */
	public static void w(String tag, Throwable tr) {
		if (Debuggable && Level <= WARN)
			android.util.Log.w(tag, tr);
	}

	/**
	 * Send an ERROR log message.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		if (Debuggable && Level <= ERROR)
			android.util.Log.e(tag, msg);
	}

	/**
	 * Send a ERROR log message and log the exception.
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 * @param tr An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (Debuggable && Level <= ERROR)
			android.util.Log.e(tag, msg, tr);
	}

}
