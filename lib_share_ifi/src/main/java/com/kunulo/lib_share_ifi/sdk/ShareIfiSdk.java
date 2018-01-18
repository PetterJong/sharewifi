package com.kunulo.lib_share_ifi.sdk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.kunulo.lib_share_ifi.jsonBen.DeviceBean;
import com.kunulo.lib_share_ifi.jsonBen.DevicePoint;
import com.kunulo.lib_share_ifi.jsonBen.MyDevicesJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean.OrderBean;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean;
import com.kunulo.lib_share_ifi.jsonBen.NearJsonBean;
import com.kunulo.lib_share_ifi.service.ShareIfiSdkService;
import com.kunulo.lib_share_ifi.util.SdkUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ShareIfiSdk {
    private static Context mContext;
    private static ShareIfiSdk shareIfiSdk;
    private static ShareIfiSdkService mShareIfiService;
    private static ShareIfiSdkCallback mShareIfiSdkCallback;

    private static Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean result = false;
            String message = null;
            String code = "";
            if (data != null) {
                result = data.getBoolean("result");
                message = data.getString("message");
                code = data.getString("code");
            }
            switch (msg.what) {
                case Constant.WHAT_GET_SMS_CODE:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didGetSMSCode(result, code, message);
                    }
                    break;
                case Constant.WHAT_LOGIN:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didLogin(result, code, message);
                    }
                    break;
                case Constant.WHAT_NEARBY_DEVICE:
                    if (mShareIfiSdkCallback != null) {
                        if(data != null){
                            Object o = msg.obj;
                            if(o != null && o instanceof NearJsonBean){
                                NearJsonBean bean = (NearJsonBean) o;
                                mShareIfiSdkCallback.didGetNearbyDevice(result, code, message, bean.getDevicePoints());
                            } else {
                                mShareIfiSdkCallback.didGetNearbyDevice(result, code, message, null);
                            }
                        } else {
                            mShareIfiSdkCallback.didGetNearbyDevice(result, code, message, null);
                        }
                    }
                    break;
                case Constant.WHAT_IS_PAY_DEPOSIT:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didIspayDeposit(result, code, message);
                    }
                    break;
                case Constant.WHAT_IS_CAN_BORROW:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didIsCanBorrow(result, code, message);
                    }
                    break;
                case Constant.WHAT_RENT_DEVICE:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didRentDevice(result, code, message);
                    }
                    break;
                case Constant.WHAT_GET_MY_DEVICE:
                    if (mShareIfiSdkCallback != null) {
                        if(data != null){
                            Object o = msg.obj;
                            if(o != null && o instanceof MyDevicesJsonBean){
                                MyDevicesJsonBean bean = (MyDevicesJsonBean) o;
                                mShareIfiSdkCallback.didGetMyDevices(result, code, message, bean.getValue());
                            } else {
                                mShareIfiSdkCallback.didGetMyDevices(result, code, message, null);
                            }
                        } else {
                            mShareIfiSdkCallback.didGetMyDevices(result, code, message, null);
                        }
                    }
                    break;
                case Constant.WHAT_GET_PASSWORD:
                    if (mShareIfiSdkCallback != null) {
                        if(data != null){
                            Object o = msg.obj;
                            if(o != null && o instanceof String){
                                String bean = (String) o;
                                mShareIfiSdkCallback.didGetDeviceWifi(result, code, message, bean);
                            } else {
                                mShareIfiSdkCallback.didGetDeviceWifi(result, code, message, null);
                            }
                        } else {
                            mShareIfiSdkCallback.didGetDeviceWifi(result, code, message, null);
                        }
                    }
                    break;
                case Constant.WHAT_RETURN_DEVICE:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didReturnDevice(result, code, message);
                    }
                    break;
                case Constant.WHAT_GET_PAY_NUM:
                    if (mShareIfiSdkCallback != null) {
                        if(data != null){
                            Object o = msg.obj;
                            if(o != null && o instanceof String){
                                String bean = (String) o;
                                mShareIfiSdkCallback.didGetPayNum(result, code, message, bean);
                            } else {
                                mShareIfiSdkCallback.didGetPayNum(result, code, message, null);
                            }
                        } else {
                            mShareIfiSdkCallback.didGetPayNum(result, code, message, null);
                        }
                    }
                    break;
                case Constant.WHAT_GET_MY_ORDER:
                if (mShareIfiSdkCallback != null) {

                    Object o = msg.obj;
                    if(data != null){
                        if(o != null && o instanceof MyOrderBean){
                            MyOrderBean bean = (MyOrderBean) o;
                            int orderType = data.getInt("orderType");
                            int nextPage = data.getInt("nextPage");
                            mShareIfiSdkCallback.didGetMyOrder(result, code, message, orderType, nextPage, bean.getList());
                        } else {
                            mShareIfiSdkCallback.didGetMyDevices(result, code, message, null);
                        }
                    } else {
                        mShareIfiSdkCallback.didGetMyDevices(result, code, message, null);
                    }
                }
                break;
                case Constant.WHAT_PAY:
                    if (mShareIfiSdkCallback != null) {
                        mShareIfiSdkCallback.didPay(result, code, message);
                    }
                    break;
                default:
                    break;

            }
        }
    };
    private static boolean hasInit;

    private static LocationClient mLocationClient = null;


    public static  BDAbstractLocationListener myListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
//            initSdk();
            if(mShareIfiSdkCallback != null){
                mShareIfiSdkCallback.onReceiveLocation(location);
            }
            // 当不需要定位图层时关闭定位图层
//            mBaiduMap.setMyLocationEnabled(false);
        }

        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {
                //建议打开GPS
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {
                //建议打开wifi，不必连接，这样有助于提高网络定位精度！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {
                //定位权限受限，建议提示用户授予APP定位权限！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {
                //网络异常造成定位失败，建议用户确认网络状态是否异常！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {
                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {
                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {
                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {
                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {
                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！
            }
        }
    };

    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
        }
    };


    private static ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service instanceof ShareIfiSdkService.LocalBinder){
                mShareIfiService = ((ShareIfiSdkService.LocalBinder) service).getService();
                mShareIfiService.setHandle(mHandle);
                LogUtils.d("service onServiceConnected");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mShareIfiService = null;
            LogUtils.d("service onServiceDisconnected");
        }
    };

    public static void init(Context context){
        mContext = context;
        mLocationClient = new LocationClient(mContext);
        // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        initSdk();
    }

    /**
     * 当道定位后在做 否则会导致定位失败
     */
    private static void initSdk(){
        if(hasInit){
            return;
        }
        String page = mContext.getPackageName();
        Intent intent = new Intent(mContext, ShareIfiSdkService.class);
        mContext.bindService(intent, connection, Service.BIND_AUTO_CREATE);
        IntentFilter fileter = new IntentFilter();
        fileter.addAction(page);
        mContext.registerReceiver(mReceiver, fileter);
        hasInit = true;
    }

    private static void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }




    private ShareIfiSdk(){}

    public static ShareIfiSdk getInstance() {
        if(shareIfiSdk == null){
            synchronized (ShareIfiSdk.class){
                if(shareIfiSdk == null){
                    shareIfiSdk = new ShareIfiSdk();
                }
            }
        }
        return shareIfiSdk;
    }


    public static void stopLocationDingWei(){
        if(mLocationClient == null)
            return;
        if(mLocationClient.isStarted()){
            mLocationClient.stop();
        }
    }

    public static void startLocationDingWei(){
        if(mLocationClient == null)
            return;
        if(!mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }

    private static void checkService(){
        if(mShareIfiService == null){
            throw new IllegalStateException("service is null, please check your service!");
        }
    }


    public  void setShareIfiSdkCallback(ShareIfiSdkCallback shareIfiSdkCallback){
        mShareIfiSdkCallback = shareIfiSdkCallback;
    }


    public void stop(){
        if(mShareIfiService != null){
            mContext.unbindService(connection);
        }
        if(mLocationClient != null){
            stopLocationDingWei();
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient = null;
            myListener = null;
        }
        setShareIfiSdkCallback(null);
        mContext.unregisterReceiver(mReceiver);
        hasInit = false;
    }

    /**
     * 获取验证码
     * @param phone
     *
     * 关注回调接口 {@link ShareIfiSdkCallback#didGetSMSCode(boolean, String, String)}
     * "type":"1" // 1 - login ;2-注册；3-修改密码 使用1，不做区分
     */
    public void getSMSCode(String phone){
        checkService();
        mShareIfiService.getSMSCode(phone);
    }

    /**
     * 登入接口
     * @param phone
     * @param smsCode
     * 关注回调接口 {@link ShareIfiSdkCallback#didLogin(boolean, String, String)}
     */
    public void login(String phone, String smsCode){
        checkService();
        mShareIfiService.login(phone, smsCode);
    }


    /**
     * 是否交付押金
     * 关注回调接口 {@link ShareIfiSdkCallback#didIspayDeposit(boolean, String, String)}
     */
    public void isPayDeposit(){
        checkService();
        mShareIfiService.isPayDeposit();
    }

    /**
     * 是否可借
     * 关注回调接口 {@link ShareIfiSdkCallback#didIsCanBorrow(boolean, String, String)}
     */
    public void isCanBorrow(){
        checkService();
        mShareIfiService.isCanBorrow();
    }

    /**
     *
     * @param longitude 经度
     * @param latitude 纬度
     * 关注回调接口 {@link ShareIfiSdkCallback#didGetNearbyDevice(boolean, String,String, List<DevicePoint> )}
     *
     */
    public void getNearbyDevice(double longitude, double latitude){
        checkService();
        mShareIfiService.getNearbyDevice(longitude, latitude);
    }

    /**
     * 获取设备的imei号
     * @param imei
     * 关注回调接口 {@link ShareIfiSdkCallback#didGetDeviceWifi(boolean,String, String, String)}
     */
    public void getDeviceWifi(String imei){
        checkService();
        mShareIfiService.getDeviceWifi(imei);
    }


    /**
     * 租用设备
     * @param deviceId
     */
    public void rentDevice(String deviceId){
        checkService();
        mShareIfiService.rentDevice(deviceId);
    }

    /**
     * 还设备
     * @param deviceId
     */
    public void returnDevice(String deviceId){
        checkService();
        mShareIfiService.returnDevice(deviceId);
    }

    /**
     * 获取我当前租用的设备
     * 关注回调接口 {@link ShareIfiSdkCallback#didGetMyDevices(boolean,String, String, List<DeviceBean> )}
     */
    public void getMyDevices(){
        checkService();
        mShareIfiService.getMyDevices();
    }

    /**
     * 支付接口（获取结算金额）
     * 关注回调接口 {@link ShareIfiSdkCallback#didGetPayNum(boolean,String, String, String )}
     */
    public void getPayNum(String deviceId){
        checkService();
        mShareIfiService.getPayNum(deviceId);
    }

    /**
     * 获取订单接口
     * 关注回调接口 {@link ShareIfiSdkCallback#didGetMyOrder(boolean, String, String, int, int, List)}
     * @param orderType
     * <p>orderTupe : 0 代表所有订单;1 租借中 ；2 归还完成，3 结算完成</p>
     * @param currentpage 获取下一页数，从1开始
     * @param showNum 查询行数，推荐10
     */
    public void getMyOrder(int orderType, int currentpage, int showNum){
        checkService();
        mShareIfiService.getGetMyOrder(orderType, currentpage, showNum);
    }

    /**
     *  支付订单接口
     * 关注回调接口 {@link ShareIfiSdkCallback#didPay(boolean, String, String)}
     * @param orderId 订单号
     */
    public void pay(String orderId){
        checkService();
        mShareIfiService.pay(orderId);
    }

    /**
     * 重置wifi密码
     * 关注回调接口 {@link ShareIfiSdkCallback#didResetPassWord(boolean, String, String)}
     * @param deviceid
     */
    public void resetPassword(String deviceid){
        checkService();
        mShareIfiService.resetPassword(deviceid);
    }

    public void loginOut(){
        SdkUtils.clearData(mContext);
        if(mShareIfiSdkCallback != null) {
            mShareIfiSdkCallback.didLoginOut(true, "success");
        }
    }

    public static String getDeviceToken(){
        return Constant.deviceToken;
    }

    public static String getUserToken(){
        if(StringUtils.isEmpty(Constant.userToken) ){
            Constant.userToken = SdkUtils.getUserToken(mContext);
        }
        return Constant.userToken;
    }
    public static String getUserNumber(){
        if(StringUtils.isEmpty(Constant.userNumber)){
            Constant.userNumber = SdkUtils.getUserNumber(mContext);
        }
        return Constant.userNumber;
    }


    public interface ShareIfiSdkCallback{
        void onReceiveLocation(BDLocation location);
        void didGetSMSCode(boolean result, String code, String data);
        void didLogin(boolean result, String code, String phone);
        void didIspayDeposit(boolean reuslt, String code, String msg);
        void didIsCanBorrow(boolean reuslt, String code, String msg);
        void didGetNearbyDevice(boolean result, String code, String message, List<DevicePoint> devicePoints);
        void didGetDeviceWifi(boolean result, String code, String ssid, String password);
        void didRentDevice(boolean result, String code, String message);
        void didReturnDevice(boolean result, String code, String message);
        void didGetMyDevices(boolean result, String code, String message ,List<DeviceBean> deviceBeens);
        void didGetPayNum(boolean result, String code, String message, String payNum);
        void didGetMyOrder(boolean result, String code, String message, int orderType, int nextPage, List<OrderBean> deviceBeens);
        void didLoginOut(boolean result, String message);
        void didPay(boolean result, String code, String message);
        void didResetPassWord(boolean result, String code, String deviceId);
    }
}
