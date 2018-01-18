package com.kunulo.share_ifi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.ClipboardUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.utils.DialogHelper;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 定制化显示扫描界面
 */
public class CaptureActivity extends BaseActivity {

    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    public static boolean isOpen = false;
    private EditText etInput;
    private void initView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linear2);
        etInput = (EditText) findViewById(R.id.et_input);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }

            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etInput.getText().toString();
                mShareIfiSdk.rentDevice(id);
                resultMessage = id;
            }
        });
    }

    private String resultMessage;

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {

//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
//            CaptureActivity.this.finish();
            mShareIfiSdk.rentDevice(result);
            resultMessage = result;
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }
    };

    @Override
    public void didRentDevice(boolean result, String code, String message) {
        super.didRentDevice(result, code,  message);
        if(result){
            mShareIfiSdk.getDeviceWifi(resultMessage);
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, resultMessage);
//            resultIntent.putExtras(bundle);
//            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
//            CaptureActivity.this.finish();
        } else {
            ToastUtils.showShortToast(message);
        }
    }

    @Override
    public void didGetDeviceWifi(boolean result, String code, String ssid, String password) {
        super.didGetDeviceWifi(result,code, ssid, password);
        if(result) {
            DialogHelper helper = new DialogHelper(this, DialogHelper.DialogType.YesOrNo);
            helper.setTitle(getString(R.string.wifi_info, ssid, password));
            helper.setOkTxt(R.string.toPaste);
            if (helper.showYesOrNoDialog()) {
                ClipboardUtils.copyText(password);
            }
            CaptureActivity.this.finish();
        } else {
            ToastUtils.showShortToast(ssid);
        }
    }
}
