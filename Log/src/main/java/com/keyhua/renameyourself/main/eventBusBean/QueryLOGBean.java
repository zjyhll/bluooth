package com.keyhua.renameyourself.main.eventBusBean;

/**
 * Created by 曾金叶 on 2016/4/7.
 */
public class QueryLOGBean {
    private int status = 0;//根据status来判断该执行哪一条
    private String strMCUFW = "";//1
    private String strBTFW = "";//2
    private String bWorkMode = "";//3
    private String devSN = "";//4
    private String strHWTX_GroupInfoAll_BtAPP2 = "";//5
    private String strHWTX_FW_Config = "";//6
    private String strLog = "";//7

    public QueryLOGBean(int status, String strMCUFW, String strBTFW, String bWorkMode, String devSN, String strHWTX_GroupInfoAll_BtAPP2, String strHWTX_FW_Config, String strLog) {
        this.status = status;
        this.strMCUFW = strMCUFW;
        this.strBTFW = strBTFW;
        this.bWorkMode = bWorkMode;
        this.devSN = devSN;
        this.strHWTX_GroupInfoAll_BtAPP2 = strHWTX_GroupInfoAll_BtAPP2;
        this.strHWTX_FW_Config = strHWTX_FW_Config;
        this.strLog = strLog;
    }

    public QueryLOGBean(int status) {
        this.status = status;
    }

    public QueryLOGBean(String strLog) {
        this.strLog = strLog;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStrMCUFW() {
        return strMCUFW;
    }

    public void setStrMCUFW(String strMCUFW) {
        this.strMCUFW = strMCUFW;
    }

    public String getStrBTFW() {
        return strBTFW;
    }

    public void setStrBTFW(String strBTFW) {
        this.strBTFW = strBTFW;
    }

    public String getbWorkMode() {
        return bWorkMode;
    }

    public void setbWorkMode(String bWorkMode) {
        this.bWorkMode = bWorkMode;
    }

    public String getDevSN() {
        return devSN;
    }

    public void setDevSN(String devSN) {
        this.devSN = devSN;
    }

    public String getStrHWTX_GroupInfoAll_BtAPP2() {
        return strHWTX_GroupInfoAll_BtAPP2;
    }

    public void setStrHWTX_GroupInfoAll_BtAPP2(String strHWTX_GroupInfoAll_BtAPP2) {
        this.strHWTX_GroupInfoAll_BtAPP2 = strHWTX_GroupInfoAll_BtAPP2;
    }

    public String getStrHWTX_FW_Config() {
        return strHWTX_FW_Config;
    }

    public void setStrHWTX_FW_Config(String strHWTX_FW_Config) {
        this.strHWTX_FW_Config = strHWTX_FW_Config;
    }

    public String getStrLog() {
        return strLog;
    }

    public void setStrLog(String strLog) {
        this.strLog = strLog;
    }
}
