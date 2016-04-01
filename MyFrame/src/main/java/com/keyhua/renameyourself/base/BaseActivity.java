package com.keyhua.renameyourself.base;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.MyLogger;
import com.keyhua.renameyourself.view.CustomProgressDialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.srain.cube.image.impl.DefaultImageLoadHandler;

/**
 * @author 曾金叶
 * @2015-8-4 @下午5:42:44
 */
@SuppressWarnings("deprecation")
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
    protected static final int REQUEST_IMAGE = 2;
    // 屏幕分辨率
    protected static int displayWidth = 0;
    protected static int displayHeight = 0;

    // log
    public MyLogger loghxd = MyLogger.hxdLog();
    public MyLogger logzt = MyLogger.ztLog();
    public MyLogger logzjy = MyLogger.zjyLog();
    protected RequestQueue requestQueue = null;

    /**
     * 当构造Activity时将来调用, 主要用做数据初始化 , 在onResload之前调用
     */
    protected abstract void onInitData();

    /**
     * 每当Activity创建出来时,将先调用, 主要用做资源的加载
     */
    protected abstract void onResload();

    /**
     * 每当Activity创建出来时,主要是针对控件的点击事件
     */
    protected abstract void setMyViewClick();

    /***
     * [对Activity生命周期的管理]
     ***********************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setFullScreen(true);
        DefaultImageLoadHandler handler = new DefaultImageLoadHandler(this);
        // pick one of the following method
        // handler.setLoadingBitmap(Bitmap loadingBitmap);
        // handler.setLoadingResources(int loadingBitmap);
        // handler.setLoadingImageColor(int color);
        // handler.setLoadingImageColor(String colorString);
        handler.setLoadingImageColor(getResources().getColor(R.color.white));
        handler.setErrorResources(R.mipmap.dmbg_default);

        DefaultImageLoadHandler handler1 = new DefaultImageLoadHandler(this);
        // pick one of the following method
        // handler.setLoadingBitmap(Bitmap loadingBitmap);
        // handler.setLoadingResources(int loadingBitmap);
        // handler.setLoadingImageColor(int color);
        // handler.setLoadingImageColor(String colorString);
        handler1.setLoadingImageColor(getResources().getColor(R.color.white));
        handler1.setErrorResources(R.mipmap.dmbg_default);
        handler1.setImageRounded(true, 8);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void init() {
        requestQueue = Volley.newRequestQueue(this);//这里的this指的是Context
        onInitData();
        onResload();
        setMyViewClick();
    }

    /**
     * 获取版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = String.valueOf(info.versionCode);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    // 解决用户连续点击造成出现多个相同的activity
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (CommonUtility.isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 检测Android设备是否支持摄像机
     */
    protected static boolean checkCameraDevice(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            // TODO camera驱动挂掉,处理??
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }

    // 判断是否有空格
    protected static boolean isTextReplaceAll(String text) {
        if (TextUtils.isEmpty(text.replaceAll("\\s*", ""))) {
            return true;
        } else {
            return false;
        }
    }

    private CustomProgressDialog mydialog = null;

    /**
     * 显示美团进度对话框
     *
     * @param
     */
    protected void showdialog() {
        try {
            mydialog = new CustomProgressDialog(this, "正在加载中...", R.anim.frame);
            mydialog.show();
        } catch (Exception e) {
        }
    }

    protected void showdialogtext(String text) {
        try {
            mydialog = new CustomProgressDialog(this, text, R.anim.frame);
            mydialog.show();
        } catch (Exception e) {
        }
    }

    protected void closedialog() {
        try {
            if (mydialog != null && this != null && mydialog.isShowing()) {
                mydialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    protected boolean isshowdialog() {
        if (mydialog != null && this != null) {
            if (mydialog.isShowing()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected CustomProgressDialog getCustomProgressDialog() {
        try {
            if (mydialog != null) {
                return mydialog;
            }
        } catch (Exception e) {
        }
        return null;
    }

    // 返回键设置，两次点击退出
    private long firstTime = 0;

    protected void twoToDefinish() {
        long secondtime = System.currentTimeMillis();
        // 连续点击两次时间少于2秒退出
        if (secondtime - firstTime > 2000) {
            showToast("再按一次返回键退出");
            // 保存第一次按下的时间
            firstTime = secondtime;
        } else {
            finish();
            // 清除缓存
            System.exit(0);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see Activity#onSaveInstanceState(Bundle)
     * 没root的手机可用这个储存，root的文件夹还是在，所以还是能用
     */
    protected void setFullScreen(boolean needState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!needState) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /***
     * [重写的toast]
     ***********************************************/
    protected Toast toast;

    protected void showToast(String text) {
        try {
            // 判断toast是否为空 ，空则执行
            if (toast == null) {
                // 将toast的时间设置
                toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            }
            // 通过传参的方式将text赋值到toast上
            toast.setText(text);
            // 显示toast
            toast.show();
        } catch (Exception e) {
        }
    }

    protected void showToastLogin() {
        try {
            // 判断toast是否为空 ，空则执行
            if (toast == null) {
                // 将toast的时间设置
                toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            }
            // 通过传参的方式将text赋值到toast上
            toast.setText("登录失效，请重新登录");
            // 显示toast
            toast.show();
        } catch (Exception e) {
        }
    }

    protected void showToastDengLu() {
        try {
            // 判断toast是否为空 ，空则执行
            if (toast == null) {
                // 将toast的时间设置
                toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            }
            // 通过传参的方式将text赋值到toast上
            toast.setText("请先登录");
            // 显示toast
            toast.show();
        } catch (Exception e) {
        }
    }

    protected void showToastNet() {
        try {
            // 判断toast是否为空 ，空则执行
            if (toast == null) {
                // 将toast的时间设置
                toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            }
            // 通过传参的方式将text赋值到toast上
            toast.setText("没有网络连接，请检查网络");
            // 显示toast
            toast.show();
        } catch (Exception e) {
        }
    }

    /**
     * 不带参数跳转
     */
    protected void openActivity(Class<?> cls) {
        // 新建意图
        Intent intent = new Intent();
        // 设置要跳转的activity
        // 参数1，Context得到包名。参数2，class得到类名，来唯一确定Activity
        intent.setClass(this, cls);
        // 跳转到相应的activity
        startActivity(intent);
        // 切换场景时动作设置
    }

    /**
     * 带参数跳转
     */
    protected void openActivity(Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /***
     * [初始化arraylist]
     ***********************************************/
    public static <T> ArrayList<T> createArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<T>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    protected void getWidthHeight() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        displayWidth = mDisplayMetrics.widthPixels;
        displayHeight = mDisplayMetrics.heightPixels;
    }


    // MainActivity关联
    public void initHeaderOrFooterMain() {
    }

    protected TextView toolbar_bac = null;// 左边的文字返回键
    protected TextView toolbar_title = null;// 中间的文字
    protected ImageView toolbar_right_l = null;// 搜索按钮
    protected ImageView toolbar_right_r = null;// 右边云同步等按钮
    protected TextView toolbar_tv_right_ok = null;// 右边文字
    protected TextView toolbar_tv_right_cancle = null;// 右边文字
    //相关文字
    protected String bacStr = "";
    protected String titleStr = "";

    /**
     * OtherActivity 关联
     *
     * @param bac        返回键
     * @param title      中间文字
     * @param bacBoll    是否显示返回键
     * @param searchBool 是否显示右边第一个按钮
     * @param rightBool  是否显示右边第二个按钮
     */
    public void initHeaderOther(String bac, String title, boolean bacBoll, boolean searchBool, boolean rightBool) {
        toolbar_bac = (TextView) findViewById(R.id.toolbar_bac);
        toolbar_tv_right_ok = (TextView) findViewById(R.id.toolbar_tv_right_ok);
        toolbar_tv_right_cancle = (TextView) findViewById(R.id.toolbar_tv_right_cancle);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_right_l = (ImageView) findViewById(R.id.toolbar_right_l);
        toolbar_right_r = (ImageView) findViewById(R.id.toolbar_right_r);
        toolbar_right_l.setOnClickListener(this);
        toolbar_right_r.setOnClickListener(this);
        toolbar_bac.setOnClickListener(this);
        toolbar_tv_right_ok.setOnClickListener(this);
        toolbar_tv_right_cancle.setOnClickListener(this);
        toolbar_bac.setText(bac);
        toolbar_title.setText(title);
        toolbar_tv_right_ok.setVisibility(View.GONE);
        toolbar_tv_right_cancle.setVisibility(View.GONE);
        if (bacBoll) {
            toolbar_bac.setVisibility(View.VISIBLE);
        } else {
            toolbar_bac.setVisibility(View.GONE);
        }
        if (searchBool) {
            toolbar_right_l.setVisibility(View.VISIBLE);
        } else {
            toolbar_right_l.setVisibility(View.GONE);
        }
        if (rightBool) {
            toolbar_right_r.setVisibility(View.VISIBLE);
        } else {
            toolbar_right_r.setVisibility(View.GONE);
        }
    }


    // OtherActivity关联
    public void initFooterOther(String str1, String str2, String str3) {
    }


    public void initHeaderOtherHuoDong() {
    }


    public void initFooterOtherHuoDong(String str1, String str2, String str3, boolean three, int index) {
    }

    /**
     * 判断服务是否启动
     */
    public boolean isWorked() {
        ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals("com.hwacreate.outdoor.service.GpsInfoCollectionService")) {
                return true;
            }
        }
        return false;
    }


    /**
     * @author zjy java的垃圾回收机制并没有那么的聪明，我们finish掉了，但里面相关的资源他未必回收。
     * 有可能他自以为很聪明的留下来等着我们下次使用。 所以我们需要在onStop的方法中手动释放imageView这样的大型资源。
     */
    private void releaseImageView(ImageView imageView) {
        Drawable d = imageView.getDrawable();
        if (d != null)
            d.setCallback(null);
        imageView.setImageDrawable(null);
        imageView.setBackgroundDrawable(null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
