package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 曾金叶 on 2016/3/30.
 * 用户商家中间表
 */
public class UserMerchant extends DataSupport {
    private long id = 0;
    private String user_uid = "";//用户uuid
    private String merchant_uid = "";//商家uuid

    public long getId() {
        return id;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getMerchant_uid() {
        return merchant_uid;
    }

    public void setMerchant_uid(String merchant_uid) {
        this.merchant_uid = merchant_uid;
    }
}
