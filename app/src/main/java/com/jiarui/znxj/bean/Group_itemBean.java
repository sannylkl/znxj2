package com.jiarui.znxj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15 0015.
 */

public class Group_itemBean {
    int image;
    private String name;
    private String cont;
    private List<Group_item_gridBean> bean;
    public Group_itemBean() {

    }
    public Group_itemBean(int image, String name, String cont) {
        this.image = image;
        this.name = name;
        this.cont = cont;
    }

    public Group_itemBean(int image, String name, List<Group_item_gridBean> bean) {
        this.image = image;
        this.name = name;
        this.bean = bean;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }
    public List<Group_item_gridBean> getBean() {
        return bean;
    }
    public void setBean(List<Group_item_gridBean> bean) {
        this.bean = bean;
    }

}
