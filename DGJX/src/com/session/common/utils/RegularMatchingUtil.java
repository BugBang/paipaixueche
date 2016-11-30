package com.session.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularMatchingUtil {
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	public static final String US_ASCII = "US-ASCII";
	/** ISO拉丁字母表 No.1，也叫做ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";
	/** 8 位 UCS 转换格式 */
	public static final String UTF_8 = "UTF-8";
	/** 16 位 UCS 转换格式，Big Endian(最低地址存放高位字节）字节顺序 */
	public static final String UTF_16BE = "UTF-16BE";
	/** 16 位 UCS 转换格式，Litter Endian（最高地址存放地位字节）字节顺序 */
	public static final String UTF_16LE = "UTF-16LE";
	/** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	public static final String UTF_16 = "UTF-16";
	/** 中文超大字符集 **/
	public static final String GBK = "GBK";
	/** 中文超大字符集 */
	public static final String GB2312 = "GB2312";

	private RegularMatchingUtil() {
		// 工具类不需要实例
	}

	/** 匹配房屋楼栋二维码，相关信息保存到res */
	public final static boolean match(String[] res, String str) {
		if (null == res) {
			return false;
		}
//		str = "44030600200304T0009下沙二巷1号*wj*联系电话：29608835";
		Pattern p = Pattern.compile("^([0-9|T]{19,25})(.+)(?:咨询|联系)(?:电话|方式)[:|：](.+)$");// 房屋19位编码，楼栋25位编码，贪婪模式匹配，以25位优先
		Matcher m = p.matcher(str);
		if (m.find()) {
			int iMax = m.groupCount() <= res.length ? m.groupCount() : res.length;
			for (int i = 0; i < iMax; i++) {
				res[i] = m.group(i + 1);
			}
			return true;
		} else {
			return false;
		}
	}

	// /** 重新编码 */
	// public final static String recode(String str) {// 没有效果
	// String formart = "";
	// boolean ISO = Charset.forName(ISO_8859_1).newEncoder().canEncode(str);
	// if (ISO) {
	// formart = changeCharset(str, ISO_8859_1, GB2312);
	// if (null == formart) {
	// formart = "";
	// }
	// } else {
	// formart = str;
	// }
	// return formart;
	// }

	/** 将字符编码转换成US-ASCII码 */
	public static String toASCII(String str) {
		return changeCharset(str, US_ASCII);
	}

	/** 将字符编码转换成ISO-8859-1 */
	public static String toISO_8859_1(String str) {
		return changeCharset(str, ISO_8859_1);
	}

	/** 将字符编码转换成UTF-8 */
	public static String toUTF_8(String str) {
		return changeCharset(str, UTF_8);
	}

	/** 将字符编码转换成UTF-16BE */
	public static String toUTF_16BE(String str) {
		return changeCharset(str, UTF_16BE);
	}

	/** 将字符编码转换成UTF-16LE */
	public static String toUTF_16LE(String str) {
		return changeCharset(str, UTF_16LE);
	}

	/** 将字符编码转换成UTF-16 */
	public static String toUTF_16(String str) {
		return changeCharset(str, UTF_16);
	}

	/** 将字符编码转换成GBK */
	public static String toGBK(String str) {
		return changeCharset(str, GBK);
	}

	/** 将字符编码转换成GB2312 */
	public static String toGB2312(String str) {
		return changeCharset(str, GB2312);
	}

	/**
	 * 字符串编码转换的实现方法
	 * @param str 待转换的字符串
	 * @param newCharset 目标编码
	 */
	public static String changeCharset(String str, String newCharset) {
		if (str != null) {
			// 用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
			try {
				byte[] bs = str.getBytes();
				return new String(bs, newCharset); // 用新的字符编码生成字符串
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 字符串编码转换的实现方法
	 * @param str 待转换的字符串
	 * @param oldCharset 源字符集
	 * @param newCharset 目标字符集
	 */
	public static String changeCharset(String str, String oldCharset, String newCharset) {
		if (str != null) {
			// 用源字符编码解码字符串
			try {
				byte[] bs = str.getBytes(oldCharset);
				return new String(bs, newCharset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
