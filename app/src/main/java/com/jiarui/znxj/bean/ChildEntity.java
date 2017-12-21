package com.jiarui.znxj.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class ChildEntity {
    private int groupColor;

    private String groupName;
    private String yichang;
    private ArrayList<String> childNames;
    private ArrayList<Name> names;

    public ChildEntity(String groupName, String yichang) {
        this.groupName = groupName;
        this.yichang = yichang;
    }

    public int getGroupColor() {
        return groupColor;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<String> getChildNames() {
        return childNames;
    }

    public void setGroupColor(int groupColor) {
        this.groupColor = groupColor;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Name> getNames() {
        return names;
    }

    public void setNames(ArrayList<Name> names) {
        this.names = names;
    }

    public void setChildNames(ArrayList<String> childNames) {
        this.childNames = childNames;
    }

    public String getYichang() {
        return yichang;
    }

    public void setYichang(String yichang) {
        this.yichang = yichang;
    }
}
