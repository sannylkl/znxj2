package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 任务表
 */
@Table(name = "xj_task_x")
public class TaslListBeanTable {
//     "id":"2",
//             "start_time":"1511539200",
//             "end_time":"1508837100",
//             "reservoir":"测试水库",
//             "creator":"艾正强",
//             "route_id":"1",
//             "items"
    @Column(name = "id", isId = true, autoGen = false)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "start_time")
    private int start_time;//开始时间
    @Column(name = "end_time")
    private int end_time;//结束时间
    @Column(name = "reservoir")
    private String reservoir;//水库
    @Column(name = "creator")
    private String creator;//发布者
    @Column(name = "route_id")
    private String route_id;//路线id
    @Column(name = "items")
    private String items;//检查项
    @Column(name = "status")
    private String status;//任务状态
    @Column(name = "task_type")
    private String task_type;//任务类型
    @Column(name = "water_line")
    private String water_line;//'水位'
    @Column(name = "weather")
    private String weather;//'天气'
    @Column(name = "down_water")
    private String down_water;//'下游水位'
    @Column(name = "uid")
    private String uid;//'用户id'
    @Column(name = "reseid")
    private int reseid;//水库id
    public TaslListBeanTable() {
    }

    public TaslListBeanTable(int id, int start_time, int end_time, String reservoir, String creator,
                             String route_id, String items, String status, String task_type,
                             String water_line, String weather, String down_water, String uid,int reseid) {
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.reservoir = reservoir;
        this.creator = creator;
        this.route_id = route_id;
        this.items = items;
        this.status = status;
        this.task_type = task_type;
        this.water_line = water_line;
        this.weather = weather;
        this.down_water = down_water;
        this.uid = uid;
        this.reseid = reseid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getWater_line() {
        return water_line;
    }

    public void setWater_line(String water_line) {
        this.water_line = water_line;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDown_water() {
        return down_water;
    }

    public void setDown_water(String down_water) {
        this.down_water = down_water;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
    }
}
