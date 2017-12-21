package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/21 0021.
 * 水库点位表
 */
@Table(name = "xj_location")
public class Xj_LocationBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "reseid")
    private int reseid;//所属水库id
    @Column(name = "title")
    private String title;//点位名称
    @Column(name = "idcard")
    private String idcard;//标识卡
    @Column(name = "item_name")
    private String item_name;//观测物(检查项目)
    @Column(name = "item_id")
    private int item_id;//观测物id
    @Column(name = "address")
    private String address;//地址
    @Column(name = "xpoint")
    private String xpoint;//经度
    @Column(name = "ypoint")
    private String ypoint;//纬度
    @Column(name = "status")
    private int status;//是否启用 0否1是
    @Column(name = "signup")
    private String signup;//签到情况
    @Override
    public String toString() {
        return "Xj_LocationBean{" +
                "id='" + id + '\'' +
                "reseid='" + reseid + '\'' +
                "title='" + title + '\'' +
                "idcard='" + idcard + '\'' +
                "item_name='" + item_name + '\'' +
                "item_id='" + item_id + '\'' +
                "address='" + address + '\'' +
                "xpoint='" + xpoint + '\'' +
                "ypoint='" + ypoint + '\'' +
                "status='" + status + '\'' +
                "signup='" + signup + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getXpoint() {
        return xpoint;
    }

    public void setXpoint(String xpoint) {
        this.xpoint = xpoint;
    }

    public String getYpoint() {
        return ypoint;
    }

    public void setYpoint(String ypoint) {
        this.ypoint = ypoint;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSignup() {
        return signup;
    }

    public void setSignup(String signup) {
        this.signup = signup;
    }
}
