package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/21 0021.
 * 水库表
 */
@Table(name = "xj_reservoirs")
public class Xj_ReservoirsBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "title")
    private String title;//水库名称
    @Column(name = "leader")
    private String leader;//负责人
    @Column(name = "year")
    private int year;//建成时间
    @Column(name = "water_limit")
    private String water_limit;//水库汛限水位
    @Column(name = "water_level")
    private String water_level;//警戒水位
    @Column(name = "description")
    private String description;//水库简介
    @Column(name = "status")
    private int status;//是否启用 0否,1是

    @Override
    public String toString() {
        return "Xj_ReservoirsBean{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                "leader='" + leader + '\'' +
                "year='" + year + '\'' +
                "water_limit='" + water_limit + '\'' +
                "water_level='" + water_level + '\'' +
                "description='" + description + '\'' +
                "status='" + status + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getWater_limit() {
        return water_limit;
    }

    public void setWater_limit(String water_limit) {
        this.water_limit = water_limit;
    }

    public String getWater_level() {
        return water_level;
    }

    public void setWater_level(String water_level) {
        this.water_level = water_level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
