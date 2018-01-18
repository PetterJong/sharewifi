package com.kunulo.share_ifi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.Utils;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.utils.DialogHelper;
import com.kunulo.share_ifi.utils.HttpUtils;
import com.kunulo.share_ifi.utils.PaymentRequest;
import com.kunulo.share_ifi.utils.TitlebarHelper;
import com.pingplusplus.android.Pingpp;

import org.json.JSONObject;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    private TitlebarHelper helper;
    private TextView tvBalance;
    private Button btRechargeBalance;
    private TextView tvMyDeeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        findView();
        init();
        initTitlebar();
        addListener();
    }

    private void findView() {
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        btRechargeBalance = (Button) findViewById(R.id.bt_recharge_balance);
        tvMyDeeposit = (TextView) findViewById(R.id.tv_my_deposit);
    }

    private void init() {

    }

    private void initTitlebar() {
        helper = new TitlebarHelper(getWindow().getDecorView());
        helper.setBackAndTitle(R.string.my_wallet);
        helper.ivLeft.setOnClickListener(this);
    }

    private void addListener() {
        btRechargeBalance.setOnClickListener(this);
        tvMyDeeposit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.iv_left:
                 finish();
                 break;
             case R.id.bt_recharge_balance:
                 new PaymentTask().execute(new PaymentRequest(PaymentRequest.CHANNEL_WECHAT, 1));
                 break;
             case R.id.tv_my_deposit:
                 break;
         }
    }


    /**
     * 获取json（data）数据串， 实际过程中，此串应该由服务器回过来
     */
    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            try {
                JSONObject object = new JSONObject();
                object.put("channel", paymentRequest.getChannel());
                object.put("amount", paymentRequest.getAmount());
                object.put("livemode", paymentRequest.isLivemode());
                String json = object.toString();
                //向Your Ping++ Server SDK请求数据
                data = HttpUtils.postJson(PaymentRequest.CHARGE_URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if(null == data){
                DialogHelper dialogHelper = new DialogHelper(Utils.getContext(), DialogHelper.DialogType.YesOrNo);
                dialogHelper.setTitle("请求出错 ：请检查URL，URL无法获取charge");
                return;
            }
            Log.d("charge", data);

            //除QQ钱包外，其他渠道调起支付方式：
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
            Pingpp.createPayment(MyWalletActivity.this, data);

            //QQ钱包调用方式
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
            //参数三：“qwalletXXXXXXX”需与AndroidManifest.xml中的scheme值一致
            //Pingpp.createPayment(ClientSDKActivity.this, data, "qwalletXXXXXXX");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                DialogHelper helper = new DialogHelper(this, DialogHelper.DialogType.YesOrNo);
                helper.setTitle(result + "\n" + errorMsg + "\n" + extraMsg);
                helper.showYesOrNoDialog();
            }
        }

    }
}
