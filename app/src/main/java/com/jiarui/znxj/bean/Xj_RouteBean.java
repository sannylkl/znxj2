package com.jiarui.znxj.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import static com.jiarui.znxj.R.id.year;

/**
 * Created by Administrator on 2017/10/21 0021.
 * 路线名称表
 */
@Table(name = "xj_route")
public class Xj_RouteBean {
    @Column(name = "id", isId = true, autoGen = true)//注释列名主键，主动增长
    private int id;//主键
    @Column(name = "reseid")
    private int reseid;//水库id
    @Column(name = "title")
    private String title;//路线名称
    @Column(name = "route")
    private String route;//路线名称
    @Column(name = "status")
    private int status;//水库id

    @Override
    public String toString() {
        return "Xj_RouteBean{" +
                "id='" + id + '\'' +
                "reseid='" + reseid + '\'' +
                "title='" + title + '\'' +
                "year='" + year + '\'' +
                "route='" + route + '\'' +
                "status='" + status + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReseid() {
        return reseid;
    }

    public void setReseid(int reseid) {
        this.reseid = reseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
