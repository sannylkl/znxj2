package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/9 0009.
 */

public class TaskCenter_listBean {

    String task_item_serial;//序号
    String task_item_commis;//内容
    String task_item_time;//时间

    public String getTask_item_serial() {
        return task_item_serial;
    }

    public void setTask_item_serial(String task_item_serial) {
        this.task_item_serial = task_item_serial;
    }

    public String getTask_item_commis() {
        return task_item_commis;
    }

    public void setTask_item_commis(String task_item_commis) {
        this.task_item_commis = task_item_commis;
    }

    public String getTask_item_time() {
        return task_item_time;
    }

    public void setTask_item_time(String task_item_time) {
        this.task_item_time = task_item_time;
    }

    public TaskCenter_listBean(String task_item_serial, String task_item_commis, String task_item_time) {
        this.task_item_serial = task_item_serial;
        this.task_item_commis = task_item_commis;
        this.task_item_time = task_item_time;
    }
}
