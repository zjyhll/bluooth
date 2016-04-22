package com.keyhua.renameyourself.main.eventBusBean;

/**
 * Created by 曾金叶 on 2016/4/7.
 */
public class ConnectBean {
    boolean mConnected=false;

    public ConnectBean(boolean mConnected) {
        this.mConnected = mConnected;
    }

    public boolean ismConnected() {
        return mConnected;
    }

    public void setmConnected(boolean mConnected) {
        this.mConnected = mConnected;
    }
}
