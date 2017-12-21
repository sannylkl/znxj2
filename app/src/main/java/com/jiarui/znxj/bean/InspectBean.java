package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class InspectBean {
    private int id;
    String title;
    String Situation;

    public InspectBean(int id, String title, String situation) {
        this.id = id;
        this.title = title;
        Situation = situation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSituation() {
        return Situation;
    }

    public void setSituation(String situation) {
        Situation = situation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
