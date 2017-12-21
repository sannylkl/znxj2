package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class UrgentBean2 {
    String time;//时间
    String site;//站点
    String waterdepth;//水深
    String rflow;//入水流量
    String cflow;//出水流量
    String interval;//时段水量

    public UrgentBean2(String time, String site, String waterdepth, String rflow, String cflow, String interval) {
        this.time = time;
        this.site = site;
        this.waterdepth = waterdepth;
        this.rflow = rflow;
        this.cflow = cflow;
        this.interval = interval;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getWaterdepth() {
        return waterdepth;
    }

    public void setWaterdepth(String waterdepth) {
        this.waterdepth = waterdepth;
    }

    public String getRflow() {
        return rflow;
    }

    public void setRflow(String rflow) {
        this.rflow = rflow;
    }

    public String getCflow() {
        return cflow;
    }

    public void setCflow(String cflow) {
        this.cflow = cflow;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
