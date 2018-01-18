package com.kunulo.lib_share_ifi.jsonBen;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyDevicesJsonBean {

    private List<DeviceBean> value;

    public List<DeviceBean> getValue() {
        return value;
    }

    public void setValue(List<DeviceBean> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyDevicesJsonBean{" +
                "value=" + value +
                '}';
    }
}
