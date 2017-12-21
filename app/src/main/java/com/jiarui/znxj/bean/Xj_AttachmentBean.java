package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 检查项名称
 */
@Table(name = "xj_attachment")
public class Xj_AttachmentBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "url")
    private String url;//文件地址
    @Column(name = "type")
    private String type;//文件类型(video,image,audio)
    @Column(name = "remark")
    private String remark;//备注
    @Column(name = "related_id")
    private int related_id;//关联id
    @Column(name = "item")
    private String item;//检查项名称
    @Override
    public String toString() {
        return "Xj_AttachmentBean{" +
                "id='" + id + '\'' +
                "url='" + url + '\'' +
                "type='" + type + '\'' +
                "remark='" + remark + '\'' +
                "related_id='" + related_id + '\'' +
                "item='" + item + '\'' +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRelated_id() {
        return related_id;
    }

    public void setRelated_id(int related_id) {
        this.related_id = related_id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
