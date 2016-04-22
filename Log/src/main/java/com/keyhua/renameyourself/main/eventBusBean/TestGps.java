package com.keyhua.renameyourself.main.eventBusBean;

/**
 * Created by 曾金叶 on 2016/4/13.
 */
public class TestGps {
    byte[] buffer=null;

    public TestGps(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
}
