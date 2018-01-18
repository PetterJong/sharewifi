package com.kunulo.share_ifi.app;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.Utils;
import com.kunulo.lib_share_ifi.IFIApp;
import com.kunulo.lib_share_ifi.sdk.Constant;
import com.kunulo.lib_share_ifi.sdk.ShareIfiSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class App extends IFIApp {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareIfiSdk.init(this);
        Utils.init(getApplicationContext());
        ZXingLibrary.initDisplayOpinion(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.v("devicetoken ==>" + deviceToken);
                Constant.deviceToken = deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.v("register umeng error ==>" + s + " " + s1);
            }
        });
    }

}
