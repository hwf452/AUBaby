package com.halong.aubaby.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理类
 * 字符串是星期几
 * 字符串离现在间隔时间
 * @author lihua
 */
public class TimeFormatUtil {
	
	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long day7 = 7 * day;// 月
	private static SimpleDateFormat mDateFormat;

	private static SimpleDateFormat getDateFormat() {
		if (mDateFormat == null) {
			mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		return mDateFormat;
	}

	/**
	 * 返回文字描述的日期 ：刚刚、几分钟前、几小时前
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText(String str) {
		Date date = null;

		try {
			date = getDateFormat().parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (date == null) {
			return str;
		}

		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if (diff > day7) {
			return (date.getMonth()+1)+"月"+date.getDate()+"日";
		}
		if (diff > day) {
			Date today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			diff -= (new Date().getTime()-today.getTime());
			r = (diff / day)+1;
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}
}
