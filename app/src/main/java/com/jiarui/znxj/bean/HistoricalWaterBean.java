package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class HistoricalWaterBean {
    private String time;
    private String shuiwei;
    private int qushi;

    public HistoricalWaterBean(String time, String shuiwei, int qushi) {
        this.time = time;
        this.shuiwei = shuiwei;
        this.qushi = qushi;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShuiwei() {
        return shuiwei;
    }

    public void setShuiwei(String shuiwei) {
        this.shuiwei = shuiwei;
    }

    public int getQushi() {
        return qushi;
    }

    public void setQushi(int qushi) {
        this.qushi = qushi;
    }
}
