package com.session.common.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 固话、手机号码相关工具类 */
public final class NumberUtil {
	
	private final static DecimalFormat moneyFormat = new DecimalFormat("0.00");
	
	/** 用于匹配手机号码 */
	private final static String REGEX_MOBILEPHONE = "^0?1[34578]\\d{9}$";

	/** 用于匹配固定电话号码 */
	private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?(\\d{6,8})$";

	private static Pattern PATTERN_MOBILEPHONE;
	private static Pattern PATTERN_FIXEDPHONE;

	static {
		PATTERN_FIXEDPHONE = Pattern.compile(REGEX_FIXEDPHONE);
		PATTERN_MOBILEPHONE = Pattern.compile(REGEX_MOBILEPHONE);
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param number 手机号码
	 * @return
	 */
	public static boolean isCellPhone(String number) {
		Matcher match = PATTERN_MOBILEPHONE.matcher(number);
		return match.matches();
	}

	/**
	 * 判断是否为固定电话号码
	 * 
	 * @param number 固定电话号码
	 * @return
	 */
	public static boolean isFixedPhone(String number) {
		Matcher match = PATTERN_FIXEDPHONE.matcher(number);
		return match.matches();
	}

	/**
	 * 获取固定号码号码中的区号
	 * 
	 * @param strNumber
	 * @return
	 */
	public static String getZipFromHomephone(String strNumber) {
		Matcher matcher = PATTERN_FIXEDPHONE.matcher(strNumber);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	/** 判断号码是否为正常号码 */
	public static boolean isNumber(String number) {
		if (number != null && number.length() > 0) {
			if (isCellPhone(number) || isFixedPhone(number)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getMoneyFormat(double value)
	{
		return moneyFormat.format(value);
	}

}
