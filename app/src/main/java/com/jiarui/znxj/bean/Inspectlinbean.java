package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class Inspectlinbean {

    boolean lin;
    String name;
    int index;

    public Inspectlinbean(String name) {
        this.name = name;
    }

    public boolean isLin() {
        return lin;
    }

    public void setLin(boolean lin) {
        this.lin = lin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
