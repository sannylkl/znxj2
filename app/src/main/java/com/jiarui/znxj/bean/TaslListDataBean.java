package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 任务列表数据
 */
public class TaslListDataBean {
//     "id":"2",
//             "start_time":"1511539200",
//             "end_time":"1508837100",
//             "reservoir":"测试水库",
//             "creator":"艾正强",
//             "route_id":"1",
//             "items"
    private int id;//主键
    private int start_time;//开始时间
    private int end_time;//结束时间
    private String reservoir;//水库
    private String creator;//发布者
    private String route_id;//路线id
    private String items;//检查项
    private String status;//任务状态
    private String task_type;//任务类型
    private int reseid;//水库id


    public TaslListDataBean(int id, int start_time, int end_time, String reservoir, String creator,
                            String route_id, String items, String status, String task_type, int reseid) {
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.reservoir = reservoir;
        this.creator = creator;
        this.route_id = route_id;
        this.items = items;
        this.status = status;
        this.task_type = task_type;
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

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
    }
}
