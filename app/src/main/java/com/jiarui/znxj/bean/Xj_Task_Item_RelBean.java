package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 */
@Table(name = "xj_task_item_rel")
public class Xj_Task_Item_RelBean {
    @Column(name = "task_id",isId = true, autoGen = true)
    private int task_id;//任务id
    @Column(name = "item_id")
    private int item_id;//检查项目id
    @Override
    public String toString() {
        return "Xj_Task_Item_RelBean{" +
                "task_id='" + task_id + '\'' +
                "item_id='" + item_id + '\'' +
                '}';
    }
    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }
}
