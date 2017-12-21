package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class PatrolReportBean {
    private String data;
    private String name;
    private String dianwei;
    private String jiancha;
    private String time;

    public PatrolReportBean(String data, String name, String dianwei, String jiancha, String time) {
        this.data = data;
        this.name = name;
        this.dianwei = dianwei;
        this.jiancha = jiancha;
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDianwei() {
        return dianwei;
    }

    public void setDianwei(String dianwei) {
        this.dianwei = dianwei;
    }

    public String getJiancha() {
        return jiancha;
    }

    public void setJiancha(String jiancha) {
        this.jiancha = jiancha;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
