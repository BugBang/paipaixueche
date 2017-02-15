package com.session.common.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {
    public static final String NETWORK_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat NETWORK_TIME_SDF = new SimpleDateFormat(NETWORK_TIME_FORMAT, Locale.getDefault());
    public static final String NETWORK_DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat NETWORK_DATE_SDF = new SimpleDateFormat(NETWORK_DATE_FORMAT, Locale.getDefault());
    public static final SimpleDateFormat LOCAL_DATE_SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat LOCAL_TIME_SDF = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static final SimpleDateFormat LOCAL_SIMPLE_SDF = new SimpleDateFormat("M月d日", Locale.getDefault());

    /**
     * 获取两个时间的小时差，如果结果为正，表示time1时间晚于time2,如果为负，表示time1时间早于time2
     *
     * @param time1 1970.1.1到该时间点的毫秒数
     * @param time2 1970.1.1到该时间点的毫秒数
     * @return
     * @author YJ Liang
     * 2016  上午9:49:51
     */
    public static int hourDiff(long time1, long time2) {
        return (int) ((time1 - time2) / (1000 * 60 * 60));
    }

    public static String getTimePeroidString(Date beginDate, Date endDate) {
        String dateStr = LOCAL_DATE_SDF.format(beginDate);
        String beginTimeStr = LOCAL_TIME_SDF.format(beginDate);
        String endTimeStr = LOCAL_TIME_SDF.format(endDate);
        return dateStr + " " + beginTimeStr + "-" + endTimeStr;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     * @author BAO
     */
    public static String getWeekOfDate() {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static List<Long> getWeekDayList(String date, String formatSrt) {
        // 存放每一天时间的集合
        List<Long> weekMillisList = new ArrayList<Long>();
        long dateMill = 0;
        try {
            // 获取date的毫秒值
            dateMill = getMillis(date, formatSrt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMill);
        // 本周的第几天
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e("本周第几天", weekNumber + "");
        // 获取本周一的毫秒值
        long mondayMill = dateMill - 86400000 * (weekNumber - 2);

        for (int i = 0; i < 7; i++) {
            weekMillisList.add(mondayMill + 86400000 * i);
        }
        return weekMillisList;
    }

    public static long getMillis(String time, String formatSrt) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(formatSrt);
        return format.parse(time).getTime();
    }
    public static String formatDate(Long date, String format) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
