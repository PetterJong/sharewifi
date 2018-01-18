package com.kunulo.datacontrol;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private TextView tvPlace;
    private ListView lvPlace;
    private ListView lvCenter;

    private List<String> mPlaces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPlace = (TextView) findViewById(R.id.tv_place);
        lvPlace = (ListView) findViewById(R.id.lv_place);
        lvCenter = (ListView) findViewById(R.id.lv_center);
        mPlaces.add("位置1 ");
        mPlaces.add("位置2");
        mPlaces.add("位置3");
        mPlaces.add("位置4");
        mPlaces.add("位置5");
        mPlaces.add("位置6");
        mPlaces.add("位置7");
        lvPlace.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mPlaces));


        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();
        String tel = tm.getLine1Number();//手机号码
        String imei = tm.getSimSerialNumber();
        String imsi = tm.getSubscriberId();

        tvPlace.setText(" deviceid : " + deviceid + " tel : " + tel + " imei : " + imei + " imsi : " + imsi);



    }
}
