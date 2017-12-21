package com.jiarui.znxj.bean;

import java.util.List;

/**
 * 个人头像 2017/12/11 0011.
 */

public class MineHeaderPhoto extends  BaseBean{

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * name : IMG_20171211_201011.jpg
         * type : image/jpeg
         * size : 13951
         * key : 0
         * extension : jpg
         * md5 : 59a4b66fd76100633763460b3f677894
         * sha1 : 13567a30a7421e13454eecd530a23c6a93afec07
         * save_name : 5a2e75a31ede2.jpg
         * save_path : photo/2017/12/
         */

        private String name;
        private String type;
        private int size;
        private int key;
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

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
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
