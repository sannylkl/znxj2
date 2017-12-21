package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class AddressListBean {
    private String icon;
    private String name;
    private String phone;

    public AddressListBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
