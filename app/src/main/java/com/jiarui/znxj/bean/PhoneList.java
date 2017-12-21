package com.jiarui.znxj.bean;

import java.util.List;

/**
 *  通讯录 2017/11/24 0024.
 */

public class PhoneList extends  BaseBean {

    /**
     * result : {"data":[{"id":"3","name":"张三","phone":"13803504343","avatar":""}],"total":"1","pageCount":1}
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
         * data : [{"id":"3","name":"张三","phone":"13803504343","avatar":""}]
         * total : 1
         * pageCount : 1
         */

        private String total;
        private int pageCount;
        private List<DataBean> data;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 3
             * name : 张三
             * phone : 13803504343
             * avatar :
             */

            private String id;
            private String name;
            private String phone;
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
