package com.kunulo.share_ifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kunulo.lib_share_ifi.sdk.ShareIfiSdk;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public abstract class AppBaseAdapter<T> extends BaseAdapter {
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected ShareIfiSdk mShareIfiSdk;



    public AppBaseAdapter(Context context, List<T> list){
        this.mList = list;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mShareIfiSdk = ShareIfiSdk.getInstance();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
