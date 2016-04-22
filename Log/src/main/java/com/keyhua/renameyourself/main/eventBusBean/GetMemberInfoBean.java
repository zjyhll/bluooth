package com.keyhua.renameyourself.main.eventBusBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 曾金叶 on 2016/4/8.
 */
public class GetMemberInfoBean {
    private String strNickName = "";//昵称，最大10字节字母或5个汉字
    private String strDeviceSN = "";//唯一的设备SN串号，固定6字节的数字，由设备量产时写入。有非0值，则表示该队员有同行宝；如果为0值，则表示该队员无同行宝硬件。
    private boolean uIsAttend = false;//该编号成员是否参加了这次活动。0-未参加，1-参加了本次活动
    private boolean uIsUseAPP = false; //该编号成员是否启用APP图标. 0-未使用队员APP，1-使用了队员APP


    private Integer uDevNumInGroup = 0; //该设备队员的组内编号，范围[1,31]	;
    private Integer uRev = 0;    //Reserved bit，暂未定义
    private boolean uDeviceIsLDB = false;//本设备是否领队宝（APP确保一个队伍只有一个领队）,0-TXB同行宝（队员）,1-LDB领队宝(领队)；
    private boolean uDeviceReady = false;//是否有同行宝设备。 由领队APP设定：根据strDeviceSN是否为非零来判断，0-无同行宝；1-有同行宝

    public GetMemberInfoBean() {
    }

    public String getStrNickName() {
        return strNickName;
    }

    public void setStrNickName(String strNickName) {
        this.strNickName = strNickName;
    }

    public String getStrDeviceSN() {
        return strDeviceSN;
    }

    public void setStrDeviceSN(String strDeviceSN) {
        this.strDeviceSN = strDeviceSN;
    }

    public boolean isuIsAttend() {
        return uIsAttend;
    }

    public void setuIsAttend(boolean uIsAttend) {
        this.uIsAttend = uIsAttend;
    }

    public boolean isuIsUseAPP() {
        return uIsUseAPP;
    }

    public void setuIsUseAPP(boolean uIsUseAPP) {
        this.uIsUseAPP = uIsUseAPP;
    }

    public Integer getuDevNumInGroup() {
        return uDevNumInGroup;
    }

    public void setuDevNumInGroup(Integer uDevNumInGroup) {
        this.uDevNumInGroup = uDevNumInGroup;
    }

    public Integer getuRev() {
        return uRev;
    }

    public void setuRev(Integer uRev) {
        this.uRev = uRev;
    }

    public boolean isuDeviceIsLDB() {
        return uDeviceIsLDB;
    }

    public void setuDeviceIsLDB(boolean uDeviceIsLDB) {
        this.uDeviceIsLDB = uDeviceIsLDB;
    }

    public boolean isuDeviceReady() {
        return uDeviceReady;
    }

    public void setuDeviceReady(boolean uDeviceReady) {
        this.uDeviceReady = uDeviceReady;
    }
}
