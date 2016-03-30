package com.keyhua.renameyourself.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

public class StringUtils {
	public static final String EMPTY_STRING = "";
	/**
	 * 定义联通号段
	 */
	private static final String[] TEL_LT = { "130", "131", "132", "156", "185",
			"186" };
	/**
	 * 定义移动号段
	 */
	private static final String[] TEL_YD = { "134", "135", "136", "137", "138",
			"139", "145", "147", "150", "151", "152", "155", "157", "158",
			"159", "182", "187", "188" };

	/**
	 * 定义电信号段
	 * 
	 */
	private static final String[] TEL_DX = { "133", "153", "180", "189" };

	/**
	 * 判断字符串是否是手机号码,以联通或移动的开头
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isTel(String phone) {
		if (!StringUtils.isNumeric(phone))
			return false;

		int size = TEL_LT.length;
		for (int i = 0; i < size; i++) {
			if (phone.startsWith(TEL_LT[i])) {
				return true;
			}
		}
		size = TEL_YD.length;
		for (int i = 0; i < size; i++) {
			if (phone.startsWith(TEL_YD[i])) {
				return true;
			}
		}
		size = TEL_DX.length;
		for (int i = 0; i < size; i++) {
			if (phone.startsWith(TEL_DX[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 截取手机号码
	 * 
	 * @param number
	 * @return
	 */
	public static String splitePhoneNumber(String number) {
		String mobile = "";
		if (number != null && number.length() >= 11) {
			mobile = number.substring(number.length() - 11);
			LogOut.logD("StringUtils", "number" + number + "mobile:" + mobile);
		}
		return mobile;
	}


	/**
	 * 字符串工具类 功能： 1、判断字符串是否为空 2、判断是否是有效的电话号码 3、替换字符串 4、转换url 5、字符串分割 6、字符串分行
	 * 7、判断字符串是否是数字 8、转换密码
	 */
	private StringUtils() {

	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.equals("null") || str.equals("") || str
				.equals("null null"));//
	}

	public static boolean isNullOrEmpty(Object value) {
		return (value == null) || ("".equals(value)) || ("null".equals(value));
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String encodeURl(String url) {
		try {
			// url = new String(url.getBytes("iso-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		/***************** ******************/
		String protocol = url.substring(0, url.indexOf("//") + 2);
		String paramString = url.substring(url.indexOf("?"), url.length());
		url = url.substring(url.indexOf(protocol) + protocol.length(),
				url.length() - paramString.length());

		String[] tempUrlArray = url.split("/");
		url = protocol;
		for (int i = 0; i < tempUrlArray.length; i++) {
			if (tempUrlArray[i].indexOf("?") == -1) {
				url += URLEncoder.encode(tempUrlArray[i]);
				if (i < tempUrlArray.length - 1)
					url += "/";

			} else {
				if (!url.endsWith("/"))
					url += "/" + tempUrlArray[i];
				else
					url += tempUrlArray[i];
			}
		}

		url += paramString;

		/***************** ******************/
		int paramIndex = url.indexOf("?");
		if (paramIndex != -1) {
			StringBuffer urlLink = new StringBuffer(
					url.substring(0, paramIndex));
			String[] paramters = url.substring(paramIndex + 1, url.length())
					.split("&");

			if (paramters.length > 0) {
				urlLink.append("?");
			}

			for (int i = 0; i < paramters.length; i++) {
				String[] valueName = paramters[i].split("=");
				urlLink.append(URLEncoder.encode(valueName[0]));

				if (paramters[i].indexOf("=") != -1)
					urlLink.append("=");

				if (valueName.length > 1) {
					urlLink.append(URLEncoder.encode(valueName[1]));
				}

				if (i < paramters.length - 1) {
					urlLink.append("&");
				}
			}

			url = urlLink.toString();
		} else {
			String paramStr = url.substring(url.lastIndexOf("/") + 1,
					url.length());
			StringBuffer urlLink = new StringBuffer(url.substring(0,
					url.lastIndexOf("/")));
			if (paramStr.indexOf("&") == -1) {
				return urlLink + "/" + URLEncoder.encode(paramStr);
			}

		}

		return url;
	}

	/**
	 * �滻Ŀ���ַ�str��str1Ϊstr2
	 */
	public static String replaceString(String str, String str1, String str2) {
		if (isEmpty(str) || isEmpty(str1)) {
			return str;
		}

		if (str1.equals(str2)) {
			return str;
		}

		StringBuffer temp = new StringBuffer(str);

		int post = temp.toString().indexOf(str1);

		while (post > -1) {
			temp.delete(post, post + str1.length());
			temp.insert(post, str2);
			post = temp.toString().indexOf(str1);
		}
		return temp.toString();
	}

	/**
	 * ʹ���ַ������е������滻�ַ�str�е�"%S"
	 */
	public static String formatString(String str, String[] temp) {
		StringBuffer sb = new StringBuffer(str);
		int post = -1;
		for (int i = 0; i < temp.length; i++) {
			post = sb.toString().indexOf("%S");
			if (post > -1) {
				sb.delete(post, post + 2);
				sb.insert(post, temp[i]);
			}
		}
		return sb.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// ///
	public static String[] parserUrl(String url) {
		String[] str = new String[3];
		int poste = url.indexOf("?");
		int post = url.indexOf("http://");
		int postl = 0;
		if (post > -1) {
			postl = url.substring(post + 7).indexOf("/");
			if (postl > -1) {
				str[0] = "http://"
						+ url.substring(post + 7, post + 8 + postl - 1);
				if (poste < 0) {
					str[1] = url.substring(post + 8 + postl - 1);
				} else {
					str[1] = url.substring(post + 8 + postl - 1, poste);
					str[2] = url.substring(poste);
				}
				return str;
			}
		}
		return null;
	}

	public static String checkPushPage(String data, String url) {
		String[] str = StringUtils.parserUrl(url);
		if (str != null) {
			int post = data.indexOf(str[1]);
			int post1 = 0;
			if (post > -1) {
				post1 = data.substring(post + 1).indexOf("\"");
				if (post1 > -1) {
					return str[0] + data.substring(post, post + post1 + 1);
				}
			}
		}
		return url;
	}

	// //////////////////////////////////////////////////////////////////////////
	// ///
	/**
	 * @brief �ָ��ַ�ԭ�?����ַ��еķָ��ַ�Ȼ��ȡ�Ӵ�
	 * @param original
	 *            ��Ҫ�ָ���ַ�
	 * @paran regex �ָ��ַ�
	 * @return String[] �ָ����ɵ��ַ�����
	 */

	public static String[] split(String original, String regex) {
		int startIndex = 0; // ȡ�Ӵ�����ʼλ��
		Vector<String> v = new Vector<String>(); // ���������ȷ���Vector��
		String[] str = null; // ���صĽ���ַ�����
		int index = 0; // �洢ȡ�Ӵ�ʱ��ʼλ��

		startIndex = original.indexOf(regex); // ���ƥ���Ӵ���λ��
		// �����ʼ�ַ��λ��С���ַ�ĳ��ȣ���֤��û��ȡ���ַ�ĩβ��
		// -1���ȡ����ĩβ
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			v.addElement(temp); // ȡ�Ӵ�
			index = startIndex + regex.length(); // ����ȡ�Ӵ�����ʼλ��
			startIndex = original.indexOf(regex, startIndex + regex.length());
			// ���ƥ���Ӵ���λ��
		} // ȡ������Ӵ�
		v.addElement(original.substring(index + 1 - regex.length()));
		str = new String[v.size()]; // ��Vector����ת��������
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		return str;
	}

	/**
	 * 
	 * 
	 * @param text
	 *            String
	 * @return boolean
	 */
	public static boolean isNumeric(String text) {
		LogOut.systemOut(text);
		char[] chars = text.toCharArray();
		if (chars.length == 0) {
			return false;
		}
		int start = 0;
		if (chars[0] == '-') {
			start = 1;
		}
		boolean dotEncountered = false;
		for (int i = start; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isDigit(c)) {
				// that's okay
			} else if (!dotEncountered && c == '.') {
				dotEncountered = true;
			} else {
				return false;
			}
		}
		return true;
	}

	public static String parsePassword(String pw) {
		if (pw == null)
			return null;

		int size = pw.length();
		if (size == 0)
			return null;

		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < size; i++) {
			buff.append("*");
		}
		return buff.toString();
	}

	private static Paint paint;
	private static Rect rect;

	public static int getTextWidth(char text, float size) {
		if (paint == null) {
			paint = new Paint();
		}
		paint.setTextSize(size);
		if (rect == null) {
			rect = new Rect();
		}
		paint.getTextBounds(new char[] { text }, 0, 1, rect);
		int w = rect.width();

		return w;
	}

	public static Vector<String> parseString(String s, float textsize, float w) {
		if (s == null || s.equals("")) {
			return null;
		}

		Vector<String> messages = new Vector<String>();
		int totalLine = 1;
		Vector<Integer> vLine = new Vector<Integer>();
		vLine.addElement(new Integer(0));
		int currentLen = 0;
		int singleWordLen = 0;
		char[] charAry = s.toCharArray();
		int lastSpaceIndex = 0;
		int lineLen = 0;
		for (int i = 0; i < charAry.length; ++i) {

			singleWordLen = getTextWidth(charAry[i], textsize);
			if (singleWordLen == 0) {
				singleWordLen = (int) ((textsize) / 2 + 1);
			}
			if (charAry[i] == '\n' || charAry[i] == '\r' || charAry[i] == '\t') {
				vLine.addElement(new Integer((i - 1)));
				currentLen = 0;
				++totalLine;
				lastSpaceIndex = i + 1;
			} else if (charAry[i] == '\\'
					&& ((i + 1) < charAry.length)
					&& (charAry[i + 1] == 'n' || charAry[i + 1] == 'r' || charAry[i + 1] == 't')) {
				vLine.addElement(new Integer((i - 1)));
				currentLen = 0;
				++totalLine;
				++i;
				lastSpaceIndex = i + 1;
			} else if (currentLen + singleWordLen > w - textsize * 2) {
				if (lineLen > 0) {
					vLine.addElement(new Integer(lastSpaceIndex));
					currentLen = currentLen + singleWordLen - lineLen;
					lineLen = 0;
				} else {
					vLine.addElement(new Integer(i));
					currentLen = 0;
					lineLen = 0;
					lastSpaceIndex = i + 1;
				}
				++totalLine;
			} else {
				if ((((charAry[i] & 0xFF00) > 0) || charAry[i] == ' '
						|| charAry[i] == '-' || charAry[i] == '\t' || charAry[i] == '\r')
						&& i != 0) {
					lastSpaceIndex = i;
					lineLen = currentLen + singleWordLen;
				}
				if (charAry[i] != ' ' && charAry[i] != '-'
						&& charAry[i] != '\r') {
					currentLen += singleWordLen;
				} else {
					currentLen += singleWordLen / 2;
				}
			}
		}
		vLine.addElement(new Integer((s.length() - 1)));

		int startIndex;
		int lastIndex;

		for (int i = 0; i < totalLine; ++i) {
			if (i < totalLine - 1) {
				lastIndex = ((Integer) (vLine.elementAt((i + 1)))).intValue();
			} else {

				lastIndex = s.length() - 1;
			}

			if (i > 0) {
				startIndex = ((Integer) (vLine.elementAt((i)))).intValue() + 1;
			} else {
				startIndex = 0;
			}
			if (startIndex <= charAry.length - 1) {
				if (charAry[startIndex] == '\n' || charAry[startIndex] == '\t'
						|| charAry[startIndex] == '\r') {
					++startIndex;
				} else if (charAry[startIndex] == '\\'
						&& ((startIndex + 1) < charAry.length)
						&& (charAry[startIndex + 1] == 'n'
								|| charAry[startIndex + 1] == 't' || charAry[startIndex + 1] == 'r')) {
					startIndex += 2;
				}
			}

			if (startIndex > lastIndex) {
				messages.addElement("");
			} else {
				messages.addElement(s.substring(startIndex, lastIndex + 1)
						.trim());
			}
		}
		System.gc();
		return messages;
	}

	/**
	 * 截取字符串
	 * 
	 * @param arrStr
	 * @param mSplit
	 *            截取表示 ，注意 转义 \\
	 * @param i
	 * @return
	 */
	public static String getSplitStr(String arrStr, String mSplit, int i) {

		String result = "";
		if (arrStr != null && !"".equals(arrStr)) {
			String[] tempStr = arrStr.split(mSplit);
			if (tempStr != null && tempStr.length > i) {
				result = tempStr[i];
			}
		}
		return result;
	}

	/**
	 * 检查是否合法的电话号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {

		boolean isValid = false;
		String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";
		String expression2 = "^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);

		Matcher matcher2 = pattern2.matcher(inputStr);

		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}

		return isValid;

	}

	/**
	 * 得到文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNa(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
	}

	/**
	 * 得到文件後綴名
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileEx(String url) {
		return url.substring(url.lastIndexOf("."));
	}

	/**
	 * 把字串(通常用于中文)转成16进制字符串
	 * 
	 * @return null if given str is null 如: 37efd
	 * */
	public static String getHex(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = str.length(); i < j; ++i) {
			sb.append(Integer.toHexString(str.charAt(i)));
		}
		return sb.toString();
	}

	public static void printLog(int logLevel, String tag, String log,
			Exception e) {
		StringBuffer strB = new StringBuffer();
		switch (logLevel) {
		case Log.VERBOSE:
			if (e == null) {
				Log.v(tag, log);
			} else {
				Log.v(tag, log, e);
			}
			break;

		case Log.DEBUG:

			if (e == null) {
				Log.d(tag, log);
			} else {
				Log.d(tag, log, e);
			}

			break;

		case Log.INFO:

			if (e == null) {
				Log.i(tag, log);
			} else {
				Log.i(tag, log, e);
			}

			break;

		case Log.WARN:

			if (e == null) {
				Log.w(tag, log);
			} else {
				Log.w(tag, log, e);
			}

			break;

		case Log.ERROR:

			if (e == null) {
				Log.e(tag, log);
			} else {
				Log.e(tag, log, e);
			}

			break;

		default:

			if (e == null) {
				Log.d(tag, log);
			} else {
				Log.d(tag, log, e);
			}
		}
	}

	public static String urlEncoder(String str) {
		if (isNullOrEmpty(str)) {
			return "";
		}
		String ret = "";
		try {
			ret = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static void showShortToast(Context ctx, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
	}

	public interface IExecuteAction {
		/**
		 * TODO
		 */
		public void execute();

		/**
		 * TODO
		 * 
		 * @param item
		 *            TODO
		 */
		public void execute(int item);
	}

	/**
	 * 在val不为空的情况下，是否是有效的数字
	 * 
	 * @param val
	 * @return 有效的數字 返回 true 否則為false
	 */

	public static boolean isValNumeric(String val) {
		if (!isEmpty(val)) {
			if (!isNumeric(val)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符是否中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		String chinese = "[\u0391-\uFFE5]";
		if (str.matches(chinese)) {
			return true;
		}
		return false;
	}

	public static boolean isChineseForLastChar(String lastStr) {
		if (!isNullOrEmpty(lastStr)) {
			String temp = lastStr.substring(lastStr.length() - 1);
			if (isChinese(temp)) {
				return true;
			}
		} else {// 为空的时候不加后缀字
			return true;
		}
		return false;
	}

	/**
	 * 如果是0 则返回 空 integer 转化为 字符串
	 * 
	 * @param val
	 * @return
	 */
	public static String convertInteger2String(int val) {
		if (val == 0) {
			return "";
		}
		return String.valueOf(val);
	}

	public static String convertDouble2String(double val) {
		if (val == 0) {
			return "";
		}
		return String.valueOf(val);
	}

	public static int convertString2Integer(String val) {
		int result = 0;
		if (isNullOrEmpty(val))
			return result;
		try {
			result = Integer.parseInt(val);
		} catch (NumberFormatException e) {
		}

		return result;
	}

	public static double convertString2Double(String val) {
		double result = 0;
		if (isNullOrEmpty(val))
			return result;
		try {
			result = Double.parseDouble(val);
		} catch (NumberFormatException e) {
		}

		return result;
	}

	public static long convertString2long(String val) {
		long result = 0;
		if (isNullOrEmpty(val))
			return result;
		try {
			result = Long.parseLong(val);
		} catch (NumberFormatException e) {
		}

		return result;
	}

	public static String getFormatString(String str) { // 将服务端不规则的数据 转成标准的时间格式
		String result = "";
		if (!StringUtils.isNullOrEmpty(str)) {
			String Assessortime = str.substring(6, str.length() - 2);
			long assessortime = Long.parseLong(Assessortime);
			result = TimeDateUtils.formatDate(assessortime);
		}
		return result;
	}

	public static double EARTH_RADIUS = 6378.137;

	public static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static String getFormatString2(String str) { // 将服务端不规则的数据 转成标准的时间格式
		String result = "";
		if (!StringUtils.isNullOrEmpty(str)) {
			String Assessortime = str.substring(6, str.length() - 2);
			long assessortime = Long.parseLong(Assessortime);
			result = TimeDateUtils.formatDate(assessortime);
		}
		return result;
	}

	public static String isLocation(double lng, double lat) {
		String result = "未定位";
		if (lng != 0 && lat != 0) {
			result = "已定位";
		}
		return result + "  ";
	}

	public static String imageCount(int count) {
		String result = "照片(" + count + ")  ";
		return result;
	}

	public static String Stringsplit(String detail) {
		String update_info = "";
		if (StringUtils.isNullOrEmpty(detail)) {
			update_info = "该版本优化部分功能";
		} else {
			String detail_info = detail;
			String[] update_infos = detail_info.split("\\;");
			for (int i = 0; i < update_infos.length; i++) {
				update_info = update_info + update_infos[i] + "\n";
			}
		}

		return update_info;
	}

	/**
	 * 判断字符是否是无效
	 * 
	 * @param text
	 * @return true 是 false 不是
	 */
	public static boolean isNonBlank(String text) {
		boolean isBlank = true;
		if (text == null) {
			return false;
		}
		if (text.trim().equals("")) {
			return false;
		}
		return isBlank;
	}

}
