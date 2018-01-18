package com.kunulo.share_ifi.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RentingFragment extends BaseOrderFragment {
    private TextView tvContent;
    private PullToRefreshListView mPullToRefreshListView;

    private OrderAdapter mAdapter;
    private List<OrderBean> mList = new ArrayList<>();

    private TimeOutTask timeOutTask;


    public RentingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_renting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
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
        mShareIfiSdk.getMyOrder(rentOrdertype, rentNextPage, mShowCount);
    }


    @Override
    public void onReciveOrderEvent(int orderType, int nextPage, List<OrderBean> orderBeanList) {
        if(orderBeanList != null) {
            mPullToRefreshListView.onRefreshComplete();
            if(timeOutTask != null)
                timeOutTask.cancel(true);
            rentNextPage = nextPage;
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

    }

    public void getOrderBeans(){
        mShareIfiSdk.getMyOrder(rentOrdertype, rentNextPage, mShowCount);
    }



}
