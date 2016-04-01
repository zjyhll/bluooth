package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 曾金叶 on 2016/3/30.
 * 商家表
 */
public class MerchantList extends DataSupport {
    private long id = 0;
    private String merchant_uid = "";//商家uuid
    private String merchant_img = "";//商家图片地址
    private String merchant_desc = "";//商家介绍
    private String merchant_phone = "";//商家电话
    private String merchant_location = "";//商家地址
    private String merchant_score = "";//商家评分

    public long getId() {
        return id;
    }

    public String getMerchant_uid() {
        return merchant_uid;
    }

    public void setMerchant_uid(String merchant_uid) {
        this.merchant_uid = merchant_uid;
    }

    public String getMerchant_img() {
        return merchant_img;
    }

    public void setMerchant_img(String merchant_img) {
        this.merchant_img = merchant_img;
    }

    public String getMerchant_desc() {
        return merchant_desc;
    }

    public void setMerchant_desc(String merchant_desc) {
        this.merchant_desc = merchant_desc;
    }

    public String getMerchant_phone() {
        return merchant_phone;
    }

    public void setMerchant_phone(String merchant_phone) {
        this.merchant_phone = merchant_phone;
    }

    public String getMerchant_location() {
        return merchant_location;
    }

    public void setMerchant_location(String merchant_location) {
        this.merchant_location = merchant_location;
    }

    public String getMerchant_score() {
        return merchant_score;
    }

    public void setMerchant_score(String merchant_score) {
        this.merchant_score = merchant_score;
    }
}
