package com.kunulo.share_ifi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.model.LatLng;
import com.kunulo.lib_share_ifi.jsonBen.DevicePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Utils {

    public static List<LatLng> change2LatLans(List<DevicePoint> points){
        if(points == null)
            return null;
        List<LatLng> latLngs = new ArrayList<>();
        for (DevicePoint point : points){
            LatLng latLng = new LatLng(Double.valueOf(point.getLatitude()), Double.valueOf(point.getLongitude()));
            latLngs.add(latLng);
        }
        return latLngs;
    }


}
