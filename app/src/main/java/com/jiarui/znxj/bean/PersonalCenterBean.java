package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/9/9 0009.
 * 个人中心表
 */
@Table(name = "personal_center")
public class PersonalCenterBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;
    @Column(name = "icon")//头像
    private String icon;
    @Column(name = "username")
    private String username;
    @Column(name = "work")
    private String work;//工作效率
    @Column(name = "number")
    private String number;
    @Column(name = "number2")
    private String number2;
    @Column(name = "number3")
    private String number3;
    @Column(name = "name")
    private String name;
    @Column(name = "version")
    private String version;//版本更新

    @Override
    public String toString() {
        return "PatrolSettingBean{" +
                "id='" + getId() + '\'' +
                "icon='" + getIcon() + '\'' +
                "work='" + getWork() + '\'' +
                "number='" + getNumber() + '\'' +
                "name='" + getName() + '\'' +
                "version='" + getVersion() + '\'' +
                '}';
    }

    public PersonalCenterBean(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public PersonalCenterBean() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public String getNumber3() {
        return number3;
    }

    public void setNumber3(String number3) {
        this.number3 = number3;
    }
}
