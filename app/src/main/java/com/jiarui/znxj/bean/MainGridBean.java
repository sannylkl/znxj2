package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class MainGridBean {

    int Image;
    String title;

    public MainGridBean(int image, String title) {
        Image = image;
        this.title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
