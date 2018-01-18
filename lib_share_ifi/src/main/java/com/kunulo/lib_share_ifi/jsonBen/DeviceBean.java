package com.kunulo.lib_share_ifi.jsonBen;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DeviceBean {
    private String wifi_name;
    private String device_id;
    private String keycode;
    private String start_time;

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

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Override
    public String toString() {
        return "DeviceBean{" +
                "wifi_name='" + wifi_name + '\'' +
                ", device_id='" + device_id + '\'' +
                ", keycode='" + keycode + '\'' +
                ", start_time='" + start_time + '\'' +
                '}';
    }
}
