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
public class ReturnDeviceJsonBean {

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
        private String isBorrow;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getIsBorrow() {
            return isBorrow;
        }

        public void setIsBorrow(String isBorrow) {
            this.isBorrow = isBorrow;
        }

        @Override
        public String toString() {
            return "RentDeviceValue{" +
                    "deviceId='" + deviceId + '\'' +
                    ", isBorrow='" + isBorrow + '\'' +
                    '}';
        }
    }

    public String getDeviceId(){
        return value == null ? "": value.deviceId;
    }

}
