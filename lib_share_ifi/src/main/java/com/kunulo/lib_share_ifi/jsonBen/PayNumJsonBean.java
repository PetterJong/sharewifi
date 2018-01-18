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
public class PayNumJsonBean {

    private RentDeviceValue value;

    public RentDeviceValue getValue() {
        return value;
    }

    public void setValue(RentDeviceValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RentDeviceJsonBean{" +
                "value=" + value +
                '}';
    }

    private class RentDeviceValue{
        private String deviceId;
        private String money;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Override
        public String toString() {
            return "RentDeviceValue{" +
                    "deviceId='" + deviceId + '\'' +
                    ", money='" + money + '\'' +
                    '}';
        }
    }

    public String getDeviceId(){
        return value == null ? "": value.deviceId;
    }

    public String getMoney(){
        return value == null ? "": value.money;
    }


}
