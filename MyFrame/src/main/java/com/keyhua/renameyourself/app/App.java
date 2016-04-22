package com.keyhua.renameyourself.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.keyhua.renameyourself.util.SPUtils;

import org.litepal.LitePalApplication;

    /**
     * @author 曾金叶
     * @2015-8-5 @下午2:40:52
     * @category 将大部分数据保存在这里，也可以使用工具类SPUtils来存储一些长久数据
     */
    public class App extends LitePalApplication {
        private double gpsLatitude = 0;
        private double gpsLongitude = 0;

        public double getGpsLatitude() {
            return gpsLatitude;
        }

        public void setGpsLatitude(double gpsLatitude) {
            this.gpsLatitude = gpsLatitude;
        }

        public double getGpsLongitude() {
            return gpsLongitude;
        }

        public void setGpsLongitude(double gpsLongitude) {
            this.gpsLongitude = gpsLongitude;
        }

        // 第一次进入定位
        private boolean isFirst = false;

        public boolean isFirst() {
            return isFirst;
        }

        public void setFirst(boolean isFirst) {
            this.isFirst = isFirst;
        }

        private boolean isPush = false;

        public boolean isPush() {
            return isPush;
        }

        public void setPush(boolean isPush) {
            this.isPush = isPush;
        }

        // 途中计划轨迹
        public boolean getJiHuaGuiJi() {
            return (Boolean) SPUtils.get(this, "getJiHuaGuiJi", false);
        }

        public void setJiHuaGuiJi(boolean getJiHuaGuiJi) {
            SPUtils.put(this, "getJiHuaGuiJi", getJiHuaGuiJi);
        }

        // 途中我的轨迹
        public boolean getMyGuiJi() {
            return (Boolean) SPUtils.get(this, "getMyGuiJi", false);
        }

        public void setMyGuiJi(boolean getMyGuiJi) {
            SPUtils.put(this, "getMyGuiJi", getMyGuiJi);
        }

        // 队员连接蓝牙名
        public String getBleDuiYuanName() {
            return (String) SPUtils.get(this, "BleDuiYuanName", "");
        }

        public void setBleDuiYuanName(String ble) {
            SPUtils.put(this, "BleDuiYuanName", ble);
        }

        // 关联队员蓝牙地址
        public String getContactBleDuiYuanAddress() {
            return (String) SPUtils.get(this, "ContactBleDuiYuanAddress", "");
        }

        public void setContactBleDuiYuanAddress(String ble) {
            SPUtils.put(this, "ContactBleDuiYuanAddress", ble);
        }

        // 队员连接蓝地址
        public String getBleDuiYuanAddress() {
            return (String) SPUtils.get(this, "BleDuiYuanAddress", "");
        }

        public void setBleDuiYuanAddress(String ble) {
            SPUtils.put(this, "BleDuiYuanAddress", ble);
        }

        // 领队连接蓝牙名
        public String getBleLingDuiName() {
            return (String) SPUtils.get(this, "BleLingDuiName", "");
        }

        public void setBleLingDuiName(String ble) {
            SPUtils.put(this, "BleLingDuiName", ble);
        }

        // 领队连接蓝地址
        public String getBleLingDuiDuiAddress() {
            return (String) SPUtils.get(this, "BleLingDuiAddress", "");
        }

        public void setBleLingDuiAddress(String ble) {
            SPUtils.put(this, "BleLingDuiAddress", ble);
        }

        // 调用时使用的实例
        private static App mInstance;
        // 屏幕长宽
        private int screenWidth, screenHeight;

        // 判断点击得是途中OR领队工具OR我的卫士
        public int getBottonChoice() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this,
                    "bottonChoice", 0)));
        }

        public void setBottonChoice(int bottonChoice) {
            SPUtils.put(this, "bottonChoice", bottonChoice);
        }

        // 是否点击头像
        public boolean getDownTouXiang() {
            return (Boolean) SPUtils.get(this, "DownTouXiang", false);
        }

        public void setDownTouXiang(boolean DownTouXiang) {
            SPUtils.put(this, "DownTouXiang", DownTouXiang);
        }

        // 定位选择的城市
        public String getContactCity() {
            return String.valueOf(SPUtils.get(this, "ContactCity", ""));
        }

        public void setContactCity(String ContactCity) {
            SPUtils.put(this, "ContactCity", ContactCity);
        }

        // Aut登陆标识
        public String getAut() {
            return String.valueOf(SPUtils.get(this, "Aut", ""));
        }

        public void setAut(String aut) {
            SPUtils.put(this, "Aut", aut);
        }

        // channelId推送唯一id
        public String getChannelId() {
            return String.valueOf(SPUtils.get(this, "ChannelId", ""));
        }

        public void setChannelId(String channelId) {
            SPUtils.put(this, "ChannelId", channelId);
        }

        // channelId推送唯一id
        public boolean getIsChannelId() {
            return (Boolean) SPUtils.get(this, "IsChannelId", false);
        }

        public void setIsChannelId(boolean channelId) {
            SPUtils.put(this, "IsChannelId", channelId);
        }

        // role权限
        public int getRole() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "Role", 0)));
        }

        public void setRole(int role) {
            SPUtils.put(this, "Role", role);
        }

        // userid用户id
        public String getUserid() {
            return String.valueOf(SPUtils.get(this, "Userid", ""));
        }

        public void setUserid(String userid) {
            SPUtils.put(this, "Userid", userid);
        }

        // 用户积分
        public int getFraction() {
            return Integer
                    .parseInt(String.valueOf(SPUtils.get(this, "Fraction", 0)));
        }

        public void setFraction(int Fraction) {
            SPUtils.put(this, "Fraction", Fraction);
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

        // email邮箱
        public String getEmail() {
            return String.valueOf(SPUtils.get(this, "Email", ""));
        }

        public void setEmail(String email) {
            SPUtils.put(this, "Email", email);
        }

        // qq
        public String getQq() {
            return String.valueOf(SPUtils.get(this, "QQ", ""));
        }

        public void setQq(String qq) {
            SPUtils.put(this, "QQ", qq);
        }

        // 微信
        public String getMicroblog() {
            return String.valueOf(SPUtils.get(this, "Microblog", ""));
        }

        public void setMicroblog(String microblog) {
            SPUtils.put(this, "Microblog", microblog);
        }

        // sex性别
        public String getSex() {
            return String.valueOf(SPUtils.get(this, "Sex", ""));
        }

        public void setSex(String sex) {
            SPUtils.put(this, "Sex", sex);
        }

        // brith生日
        public String getBrith() {
            return String.valueOf(SPUtils.get(this, "Brith", ""));
        }

        public void setBrith(String brith) {
            SPUtils.put(this, "Brith", brith);
        }

        // bloodtype血型
        public String getBloodtype() {
            return String.valueOf(SPUtils.get(this, "Bloodtype", ""));
        }

        public void setBloodtype(String bloodtype) {
            SPUtils.put(this, "Bloodtype", bloodtype);
        }

        // emergency_name应急联系人姓名
        public String getEmergency_name() {
            return String.valueOf(SPUtils.get(this, "Emergency_name", ""));
        }

        public void setEmergency_name(String emergency_name) {
            SPUtils.put(this, "Emergency_name", emergency_name);
        }

        // emergency_phone应急联系人电话
        public String getEmergency_phone() {
            return String.valueOf(SPUtils.get(this, "Emergency_phone", ""));
        }

        public void setEmergency_phone(String emergency_phone) {
            SPUtils.put(this, "Emergency_phone", emergency_phone);
        }

        // 身份证号码
        public String getSfz() {
            return String.valueOf(SPUtils.get(this, "Sfz", ""));
        }

        public void setSfz(String sfz) {
            SPUtils.put(this, "Sfz", sfz);
        }

        // 护照
        public String getHz() {
            return String.valueOf(SPUtils.get(this, "Hz", ""));
        }

        public void setHz(String hz) {
            SPUtils.put(this, "Hz", hz);
        }

        // 驾驶证
        public String getJsz() {
            return String.valueOf(SPUtils.get(this, "Jsz", ""));
        }

        public void setJsz(String jsz) {
            SPUtils.put(this, "Jsz", jsz);
        }

        // 邮编
        public String getYb() {
            return String.valueOf(SPUtils.get(this, "Yb", ""));
        }

        public void setYb(String yb) {
            SPUtils.put(this, "Yb", yb);
        }

        // 固话
        public String getGh() {
            return String.valueOf(SPUtils.get(this, "Gh", ""));
        }

        public void setGh(String gh) {
            SPUtils.put(this, "Gh", gh);
        }

        // 个性签名
        public String getGxqm() {
            return String.valueOf(SPUtils.get(this, "Gxqm", ""));
        }

        public void setGxqm(String gxqm) {
            SPUtils.put(this, "Gxqm", gxqm);
        }

        // 家庭地址
        public String getAddress() {
            return String.valueOf(SPUtils.get(this, "Address", ""));
        }

        public void setAddress(String address) {
            SPUtils.put(this, "Address", address);
        }

        // privince省
        public int getPrivince() {
            return Integer
                    .parseInt(String.valueOf(SPUtils.get(this, "Privince", 0)));
        }

        public void setPrivince(int privince) {
            SPUtils.put(this, "Privince", privince);
        }

        // privinceString省
        public String getPrivincename() {
            return String.valueOf(SPUtils.get(this, "PrivinceName", ""));
        }

        public void setPrivincename(String privincename) {
            SPUtils.put(this, "PrivinceName", privincename);
        }

        // city市
        public int getCity() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "City", 0)));
        }

        public void setCity(int city) {
            SPUtils.put(this, "City", city);
        }

        // CityString省
        public String getCityname() {
            return String.valueOf(SPUtils.get(this, "CityName", ""));
        }

        public void setCityname(String cityname) {
            SPUtils.put(this, "CityName", cityname);
        }

        // county区县
        public int getCounty() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "County", 0)));
        }

        public void setCounty(int county) {
            SPUtils.put(this, "County", county);
        }

        // countyString区县
        public String getCountyname() {
            return String.valueOf(SPUtils.get(this, "CountyName", ""));
        }

        public void setCountyname(String countyname) {
            SPUtils.put(this, "CountyName", countyname);
        }

        // ZoneString字符串地址
        public String getZoneString() {
            return String.valueOf(SPUtils.get(this, "ZoneString", ""));
        }

        public void setZoneString(String ZoneString) {
            SPUtils.put(this, "ZoneString", ZoneString);
        }

        // registtime注册时间
        public String getRegisttime() {
            return String.valueOf(SPUtils.get(this, "Registtime", ""));
        }

        public void setRegisttime(String registtime) {
            SPUtils.put(this, "Registtime", registtime);
        }

        // ulevel级别
        public int getUlevel() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "Ulevel", 0)));
        }

        public void setUlevel(int ulevel) {
            SPUtils.put(this, "Ulevel", ulevel);
        }

        // 星级
        public int getStar() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "Star", 0)));
        }

        public void setStar(int Star) {
            SPUtils.put(this, "Star", Star);
        }

        // is_leader是否成为领队 1队员，2领队
        public int getIs_leader() {
            return Integer.parseInt(String.valueOf(SPUtils
                    .get(this, "Is_leader", 0)));
        }

        public void setIs_leader(int is_leader) {
            SPUtils.put(this, "Is_leader", is_leader);
        }

        // verify领队资质审核情况0为待审核,1为通过,2为不通过
        public int getVerify() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "Verify", 0)));
        }

        public void setVerify(int verify) {
            SPUtils.put(this, "Verify", verify);
        }

        // 领取的活动id
        public String getHuoDongId() {
            return String.valueOf(SPUtils.get(this, "HuoDongId", ""));
        }

        public void setHuoDongId(String HuoDongId) {
            SPUtils.put(this, "HuoDongId", HuoDongId);
        }

        // 领取的活动id领队
        public String getLeaderHuoDongId() {
            return String.valueOf(SPUtils.get(this, "LeaderHuoDongId", ""));
        }

        public void setLeaderHuoDongId(String HuoDongId) {
            SPUtils.put(this, "LeaderHuoDongId", HuoDongId);
        }

        // 我写的游记
        public int getMyWritedTvl() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this, "myWritedTvl",
                    0)));
        }

        public void setMyWritedTvl(int myWritedTvl) {
            SPUtils.put(this, "myWritedTvl", myWritedTvl);
        }

        // 我收藏的游记
        public int getMyCollectedTvl() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this,
                    "myCollectedTvl", 0)));
        }

        public void setMyCollectedTvl(int myCollectedTvl) {
            SPUtils.put(this, "myCollectedTvl", myCollectedTvl);
        }

        // 我收藏的活动
        public int getMyCollectedAct() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this,
                    "myCollectedAct", 0)));
        }

        public void setMyCollectedAct(int myCollectedAct) {
            SPUtils.put(this, "myCollectedAct", myCollectedAct);
        }

        // 我下载的游记
        public int getMyDownloadTvl() {
            return Integer.parseInt(String.valueOf(SPUtils.get(this,
                    "myDownloadTvl", 0)));
        }

        public void setMyDownloadTvl(int myDownloadTvl) {
            SPUtils.put(this, "myDownloadTvl", myDownloadTvl);
        }

        // 纬度
        private double latitude = 0;
        // 经度
        private double lontitude = 0;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLontitude() {
            return lontitude;
        }

        public void setLontitude(double lontitude) {
            this.lontitude = lontitude;
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

        //当为false时通过设备获取gps数据,默认从手机获取
        public boolean isTb_phonelocation() {
            return (Boolean) SPUtils.get(this, "Tb_phonelocation", true);
        }

        public void setTb_phonelocation(boolean tb_phonelocation) {
            SPUtils.put(this, "Tb_phonelocation", tb_phonelocation);
        }

        public boolean isTb_outh() {
            return (Boolean) SPUtils.get(this, "Tb_outh", false);
        }

        public void setTb_outh(boolean tb_outh) {
            SPUtils.put(this, "Tb_outh", tb_outh);
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

        public void ClearAll() {
            setContactCity("");
            // setAut("");
            setRole(0);
            setUserid("");
            setFraction(0);
            setRealname("");
            setHeadurl("");
            setNickname("");
            // setPhonenum("");
            setEmail("");
            setQq("");
            setMicroblog("");
            setSex("");
            setBrith("");
            setBloodtype("");
            setEmergency_name("");
            setEmergency_phone("");
            setSfz("");
            setHz("");
            setJsz("");
            setYb("");
            setGh("");
            setGxqm("");
            setAddress("");
            setPrivince(0);
            setPrivincename("");
            setCity(0);
            setCityname("");
            setCounty(0);
            setCountyname("");
            setZoneString("");
            setRegisttime("");
            setUlevel(0);
            setStar(0);
            setIs_leader(0);
            setVerify(0);
            setHuoDongId("");
            setMyWritedTvl(0);
            setMyCollectedTvl(0);
            setMyCollectedAct(0);
            setMyDownloadTvl(0);
            setHuoDongId("");
            setLeaderHuoDongId("");
            setCentNum("");
        }

        private String huodongleixing = "";
        private String club_name = "";
        private String club_id = "";

        public String getHuodongleixing() {
            return huodongleixing;
        }

        public void setHuodongleixing(String huodongleixing) {
            this.huodongleixing = huodongleixing;
        }

        public String getClub_name() {
            return club_name;
        }

        public void setClub_name(String club_name) {
            this.club_name = club_name;
        }

        public String getClub_id() {
            return club_id;
        }

        public void setClub_id(String club_id) {
            this.club_id = club_id;
        }

        private int AreaInfo = 0;// 个人资料==1 游记地址==2

        public int getAreaInfo() {
            return AreaInfo;
        }

        public void setAreaInfo(int areaInfo) {
            AreaInfo = areaInfo;
        }

        @SuppressWarnings("deprecation")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onCreate() {
            super.onCreate();
            LitePalApplication.initialize(this);
            mInstance = this;
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            this.setScreenWidth(wm.getDefaultDisplay().getWidth());// 屏幕宽度
            this.setScreenHeight(wm.getDefaultDisplay().getHeight());// 屏幕高度

            // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext,百度地图
            SDKInitializer.initialize(getApplicationContext());
        }

        // 服务中心号

        public String getCentNum() {
            return String.valueOf(SPUtils.get(this, "CentNum", ""));
        }

        public void setCentNum(String centNum) {
            SPUtils.put(this, "CentNum", centNum);
        }

    }
