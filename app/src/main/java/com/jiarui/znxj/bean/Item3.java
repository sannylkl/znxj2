package com.jiarui.znxj.bean;

import java.util.ArrayList;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item3 {
    private String gNameitme;
    private String rNameitme;
    ArrayList<Group_itemBean> secondBean = new ArrayList<Group_itemBean>();
    public Item3() {
    }

    public Item3(String gNameitme, String rNameitme, ArrayList<Group_itemBean> secondBean) {
        this.gNameitme = gNameitme;
        this.rNameitme = rNameitme;
        this.secondBean = secondBean;
    }

    public ArrayList<Group_itemBean> getSecondBean() {
        return secondBean;
    }

    public void setSecondBean(ArrayList<Group_itemBean> secondBean) {
        this.secondBean = secondBean;
    }

    public String getgNameitme() {
        return gNameitme;
    }

    public void setgNameitme(String gNameitme) {
        this.gNameitme = gNameitme;
    }

    public String getrNameitme() {
        return rNameitme;
    }

    public void setrNameitme(String rNameitme) {
        this.rNameitme = rNameitme;
    }
}
