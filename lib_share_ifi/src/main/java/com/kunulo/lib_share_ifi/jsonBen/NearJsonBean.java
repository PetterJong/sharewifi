package com.kunulo.lib_share_ifi.jsonBen;


import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NearJsonBean extends Bean {

    private List<DevicePoint> value;

    public List<DevicePoint> getValue() {
        return value;
    }

    public void setValue(List<DevicePoint> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NearJsonBean{" +
                "value=" + value +
                '}';
    }

    public List<DevicePoint> getDevicePoints(){
        return getValue();
    }


}
