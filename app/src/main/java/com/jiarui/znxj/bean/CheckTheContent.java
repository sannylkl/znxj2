package com.jiarui.znxj.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 检查内容 2017/11/8 0008.
 */

public class CheckTheContent extends BaseBean {


    /**
     * result : {"id":"1","part_id":"10","content":"异常，有遗漏；","status":"1","abnormals":[{"id":"1","content_id":"1","name":"有异常,","status":"1"}]}
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
         * id : 1
         * part_id : 10
         * content : 异常，有遗漏；
         * status : 1
         * abnormals : [{"id":"1","content_id":"1","name":"有异常,","status":"1"}]
         */

        private int id;
        private int part_id;
        private String content;
        @SerializedName("status")
        private String statusX;
        private List<AbnormalsBean> abnormals;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPart_id() {
            return part_id;
        }

        public void setPart_id(int part_id) {
            this.part_id = part_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public List<AbnormalsBean> getAbnormals() {
            return abnormals;
        }

        public void setAbnormals(List<AbnormalsBean> abnormals) {
            this.abnormals = abnormals;
        }

        public static class AbnormalsBean {
            /**
             * id : 1
             * content_id : 1
             * name : 有异常,
             * status : 1
             */

            private int id;
            private int content_id;
            private String name;
            @SerializedName("status")
            private String statusX;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getContent_id() {
                return content_id;
            }

            public void setContent_id(int content_id) {
                this.content_id = content_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStatusX() {
                return statusX;
            }

            public void setStatusX(String statusX) {
                this.statusX = statusX;
            }
        }
    }
}
