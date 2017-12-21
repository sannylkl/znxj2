package com.jiarui.znxj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class MemberRules {
    private int id;
    private String name;
    private String yichang;
    private List<Name> lstContent;
    private boolean isCheck;

    public MemberRules(int id, String name, String yichang, List<Name> lstContent, boolean isCheck) {
        this.id = id;
        this.name = name;
        this.yichang = yichang;
        this.lstContent = lstContent;
        this.isCheck = isCheck;
    }
    public MemberRules(){
        
    }
    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
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

    public List<Name> getLstContent() {
        return lstContent;
    }

    public void setLstContent(List<Name> lstContent) {
        this.lstContent = lstContent;
    }

    public String getYichang() {
        return yichang;
    }

    public void setYichang(String yichang) {
        this.yichang = yichang;
    }
}
