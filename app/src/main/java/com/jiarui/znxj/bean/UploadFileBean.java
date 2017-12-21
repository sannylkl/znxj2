package com.jiarui.znxj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class UploadFileBean {

    /**
     * error : 0
     * message : 上传成功
     * result : [{"name":"1320171124171208.pdf","type":"application/pdf","size":50666,"key":"file","extension":"pdf","md5":"a338cba0a04a34cf5fd75c6a3485d158","sha1":"720532a5690c909b3a8b11bf1a980908102da321","save_name":"5a17e26a62888.pdf","save_path":"files2017/11/"}]
     */

    private int error;
    private String message;
    private List<ResultBean> result;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * name : 1320171124171208.pdf
         * type : application/pdf
         * size : 50666
         * key : file
         * extension : pdf
         * md5 : a338cba0a04a34cf5fd75c6a3485d158
         * sha1 : 720532a5690c909b3a8b11bf1a980908102da321
         * save_name : 5a17e26a62888.pdf
         * save_path : files2017/11/
         */

        private String name;
        private String type;
        private int size;
        private String key;
        private String extension;
        private String md5;
        private String sha1;
        private String save_name;
        private String save_path;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getSha1() {
            return sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        public String getSave_name() {
            return save_name;
        }

        public void setSave_name(String save_name) {
            this.save_name = save_name;
        }

        public String getSave_path() {
            return save_path;
        }

        public void setSave_path(String save_path) {
            this.save_path = save_path;
        }
    }
}
