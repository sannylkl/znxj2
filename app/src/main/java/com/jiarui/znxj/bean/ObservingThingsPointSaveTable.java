package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 巡检点单个点位的观测物的本地表（要区分用户，因为不同用户给同一个水库设置点位，但是观测物品可能是不同的）2017/12/1 0001.
 */
@Table(name = "xj_location_pott")
public class ObservingThingsPointSaveTable {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "user_id")
    private int user_id;//用户id
    @Column(name = "reseid")
    private int reseid;//水库id
    @Column(name = "did")
    private int did;//点位id
    @Column(name = "otid")
    private int otid;//观测物id
    @Column(name = "pid")
    private int pid;//检查部位id=0
    @Column(name = "category")
    private int category;//类别
    @Column(name = "name")
    private String name;//观测物品名称
    @Column(name = "description")
    private String description;//描述
    @Column(name = "status")
    private boolean status;//状态
    @Column(name = "level")
    private int level;//等级

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getOtid() {
        return otid;
    }

    public void setOtid(int otid) {
        this.otid = otid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ObservingThingsPointSaveTable() {
    }

    public ObservingThingsPointSaveTable(int user_id, int reseid, int did, int otid, int pid, int category, String name, String description, boolean status, int level) {
        this.user_id = user_id;
        this.reseid = reseid;
        this.did = did;
        this.otid = otid;
        this.pid = pid;
        this.category = category;
        this.name = name;
        this.description = description;
        this.status = status;
        this.level = level;
    }
}
