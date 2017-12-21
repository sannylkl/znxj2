package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/24 0024.
 */
@Table(name = "xj_longitude_latitude")
public class Xj_Longitude_LatitudeBean {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;//主键
    @Column(name = "taskid")
    private int taskid;//任务id
    @Column(name = "longitude")
    private double longitude;//经度
    @Column(name = "latitude")
    private double latitude;//纬度

    public String toString() {
        return "Xj_Longitude_LatitudeBean{" +
                "id='" + id + '\'' +
                "taskid='" + taskid + '\'' +
                "longitude='" + longitude + '\'' +
                "latitude='" + latitude + '\'' +
                '}';
    }

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
