package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 任务与点位关系表
 */
@Table(name = "xj_task_location_rel")
public class Xj_Task_Location_RelBean {
    @Column(name = "task_id")
    private int task_id;//任务id
    @Column(name = "location_id")
    private int location_id;//点位id
    @Override
    public String toString() {
        return "Xj_Task_Location_RelBean{" +
                "task_id='" + task_id + '\'' +
                "location_id='" + location_id + '\'' +
                '}';
    }
    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }
}
