package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/20 0020.
 * 巡检点设置
 */
@Table(name = "patrol_setting")
public class PatrolSettingBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主见
    @Column(name = "skname")
    private String skname;//水库名称
    @Column(name = "dwname")
    private String dwname;//点位名称
    @Column(name = "Observation")
    private String Observation;//观察物
    @Column(name = "weidu")
    private String weidu;//纬度
    @Column(name = "jingdu")
    private String jingdu;//精度
    @Column(name = "card")
    private String card;//标示卡

    @Override
    public String toString() {
        return "PatrolSettingBean{" +
                "id='" + id + '\'' +
                "skname='" + skname + '\'' +
                "dwname='" + dwname + '\'' +
                "Observation='" + Observation + '\'' +
                "weidu='" + weidu + '\'' +
                "jingdu='" + jingdu + '\'' +
                "card='" + card + '\'' +
                '}';
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkname() {
        return skname;
    }

    public void setSkname(String skname) {
        this.skname = skname;
    }

    public String getDwname() {
        return dwname;
    }

    public void setDwname(String dwname) {
        this.dwname = dwname;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
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
