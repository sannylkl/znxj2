package com.jiarui.znxj.bean;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Group {
    private String gName;
    private String rName;

    public Group(String gName, String rName) {
        this.gName = gName;
        this.rName = rName;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }
}
