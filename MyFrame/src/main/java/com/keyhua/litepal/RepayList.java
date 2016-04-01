package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 曾金叶 on 2016/3/29.
 * 送礼单表
 */
public class RepayList extends DataSupport {
    private long id = 0;
    private String repay_uid = "";//还礼uuid
    private String repay_person = "";//还礼人
    private String repay_money = "";//礼金额
    private String repay_remark = "";//备注
    private String gift_uid = "";//收礼单id
    private String event_uid = "";//还礼单对应的事件id

    public long getId() {
        return id;
    }

    public String getRepay_uid() {
        return repay_uid;
    }

    public void setRepay_uid(String repay_uid) {
        this.repay_uid = repay_uid;
    }

    public String getRepay_person() {
        return repay_person;
    }

    public void setRepay_person(String repay_person) {
        this.repay_person = repay_person;
    }

    public String getRepay_money() {
        return repay_money;
    }

    public void setRepay_money(String repay_money) {
        this.repay_money = repay_money;
    }

    public String getRepay_remark() {
        return repay_remark;
    }

    public void setRepay_remark(String repay_remark) {
        this.repay_remark = repay_remark;
    }

    public String getGift_uid() {
        return gift_uid;
    }

    public void setGift_uid(String gift_uid) {
        this.gift_uid = gift_uid;
    }

    public String getEvent_uid() {
        return event_uid;
    }

    public void setEvent_uid(String event_uid) {
        this.event_uid = event_uid;
    }
}
