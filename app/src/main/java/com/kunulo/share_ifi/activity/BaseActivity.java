package com.kunulo.share_ifi.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.blankj.utilcode.utils.LogUtils;
import com.kunulo.lib_share_ifi.jsonBen.DeviceBean;
import com.kunulo.lib_share_ifi.jsonBen.DevicePoint;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean.OrderBean;
import com.kunulo.lib_share_ifi.sdk.ShareIfiSdk;
import com.kunulo.share_ifi.utils.PageManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class BaseActivity extends AppCompatActivity implements ShareIfiSdk.ShareIfiSdkCallback {
    private final static String TAG = "TAG_RECIVE_DATA";
    protected static Handler mHandler = new Handler();
    protected ShareIfiSdk mShareIfiSdk;
    protected PageManager pageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageManager = PageManager.getAppManager();
        pageManager.addActivity(this);
        PushAgent.getInstance(getApplicationContext()).onAppStart(); // umeng 信息
        mShareIfiSdk = ShareIfiSdk.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShareIfiSdk.setShareIfiSdkCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageManager.finishActivity(this);//移除activity栈
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        String s = String.format("onReceiveLocation, Latitude = %f, Longitude = %f", location.getLatitude(), location.getLongitude());
        LogUtils.d(TAG, s);
    }

    @Override
    public void didGetSMSCode(boolean result, String code, String message) {
        String s = String.format("didGetSMSCode result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didLogin(boolean result, String code, String phone) {
        String s = String.format("didLogin result = %b, message = %s", result, phone);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didIspayDeposit(boolean result, String code, String message) {
        String s = String.format("didIspayDeposit result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didIsCanBorrow(boolean result, String code, String message) {
        String s = String.format("didIsCanBorrow result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didGetNearbyDevice(boolean result, String code, String message, List<DevicePoint> devicePoints) {
        String s = String.format("didGetNearbyDevice result = %b, message = %s, devicePoints = %s ", result, message, devicePoints);
        LogUtils.d(TAG, s);

    }

    @Override
    public void didGetDeviceWifi(boolean result, String code, String ssid, String password) {
        String s = String.format("didGetDeviceWifi result = %b, ssid = %s, password = %s", result, ssid, password);
        LogUtils.d(TAG, s);

    }

    @Override
    public void didRentDevice(boolean result, String code, String message) {
        String s = String.format("didRentDevice result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didReturnDevice(boolean result, String code, String message) {
        String s = String.format("didReturnDevice result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didGetMyDevices(boolean result, String code, String message, List<DeviceBean> deviceBeans) {
        String s = String.format("didGetMyDevices result = %b, message = %s, deviceBeans = %s ", result, message, deviceBeans);
        LogUtils.d(TAG, s);

    }

    @Override
    public void didGetPayNum(boolean result, String code, String message, String payNum) {
        String s = String.format("didGetPayNum result = %b, message = %s, payNum = %s ", result, message, payNum);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didGetMyOrder(boolean result, String code, String message, int orderType, int nextPage, List<OrderBean> deviceBeens) {
        String s = String.format("didGetPayNum result = %b,code = %s message = %s," +
                " orderType = %d, nextPage = %d, deviceBeens = %s ", result, code, message, orderType, nextPage, deviceBeens);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didLoginOut(boolean result, String message) {
        String s = String.format("didLoginOut result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didPay(boolean result, String code, String message) {
        String s = String.format("didReturnDevice result = %b, message = %s", result, message);
        LogUtils.d(TAG, s);
    }

    @Override
    public void didResetPassWord(boolean result, String code, String deviceId) {
        String s = String.format("didReturnDevice result = %b, message = %s", result, deviceId);
        LogUtils.d(TAG, s);
    }
}
