package com.jiarui.znxj.bean;

import java.util.List;

/**
 * 任务列表数据
 */

public class TaslListBean extends BaseBean {

    /**
     * data : {"result":[{"id":"2","start_time":"1511539200","end_time":"1508837100","reservoir":"测试水库","creator":"艾正强","route_id":"1","items":[{"item_id":"1","item_name":"坝体"}],"routes":[{"id":"10","route_id":"1","location_id":"2","location_name":"点位2","finished_time":"10分钟","next_time":"0","if_end":"0","sortby":"2"},{"id":"9","route_id":"1","location_id":"1","location_name":"点位1","finished_time":"10分钟","next_time":"10分钟","if_end":"1","sortby":"1"}]}],"totalCount":"1","pageCount":1}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * result : [{"id":"2","start_time":"1511539200","end_time":"1508837100","reservoir":"测试水库","creator":"艾正强","route_id":"1","items":[{"item_id":"1","item_name":"坝体"}],"routes":[{"id":"10","route_id":"1","location_id":"2","location_name":"点位2","finished_time":"10分钟","next_time":"0","if_end":"0","sortby":"2"},{"id":"9","route_id":"1","location_id":"1","location_name":"点位1","finished_time":"10分钟","next_time":"10分钟","if_end":"1","sortby":"1"}]}]
         * totalCount : 1
         * pageCount : 1
         */

//        private String totalCount;
//        private int pageCount;
        private List<ResultBean> result;

//        public String getTotalCount() {
//            return totalCount;
//        }
//
//        public void setTotalCount(String totalCount) {
//            this.totalCount = totalCount;
//        }
//
//        public int getPageCount() {
//            return pageCount;
//        }
//
//        public void setPageCount(int pageCount) {
//            this.pageCount = pageCount;
//        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * id : 2
             * start_time : 1511539200
             * end_time : 1508837100
             * reservoir : 测试水库
             * creator : 艾正强
             * route_id : 1
             * items : [{"item_id":"1","item_name":"坝体"}]
             * routes : [{"id":"10","route_id":"1","location_id":"2","location_name":"点位2","finished_time":"10分钟","next_time":"0","if_end":"0","sortby":"2"},{"id":"9","route_id":"1","location_id":"1","location_name":"点位1","finished_time":"10分钟","next_time":"10分钟","if_end":"1","sortby":"1"}]
             */

            private int id;
            private int start_time;
            private int end_time;
            private String reservoir;
            private String creator;
            private String route_id;
            private List<ItemsBean> items;
            private List<RoutesBean> routes;
            private String status;
            private String task_type;//任务类型
            private int reseid;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public String getReservoir() {
                return reservoir;
            }

            public void setReservoir(String reservoir) {
                this.reservoir = reservoir;
            }

            public String getCreator() {
                return creator;
            }

            public void setCreator(String creator) {
                this.creator = creator;
            }

            public String getRoute_id() {
                return route_id;
            }

            public void setRoute_id(String route_id) {
                this.route_id = route_id;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public List<RoutesBean> getRoutes() {
                return routes;
            }

            public void setRoutes(List<RoutesBean> routes) {
                this.routes = routes;
            }

            public static class ItemsBean {
                /**
                 * item_id : 1
                 * item_name : 坝体
                 */

                private String item_id;
                private String item_name;

                public String getItem_id() {
                    return item_id;
                }

                public void setItem_id(String item_id) {
                    this.item_id = item_id;
                }

                public String getItem_name() {
                    return item_name;
                }

                public void setItem_name(String item_name) {
                    this.item_name = item_name;
                }
            }

            public static class RoutesBean {
                /**
                 * id : 10
                 * route_id : 1
                 * location_id : 2
                 * location_name : 点位2
                 * finished_time : 10分钟
                 * next_time : 0
                 * if_end : 0
                 * sortby : 2
                 */

                private int id;
                private int route_id;
                private int location_id;
                private String location_name;
                private String finished_time;
                private String next_time;
                private int if_end;
                private int sortby;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getRoute_id() {
                    return route_id;
                }

                public void setRoute_id(int route_id) {
                    this.route_id = route_id;
                }

                public int getLocation_id() {
                    return location_id;
                }

                public void setLocation_id(int location_id) {
                    this.location_id = location_id;
                }

                public String getLocation_name() {
                    return location_name;
                }

                public void setLocation_name(String location_name) {
                    this.location_name = location_name;
                }

                public String getFinished_time() {
                    return finished_time;
                }

                public void setFinished_time(String finished_time) {
                    this.finished_time = finished_time;
                }

                public String getNext_time() {
                    return next_time;
                }

                public void setNext_time(String next_time) {
                    this.next_time = next_time;
                }

                public int getIf_end() {
                    return if_end;
                }

                public void setIf_end(int if_end) {
                    this.if_end = if_end;
                }

                public int getSortby() {
                    return sortby;
                }

                public void setSortby(int sortby) {
                    this.sortby = sortby;
                }
            }


            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
            public String getTask_type() {
                return task_type;
            }

            public void setTask_type(String task_type) {
                this.task_type = task_type;
            }

            public int getReseid() {
                return reseid;
            }

            public void setReseid(int reseid) {
                this.reseid = reseid;
            }
        }
    }
}
