package com.keyhua.renameyourself.main.eventBusBean;


/**
 * Created by 曾金叶 on 2016/4/8.
 */
public class ActivitySetBean{
    private int id = 0;
    private Integer bGpsInterval = 0;
    private Integer bHktAtInterval = 0;
    private Integer bLostContactNum = 0;
    private Integer wWarningDistance1 = 0;
    private Integer wWarningDistance2 = 0;
    private Integer wWarningDistance3 = 0;
    private Integer bWarningBatteryPercent = 0;

    public ActivitySetBean(Integer bGpsInterval, Integer bHktAtInterval, Integer bLostContactNum, Integer wWarningDistance1, Integer wWarningDistance2, Integer wWarningDistance3, Integer bWarningBatteryPercent) {
        this.bGpsInterval = bGpsInterval;
        this.bHktAtInterval = bHktAtInterval;
        this.bLostContactNum = bLostContactNum;
        this.wWarningDistance1 = wWarningDistance1;
        this.wWarningDistance2 = wWarningDistance2;
        this.wWarningDistance3 = wWarningDistance3;
        this.bWarningBatteryPercent = bWarningBatteryPercent;
    }

    public int getId() {
        return id;
    }

    public Integer getbGpsInterval() {
        return bGpsInterval;
    }

    public void setbGpsInterval(Integer bGpsInterval) {
        this.bGpsInterval = bGpsInterval;
    }

    public Integer getbHktAtInterval() {
        return bHktAtInterval;
    }

    public void setbHktAtInterval(Integer bHktAtInterval) {
        this.bHktAtInterval = bHktAtInterval;
    }

    public Integer getbLostContactNum() {
        return bLostContactNum;
    }

    public void setbLostContactNum(Integer bLostContactNum) {
        this.bLostContactNum = bLostContactNum;
    }

    public Integer getwWarningDistance1() {
        return wWarningDistance1;
    }

    public void setwWarningDistance1(Integer wWarningDistance1) {
        this.wWarningDistance1 = wWarningDistance1;
    }

    public Integer getwWarningDistance2() {
        return wWarningDistance2;
    }

    public void setwWarningDistance2(Integer wWarningDistance2) {
        this.wWarningDistance2 = wWarningDistance2;
    }

    public Integer getwWarningDistance3() {
        return wWarningDistance3;
    }

    public void setwWarningDistance3(Integer wWarningDistance3) {
        this.wWarningDistance3 = wWarningDistance3;
    }

    public Integer getbWarningBatteryPercent() {
        return bWarningBatteryPercent;
    }

    public void setbWarningBatteryPercent(Integer bWarningBatteryPercent) {
        this.bWarningBatteryPercent = bWarningBatteryPercent;
    }
}
