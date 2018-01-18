package com.kunulo.share_ifi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.Collections;
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
public class WaitPayFragment extends BaseOrderFragment {

    private TextView tvContent;
    private PullToRefreshListView mPullToRefreshListView;
    private OrderAdapter mAdapter;
    private List<MyOrderJsonBean.MyOrderBean.OrderBean> mList = new ArrayList<>();
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

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                timeOutTask = new TimeOutTask(mPullToRefreshListView);
                timeOutTask.execute();
                getOrderBeans();
            }
        });


        tvContent = (TextView) view.findViewById(R.id.tv);

        mAdapter = new OrderAdapter(getActivity(), 2, mList);
        mPullToRefreshListView.setAdapter(mAdapter);
        mShareIfiSdk.getMyOrder(waitPayOrdertype, waitPayNextPage, mShowCount);
    }


    @Override
    public void onReciveOrderEvent(int orderType, int nextPage, List<MyOrderJsonBean.MyOrderBean.OrderBean> orderBeanList) {
        if (orderBeanList != null) {
            mPullToRefreshListView.onRefreshComplete();
            if(timeOutTask != null)
                timeOutTask.cancel(true);
            waitPayNextPage = nextPage;
            for (MyOrderJsonBean.MyOrderBean.OrderBean bean : orderBeanList){
                if(!mList.contains(bean)){
                    mList.addAll(orderBeanList);
                }
            }
            Collections.sort(mList);
            mAdapter.notifyDataSetChanged();
        }
        if (mList.size() <= 0) {
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPayOrderEvent(long orderId) {
        for (MyOrderJsonBean.MyOrderBean.OrderBean bean : mList){
            if(orderId == bean.getId()){
                mList.remove(bean);
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void getOrderBeans(){
        mShareIfiSdk.getMyOrder(waitPayOrdertype, waitPayNextPage, mShowCount);
    }

}
