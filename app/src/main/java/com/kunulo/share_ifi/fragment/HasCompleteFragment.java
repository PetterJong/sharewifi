package com.kunulo.share_ifi.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean.OrderBean;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.adapter.OrderAdapter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : xxx@xx
 *     time   : 2017/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HasCompleteFragment extends BaseOrderFragment {
    private TextView tvContent;
    private PullToRefreshListView mPullToRefreshListView;
    private OrderAdapter mAdapter;
    private List<OrderBean> mList = new ArrayList<>();
    private TimeOutTask timeOutTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.v("onPullDownToRefresh"); // 下拉刷新
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtils.v("onPullUpToRefresh"); // 上拉加载
                timeOutTask = new TimeOutTask(mPullToRefreshListView);
                timeOutTask.execute();
                getOrderBeans();
            }
        });

        tvContent = (TextView) view.findViewById(R.id.tv);

        mAdapter = new OrderAdapter(getActivity(), mList);
        mPullToRefreshListView.setAdapter(mAdapter);
        getOrderBeans();
    }

    @Override
    public void onReciveOrderEvent(int orderType, int nextPage, List<OrderBean> orderBeanList) {
        if(orderBeanList != null){
            mPullToRefreshListView.onRefreshComplete();
            if(timeOutTask != null){
                timeOutTask.cancel(true);
            }
            hasComplemtNextPage = nextPage;
            for (OrderBean bean : orderBeanList){
                if(!mList.contains(bean)){
                    mList.addAll(orderBeanList);
                }
            }
            Collections.sort(mList);
            mAdapter.notifyDataSetChanged();
        }
        if(mList.size() <= 0){
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPayOrderEvent(long orderId) {
        mList.clear();
        mShareIfiSdk.getMyOrder(hasComplemtOrdertype, hasComplemtNextPage, mShowCount);
    }

    public void getOrderBeans(){
        mShareIfiSdk.getMyOrder(hasComplemtOrdertype, hasComplemtNextPage, mShowCount);
    }



}
