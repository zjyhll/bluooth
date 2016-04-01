package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 曾金叶 on 2016/3/29.
 * 收礼单表
 */
public class GiftList extends DataSupport {
    private long id = 0;
    private String gift_uid = "";//礼单uuid
    private String gift_person = "";//礼单参与人
    private String gift_money = "";//礼金额
    private String gift_hl_money = "";//还礼礼金额
    private String gift_remark = "";//备注
    private String event_uid = "";//收礼单对应的事件id
    private String gift_status = "";//收礼单对应的状态,是否还礼(0未还礼，1已还礼)
    private String user_uid = "";//用户id

    public String getGift_hl_money() {
        return gift_hl_money;
    }

    public void setGift_hl_money(String gift_hl_money) {
        this.gift_hl_money = gift_hl_money;
    }

    public String getGift_status() {
        return gift_status;
    }

    public long getId() {
        return id;
    }

    public void setGift_status(String gift_status) {
        this.gift_status = gift_status;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

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
