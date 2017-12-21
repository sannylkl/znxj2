package com.jiarui.znxj.bean;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item {
    private String gNameitme;
    private String rNameitme;

    public Item(String gNameitme, String rNameitme) {
        this.gNameitme = gNameitme;
        this.rNameitme = rNameitme;
    }

    public String getgNameitme() {
        return gNameitme;
    }

    public void setgNameitme(String gNameitme) {
        this.gNameitme = gNameitme;
    }

    public String getrNameitme() {
        return rNameitme;
    }

    public void setrNameitme(String rNameitme) {
        this.rNameitme = rNameitme;
    }
}
