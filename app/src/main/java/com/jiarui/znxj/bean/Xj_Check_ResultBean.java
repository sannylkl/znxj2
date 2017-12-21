package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 问题描述
 */
@Table(name = "xj_check_result")
public class Xj_Check_ResultBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "related_id")
    private int related_id;//关联id
    @Column(name = "type")
    private String type;//检查项名称
    @Column(name = "normal")
    private int normal;//是否正常 0否，1是
    @Column(name = "work")
    private int work;//设备运转，0否,1是
    @Column(name = "repeat")
    private int repeat;//是否重复 0否，1是
    @Column(name = "question")
    private String question;//异常问题
    @Column(name = "remark")
    private String remark;//问题描述

    @Override
    public String toString() {
        return "Xj_Check_ResultBean{" +
                "id='" + id + '\'' +
                "related_id='" + related_id + '\'' +
                "type='" + type + '\'' +
                "normal='" + normal + '\'' +
                "work='" + work + '\'' +
                "repeat='" + repeat + '\'' +
                "question='" + question + '\'' +
                "remark='" + remark + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRelated_id() {
        return related_id;
    }

    public void setRelated_id(int related_id) {
        this.related_id = related_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
