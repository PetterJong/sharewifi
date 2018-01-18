package com.kunulo.share_ifi.fragment;

import android.os.AsyncTask;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean.OrderBean;

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
public abstract class BaseOrderFragment extends BaseFragment{
    protected int mShowCount = 10;
    protected int rentNextPage = 1;
    protected int waitPayNextPage = 1;
    protected int hasComplemtNextPage = 1;
    protected final static int rentOrdertype = 1;
    protected final static int waitPayOrdertype = 2;
    protected final static int hasComplemtOrdertype = 3;
    private final static int timeOut = 6000;

    public abstract void onReciveOrderEvent(int orderType, int nextPage, List<OrderBean> orderBeanList);

    public abstract void onPayOrderEvent(long orderId);

    protected class TimeOutTask extends AsyncTask<Void, Void, String[]> {
        private PullToRefreshListView pullToRefreshListView;

        public TimeOutTask(PullToRefreshListView pullToRefreshListView){
            this.pullToRefreshListView = pullToRefreshListView;
        }


        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            if(pullToRefreshListView.isRefreshing()){
                pullToRefreshListView.onRefreshComplete();
            }
            super.onPostExecute(result);
        }
    }
}
