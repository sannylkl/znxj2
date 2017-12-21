package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/21 0021.
 * 检查部位表
 */
@Table(name = "xj_part_setting")
public class Xj_Part_SettingBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "item_id")
    private String item_id;//所属检查项目id
    @Column(name = "name")
    private String name;//部位名称
    @Column(name = "status")
    private int status;//是否启用

    @Override
    public String toString() {
        return "Xj_Part_SettingBean{" +
                "id='" + id + '\'' +
                "item_id='" + item_id + '\'' +
                "name='" + name + '\'' +
                "status='" + status + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
