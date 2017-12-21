package com.jiarui.znxj.bean;

import java.util.List;

/**
 * 获取巡检点列表 2017/12/11 0011.
 */

public class CheckingPointList extends BaseBean{

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 27
         * reseid : 2
         * title : 点位2
         * idcard : 2233
         * xpoint : 115.89248721
         * ypoint : 28.66427145
         */

        private int id;
        private int reseid;
        private String title;
        private String idcard;
        private String xpoint;
        private String ypoint;

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

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getXpoint() {
            return xpoint;
        }

        public void setXpoint(String xpoint) {
            this.xpoint = xpoint;
        }

        public String getYpoint() {
            return ypoint;
        }

        public void setYpoint(String ypoint) {
            this.ypoint = ypoint;
        }
    }
}
