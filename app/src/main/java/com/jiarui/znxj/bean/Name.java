package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class Name {
    private int color;
    private String name;
    private String message;

    public Name(String name) {
        this.name = name;
        this.message = message;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
