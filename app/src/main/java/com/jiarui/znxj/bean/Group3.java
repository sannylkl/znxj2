package com.jiarui.znxj.bean;

import java.util.ArrayList;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Group3 {
    private String gName;
    private String rName;
    ArrayList<Item3> firstData = new ArrayList<Item3>();

    public Group3() {

    }

    public Group3(String gName, String rName, ArrayList<Item3> firstData) {
        this.gName = gName;
        this.rName = rName;
        this.firstData = firstData;
    }

    public ArrayList<Item3> getFirstData() {
        return firstData;
    }

    public void setFirstData(ArrayList<Item3> firstData) {
        this.firstData = firstData;
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
