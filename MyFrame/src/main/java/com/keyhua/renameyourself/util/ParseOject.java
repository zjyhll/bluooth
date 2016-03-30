package com.keyhua.renameyourself.util;

import java.util.Date;

public class ParseOject {
	// 整数转换String
	public static int ParseInt(Object obj) {
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	// 长整型类型转换String
	public static long ParseLong(Object obj) {
		try {
			return Long.parseLong(obj.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	// 布尔型转换String
	public static Boolean ParseBool(Object obj) {
		try {
			return Boolean.parseBoolean(obj.toString());
		} catch (Exception e) {
			return false;
		}
	}

	// 浮点型转换String
	public static Float ParseFloat(Object obj) {
		try {
			return Float.parseFloat(obj.toString());
		} catch (Exception e) {
			return Float.valueOf("0.00");
		}
	}

	// 双精度类型转换String
	public static Double ParseDouble(Object obj) {
		try {
			return Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return Double.valueOf("0.00");
		}
	}

	// 字符串转double
	public static String StringToDouble(Double str) {
		if (str != null) {
			// 保留两位数
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
			return df.format(str);
		} else {
			return "0";
		}
	}

	// 时间类型转换String
	public static Date ParseDate(Object obj) {
		try {
			return (Date) obj;
		} catch (Exception e) {
		}
		return new Date();
	}
}
