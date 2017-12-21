package com.jiarui.znxj.bean;


/**
 * 个人工作任务数据 2017/12/11 0011.
 */

public class MineWorkTaskBean {
    //    任务对应名称
    private String name;
//    任务数量
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public MineWorkTaskBean(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
