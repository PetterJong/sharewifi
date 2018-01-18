package com.kunulo.share_ifi.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.kunulo.share_ifi.R;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DialogHelper extends Dialog implements View.OnClickListener{
    private boolean result;
    private TextView tvTitle;
    private TextView tvCancle;
    private TextView tvSure;
    private Handler mHandler;
    private int what = 0x101;

    private DialogHelper(Context context) {
        super(context);
    }

    public DialogHelper(Context context, DialogType type) {
        this(context);
        init(type);
    }

    private void init(DialogType type) {
        switch (type) {
            case YesOrNo: {
                setContentView(R.layout.dialog_confirm);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                tvCancle = (TextView) findViewById(R.id.tv_cancle);
                tvSure = (TextView) findViewById(R.id.tv_sure);
            }
                break;
            default:
                break;
        }
        if(tvCancle != null){
            tvCancle.setOnClickListener(this);
        }
        if(tvSure != null){
            tvSure.setOnClickListener(this);
        }
    }

    public boolean showYesOrNoDialog() {
        show();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == what){
                    super.handleMessage(msg);
                    throw new RuntimeException("");
                }
            }
        };
        try{
            Looper.loop();
        } catch (RuntimeException e){

        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancle:
                endDialogYesOrNo(false);
                break;
            case R.id.tv_sure:
                endDialogYesOrNo(true);
                break;
        }

    }

    private void endDialogYesOrNo(boolean result){
        dismiss();
        this.result = result;
        mHandler.sendEmptyMessage(what);
    }

    public void setTitle(String title){
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }
    public void setTitle(int titleRes){
        if(tvTitle != null){
            tvTitle.setText(titleRes);
        }
    }
    public void setOkTxt(String txt){
        if(tvSure != null){
            tvSure.setText(txt);
        }
    }
    public void setCancleTxt(int titleRes){
        if(tvCancle != null){
            tvCancle.setText(titleRes);
        }
    }
    public void setCancleTxt(String txt){
        if(tvCancle != null){
            tvCancle.setText(txt);
        }
    }
    public void setOkTxt(int titleRes){
        if(tvSure != null){
            tvSure.setText(titleRes);
        }
    }

    public boolean isShowing(){
        return isShowing();
    }

    public enum DialogType {
        Sigle, YesOrNo, Edit
    }

}
