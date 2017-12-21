package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 检查内容视频表 2017/11/9 0009.
 */
@Table(name = "xj_location_ccvideotable")
public class CheckTheContentVideoTable {
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
    @Column(name = "video_path")
    private String video_path;//检查项目已经录制了的视频

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

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public CheckTheContentVideoTable() {
    }
    public CheckTheContentVideoTable(int taskid, int did, int pid, int cid , String video_path) {
        this.taskid = taskid;
        this.did = did;
        this.pid = pid;
        this.cid = cid;
        this.video_path = video_path;
    }
}
