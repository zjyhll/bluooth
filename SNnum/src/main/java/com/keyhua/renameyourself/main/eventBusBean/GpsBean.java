package com.keyhua.renameyourself.main.eventBusBean;

import com.keyhua.renameyourself.main.protocol.HwtxDataGpsInfoDataComp;
import com.keyhua.renameyourself.main.protocol.HwtxDataGrpGpsInfoItem;

import java.util.ArrayList;

/**
 * Created by 曾金叶 on 2016/4/8.
 */
public class GpsBean {
    private String gpsTime = "";
    private double gpsLatitude = 0;
    private double gpsLongitude = 0;
    private ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray  = null;

    private boolean hasNewConfig = false;
    //大表2更新标志
    private  boolean hasNewBt = false;
    public GpsBean(String gpsTime, double gpsLatitude, double gpsLongitude, ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray, boolean hasNewConfig,boolean hasNewBt) {
        this.gpsTime = gpsTime;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.indexGpsInfoArray = indexGpsInfoArray;
        this.hasNewConfig = hasNewConfig;
        this.hasNewBt = hasNewBt;
    }

    public boolean isHasNewConfig() {
        return hasNewConfig;
    }

    public void setHasNewConfig(boolean hasNewConfig) {
        this.hasNewConfig = hasNewConfig;
    }

    public boolean isHasNewBt() {
        return hasNewBt;
    }

    public void setHasNewBt(boolean hasNewBt) {
        this.hasNewBt = hasNewBt;
    }

    public ArrayList<HwtxDataGrpGpsInfoItem> getIndexGpsInfoArray() {
        return indexGpsInfoArray;
    }

    public void setIndexGpsInfoArray(ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray) {
        this.indexGpsInfoArray = indexGpsInfoArray;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

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
}
