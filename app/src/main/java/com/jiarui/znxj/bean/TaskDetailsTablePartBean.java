package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 任务详情点位检查部位+检查项目表 （树状）2017/11/3 0003.
 */
@Table(name = "xj_location_part")
public class TaskDetailsTablePartBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键
    private int id;
    @Column(name = "did")
    private int did;//点位id
    @Column(name = "taskid")
    private int taskid;//任务id（不同任务会有相同点位）
    @Column(name = "cid")
    private int cid;////部位/检查项自己在后台的id（不同任务会有相同的检查部位/检查项）
    //因此不仅仅是要查出所有检查部位还要找到属于这个任务的检查部位
    @Column(name = "pid")
    private int pid;//所属部位id
    @Column(name = "name")
    private String name;//部位/检查项名称
    @Column(name = "status")
    private int status;//是否启用 0否1是

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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TaskDetailsTablePartBean() {
    }

    public TaskDetailsTablePartBean(int did, int taskid, int cid, int pid, String name, int status) {
        this.did = did;
        this.taskid = taskid;
        this.cid = cid;
        this.pid = pid;
        this.name = name;
        this.status = status;
    }
}
