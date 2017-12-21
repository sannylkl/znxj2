package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 检查内容异常项表 2017/11/9 0009.
 */
@Table(name = "xj_location_ccabnormalsTable")
public class CheckTheContentAbnormalsTable {
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
    private int content_id;//內容id
    @Column(name = "abnormals_id")
    private int abnormals_id;//内容异常项自身id
    @Column(name = "status")
    private boolean status;//状态，是否选中
    @Column(name = "name")
    private String name;//内容异常项自身名称

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

    public int getAbnormals_id() {
        return abnormals_id;
    }

    public void setAbnormals_id(int abnormals_id) {
        this.abnormals_id = abnormals_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public CheckTheContentAbnormalsTable() {
    }
    public CheckTheContentAbnormalsTable(int taskid, int did, int pid, int cid, int content_id, int abnormals_id, boolean status, String name) {
        this.taskid = taskid;
        this.did = did;
        this.pid = pid;
        this.cid = cid;
        this.content_id = content_id;
        this.abnormals_id = abnormals_id;
        this.status = status;
        this.name = name;
    }
}
