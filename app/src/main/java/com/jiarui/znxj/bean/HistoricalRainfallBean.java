package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class HistoricalRainfallBean {
    private String time;
    private String jianyu;
    private String timeduan;

    public HistoricalRainfallBean(String time, String jianyu, String timeduan) {
        this.time = time;
        this.jianyu = jianyu;
        this.timeduan = timeduan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJianyu() {
        return jianyu;
    }

    public void setJianyu(String jianyu) {
        this.jianyu = jianyu;
    }

    public String getTimeduan() {
        return timeduan;
    }

    public void setTimeduan(String timeduan) {
        this.timeduan = timeduan;
    }
}
