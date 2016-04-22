package com.keyhua.renameyourself.main.eventBusBean;

/**
 * Created by 曾金叶 on 2016/4/20.表示返回长度为0，没有表
 */
public class LengthZero {
    private int whitchInt = 0;//1为gps数据表为空，2为队员信息表为空

    public LengthZero(int whitchInt) {
        this.whitchInt = whitchInt;
    }

    public int getWhitchInt() {
        return whitchInt;
    }

    public void setWhitchInt(int whitchInt) {
        this.whitchInt = whitchInt;
    }
}
