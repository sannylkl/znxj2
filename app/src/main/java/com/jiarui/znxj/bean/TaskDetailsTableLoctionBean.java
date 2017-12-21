package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 任务详情检查点位表 2017/11/3 0003.
 */
@Table(name = "xj_location_task")
public class TaskDetailsTableLoctionBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "did")
    private int did;//点位id
    @Column(name = "taskid")
    private int taskid;//任务id（不同任务会有相同点位）
    @Column(name = "reseid")
    private int reseid;//所属水库id
    @Column(name = "title")
    private String title;//点位名称
    @Column(name = "idcard")
    private String idcard;//标识卡
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

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
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

    public TaskDetailsTableLoctionBean() {
    }

    public TaskDetailsTableLoctionBean(int did,int taskid, int reseid, String title, String idcard, String address, String xpoint, String ypoint, int status, String signup) {
        this.did = did;
        this.taskid = taskid;
        this.reseid = reseid;
        this.title = title;
        this.idcard = idcard;
        this.address = address;
        this.xpoint = xpoint;
        this.ypoint = ypoint;
        this.status = status;
        this.signup = signup;
    }
}
