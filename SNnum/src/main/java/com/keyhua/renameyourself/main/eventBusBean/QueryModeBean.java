package com.keyhua.renameyourself.main.eventBusBean;

/**
 * Created by 曾金叶 on 2016/4/7.
 */
public class QueryModeBean {
    private byte[] mode;

    public QueryModeBean(byte[] mode) {
        this.mode = mode;
    }

    public byte[] getMode() {
        return mode;
    }

    public void setMode(byte[] mode) {
        this.mode = mode;
    }
}
