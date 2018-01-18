package com.kunulo.share_ifi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.ClipboardUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.kunulo.lib_share_ifi.jsonBen.DeviceBean;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.utils.DialogHelper;
import com.kunulo.share_ifi.utils.TitlebarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyDevicesActivity extends BaseActivity implements View.OnClickListener {

    private TitlebarHelper titlebarHelper;
    private TextView tvDeviceName;
    private TextView tvDeviceWifiName;
    private TextView tvDeviceWifiPassword;
    private TextView tvRentPlace;
    private TextView tvStartTime;
    private TextView tvBattery;
    private TextView tvChargeStatus;
    private TextView tvConnectNum;
    private TextView tvFlow;

    private TextView tvPaste;
    private Button btGetWifi;
    private Button btGetPayNum;
    private Button btReturnDevice;
    private String deviceID;

    private View layContent;
    private TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
        findView();
        init();
        initTitlebar();
        addListener();
    }

    private void findView() {
        layContent = findViewById(R.id.lay_content);
        tvDeviceName = (TextView) findViewById(R.id.tv_device_name);
        tvDeviceWifiName = (TextView) findViewById(R.id.tv_device_wifi_name);
        tvDeviceWifiPassword = (TextView) findViewById(R.id.tv_device_wifi_password);
        tvRentPlace = (TextView) findViewById(R.id.tv_rent_place);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvBattery = (TextView) findViewById(R.id.tv_battery);
        tvChargeStatus = (TextView) findViewById(R.id.tv_charge_status);
        tvConnectNum = (TextView) findViewById(R.id.tv_connect_num);
        tvFlow = (TextView) findViewById(R.id.tv_flow);
        tvPaste = (TextView) findViewById(R.id.tv_paste);

        btGetWifi = (Button) findViewById(R.id.bt_get_wifi);
        btGetPayNum = (Button) findViewById(R.id.bt_get_pay_num);
        btReturnDevice = (Button) findViewById(R.id.bt_return_device);

        tv = (TextView) findViewById(R.id.tv);
    }

    private void init() {
        mShareIfiSdk.getMyDevices();
    }


    private void initTitlebar() {
        titlebarHelper = new TitlebarHelper(getWindow().getDecorView());
        titlebarHelper.setBackAndTitle(R.string.my_devices);
        titlebarHelper.ivLeft.setOnClickListener(this);
    }

    private void addListener() {
        btGetWifi.setOnClickListener(this);
        btGetPayNum.setOnClickListener(this);
        btReturnDevice.setOnClickListener(this);
        tvPaste.setOnClickListener(this);
    }

    private void refreshview(DeviceBean bean) {
        deviceID = bean.getDevice_id();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvDeviceName.setText(deviceID);
        tvDeviceWifiName.setText(bean.getWifi_name());
        tvDeviceWifiPassword.setText(bean.getKeycode());
        tvStartTime.setText(sdf.format(new Date(Long.parseLong(bean.getStart_time()))));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_wifi:
                if (deviceID != null) {
                    mShareIfiSdk.getDeviceWifi(deviceID);
                }
                break;
            case R.id.bt_return_device:
                if (deviceID != null) {
                    mShareIfiSdk.returnDevice(deviceID);
                }
                break;
            case R.id.bt_get_pay_num:
                if (deviceID != null) {
                    mShareIfiSdk.getPayNum(deviceID);
                }
                break;
            case R.id.tv_paste:
                if(!StringUtils.isEmpty(deviceID)){
                    CharSequence password = tvDeviceWifiPassword.getText();
                    ClipboardUtils.copyText(password);
                    ToastUtils.showShortToast(R.string.copy_success);
                }
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    @Override
    public void didGetDeviceWifi(boolean result, String code, String ssid, String password) {
        super.didGetDeviceWifi(result,code, ssid, password);
        if (result) {
            tvDeviceWifiName.setText(ssid);
            tvDeviceWifiPassword.setText(password);
        } else {
            ToastUtils.showShortToast(ssid);
        }
    }

    @Override
    public void didReturnDevice(boolean result, String code, String message) {
        super.didReturnDevice(result, code, message);
        if (result) {
            deviceID = "";
            tvDeviceName.setText("");
            tvDeviceWifiName.setText("");
            tvDeviceWifiPassword.setText("");
            tvRentPlace.setText("");
            tvStartTime.setText("");
            tvBattery.setText("");
            tvChargeStatus.setText("");
            tvConnectNum.setText("");
            tvFlow.setText("");
        } else {
            ToastUtils.showShortToast(message);
        }
    }


    @Override
    public void didGetPayNum(boolean result, String code, String message, String payNum) {
        super.didGetPayNum(result, code, message, payNum);
        if(result){
            DialogHelper helper = new DialogHelper(this, DialogHelper.DialogType.YesOrNo);
            helper.setTitle(getString(R.string.need_pay_num, payNum));
            helper.showYesOrNoDialog();
        } else {
            ToastUtils.showShortToast(message);
        }
    }



    @Override
    public void didGetMyDevices(boolean result, String code, String message, List<DeviceBean> deviceBeens) {
        super.didGetMyDevices(result, code, message, deviceBeens);
        if (result) {
            if (deviceBeens != null && deviceBeens.size() > 0) {
                layContent.setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
                DeviceBean bean = deviceBeens.get(0);
                refreshview(bean);
            } else {
                layContent.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }
        } else {
            ToastUtils.showShortToast(message);
        }
    }
}
