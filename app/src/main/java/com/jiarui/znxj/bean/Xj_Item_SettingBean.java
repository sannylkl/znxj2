package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 检查项目表
 */
@Table(name = "xj_item_setting")
public class Xj_Item_SettingBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "pid")
    private int pid;//父级id
    @Column(name = "name")
    private String name;//项目名称
    @Column(name = "status")
    private int status;//是否启用

    @Override
    public String toString() {
        return "Xj_Item_SettingBean{" +
                "id='" + id + '\'' +
                "pid='" + pid + '\'' +
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
