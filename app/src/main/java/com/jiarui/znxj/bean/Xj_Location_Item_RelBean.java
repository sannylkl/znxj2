package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 点位与检查项关联表
 */
@Table(name = "xj_location_item_rel")
public class Xj_Location_Item_RelBean {
    @Column(name="id",isId = true,autoGen = true)
    private int id;
    @Column(name = "location_id")
    private int location_id;//点位id
    @Column(name = "item_id")
    private int item_id;//观测物id
    @Override
    public String toString() {
        return "Xj_Location_Item_RelBean{" +
                "id='" + getId() + '\'' +
                "location_id='" + location_id + '\'' +
                "item_id='" + item_id + '\'' +
                '}';
    }
    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
