package com.jiarui.znxj.bean;

/**
 *版本更新接口 2017/11/24 0024.
 */

public class UpataVersionBean extends BaseBean{

    /**
     * result : {"id":"3","appname":"智能巡检app","name":"v1.0.0","version_code":"2","download_url":"xxxx","ctime":"1511504072"}
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
         * id : 3
         * appname : 智能巡检app
         * name : v1.0.0
         * version_code : 2
         * download_url : xxxx
         * ctime : 1511504072
         */

        private String id;
        private String appname;
        private String name;
        private String version_code;
        private String download_url;
        private String ctime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }
    }
}
