package com.jiarui.znxj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class MemberType {
    private int id;
    private String name;
    private List<MemberRules> lstFather;
    private boolean isCheck;

    public MemberType(int id, String name, List<MemberRules> lstFather, boolean isCheck) {
        this.id = id;
        this.name = name;
        this.lstFather = lstFather;
        this.setCheck(isCheck);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberRules> getLstFather() {
        return lstFather;
    }

    public void setLstFather(List<MemberRules> lstFather) {
        this.lstFather = lstFather;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
