package com.keyhua.renameyourself.util;

import android.util.Log;

public class LogOut {
	private static final boolean LOG_FLAG = true;

	public static void systemOut(String str) {
		if (LOG_FLAG && str != null) {
			System.out.println(str);
		}
	}

	public static void exceptionOut(Exception e) {
		if (LOG_FLAG) {
			e.printStackTrace();
		}
	}

	public static void logV(String tag, String msg) {
		if (LOG_FLAG) {
			Log.v(tag, msg);
		}
	}

	public static void logD(String tag, String msg) {
		if (LOG_FLAG) {
			Log.d(tag, msg);
		}
	}

	public static void logI(String tag, String msg) {
		if (LOG_FLAG) {
			Log.i(tag, msg);
		}
	}

	public static void logW(String tag, String msg) {
		if (LOG_FLAG) {
			Log.w(tag, msg);
		}
	}

	public static void logE(String tag, String msg) {
		if (LOG_FLAG) {
			Log.e(tag, msg);
		}
	}

	// 调试日志控制开关 ,发布应用时全部置为false
	private boolean isSysOutPrintln = false;// 是否用System.out.println输出调试日志，发布时关闭
	private boolean isLogV = false;// 是否用Log.v输出调试日志，发布时关闭
	private boolean isLogD = false;// 是否用Log.d输出调试日志，发布时关闭
	private boolean isLogI = false;// 是否用Log.i输出调试日志，发布时关闭
	private boolean isLogW = false;// 是否用Log.w输出调试日志，发布时关闭
	private boolean isLogE = false;// 是否用Log.e输出调试日志，发布时关闭
	private boolean isDebugMode = false;// 获取堆栈信息会影响性能，发布时关闭

	private static boolean isCloseLogOut = false;

	private static boolean isCloseLogOut() {
		return isCloseLogOut;
	}

	private static void setCloseLogOut(boolean isClose) {
		isCloseLogOut = isClose;
	}

	/**
	 * 是否关闭调试日志输出
	 * 
	 * @param isClose
	 *            true关闭，默认 false 不关闭
	 */
	public static void closeLogOut(boolean isClose) {
		if (isClose) {
			setCloseLogOut(isClose);
		}
	}

	private static String div = "  :   ";

	/**
	 * System.out.println输出调试日志封装
	 * 
	 * @param Str
	 */
	public static void SysOutPrintln(String Str) {
		if (!isCloseLogOut()) {
			System.out.println(Str);
		}
	}

	/**
	 * System.out.println输出调试日志封装
	 * 
	 * @param Str
	 */
	public static void SysOutPrintln(String tag, String Str) {
		if (!isCloseLogOut()) {
			System.out.println(tag + div + Str);
		}
	}

	/**
	 * Log.v(tag, msg)输出调试日志封装
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogV(String tag, String msg) {
		if (!isCloseLogOut()) {
			Log.v(tag, msg);
		}
	}

	/**
	 * Log.d(tag,msg);输出调试日志封装
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogD(String tag, String msg) {
		if (!isCloseLogOut()) {
			Log.d(tag, msg);
		}
	}

	/**
	 * Log.i(String tag, String msg); 输出调试日志封装
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogI(String tag, String msg) {
		if (!isCloseLogOut()) {
			Log.i(tag, msg);
		}
	}

	/**
	 * Log.w(tag, msg)输出调试日志封装
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogW(String tag, String msg) {
		if (!isCloseLogOut()) {
			Log.w(tag, msg);
		}
	}

	/**
	 * Log.e(tag, msg)输出调试日志封装
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogE(String tag, String msg) {
		if (!isCloseLogOut()) {
			Log.e(tag, msg);
		}
	}

	private static String LogTag = "LogHelper";
	private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s()  Line:%d  (%s)";

	/**
	 * 获取堆栈信息
	 */
	public static void trace() {
		if (!isCloseLogOut()) {
			StackTraceElement traceElement = Thread.currentThread()
					.getStackTrace()[3];// 从堆栈信息中获取当前被调用的方法信息
			String logText = String.format(CLASS_METHOD_LINE_FORMAT,
					traceElement.getClassName(), traceElement.getMethodName(),
					traceElement.getLineNumber(), traceElement.getFileName());
			Log.d(LogTag, logText);// 打印Log
		}
	}

}
