package com.jiarui.znxj.bean;

import java.util.List;

/**
 * 任务预览数据 2017/11/11 0011.
 */

public class TaskPreviewData {

    /**
     * taskid : 1
     * did : 1
     * name : 1
     * satus : 1
     * items : [{"taskid":"1","did":"1","pid":"0","name":"坝体","status":"1","child":[{"taskid":"23","did":"1","pid":"0","cid":"0","name":"坝顶","status":"1"}]}]
     * drowpan
     */

    private int taskid;
    private int did;
    private String name;
    private int satus;
    private List<ItemsBean> items;
    private boolean check;
    private int drowpan;
    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSatus() {
        return satus;
    }

    public void setSatus(int satus) {
        this.satus = satus;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getDrowpan() {
        return drowpan;
    }

    public void setDrowpan(int drowpan) {
        this.drowpan = drowpan;
    }

    public static class ItemsBean {
        /**
         * taskid : 1
         * did : 1
         * pid : 0
         * name : 坝体
         * status : 1
         * child : [{"taskid":"23","did":"1","pid":"0","cid":"0","name":"坝顶","status":"1"}]
         */

        private int taskid;
        private int did;
        private int pid;
        private String name;
        private int status;
        private List<ChildBean> child;
        private boolean check;
        private int browpan;
        public int getTaskid() {
            return taskid;
        }

        public void setTaskid(int taskid) {
            this.taskid = taskid;
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
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
             * taskid : 23
             * did : 1
             * pid : 0
             * cid : 0
             * name : 坝顶
             * status : 1
             */

            private int taskid;
            private int did;
            private int pid;
            private int cid;
            private String name;
            private int status;
            private String content;
            private String results;
            public int getTaskid() {
                return taskid;
            }

            public void setTaskid(int taskid) {
                this.taskid = taskid;
            }

            public int getDid() {
                return did;
            }

            public void setDid(int did) {
                this.did = did;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
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
            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getResults() {
                return results;
            }

            public void setResults(String results) {
                this.results = results;
            }

            public ChildBean(int taskid, int did, int pid, int cid, String name, int status) {
                this.taskid = taskid;
                this.did = did;
                this.pid = pid;
                this.cid = cid;
                this.name = name;
                this.status = status;
            }

            public ChildBean(int taskid, int did, int pid, int cid, String name, int status, String content, String results) {
                this.taskid = taskid;
                this.did = did;
                this.pid = pid;
                this.cid = cid;
                this.name = name;
                this.status = status;
                this.content = content;
                this.results = results;
            }
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public int getBrowpan() {
            return browpan;
        }

        public void setBrowpan(int browpan) {
            this.browpan = browpan;
        }

        public ItemsBean(int taskid, int did, int pid, String name, int status, List<ChildBean> child, boolean check) {
            this.taskid = taskid;
            this.did = did;
            this.pid = pid;
            this.name = name;
            this.status = status;
            this.child = child;
            this.check = check;
        }

        public ItemsBean(int taskid, int did, int pid, String name, int status, List<ChildBean> child, boolean check, int browpan) {
            this.taskid = taskid;
            this.did = did;
            this.pid = pid;
            this.name = name;
            this.status = status;
            this.child = child;
            this.check = check;
            this.browpan = browpan;
        }
    }

    public TaskPreviewData(int taskid, int did, String name, int satus, List<ItemsBean> items, boolean check) {
        this.taskid = taskid;
        this.did = did;
        this.name = name;
        this.satus = satus;
        this.items = items;
        this.check = check;
    }

    public TaskPreviewData(int taskid, int did, String name, int satus, List<ItemsBean> items, boolean check, int drowpan) {
        this.taskid = taskid;
        this.did = did;
        this.name = name;
        this.satus = satus;
        this.items = items;
        this.check = check;
        this.drowpan = drowpan;
    }

}
