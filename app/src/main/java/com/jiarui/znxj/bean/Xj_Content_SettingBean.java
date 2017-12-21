package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 检查内容表
 */
@Table(name = "xj_content_setting")
public class Xj_Content_SettingBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "part_id")
    private int part_id;//所属检查部位id
    @Column(name = "content")
    private String content;//内容描述
    @Column(name = "status")
    private int status;//所属检查部位id

    @Override
    public String toString() {
        return "Xj_Content_SettingBean{" +
                "id='" + id + '\'' +
                "part_id='" + part_id + '\'' +
                "content='" + content + '\'' +
                "status='" + status + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPart_id() {
        return part_id;
    }

    public void setPart_id(int part_id) {
        this.part_id = part_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
