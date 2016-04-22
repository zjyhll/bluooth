package com.keyhua.renameyourself.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间显示的多种格式类 以不同方法实现日期时间的不同显示格式
 * 
 * @author 逍湘 QQ：297187963 E-mail：tylz7758@163.com
 * @version 1.0
 * @time 2007年7月26日 上午10时23分51秒
 */
public class TimeUtil {

	/**
	 * 以字符串格式显示日期时间(Thu Jul 26 10:23:51 CST 2007)
	 * 
	 * @return datetime
	 */
	public String getDatetime_String1() {
		String datetime = new Date().toString();
		return datetime;
	}

	/**
	 * 以字符串格式显示日期时间(26 Jul 2007 02:23:51 GMT)
	 * 
	 * @return datetime
	 */
	public String getDatetime_String2() {
		String datetime = new Date().toGMTString();
		return datetime;
	}

	/**
	 * 以系统格式显示日期时间(yy-MM-dd 上午HH:mm)
	 * 
	 * @return datetime
	 */
	public static String getDatetime_System() {
		DateFormat dt = DateFormat.getInstance();
		String datetime = dt.format(new Date()).toString();
		return datetime;
	}

	/**
	 * 以中国格式显示日期时间(xxxx年xx月xx日 下午xx时xx分xx秒)
	 * 
	 * @return datetime
	 */
	public String getDatetime_China() {
		DateFormat datetime1 = DateFormat.getDateInstance(DateFormat.LONG,
				Locale.CHINA);
		DateFormat datetime2 = DateFormat.getTimeInstance(DateFormat.LONG,
				Locale.CHINA);
		String datetime = datetime1.format(new Date()) + " "
				+ datetime2.format(new Date());
		return datetime;
	}

	/**
	 * 以常用格式显示日期时间(yyyy-MM-dd HH:mm:ss+MILLISECOND)
	 * 
	 * @return datetime
	 */
	public String getDatetime_Standard() {
		Calendar now = Calendar.getInstance();
		String datetime = now.get(Calendar.YEAR) + "-"
				+ (now.get(Calendar.MONTH) + 1) + "-"
				+ now.get(Calendar.DAY_OF_MONTH) + " " + now.get(Calendar.HOUR)
				+ ":" + now.get(Calendar.MINUTE) + ":"
				+ now.get(Calendar.SECOND) + now.get(Calendar.MILLISECOND);
		return datetime;
	}

	/**
	 * 以常用格式显示日期时间(yyyy-MM-dd HH:mm:ss)
	 * TODO 程序中用的都是这个
	 * @return datetime
	 */
	public static String getDatetime() {
		SimpleDateFormat f = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date datetime = new Date();
		return f.format(datetime);
	}

	/**
	 * 以简单系统格式显示日期时间(yyyy-MM-dd)
	 * 
	 * @return
	 */
	public static String getDatetime_SimpleDateFormat() {
		try {
			SimpleDateFormat f = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String sDate = f.format(new Date());
			Date dt = f.parse(sDate);
			java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
			String datetime = sqlDate.toString();
			return datetime;
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
	}

	/**
	 * 以简单系统格式显示日期时间(yyyy-MM-dd)
	 * 
	 * @return
	 */
	public static String getDatetime_SimpleDateFormat1() {
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			String sDate = f.format(new Date());
			Date dt = f.parse(sDate);
			java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
			String datetime = sqlDate.toString();
			return datetime;
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
	}

	/**
	 * 测试 显示结果
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("以字符串格式显示日期时间: "
				+ new TimeUtil().getDatetime_String1());
		System.out.println("以字符串格式显示日期时间: "
				+ new TimeUtil().getDatetime_String2());
		System.out.println("以系统格式显示日期时间: "
				+ new TimeUtil().getDatetime_System());
		System.out
				.println("以中国格式显示日期时间: " + new TimeUtil().getDatetime_China());
		System.out.println("以常用格式显示日期时间: "
				+ new TimeUtil().getDatetime_Standard());
		System.out.println("以常用格式显示日期时间: " + new TimeUtil().getDatetime());
		System.out.println("以简单系统格式显示日期: "
				+ new TimeUtil().getDatetime_SimpleDateFormat());
	}

}
