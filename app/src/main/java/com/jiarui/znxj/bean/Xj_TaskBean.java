package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/21 0021.
 * 任务管理表
 */
@Table(name = "xj_task")
public class Xj_TaskBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主见
    @Column(name = "role_name")
    private String role_name;//任务人员类型
    @Column(name = "role_id")
    private int role_id;//任务人员类型id
    @Column(name = "uid")
    private int uid;//所属任务人员id
    @Column(name = "uname")
    private String uname;//任务人员
    @Column(name = "start_time")
    private int start_time;//最终时间
    @Column(name = "end_time")
    private int end_time;//结束时间
    @Column(name = "finished_time")
    private int finished_time;//最终完成时间
    @Column(name = "reseid")
    private int reseid;//水库id
    @Column(name = "reservoir")
    private String reservoir;//水库
    @Column(name = "route_id")
    private int route_id;//路线id
    @Column(name = "route")
    private String route;//巡检路线
    @Column(name = "water_line")
    private String water_line;//水位
    @Column(name = "weather")
    private String weather;//天气
    @Column(name = "down_water")
    private String down_water;//下游水位
    @Column(name = "if_normal")
    private int if_normal;//有无异常，0无1有
    @Column(name = "creator")
    private String creator;//发布者
    @Column(name = "creator_id")
    private int creator_id;//发布者id
    @Column(name = "status")
    private int status;//审核：0预发布，1进行中，2未开始，3完成

    @Override
    public String toString() {
        return "Xj_TaskBean{" +
                "id='" + id + '\'' +
                "role_name='" + role_name + '\'' +
                "role_id='" + role_id + '\'' +
                "uid='" + uid + '\'' +
                "uname='" + uname + '\'' +
                "start_time='" + start_time + '\'' +
                "end_time='" + end_time + '\'' +
                "finished_time='" + finished_time + '\'' +
                "reseid='" + reseid + '\'' +
                "reservoir='" + reservoir + '\'' +
                "route_id='" + route_id + '\'' +
                "route='" + route + '\'' +
                "water_line='" + water_line + '\'' +
                "weather='" + weather + '\'' +
                "down_water='" + down_water + '\'' +
                "if_normal='" + if_normal + '\'' +
                "creator='" + creator + '\'' +
                "creator_id='" + creator_id + '\'' +
                "status='" + status + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
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

    public int getFinished_time() {
        return finished_time;
    }

    public void setFinished_time(int finished_time) {
        this.finished_time = finished_time;
    }

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
    }

    public String getReservoir() {
        return reservoir;
    }

    public void setReservoir(String reservoir) {
        this.reservoir = reservoir;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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

    public int getIf_normal() {
        return if_normal;
    }

    public void setIf_normal(int if_normal) {
        this.if_normal = if_normal;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
