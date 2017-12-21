package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class PatrolListBean {
    private String name;
    private String dianwei;
    private String gcw;
    private String weidu;
    private String jingdu;
    private String card;

    public PatrolListBean(String name, String dianwei, String gcw, String weidu, String jingdu, String card) {
        this.name = name;
        this.dianwei = dianwei;
        this.gcw = gcw;
        this.weidu = weidu;
        this.jingdu = jingdu;
        this.card = card;
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

    public String getGcw() {
        return gcw;
    }

    public void setGcw(String gcw) {
        this.gcw = gcw;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
