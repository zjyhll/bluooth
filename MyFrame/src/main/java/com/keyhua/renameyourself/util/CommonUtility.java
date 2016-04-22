package com.keyhua.renameyourself.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * @author 曾金叶
 * @2015-8-5 @下午2:22:05
 * @category 有什么工具方法可以发在这里面
 */
public class CommonUtility {
    // 注册界面进行的操作1,注册---2忘记密码3修改密码(一样的步骤，aut为空不为空)
    public static final int ZHUCE = 1111;
    public static final int WANGJIMIMA = 1112;
    public static final int XIUGAIMIMA = 1113;
    public static final int LIDUI = 1;//离队
    public static final int GUIDUI = 2;//归队
    public static final int DUIYUAN = 1;//队员身份
    public static final int LINGDUI = 2;//领队身份
    public static final int SELF = 1;//当前设备上对应的自己
    public static final String LINGDUISTR = "是否离队";//是否离队
    public static final String GUIDUISTR = "是否归队";//归队
    public static final String DUIWUGUANLI = "请先关联领队机";//
    // 跳转活动详情标识
    public static final int XianShiTab_False = 1102;// 显示下方的tab
    public static final int XianShiTab_SheZhi = 1103;// 整队出行活动管理
    public static final int XianShiTab_RenWu = 1104;// 队员选择任务
    public static final int XianShiTab_Leader = 1105;// 领队选择任务
    public static final int XianShiTab_Leader_NOW = 1106;// 领队选择任务
    // 主界面底部的按钮的选择
    public static final int TUZHONG = 0;// 途中
    public static final int LINGDUIGONGJU = 1;// 领队工具
    public static final int WODEWEISHI = 2;// 我的卫士
    // 判断是否有网
    public static final String ISNETCONNECTED = "请检查手机网络,稍后再试";
    public static final int ISREFRESH = 1; // 刷新
    public static final int ISLOADMORE = 2;// 加载更多
    public static final int ISNETCONNECTEDInt = 3;
    // 服务器返回参数
    public static final int KONG = 10;// 数据为空
    public static final int UPLOADING = 60;// 上传
    public static final int UPLOADINGCANCLE = 61;// 取消上传
    public static final int SERVEROK1 = 101;// 返回成功
    public static final int SERVEROK2 = 102;// 返回成功
    public static final int SERVEROK3 = 103;// 返回成功
    public static final int SERVEROK4 = 104;// 返回成功
    public static final int SERVEROK5 = 105;// 返回成功
    public static final int SERVEROK6 = 106;// 返回成功
    public static final int SERVEROK7 = 107;// 返回成功
    public static final int SERVEROK8 = 108;// 返回成功
    public static final int SERVEROK9 = 109;// 返回成功
    public static final int SERVEROK10 = 110;// 返回成功
    public static final int SERVEROK11 = 111;// 返回成功
    public static final int SERVEROK12 = 112;// 返回成功
    public static final int SERVEROK13 = 113;// 返回成功
    public static final int SERVEROK14 = 114;// 返回成功
    public static final int SERVEROKYAN = 999;// 发送验证码失败
    public static final int SERVERERROR = 5004;// 连接服务器出现错误
    public static final int ChANNELRSERVERERROR = 5005;// 连接服务器出现错误
    public static final int SERVERERRORLOGIN = 5011;// 连接服务器5011出现错误
    // 正式服
    public final static String URL = "http://www.17hwtx.com/Main";
    // 正式服w
//	public final static String URL = "http://115.29.247.170:8080/OutdoorServer/Main";
    public final static String URLIMAIGN = "http://115.29.247.170";
    // 活动开始状态
    public final static String SHENHESHIBAIStr = "审核失败";
    public final static int SHENHESHIBAIInt = -1;
    public final static String SHENHEZHONGStr = "审核中";
    public final static int SHENHEZHONGInt = 0;
    public final static String BAOMINGZHONGStr = "报名中";
    public final static int BAOMINGZHONGInt = 1;
    public final static String ZHENGDUIStr = "整队中";
    public final static int ZHENGDUIInt = 2;
    public final static String ZHUNBEIStr = "准备出行";
    public final static int ZHUNBEIInt = 3;
    public final static String CHUXINGStr = "出行中";
    public final static int CHUXINGInt = 4;
    public final static String DIANPINGStr = "收队中";
    public final static int DIANPINGInt = 5;
    public final static String JIESHUStr = "活动结束";
    public final static int JIESHUInt = 6;
    // 二维码
    public final static int SCANNIN_GREQUEST_CODE = 1;

    /**
     * 转换gps偏移
     */
    public static LatLng convertGps(double gpsLatitude,
                                    double gpsLongitude) {
        LatLng sourceLatLng = new LatLng(gpsLatitude, gpsLongitude);
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void toggleGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    /* 配置------------------------------------------------------------------ */
    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    // 解决用户连续点击造成出现多个相同的activity
    public static long lastClickTime = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 500) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static double getLength(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        // 进位取整
        return Math.ceil(valueLength);
    }

    // 判断小数点后是否小于两位
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]{1,2}))?|([.]([0-9]{1,2}))?)$");
    }

    // 字符串转double
    public static double StringToDouble1(String str) {
        // 保留两位数
        @SuppressWarnings("unused")
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");// "#.0"表示前面不显示0
        double d = Double.valueOf(str).doubleValue();
        return d;
    }

    // 字符串转double
    public static double DoubleToDouble2(Double str) {
        // 保留两位数
        @SuppressWarnings("unused")
        BigDecimal b = new BigDecimal(str);
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 软件盘的显示与消失
     */
    public static void popupInputMethodWindow(final EditText editText) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 350);
    }

    /*------------------------------------------------------------------*/
    public static JSONObject sendRequest(JSONObject requestObject)
            throws ClientProtocolException, IOException, JSONException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        /* 连接超时 */
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
        /* 请求超时 */
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10000);

        HttpPost httpPost = new HttpPost(URL);
        // System.out.println(URL);
        // System.out.println(requestObject.toString());
        httpPost.setEntity(new StringEntity(requestObject.toString(), HTTP.UTF_8));
        // System.out.println(JSON.toJSONString(httpPost));
        HttpResponse response = httpClient.execute(httpPost);
        // System.out.println(JSON.toJSONString(response));
        // System.out.println(response);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String message = EntityUtils.toString(response.getEntity(), "UTF-8");
            // HttpEntity content = response.getEntity();
            // //int length = (int)content.getContentLength();
            // int length = 4*1024;
            // byte message[] = new byte[length];
            // content.getContent().read(message);
            return new JSONObject(message);
        } else {
            return new JSONObject(
                    "{\"errcode\": -1, \"errmsg\": \"Server response: " + response.getStatusLine().toString() + " \"}");
        }
    }

    public static ProgressDialog loader(Context context) {
        ProgressDialog loader = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        loader.setIndeterminate(false);
        loader.setCancelable(false);
        return loader;
    }

    public static void showToast(Handler handler, final Context context, final String text, final int duration) {
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, text, duration).show();
            }
        });
    }

	/*
     * public static void showLoader(Handler handler, final ProgressDialog
	 * loader) { handler.post(new Runnable() { public void run() {
	 * Toast.makeText(context, text, duration).show(); } }); }
	 */

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 判断传入的字符串是否是一个手机号码
     *
     * @param strPhone
     * @return
     */
    public static boolean isPhoneNumber(String strPhone) {
        Pattern p = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$");
        Matcher m = p.matcher(strPhone);
        return m.find();
    }

    /**
     * @param str
     * @return 是否是数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 隐藏系统软键盘
     *
     * @param ctx
     */
    public static void gone_keyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (((Activity) ctx).getCurrentFocus() != null) {
            if (((Activity) ctx).getCurrentFocus().getWindowToken() != null) {

                imm.hideSoftInputFromWindow(((Activity) ctx).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示系统软键盘
     *
     * @param ctx
     */
    public static void visible_keyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * bitmap 转 Drawable对象
     *
     * @param bm
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        return new BitmapDrawable(bm);
    }

    public static String convertTime2Date(long longtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(longtime);
        return str;
    }

    @SuppressLint("TrulyRandom")
    public static Integer getNonce() throws NoSuchAlgorithmException {

        Integer nonce = null;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        nonce = random.nextInt(100000);

        return nonce;
    }
}
