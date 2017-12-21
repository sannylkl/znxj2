package com.jiarui.znxj.bean;

/**
 * 个人资料 2017/12/11 0011.
 */

public class MineDataBean extends  BaseBean{

    /**
     * result : {"username":"15070069050","realname":"一号巡检员","id":"12","group_id":"1","avatar":"","mobile":"15070069050","email":"","total":"2","done":"0","undo":"2"}
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
         * username : 15070069050
         * realname : 一号巡检员
         * id : 12
         * group_id : 1
         * avatar :
         * mobile : 15070069050
         * email :
         * total : 2
         * done : 0
         * undo : 2
         */

        private String username;
        private String realname;
        private String id;
        private String group_id;
        private String avatar;
        private String mobile;
        private String email;
        private String total;
        private String done;
        private String undo;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDone() {
            return done;
        }

        public void setDone(String done) {
            this.done = done;
        }

        public String getUndo() {
            return undo;
        }

        public void setUndo(String undo) {
            this.undo = undo;
        }
    }
}
