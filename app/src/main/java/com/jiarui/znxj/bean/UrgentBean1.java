package com.jiarui.znxj.bean;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class UrgentBean1 {
    String time;//时间
    String name;//名字
    String position;//部位
    String situation;//情况
    String waterlevel;//水位
    String weather;//天气
    String xwaterlevel;//下游水位
    String remarks;//备注
    String image;//图片
    String soundrecording;//录音
    public UrgentBean1(String time, String name, String position, String situation, String waterlevel, String weather, String xwaterlevel, String remarks, String soundrecording) {
        this.time = time;
        this.name = name;
        this.position = position;
        this.situation = situation;
        this.waterlevel = waterlevel;
        this.weather = weather;
        this.xwaterlevel = xwaterlevel;
        this.remarks = remarks;
        this.soundrecording = soundrecording;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getWaterlevel() {
        return waterlevel;
    }

    public void setWaterlevel(String waterlevel) {
        this.waterlevel = waterlevel;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getXwaterlevel() {
        return xwaterlevel;
    }

    public void setXwaterlevel(String xwaterlevel) {
        this.xwaterlevel = xwaterlevel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSoundrecording() {
        return soundrecording;
    }

    public void setSoundrecording(String soundrecording) {
        this.soundrecording = soundrecording;
    }
}
