package com.keyhua.renameyourself.main.eventBusBean;

/**
 * Created by 曾金叶 on 2016/4/7.
 */
public class QueryTXBBean {
    private  String tcbSnNum;

    public QueryTXBBean(String tcbSnNum) {
        this.tcbSnNum = tcbSnNum;
    }

    public String getTcbSnNum() {
        return tcbSnNum;
    }

    public void setTcbSnNum(String tcbSnNum) {
        this.tcbSnNum = tcbSnNum;
    }
}
