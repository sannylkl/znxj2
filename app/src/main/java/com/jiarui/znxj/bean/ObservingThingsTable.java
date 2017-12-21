package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 巡检点预置的整个水库的观测物的本地表（不用区分用户，因为一个水库是固定了，谁取出来都是一样的） 2017/12/1 0001.
 */
@Table(name = "xj_location_ott")
public class ObservingThingsTable {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "reseid")
    private int reseid;//水库id
    @Column(name = "otid")
    private int otid;//观测物id
    @Column(name = "pid")
    private int pid;//检查部位id=0
    @Column(name = "category")
    private int category;//类别
    @Column(name = "name")
    private String name;//观测物名称
    @Column(name = "description")
    private String description;//描述
    @Column(name = "status")
    private boolean status;//状态
    @Column(name = "level")
    private int level;//等级

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
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

    public ObservingThingsTable() {
    }

    public ObservingThingsTable(int reseid, int otid, int pid, int category, String name, String description, boolean status, int level) {
        this.reseid = reseid;
        this.otid = otid;
        this.pid = pid;
        this.category = category;
        this.name = name;
        this.description = description;
        this.status = status;
        this.level = level;
    }
}
