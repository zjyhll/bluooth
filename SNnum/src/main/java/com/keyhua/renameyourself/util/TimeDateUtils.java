package com.keyhua.renameyourself.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Time;

public class TimeDateUtils {
	public static final String EMPTY_STRING = "";

	public static Date getDate(String dataString) {
		return getDate(dataString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getDate(String dataStr, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = null;

		try {
			date = f.parse(dataStr);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		return date;
	}

	public static String formatDate(long date, String format) {
		return DateFormat.format(format, date).toString();
	}

	public static String formatDate(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return sf.format(date);
	}

	public static String formatDateFromDatabase(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return sf.format(date);
	}

	/**
	 * @param time
	 * @return
	 */
	public static String formatDateFromDatabaseTime(String time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		if (!TextUtils.isEmpty(time)) {
			try {
				date = sf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date != null ? sf.format(date) : "";
		} else {
			return "";
		}

	}

	public static String formatDateFromDatabaseTimeSF(String time) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		if (!TextUtils.isEmpty(time)) {
			try {
				date = sf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date != null ? sf.format(date) : "";
		} else {
			return "";
		}

	}

	public static long getSystemTime() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;//
		int date = time.monthDay;
		int hour = time.hour; // 0-23
		int minute = time.minute;
		int second = time.second;
		String sMonth, sDate, sHour, sMinute, sSecond;
		if (month < 10) {
			sMonth = "0" + month;
		} else {
			sMonth = "" + month;
		}
		if (date < 10) {
			sDate = "0" + date;
		} else {
			sDate = "" + date;
		}
		if (hour < 10) {
			sHour = "0" + hour;
		} else {
			sHour = "" + hour;
		}
		if (minute < 10) {
			sMinute = "0" + minute;
		} else {
			sMinute = "" + minute;
		}
		if (second < 10) {
			sSecond = "0" + second;
		} else {
			sSecond = "" + second;
		}
		long currentTime = Long.parseLong(year + sMonth + sDate + sHour + sMinute + sSecond);
		return currentTime;
	}

	public static String getSystemTime(String pattern) {
		if (pattern == null || pattern.trim().length() < 5) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		return formatter.format(curDate);
	}

	public static long getAllTime(String start, String end) {
		if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long date1 = 0;
			long date2 = 0;
			try {
				date1 = sdf.parse(start).getTime();
				date2 = sdf.parse(end).getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			long now = (date2 - date1) / (3600 * 1000);
			return now;
		} else {
			return 0;
		}
	}

}
