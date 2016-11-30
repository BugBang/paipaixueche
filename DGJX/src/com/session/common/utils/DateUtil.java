package com.session.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
	public static final String NETWORK_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat NETWORK_TIME_SDF = new SimpleDateFormat(NETWORK_TIME_FORMAT,Locale.getDefault());
	public static final String NETWORK_DATE_FORMAT = "yyyy-MM-dd";
	public static final SimpleDateFormat NETWORK_DATE_SDF = new SimpleDateFormat(NETWORK_DATE_FORMAT,Locale.getDefault());
	public static final SimpleDateFormat LOCAL_DATE_SDF = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
	public static final SimpleDateFormat LOCAL_TIME_SDF = new SimpleDateFormat("HH:mm",Locale.getDefault());
	public static final SimpleDateFormat LOCAL_SIMPLE_SDF = new SimpleDateFormat("M月d日",Locale.getDefault());
	/**
	 * 获取两个时间的小时差，如果结果为正，表示time1时间晚于time2,如果为负，表示time1时间早于time2
	 * @author YJ Liang
	 * 2016  上午9:49:51
	 * @param time1 1970.1.1到该时间点的毫秒数
	 * @param time2 1970.1.1到该时间点的毫秒数
	 * @return
	 */
	public static int hourDiff(long time1, long time2)
	{
		return (int)((time1-time2)/(1000*60*60));
	}
	
	public static String getTimePeroidString(Date beginDate, Date endDate)
	{
		String dateStr = LOCAL_DATE_SDF.format(beginDate);
		String beginTimeStr = LOCAL_TIME_SDF.format(beginDate);
		String endTimeStr = LOCAL_TIME_SDF.format(endDate);
		return dateStr +" "+ beginTimeStr+"-"+endTimeStr;
	}
}
