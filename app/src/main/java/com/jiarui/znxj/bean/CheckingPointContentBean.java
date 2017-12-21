package com.jiarui.znxj.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 设置巡检点内容（巡检点预置）
 * itms这个水库所有的观测物
 * selected 这个水库选择了的观测物
 */

public class CheckingPointContentBean extends BaseBean{

    /**
     * result : {"id":"24","reseid":"1","title":"测试点2","idcard":"1235428866","address":"","xpoint":"115.92666","ypoint":"28.657463333333332","status":"0","signup":"","items":[{"id":"31","pid":"0","category":"1","name":"管理与保障设施","description":"","status":"1","level":"1"}],"selected":["22","27"]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 24
         * reseid : 1
         * title : 测试点2
         * idcard : 1235428866
         * address :
         * xpoint : 115.92666
         * ypoint : 28.657463333333332
         * status : 0
         * signup :
         * items : [{"id":"31","pid":"0","category":"1","name":"管理与保障设施","description":"","status":"1","level":"1"}]
         * selected : ["22","27"]
         */

        private String id;
        private String reseid;
        private String title;
        private String idcard;
        private String address;
        private String xpoint;
        private String ypoint;
        @SerializedName("status")
        private String statusX;
        private String signup;
        private List<ItemsBean> items;
        private List<String> selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReseid() {
            return reseid;
        }

        public void setReseid(String reseid) {
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public String getSignup() {
            return signup;
        }

        public void setSignup(String signup) {
            this.signup = signup;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public List<String> getSelected() {
            return selected;
        }

        public void setSelected(List<String> selected) {
            this.selected = selected;
        }

        public static class ItemsBean {
            /**
             * id : 31
             * pid : 0
             * category : 1
             * name : 管理与保障设施
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
            private boolean statusX;
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

            public boolean getStatusX() {
                return statusX;
            }

            public void setStatusX(boolean statusX) {
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
}
