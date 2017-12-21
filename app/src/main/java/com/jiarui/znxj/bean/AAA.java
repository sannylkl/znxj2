package com.jiarui.znxj.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class AAA {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private boolean check;
        private String title;
        private String yc;
        private List<ContentBean> content;

        public ListBean() {
        }

        public ListBean(String title) {
            this.title = title;
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        /**
         * title : 点位1
         * content : [{"small":"坝体","data1":"数据1","data2":"数据2","data3":"数据3","image":[{"url":"http://pic6.ni"}]}]
         */
        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getYc() {
            return yc;
        }

        public void setYc(String yc) {
            this.yc = yc;
        }

        public static class ContentBean {
            private boolean check;
            private String yichang;
            private String small;
            private String data1;
            private String data2;
            private String data3;
            private List<ImageBean> image;

            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getData1() {
                return data1;
            }

            public void setData1(String data1) {
                this.data1 = data1;
            }

            public String getData2() {
                return data2;
            }

            public void setData2(String data2) {
                this.data2 = data2;
            }

            public String getData3() {
                return data3;
            }

            public void setData3(String data3) {
                this.data3 = data3;
            }

            public List<ImageBean> getImage() {
                return image;
            }

            public void setImage(List<ImageBean> image) {
                this.image = image;
            }

            /**
             * small : 坝体
             * data1 : 数据1
             * data2 : 数据2
             * data3 : 数据3
             * image : [{"url":"http://pic6.ni"}]
             */
            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }

            public String getYichang() {
                return yichang;
            }

            public void setYichang(String yichang) {
                this.yichang = yichang;
            }

            public static class ImageBean {
                /**
                 * url : http://pic6.ni
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
