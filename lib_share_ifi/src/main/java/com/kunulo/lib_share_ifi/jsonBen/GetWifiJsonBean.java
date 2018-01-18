package com.kunulo.lib_share_ifi.jsonBen;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetWifiJsonBean {
    private GetWifiValue value;

    public GetWifiValue getValue() {
        return value;
    }

    public void setValue(GetWifiValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GetWifiJsonBean{" +
                "value=" + value +
                '}';
    }

    private class GetWifiValue{
        private String wifiName;
        private String key;

        public String getWifiName() {
            return wifiName;
        }

        public void setWifiName(String wifiName) {
            this.wifiName = wifiName;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "GetWifiValue{" +
                    "wifiName='" + wifiName + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public String getWifiName(){
        return value == null?"": value.wifiName;
    }

    public String getWifiPassword(){
        return value == null?"": value.key;
    }


}
