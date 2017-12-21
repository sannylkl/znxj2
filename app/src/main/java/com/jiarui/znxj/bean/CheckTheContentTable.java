package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 检查内容本地表 on 2017/11/8 0008.
 */
@Table(name = "xj_location_cctable")
public class CheckTheContentTable {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "taskid")
    private int taskid;//任务id（不同任务会有相同点位）
    @Column(name = "did")
    private int did;//点位id
    @Column(name = "pid")
    private int pid;//所属部位id
    @Column(name = "cid")
    private int cid;//自身检查项id
    @Column(name = "content_id")
    private int content_id;//自身內容id
    @Column(name = "ypath")
    private String ypath;//音频路径
    @Column(name = "content")
    private String content;//检查内容
    @Column(name = "status")
    private boolean status;//状态
    @Column(name = "equipmentstatus")
    private boolean equipmentstatus;//设备状态
    @Column(name = "repertstatus")
    private boolean repertstatus;//重复问题
    @Column(name = "othercontent")
    private String othercontent;//其他问题内容
    @Column(name = "issave")
    private boolean issave;//是否保存过

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getYpath() {
        return ypath;
    }

    public void setYpath(String ypath) {
        this.ypath = ypath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isEquipmentstatus() {
        return equipmentstatus;
    }

    public void setEquipmentstatus(boolean equipmentstatus) {
        this.equipmentstatus = equipmentstatus;
    }

    public boolean isRepertstatus() {
        return repertstatus;
    }

    public void setRepertstatus(boolean repertstatus) {
        this.repertstatus = repertstatus;
    }

    public String getOthercontent() {
        return othercontent;
    }

    public void setOthercontent(String othercontent) {
        this.othercontent = othercontent;
    }

    public boolean issave() {
        return issave;
    }

    public void setIssave(boolean issave) {
        this.issave = issave;
    }

    public CheckTheContentTable() {
    }

    public CheckTheContentTable(int taskid, int did, int pid, int cid, int content_id, String ypath, String content, boolean status, boolean equipmentstatus, boolean repertstatus, String othercontent,boolean issave) {
        this.taskid = taskid;
        this.did = did;
        this.pid = pid;
        this.cid = cid;
        this.content_id = content_id;
        this.ypath = ypath;
        this.content = content;
        this.status = status;
        this.equipmentstatus = equipmentstatus;
        this.repertstatus = repertstatus;
        this.othercontent = othercontent;
        this.issave = issave;
    }
}
