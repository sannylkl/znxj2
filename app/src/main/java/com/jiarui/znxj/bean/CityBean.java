package com.jiarui.znxj.bean;

import java.util.List;

/**
 * 城市登录区域 2017/11/15 0015.
 */

public class CityBean extends BaseBean{

    /**
     * result : {"task":[{"city_id":"1240","city_name":"南昌市","areas":[{"area_id":"1242","area_name":"西湖区","reservoirs":[{"id":"1","name":"测试水库"}]}]}],"user":{"uid":"12","username":"15070069050","realname":"一号巡检员","group_id":"1","avatar":"","mobile":"15070069050"}}
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
         * task : [{"city_id":"1240","city_name":"南昌市","areas":[{"area_id":"1242","area_name":"西湖区","reservoirs":[{"id":"1","name":"测试水库"}]}]}]
         * user : {"uid":"12","username":"15070069050","realname":"一号巡检员","group_id":"1","avatar":"","mobile":"15070069050"}
         */

        private UserBean user;
        private List<TaskBean> task;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<TaskBean> getTask() {
            return task;
        }

        public void setTask(List<TaskBean> task) {
            this.task = task;
        }

        public static class UserBean {
            /**
             * uid : 12
             * username : 15070069050
             * realname : 一号巡检员
             * group_id : 1
             * avatar :
             * mobile : 15070069050
             */

            private String uid;
            private String username;
            private String realname;
            private String group_id;
            private String avatar;
            private String mobile;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

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
        }

        public static class TaskBean {
            /**
             * city_id : 1240
             * city_name : 南昌市
             * areas : [{"area_id":"1242","area_name":"西湖区","reservoirs":[{"id":"1","name":"测试水库"}]}]
             */

            private String city_id;
            private String city_name;
            private List<AreasBean> areas;

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public List<AreasBean> getAreas() {
                return areas;
            }

            public void setAreas(List<AreasBean> areas) {
                this.areas = areas;
            }

            public static class AreasBean {
                /**
                 * area_id : 1242
                 * area_name : 西湖区
                 * reservoirs : [{"id":"1","name":"测试水库"}]
                 */

                private String area_id;
                private String area_name;
                private List<ReservoirsBean> reservoirs;

                public String getArea_id() {
                    return area_id;
                }

                public void setArea_id(String area_id) {
                    this.area_id = area_id;
                }

                public String getArea_name() {
                    return area_name;
                }

                public void setArea_name(String area_name) {
                    this.area_name = area_name;
                }

                public List<ReservoirsBean> getReservoirs() {
                    return reservoirs;
                }

                public void setReservoirs(List<ReservoirsBean> reservoirs) {
                    this.reservoirs = reservoirs;
                }

                public static class ReservoirsBean {
                    /**
                     * id : 1
                     * name : 测试水库
                     */

                    private String id;
                    private String name;

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
                }
            }
        }
    }
}
