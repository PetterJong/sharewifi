package com.kunulo.lib_share_ifi.jsonBen;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyOrderJsonBean extends Bean {
    private MyOrderBean value;

    public MyOrderBean getValue() {
        return value;
    }

    public void setValue(MyOrderBean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyOrderJsonBean{" +
                "value=" + value +
                '}';
    }

    public class MyOrderBean{
        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private List<OrderBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<OrderBean> getList() {
            return list;
        }

        public void setList(List<OrderBean> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "MyOrderBean{" +
                    "total=" + total +
                    ", list=" + list +
                    '}';
        }

        public class OrderBean implements Comparable<OrderBean>{
            private long id;
            private int status;
            private long end_time;
            private int accounts_price;
            private long start_time;
            private String wifi_name;
            private String device_id;
            private String user_id;


            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getEnd_time() {
                return end_time;
            }

            public void setEnd_time(long end_time) {
                this.end_time = end_time;
            }

            public int getAccounts_price() {
                return accounts_price;
            }

            public void setAccounts_price(int accounts_price) {
                this.accounts_price = accounts_price;
            }

            public long getStart_time() {
                return start_time;
            }

            public void setStart_time(long start_time) {
                this.start_time = start_time;
            }

            public String getWifi_name() {
                return wifi_name;
            }

            public void setWifi_name(String wifi_name) {
                this.wifi_name = wifi_name;
            }

            public String getDevice_id() {
                return device_id;
            }

            public void setDevice_id(String device_id) {
                this.device_id = device_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                OrderBean orderBean = (OrderBean) o;

                return id == orderBean.id;

            }

            @Override
            public int hashCode() {
                return (int) (id ^ (id >>> 32));
            }

            @Override
            public String toString() {
                return "OrderBean{" +
                        "id=" + id +
                        ", status=" + status +
                        ", end_time=" + end_time +
                        ", accounts_price=" + accounts_price +
                        ", start_time=" + start_time +
                        ", wifi_name='" + wifi_name + '\'' +
                        ", device_id='" + device_id + '\'' +
                        ", user_id='" + user_id + '\'' +
                        '}';
            }

            @Override
            public int compareTo(OrderBean o) {
                if(o == null){
                    return -1;
                }
                return (this.start_time > o.start_time) ? -1 : 1;
            }
        }

    }
}
