package com.keyhua.renameyourself.main.eventBusBean;

import java.util.List;

/**
 * Created by 曾金叶 on 2016/4/8.
 */
public class SetBean {
    private boolean isFaild = false;
    private List<Integer> errorNumList = null;// 未同步成功的队员

    public SetBean(boolean isFaild,List<Integer> errorNumList ) {
        this.errorNumList = errorNumList;
        this.isFaild = isFaild;
    }

    public boolean isFaild() {
        return isFaild;
    }

    public void setIsFaild(boolean isFaild) {
        this.isFaild = isFaild;
    }

    public List<Integer> getErrorNumList() {
        return errorNumList;
    }

    public void setErrorNumList(List<Integer> errorNumList) {
        this.errorNumList = errorNumList;
    }
}
