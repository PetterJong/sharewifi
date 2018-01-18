package com.kunulo.lib_share_ifi.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.Utils;
import com.kunulo.lib_share_ifi.R;
import com.kunulo.lib_share_ifi.jsonBen.Bean;
import com.kunulo.lib_share_ifi.jsonBen.CanBorrowJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.GetWifiJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.LoginJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.MyDevicesJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.NearJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.PayDepositJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.PayJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.PayNumJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.RentDeviceJsonBean;
import com.kunulo.lib_share_ifi.jsonBen.ReturnDeviceJsonBean;
import com.kunulo.lib_share_ifi.net.Error;
import com.kunulo.lib_share_ifi.net.OkHttpDownload;
import com.kunulo.lib_share_ifi.sdk.Constant;
import com.kunulo.lib_share_ifi.util.SdkUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ShareIfiSdkService extends Service {
    static String TAG = "ShareIfiSdkService";
    private IBinder mIbinder = new LocalBinder();
    private Handler mHandler ;

    @Override
    public void onCreate() {
        LogUtils.d(TAG, "onCreate");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d(TAG, "onBind");
        return mIbinder;
    }


    /* 当从新尝试绑定时执行 */
    @Override
    public void onRebind(Intent intent) {
        LogUtils.d(TAG, "onRebind");
    }

    public void setHandle(Handler handler){
        mHandler = handler;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "onRebind");
    }

    public class LocalBinder extends Binder {
        public ShareIfiSdkService getService() {
            return ShareIfiSdkService.this;
        }
    }

    public void getSMSCode(String phone){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("phone", phone);
        params.put("type", "1");
        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_GET_SMS_CODE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_GET_SMS_CODE, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body() == null){
                    sendBaseMessage(Constant.WHAT_LOGIN, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                    return ;
                }
                byte[] source = response.body().bytes();
                String data = new String(source,"utf-8");
                Bean bean = JSON.parseObject(data, Bean.class);
                if(bean != null){
                    String code = bean.getCode();
                    if( Error.REQUEST_SUCCES_CODE.equals(code)){
                        sendBaseMessage(Constant.WHAT_GET_SMS_CODE, code, true, bean.getMessage());
                    } else {
                        sendBaseMessage(Constant.WHAT_GET_SMS_CODE, code, false, bean.getMessage());
                    }
                } else {
                  sendBaseMessage(Constant.WHAT_GET_SMS_CODE, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                }
            }
        });
    }

    public void login(final String phone, String smsCode){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("phone", phone);
        params.put("smsCode", smsCode);
        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_LOGIN, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_LOGIN, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body() == null){
                    sendBaseMessage(Constant.WHAT_LOGIN, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                    return ;
                }
                byte[] source = response.body().bytes();
                String data = new String(source,"utf-8");
                Bean bean = JSON.parseObject(data, Bean.class);
                if(bean != null){
                    String code = bean.getCode();
                    if(Error.REQUEST_SUCCES_CODE.equals(code)){
                        LoginJsonBean loginJsonBean =JSON.parseObject(data, LoginJsonBean.class);
                        if(loginJsonBean != null){
                            Constant.userToken = loginJsonBean.getUserToken();
                            Constant.userId = loginJsonBean.getUserId();
                            SdkUtils.saveUserToken(Utils.getContext(), Constant.userToken );
                            SdkUtils.saveUserNumber(Utils.getContext(), phone );
                            sendBaseMessage(Constant.WHAT_LOGIN, code, true, phone);
                        } else {
                            sendBaseMessage(Constant.WHAT_LOGIN, code, false, bean.getMessage());
                        }
                    } else if(Error.VERFI_CODE_ERROR.equals(code)){
                        sendBaseMessage(Constant.WHAT_LOGIN, code, false, bean.getMessage());
                    } else{
                        sendBaseMessage(Constant.WHAT_LOGIN, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                    }
                } else {
                    sendBaseMessage(Constant.WHAT_LOGIN, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                }
            }
        });
    }

    public void isPayDeposit(){
        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_IS_PAY_DEPOSIT, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_IS_PAY_DEPOSIT, Error.REQUEST_EXCEPTION_CODE,  false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body() == null){
                    sendBaseMessage(Constant.WHAT_IS_PAY_DEPOSIT, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                    return ;
                }
                byte[] source = response.body().bytes();
                String data = new String(source,"utf-8");
                Bean bean = JSON.parseObject(data, Bean.class);
                if(bean != null){
                    String code = bean.getCode();
                    if( Error.REQUEST_SUCCES_CODE.equals(code)){
                        PayDepositJsonBean payDepositJsonBean = JSON.parseObject(data, PayDepositJsonBean.class);
                        sendBaseMessage(Constant.WHAT_IS_PAY_DEPOSIT, code, true, payDepositJsonBean.getMessage());
                    } else {
                        sendBaseMessage(Constant.WHAT_IS_PAY_DEPOSIT, code, false, bean.getMessage());
                    }
                } else {
                    sendBaseMessage(Constant.WHAT_IS_PAY_DEPOSIT, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                }
            }
        });
    }

    public void isCanBorrow(){
        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_IS_CAN_BORROW, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_IS_CAN_BORROW, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body() == null){
                    sendBaseMessage(Constant.WHAT_IS_CAN_BORROW, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                    return ;
                }
                byte[] source = response.body().bytes();
                String data = new String(source,"utf-8");
                Bean bean = JSON.parseObject(data, Bean.class);
                if(bean != null){
                    String code = bean.getCode();
                    if( Error.REQUEST_SUCCES_CODE.equals(code)){
                        CanBorrowJsonBean canBorrowJsonBean = JSON.parseObject(data, CanBorrowJsonBean.class);
                        sendBaseMessage(Constant.WHAT_IS_CAN_BORROW, code, true, bean.getMessage());
                    } else {
                        sendBaseMessage(Constant.WHAT_IS_CAN_BORROW, code, false, bean.getMessage());
                    }
                } else {
                    sendBaseMessage(Constant.WHAT_IS_CAN_BORROW, Error.REQUEST_EXCEPTION_CODE, false, getString(R.string.error_request));
                }
            }
        });
    }

    public void  getNearbyDevice(double longitude, double latitude){
        JSONObject o = new JSONObject();
        o.put("longitude", longitude+"");
        o.put("latitude", latitude+"");

        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("coords", o.toString());
        params.put("range", Constant.nearDistance);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_NEARBY_DEVICE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_NEARBY_DEVICE, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage(), null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
//                       sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  true,  data, bean);
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            NearJsonBean nearJsonBean = JSON.parseObject(data, NearJsonBean.class);
                            if(nearJsonBean != null){
                                sendBaseMessage(Constant.WHAT_NEARBY_DEVICE, code, true,  data, nearJsonBean);
                            } else {
                                sendBaseMessage(Constant.WHAT_NEARBY_DEVICE, code, false,  getString(R.string.error_request), null);
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_NEARBY_DEVICE, code, false,  bean.getMessage(), null);
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_NEARBY_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request), null);
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request), null);
                }
            }
        });

    }

    public void getDeviceWifi(String imei){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("deviceId", imei);
        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_GET_PASSWORD , params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_GET_PASSWORD, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            GetWifiJsonBean getWifiJsonBean = JSON.parseObject(data, GetWifiJsonBean.class);
                            if(getWifiJsonBean != null){
                                sendBaseMessage(Constant.WHAT_GET_PASSWORD, code, true, getWifiJsonBean.getWifiName(),  getWifiJsonBean.getWifiPassword());
                            } else {
                                sendBaseMessage(Constant.WHAT_GET_PASSWORD, code, false,  getString(R.string.error_request), null);
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_GET_PASSWORD, code, false,  bean.getMessage(), null);
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_GET_PASSWORD, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request), null);
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_GET_PASSWORD, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request), null);
                }
            }
        });

    }

    public void rentDevice(String deviceId){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("deviceId", deviceId);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST,Constant.URL_RENT_DEVICE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_RENT_DEVICE, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    LogUtils.d(TAG, data);
//                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  false, e.getMessage());
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
//                       sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  true,  data, bean);
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            RentDeviceJsonBean rentDeviceJsonBean = JSON.parseObject(data, RentDeviceJsonBean.class);
                            if(rentDeviceJsonBean != null){
                                sendBaseMessage(Constant.WHAT_RENT_DEVICE, code, true,  rentDeviceJsonBean.getDeviceId());
                            } else {
                                sendBaseMessage(Constant.WHAT_RENT_DEVICE, code, false,  getString(R.string.error_request));
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_RENT_DEVICE, code, false,  bean.getMessage());
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_RENT_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_RENT_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                }
            }
        });
    }

    public void returnDevice(String deviceId){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("deviceId", deviceId);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_RETURN_DEVICE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_RETURN_DEVICE, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    LogUtils.d(TAG, data);
//                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  false, e.getMessage());
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
//                       sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  true,  data, bean);
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            ReturnDeviceJsonBean returnDeviceJsonBean = JSON.parseObject(data, ReturnDeviceJsonBean.class);
                            if(returnDeviceJsonBean != null){
                                sendBaseMessage(Constant.WHAT_RETURN_DEVICE, code, true,  returnDeviceJsonBean.getDeviceId());
                            } else {
                                sendBaseMessage(Constant.WHAT_RETURN_DEVICE, code, false,  getString(R.string.error_request));
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_RETURN_DEVICE, code, false,  bean.getMessage());
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_RETURN_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_RETURN_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                }
            }
        });
    }

    public void getMyDevices(){
        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_GET_MY_DEVICE, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_GET_MY_DEVICE, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    byte[] source = response.body().bytes();
                    String data = new String(source, "utf-8");
                    LogUtils.d(TAG, data);
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            MyDevicesJsonBean myDevicesJsonBean = JSON.parseObject(data, MyDevicesJsonBean.class);
                            if(myDevicesJsonBean != null){
                                sendBaseMessage(Constant.WHAT_GET_MY_DEVICE, code, true, data, myDevicesJsonBean);
                            } else {
                                sendBaseMessage(Constant.WHAT_GET_MY_DEVICE, code, false,  getString(R.string.error_request));
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_GET_MY_DEVICE, code, false,  bean.getMessage());
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_GET_MY_DEVICE, Error.REQUEST_EXCEPTION_CODE,  false,  getString(R.string.error_request));
                    }
                } else {
                sendBaseMessage(Constant.WHAT_GET_MY_DEVICE, Error.REQUEST_EXCEPTION_CODE, false,getString(R.string.error_request), null);
                }
            }
        });
    }

    public void getPayNum(String deviceId){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("deviceId", deviceId);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_GET_PAY_NUM, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_RETURN_DEVICE, Error.REQUEST_EXCEPTION_CODE, false, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    LogUtils.d(TAG, data);
//                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  false, e.getMessage());
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
//                       sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  true,  data, bean);
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            PayNumJsonBean payNumJsonBean = JSON.parseObject(data, PayNumJsonBean.class);
                            if(payNumJsonBean != null){
                                sendBaseMessage(Constant.WHAT_GET_PAY_NUM, code, true,  payNumJsonBean.getDeviceId(), payNumJsonBean.getMoney());
                            } else {
                                sendBaseMessage(Constant.WHAT_GET_PAY_NUM, code, false,  getString(R.string.error_request), "");
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_GET_PAY_NUM, code, false,  bean.getMessage(), "");
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_GET_PAY_NUM,Error.REQUEST_EXCEPTION_CODE,  false,  getString(R.string.error_request), "");
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_GET_PAY_NUM, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request), "");
                }
            }
        });
    }

    public void getGetMyOrder(final int orderType, int currentpage, int showNum){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("status", orderType);
        params.put("currentPage", currentpage);
        params.put("pageNum", showNum);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_GET_MY_ORDER, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendOrderMessage(Constant.WHAT_GET_MY_ORDER, Error.REQUEST_EXCEPTION_CODE, false,  e.getMessage(), -1,-1, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    LogUtils.d(TAG, data);
//                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  false, e.getMessage());
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
//                       sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  true,  data, bean);
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
//                            JSON.parseObject(data, MyOrderJsonBean.class);
                            MyOrderJsonBean myOrderJsonBean = JSON.parseObject(data, MyOrderJsonBean.class);
                            if(myOrderJsonBean != null && myOrderJsonBean.getValue() != null){
                                MyOrderJsonBean.MyOrderBean myOrderBean = myOrderJsonBean.getValue();
                                int nextPage = myOrderBean.getPageNum();
                                nextPage = nextPage < myOrderBean.getPages()? (++nextPage) : nextPage;
                                sendOrderMessage(Constant.WHAT_GET_MY_ORDER, code, true,  bean.getMessage(), orderType, nextPage, myOrderBean);
                            } else {
                                sendOrderMessage(Constant.WHAT_GET_MY_ORDER, code, false,  getString(R.string.error_request), orderType, -1, null);
                            }
                        } else {
                            sendOrderMessage(Constant.WHAT_GET_MY_ORDER, code, false,  bean.getMessage(),   orderType, -1, null);
                        }
                    } else {
                        sendOrderMessage(Constant.WHAT_GET_MY_ORDER, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request),  orderType, -1, null);
                    }
                }  else {
                    sendOrderMessage(Constant.WHAT_GET_MY_ORDER, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request),   orderType, -1, null);
                }
            }
        });
    }

    public void pay(final String orderId) {

        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("orderId", orderId);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_PAY,  params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_PAY, Error.REQUEST_EXCEPTION_CODE, false,  e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    LogUtils.d(TAG, data);
//                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  false, e.getMessage());
                    Bean bean = JSON.parseObject(data, Bean.class);
                    if(bean != null){
//                       sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  true,  data, bean);
                        String code = bean.getCode();
                        if(Error.REQUEST_SUCCES_CODE.equals(code)){
                            PayJsonBean payJsonBean = JSON.parseObject(data, PayJsonBean.class);
                            if(payJsonBean != null){
                                sendBaseMessage(Constant.WHAT_PAY, code, true,  orderId);
                            } else {
                                sendBaseMessage(Constant.WHAT_PAY, code, false,  getString(R.string.error_request));
                            }
                        } else {
                            sendBaseMessage(Constant.WHAT_PAY, code, false,  bean.getMessage());
                        }
                    } else {
                        sendBaseMessage(Constant.WHAT_PAY, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_PAY, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                }
            }
        });
    }

    public void resetPassword(final String deviceid){
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("deviceid", deviceid);

        OkHttpDownload.httpRequestJson(OkHttpDownload.Method.POST, Constant.URL_RESET_PASSWORD,  params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendBaseMessage(Constant.WHAT_RESET_PASSWORD, Error.REQUEST_EXCEPTION_CODE, false,  e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    byte[] source = response.body().bytes();
                    String data = new String(source,"utf-8");
                    LogUtils.d(TAG, data);
//                    sendBaseMessage(Constant.WHAT_NEARBY_DEVICE,  false, e.getMessage());
                    Bean bean = JSON.parseObject(data, Bean.class);
                    String code = bean.getCode();
                    if( Error.REQUEST_SUCCES_CODE.equals(code)){
                        sendBaseMessage(Constant.WHAT_RESET_PASSWORD, code, true, deviceid);
                    } else {
                        sendBaseMessage(Constant.WHAT_RESET_PASSWORD, code, false, bean.getMessage());
                    }
                }  else {
                    sendBaseMessage(Constant.WHAT_RESET_PASSWORD, Error.REQUEST_EXCEPTION_CODE, false,  getString(R.string.error_request));
                }
            }
        });
    }



    public void sendBaseMessage(int what, String code, boolean result, String message){
        Message msg = mHandler.obtainMessage();
        Bundle data = new Bundle();
        data.putBoolean("result", result);
        data.putString("code", code);
        data.putString("message", message);
        msg.setData(data);
        msg.what = what;
        mHandler.sendMessage(msg);
    }

    public void sendBaseMessage(int what, String code, boolean result, String message, Object o){
        Message msg = mHandler.obtainMessage();
        Bundle data = new Bundle();
        data.putBoolean("result", result);
        data.putString("code", code);
        data.putString("message", message);
        msg.setData(data);
        msg.what = what;
        msg.obj = o;
        mHandler.sendMessage(msg);
    }
    public void sendOrderMessage(int what, String code, boolean result, String message, int orderType, int nextPage, Object o){
        Message msg = mHandler.obtainMessage();
        Bundle data = new Bundle();
        data.putBoolean("result", result);
        data.putString("code", code);
        data.putString("message", message);
        data.putInt("orderType", orderType);
        data.putInt("nextPage", nextPage);
        msg.setData(data);
        msg.what = what;
        msg.obj = o;
        mHandler.sendMessage(msg);
    }



}
