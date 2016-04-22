package com.keyhua.renameyourself.main.eventBusBean;

import java.util.List;

/**
 * Created by 曾金叶 on 2016/4/7.
 */
public class MemberInfoBean {
    private boolean isFaild = false;
    private List<Integer> errorNumList = null;// 未同步成功的队员

    public MemberInfoBean(boolean isFaild, List<Integer> errorNumList) {
        this.isFaild = isFaild;
        this.errorNumList = errorNumList;
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
