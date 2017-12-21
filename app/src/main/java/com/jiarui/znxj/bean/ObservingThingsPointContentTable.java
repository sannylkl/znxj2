package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 巡检点单个点保存内容本地表（要区分用户，因为不同用户给同一个水库设置点位，数据可能是不同的） 2017/12/1 0001.
 */
@Table(name = "xj_location_pc")
public class ObservingThingsPointContentTable {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键点位id
    @Column(name = "did")
    private int did;//点位id（成功就给后台的，不成功就用保存之后的id，结合状态。确保唯一.就是走修改接口的时候要用到传did进行修改。添加是不用的。
    // 本地点的观测物的did就是这个本地巡检点表里自动增长的id）
    @Column(name = "title")
    private String title;//点位名称
    @Column(name = "user_id")
    private int user_id;//用户id
    @Column(name = "reseid")
    private int reseid;//水库id
    @Column(name = "rese_name")
    private String rese_name;//水库名称
    @Column(name = "idcard")
    private String idcard;//标示卡号
    @Column(name = "xpoint")
    private String xpoint;//经度
    @Column(name = "ypoint")
    private String ypoint;//纬度
    @Column(name = "status")
    private boolean status;//状态。是保存成功，还是保存失败。这个是列表页面用来区分做过修改
    @Column(name = "isupadta")
    private boolean isupadta;//1.第一次异常添加成功过状态就是true（再进来就还是走修改接口），添加失败就是false（再进来就还是走添加接口）
    // 2.用来判断进入设置页面是走保存接口还是修改接口。
    // 3.再次进来走修改接口状态不用修改状态还是true。
    // 4.之前添加失败再次进来走添加接口成功状态就修改为false。不成功还是修改为false（其实可以不再次改状态，因为之前就是不成功状态就是false。）
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
    }

    public String getRese_name() {
        return rese_name;
    }

    public void setRese_name(String rese_name) {
        this.rese_name = rese_name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isupadta() {
        return isupadta;
    }

    public void setIsupadta(boolean isupadta) {
        this.isupadta = isupadta;
    }

    public ObservingThingsPointContentTable() {
    }

    public ObservingThingsPointContentTable(int did, String title, int user_id, int reseid, String rese_name, String idcard, String xpoint, String ypoint, boolean status, boolean isupadta) {
        this.did = did;
        this.title = title;
        this.user_id = user_id;
        this.reseid = reseid;
        this.rese_name = rese_name;
        this.idcard = idcard;
        this.xpoint = xpoint;
        this.ypoint = ypoint;
        this.status = status;
        this.isupadta = isupadta;
    }
}
