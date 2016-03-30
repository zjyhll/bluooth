package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 曾金叶 on 2016/3/29.
 */
public class GiftList extends DataSupport {
    private String gift_uid = "";
    private String gift_person = "";
    private String gift_money = "";
    private String gift_remark = "";
    private String event_uid = "";

    public String getGift_uid() {
        return gift_uid;
    }

    public void setGift_uid(String gift_uid) {
        this.gift_uid = gift_uid;
    }

    public String getGift_person() {
        return gift_person;
    }

    public void setGift_person(String gift_person) {
        this.gift_person = gift_person;
    }

    public String getGift_money() {
        return gift_money;
    }

    public void setGift_money(String gift_money) {
        this.gift_money = gift_money;
    }

    public String getGift_remark() {
        return gift_remark;
    }

    public void setGift_remark(String gift_remark) {
        this.gift_remark = gift_remark;
    }

    public String getEvent_uid() {
        return event_uid;
    }

    public void setEvent_uid(String event_uid) {
        this.event_uid = event_uid;
    }
}
