package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 路线顺序表
 */
@Table(name = "xj_route_detail")
public class xj_route_detailBean {
    @Column(name = "id", isId = true, autoGen = false)//注释列名主键
    private int id;//主键
    @Column(name = "route_id")
    private int route_id;//所属路线
    @Column(name = "location_id")
    private int location_id;//点位
    @Column(name = "location_name")
    private String location_name;//点位名称
    @Column(name = "finished_time")
    private String finished_time;//完成时间
    @Column(name = "next_time")
    private String next_time;//到达下一点位时间
    @Column(name = "if_end")
    private int if_end;//是否最后一个点位，0否，1是
    @Column(name = "sortby")
    private int sortby;//路线顺序

    @Override
    public String toString() {
        return "xj_route_detailBean{" +
                "id='" + id + '\'' +
                "route_id='" + route_id + '\'' +
                "location_id='" + location_id + '\'' +
                "location_name='" + location_name + '\'' +
                "finished_time='" + finished_time + '\'' +
                "if_end='" + if_end + '\'' +
                "sortby='" + sortby + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getFinished_time() {
        return finished_time;
    }

    public void setFinished_time(String finished_time) {
        this.finished_time = finished_time;
    }

    public String getNext_time() {
        return next_time;
    }

    public void setNext_time(String next_time) {
        this.next_time = next_time;
    }

    public int getIf_end() {
        return if_end;
    }

    public void setIf_end(int if_end) {
        this.if_end = if_end;
    }

    public int getSortby() {
        return sortby;
    }

    public void setSortby(int sortby) {
        this.sortby = sortby;
    }

    public xj_route_detailBean() {
    }

    public xj_route_detailBean(int id, int route_id, int location_id, String location_name, String finished_time, String next_time, int if_end, int sortby) {
        this.id = id;
        this.route_id = route_id;
        this.location_id = location_id;
        this.location_name = location_name;
        this.finished_time = finished_time;
        this.next_time = next_time;
        this.if_end = if_end;
        this.sortby = sortby;
    }
}
