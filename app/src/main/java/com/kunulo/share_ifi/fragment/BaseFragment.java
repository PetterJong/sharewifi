package com.kunulo.share_ifi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.kunulo.lib_share_ifi.sdk.ShareIfiSdk;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BaseFragment extends Fragment {
    protected ShareIfiSdk mShareIfiSdk;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareIfiSdk = ShareIfiSdk.getInstance();
    }
}
