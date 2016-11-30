package com.session.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * MD5工具类
 * @author JimChen
 */
public final class MD5Util {
	private final static char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private MD5Util() {
		// 不需要实例
	}

	/**
	 * 检验源串与MD5加密串，忽略大小写
	 * @param str 源串
	 * @param strMd5 MD5加密串
	 * @return 字符串是否相同
	 */
	public static boolean check(String str, String strMD5) {
		return str.equalsIgnoreCase(strMD5);
	}

	/**
	 * 把字节码转换成十六进制字符串
	 * @param b 字节码
	 * @return 十六进制字符串
	 */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 加密字符串成MD5字符串
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		try {
			byte[] buffer = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
			return toHexString(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 计算文件的MD5校验码
	 * @param filename 文件的绝对路径名
	 * @return MD5校验码
	 */
	public static String md5sum(String filename) {
		return md5sum(new File(filename));
	}

	/**
	 * 计算文件的MD5校验码
	 * @param dir 文件所在目录
	 * @param filename 文件名
	 * @return MD5校验码
	 */
	public static String md5sum(String dir, String filename) {
		return md5sum(new File(dir, filename));
	}

	/**
	 * 计算文件的MD5校验码
	 * @param file 文件
	 * @return MD5校验码
	 */
	public static String md5sum(File file) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
