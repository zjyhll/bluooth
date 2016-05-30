package com.keyhua.litepal;


import org.litepal.crud.DataSupport;

public class SignUpUser extends DataSupport {
    private long id;//自增id
    private Integer deviceReady = null;//是否有设备 1有设备,2无设备
    private Integer tps_type = null;// 人员类型：1普通队员，2领队
    private Integer tps_id = null;// 物理id
    private String phonenum = null;// 队员手机号
    private String strDeviceSN = null;// 硬件sn号码
    private String u_nickname = ""; // 用户昵称
    private String u_head = ""; // 用户头像
    private Integer act_isleave = 0;// 是否离队(1)或归队(2)
    private String user_latitude = "";
    private String user_longitude = "";
    private double distance = 0;
    private String location_time = "";
    private Integer isUsedByCurrentDevice = 0;//判断该tps_id是否是当前手机使用,列表中代表自己,1为自己，整个数据表中只会存在一个为1,0为其他人.每台手机只会指定一个用户
    private Integer act_shilian = 0;// 是否失联(1)或为未失联(0)

    public Integer getDeviceReady() {
        return deviceReady;
    }

    public Integer getAct_shilian() {
        return act_shilian;
    }

    public void setAct_shilian(Integer act_shilian) {
        this.act_shilian = act_shilian;
    }

    public void setDeviceReady(Integer deviceReady) {
        this.deviceReady = deviceReady;
    }

    public Integer getIsUsedByCurrentDevice() {
        return isUsedByCurrentDevice;
    }

    public void setIsUsedByCurrentDevice(Integer isUsedByCurrentDevice) {
        this.isUsedByCurrentDevice = isUsedByCurrentDevice;
    }

    public String getUser_latitude() {
        return user_latitude;
    }

    public void setUser_latitude(String user_latitude) {
        this.user_latitude = user_latitude;
    }

    public String getUser_longitude() {
        return user_longitude;
    }

    public void setUser_longitude(String user_longitude) {
        this.user_longitude = user_longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getTps_type() {
        return tps_type;
    }

    public void setTps_type(Integer tps_type) {
        this.tps_type = tps_type;
    }

    public Integer getTps_id() {
        return tps_id;
    }

    public void setTps_id(Integer tps_id) {
        this.tps_id = tps_id;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getStrDeviceSN() {
        return strDeviceSN;
    }

    public void setStrDeviceSN(String strDeviceSN) {
        this.strDeviceSN = strDeviceSN;
    }

    public String getU_nickname() {
        return u_nickname;
    }

    public void setU_nickname(String u_nickname) {
        this.u_nickname = u_nickname;
    }

    public String getU_head() {
        return u_head;
    }

    public void setU_head(String u_head) {
        this.u_head = u_head;
    }

    public Integer getAct_isleave() {
        return act_isleave;
    }

    public void setAct_isleave(Integer act_isleave) {
        this.act_isleave = act_isleave;
    }
}
