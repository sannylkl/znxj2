package com.jiarui.znxj.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 巡检点预置的观测物 2017/12/1 0001.
 */

public class ObservingThingsBean extends  BaseBean{

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 22
         * pid : 0
         * category : 1
         * name : 坝体
         * description :
         * status : 1
         * level : 1
         */

        private int id;
        private int pid;
        private int category;
        private String name;
        private String description;
        @SerializedName("status")
        private String statusX;
        private int level;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
