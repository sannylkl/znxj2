package com.jiarui.znxj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class TaskCenterBean {
    int id;
    private int id2;
    String name1;
    String name2;
    String name3;
    String name4;
    String name5;
    String name6;
    private int staus;
    List<TaskCenter_listBean> listBeen;

    public String getName1() {
        return name1;
    }

    public String getName6() {
        return name6;
    }

    public void setName6(String name6) {
        this.name6 = name6;
    }


    public TaskCenterBean(int id, String name1, String name2, String name3, String name4, String name5,int staus,int id2, List<TaskCenter_listBean> listBeen) {
        this.id = id;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.name5 = name5;
        this.name6 = name6;
        this.id2=id2;
        this.staus=staus;
        this.listBeen = listBeen;
    }
    public TaskCenterBean(String name1, String name2, String name3, String name4, String name5, String name6,List<TaskCenter_listBean> listBeen) {
        this.id = id;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.name5 = name5;
        this.name6 = name6;
        this.listBeen = listBeen;
    }
    public List<TaskCenter_listBean> getListBeen() {
        return listBeen;
    }

    public void setListBeen(List<TaskCenter_listBean> listBeen) {
        this.listBeen = listBeen;
    }

    public TaskCenterBean(String name1, String name2, String name3, String name4, String name5, List<TaskCenter_listBean> listBeen) {
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.name5 = name5;
        this.listBeen = listBeen;
    }


    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public String getName5() {
        return name5;
    }

    public void setName5(String name5) {
        this.name5 = name5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStaus() {
        return staus;
    }

    public void setStaus(int staus) {
        this.staus = staus;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }
}
