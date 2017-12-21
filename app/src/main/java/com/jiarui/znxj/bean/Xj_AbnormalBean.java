package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 异常分类
 */
@Table(name = "xj_abnormal")
public class Xj_AbnormalBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "content_id")
    private int content_id;//所属内容
    @Column(name = "name")
    private String name;//异常分类名称
    @Column(name = "status")
    private int status;//是否启用

    @Override
    public String toString() {
        return "Xj_AbnormalBean{" +
                "id='" + id + '\'' +
                "content_id='" + content_id + '\'' +
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

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
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
