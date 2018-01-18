package com.kunulo.share_ifi.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.blankj.utilcode.utils.LogUtils;
import com.kunulo.lib_share_ifi.sdk.Constant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/10/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private final String TAG = this.getClass().getSimpleName();
    public static final String APP_ID = Constant.WX_APP_ID;
    public static final String APP_SECRET = "请自己填写";
    private IWXAPI mApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mApi.handleIntent(this.getIntent(), this);
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq baseReq) {
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(resp instanceof SendAuth.Resp ){
                    SendAuth.Resp sr = (SendAuth.Resp) resp;
                    LogUtils.v("weixing code ==> " + sr.code);
                }

                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }
        finish();

    }
}
