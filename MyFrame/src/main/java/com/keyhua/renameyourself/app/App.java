package com.keyhua.renameyourself.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import com.keyhua.renameyourself.util.SPUtils;

import org.litepal.LitePalApplication;

/**
 * @author 曾金叶
 * @2015-8-5 @下午2:40:52
 * @category 将大部分数据保存在这里，也可以使用工具类SPUtils来存储一些长久数据
 */
public class App extends LitePalApplication {
    private boolean isPush = false;


    // 调用时使用的实例
    private static App mInstance;
    // 屏幕长宽
    private int screenWidth, screenHeight;


    // Aut登陆标识
    public String getAut() {
        return String.valueOf(SPUtils.get(this, "Aut", ""));
    }

    public void setAut(String aut) {
        SPUtils.put(this, "Aut", aut);
    }


    // role权限
    public String getRole() {
        return (String) SPUtils.get(this, "Role", "");
    }

    public void setRole(String role) {
        SPUtils.put(this, "Role", role);
    }

    // userid用户id
    public String getUserid() {
        return String.valueOf(SPUtils.get(this, "Userid", ""));
    }

    public void setUserid(String userid) {
        SPUtils.put(this, "Userid", userid);
    }

    // userid用户id
    public String getUserName() {
        return String.valueOf(SPUtils.get(this, "UserName", ""));
    }

    public void setUserName(String userid) {
        SPUtils.put(this, "UserName", userid);
    }


    // Realname真实姓名
    public String getRealname() {
        return String.valueOf(SPUtils.get(this, "Realname", ""));
    }

    public void setRealname(String realname) {
        SPUtils.put(this, "Realname", realname);
    }

    // headurl用户头像
    public String getHeadurl() {
        return String.valueOf(SPUtils.get(this, "Headurl", ""));
    }

    public void setHeadurl(String headurl) {
        SPUtils.put(this, "Headurl", headurl);
    }

    // nickname用户昵称
    public String getNickname() {
        return String.valueOf(SPUtils.get(this, "Nickname", ""));
    }

    public void setNickname(String nickname) {
        SPUtils.put(this, "Nickname", nickname);
    }

    // phonenum号码
    public String getPhonenum() {
        return String.valueOf(SPUtils.get(this, "Phonenum", ""));
    }

    public void setPhonenum(String phonenum) {
        SPUtils.put(this, "Phonenum", phonenum);
    }


    // 身份证号码
    public String getSfz() {
        return String.valueOf(SPUtils.get(this, "Sfz", ""));
    }

    public void setSfz(String sfz) {
        SPUtils.put(this, "Sfz", sfz);
    }


    // HomeFrag中的刷新时间----------------
    public String getRefreshHomeFrg() {
        return String.valueOf(SPUtils.get(this, "refreshHomeFrg", "最近无更新"));
    }

    public void setRefreshHomeFrg(String refreshHomeFrg) {
        SPUtils.put(this, "refreshHomeFrg", refreshHomeFrg);
    }

    // ---------------- ----------------
    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public static App getInstance() {
        return mInstance;
    }

    // 保存的图片（个人资料）
    private String SelectPath = null;

    public String getSelectPath() {
        return SelectPath;
    }

    public void setSelectPath(String selectPath) {
        SelectPath = selectPath;
    }

    // 保存的图片Temp（个人资料）选择照片以后
    private String SelectPathTemp = null;

    public String getSelectPathTemp() {
        return SelectPathTemp;
    }

    public void setSelectPathTemp(String selectPathTemp) {
        SelectPathTemp = selectPathTemp;
    }

    // 保存的图片（游记封面）
    private String SelectPathYouJiFenmian = null;

    public String getSelectPathYouJiFenmian() {
        return SelectPathYouJiFenmian;
    }

    public void setSelectPathYouJiFenmian(String selectPathYouJiFenmian) {
        SelectPathYouJiFenmian = selectPathYouJiFenmian;
    }

    // 设置页面参数
    public boolean isTb_wifi() {
        return (Boolean) SPUtils.get(this, "Tb_wifi", false);
    }

    public void setTb_wifi(boolean tb_wifi) {
        SPUtils.put(this, "Tb_wifi", tb_wifi);
    }

    private boolean isGuiJi = false;
    private boolean isDown = false;

    public boolean getGuiJi() {
        return isGuiJi;
    }

    public void setGuiJi(boolean GuiJi) {
        this.isGuiJi = GuiJi;
    }

    public void setDown(boolean Down) {
        this.isDown = Down;
    }

    public boolean getDown() {
        return isDown;
    }


    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//		SDKInitializer.initialize(this);
        mInstance = this;
        LitePalApplication.initialize(this);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        this.setScreenWidth(wm.getDefaultDisplay().getWidth());// 屏幕宽度
        this.setScreenHeight(wm.getDefaultDisplay().getHeight());// 屏幕高度
    }

}
