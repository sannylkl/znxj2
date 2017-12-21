package com.jiarui.znxj.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class ParentEntity {

    private int groupColor;
    private String groupName;

    public ParentEntity(String groupName) {
        this.groupName = groupName;
    }

    private ArrayList<ChildEntity> childs;

    public int getGroupColor() {
        return groupColor;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<ChildEntity> getChilds() {
        return childs;
    }

    public void setGroupColor(int groupColor) {
        this.groupColor = groupColor;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setChilds(ArrayList<ChildEntity> childs) {
        this.childs = childs;
    }
}
