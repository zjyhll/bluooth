package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 曾金叶 on 2016/3/29.
 * 事件表
 */
public class Event extends DataSupport {
    private long id = 0;
    private String event_uid = "";//事件uuid
    private String event_name = "";//事件名称
    private String event_type = "";//事件类型  0:收礼  1:送礼
    private String event_location = "";//事件地点
    private String event_time = "";//事件时间
    private String user_uid = "";//事件对应的用户id
    private String event_remarks = "";//事件对应的备注
    //对应的收礼单
    private List<GiftList> giftList = new ArrayList<>();
    //对应的送礼单
    private List<RepayList> repayList = new ArrayList<>();


    public String getEvent_remarks() {
        return event_remarks;
    }

    public void setEvent_remarks(String event_remarks) {
        this.event_remarks = event_remarks;
    }

    public List<GiftList> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftList> giftList) {
        this.giftList = giftList;
    }

    public List<RepayList> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<RepayList> repayList) {
        this.repayList = repayList;
    }

    public String getEvent_uid() {
        return String.valueOf(id);
    }

    public void setEvent_uid(String event_uid) {
        this.event_uid = event_uid;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public long getId() {
        return id;
    }
}
