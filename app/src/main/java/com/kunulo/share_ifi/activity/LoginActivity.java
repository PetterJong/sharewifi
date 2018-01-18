package com.kunulo.share_ifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.kunulo.lib_share_ifi.sdk.Constant;
import com.kunulo.share_ifi.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    protected EditText et_number;
    protected EditText et_code;
    protected TextView tv_get_code;
    protected Button bt_login;

    private String APP_ID = Constant.WX_APP_ID;
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        init();
        addListener();
        iwxapi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        iwxapi.registerApp(Constant.WX_APP_ID);
    }
    private void findView() {
        et_number = (EditText) findViewById(R.id.et_number);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        bt_login = (Button) findViewById(R.id.bt_login);
    }

    private void init() {
    }

    private void addListener() {
        tv_get_code.setOnClickListener(this);
        bt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = et_number.getText().toString();
        String smsCode = et_code.getText().toString();
        switch (v.getId()){
            case R.id.tv_get_code:
                if(!StringUtils.isSpace(phone) && RegexUtils.isMobileExact(phone)){
                    mShareIfiSdk.getSMSCode(phone);
                } else {
                    ToastUtils.showShortToast(R.string.enter_true_phone_number);
                }
                break;
            case R.id.bt_login:
//                DialogHelper helper = new DialogHelper(this, DialogHelper.DialogType.YesOrNo);
//                if(helper.showYesOrNoDialog()){
//                    Intent intent = new Intent(this, MainActivity.class);
//                    startActivity(intent);
//                }
                if(StringUtils.isSpace(smsCode)){
                    ToastUtils.showShortToast(R.string.enter_code);
                }
                if(!StringUtils.isSpace(phone) && RegexUtils.isMobileExact(phone)){
                    mShareIfiSdk.login(phone, smsCode);
                } else {
                    ToastUtils.showShortToast(R.string.enter_true_phone_number);
                }

//                if (iwxapi != null) {
//                    final SendAuth.Req req = new SendAuth.Req();
//                    req.scope = "snsapi_userinfo";
////                   req.scope = "snsapi_login";
//                    req.state = "123";
//                    iwxapi.sendReq(req);
//                }
                break;
        }
    }

    @Override
    public void didGetSMSCode(boolean result, String code, String message) {
        super.didGetSMSCode(result, code,  message);
        if(result){
            ToastUtils.showShortToast("验证发送成功");
        } else {
            ToastUtils.showShortToast(message);
        }
    }

    @Override
    public void didLogin(boolean result, String code,String message) {
        super.didLogin(result, code, message);
        if(result){
            MobclickAgent.onProfileSignIn(message);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            pageManager.finishActivity();
        } else {
            ToastUtils.showShortToast(message);
        }
    }
}
