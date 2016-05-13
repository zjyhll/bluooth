package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

public class PlanGpsInfo extends DataSupport {
    /**
     *
     */
    private int id;
    /***
     * 轨迹数据内容模板： [ { "name": "",//地点名称 "describe": "",地点描述 "longitude": "",//经度
     * "latitude": ""//纬度 } ]
     */
    private String locationInfo;// 轨迹信息
    private String name; // 轨迹名称必填
    private String start_time; // 开始时间
    private String end_time; // /结束时间
    private String isChecked;//是否被选择了 1已选，2未选
    private Integer trace_id;//
    private String picture_url;//

    public Integer getTrace_id() {
        return trace_id;
    }

    public void setTrace_id(Integer trace_id) {
        this.trace_id = trace_id;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
