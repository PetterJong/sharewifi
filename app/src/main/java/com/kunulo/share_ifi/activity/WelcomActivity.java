package com.kunulo.share_ifi.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.blankj.utilcode.utils.StringUtils;
import com.kunulo.share_ifi.R;
import com.umeng.analytics.MobclickAgent;

public class WelcomActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        initData();
    }

    protected void initData() {
        String userToken = mShareIfiSdk.getUserToken();
        final String userNumber = mShareIfiSdk.getUserNumber();
        if (StringUtils.isSpace(userToken) || StringUtils.isSpace(userNumber)) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1000);

        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MobclickAgent.onProfileSignIn(userNumber);
                    startActivity(new Intent(WelcomActivity.this, MainActivity.class));
                    finish();
                }
            }, 1000);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
