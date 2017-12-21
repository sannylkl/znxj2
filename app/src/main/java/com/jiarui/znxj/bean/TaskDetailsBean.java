package com.jiarui.znxj.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目检查 2017/11/3 0003.
 */

public class TaskDetailsBean  extends BaseBean{

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1
         * reseid : 1
         * title : 点位1
         * idcard : 23243ka
         * address : 阿道夫
         * xpoint : 
         * ypoint : 
         * status : 1
         * signup : 
         * items : [{"id":"1","pid":"0","name":"坝体","status":"1","child":[{"id":"10","pid":"1","name":"坝基","status":"1"},{"id":"12","pid":"1","name":"坝段2","status":"1"}]}]
         */

        private int id;
        private int reseid;
        private String title;
        private String idcard;
        private String address;
        private String xpoint;
        private String ypoint;
        private int status;
        private String signup;
        private List<ItemsBean> items;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public static class ItemsBean {
            /**
             * id : 1
             * pid : 0
             * name : 坝体
             * status : 1
             * child : [{"id":"10","pid":"1","name":"坝基","status":"1"},{"id":"12","pid":"1","name":"坝段2","status":"1"}]
             */

            private int id;
            private int pid;
            private String name;
            private int status;
            private List<ChildBean> child;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public List<ChildBean> getChild() {
                return child;
            }

            public void setChild(List<ChildBean> child) {
                this.child = child;
            }

            public static class ChildBean {
                /**
                 * id : 10
                 * pid : 1
                 * name : 坝基
                 * status : 1
                 */

                private int id;
                private int pid;
                private String name;
                private int status;

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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }
            }
        }
    }
}
