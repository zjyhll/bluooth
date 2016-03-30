package com.keyhua.litepal;

import org.litepal.crud.DataSupport;

/**
 * Created by 曾金叶 on 2016/3/30.
 */
public class MerchantList extends DataSupport {
    private String merchant_uid = "";
    private String merchant_img = "";
    private String merchant_desc = "";
    private String merchant_phone = "";
    private String merchant_location = "";
    private String merchant_score = "";

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
