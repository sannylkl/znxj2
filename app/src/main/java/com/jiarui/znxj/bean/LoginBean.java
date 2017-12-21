package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class LoginBean {
    private String name;
    private String id;
    public LoginBean(String name) {
    this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LoginBean(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
