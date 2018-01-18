package com.kunulo.share_ifi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean.OrderBean;
import com.kunulo.share_ifi.R;

import java.text.SimpleDateFormat;
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
public class OrderAdapter extends AppBaseAdapter<OrderBean>{
    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss ");
    private int mOrderType;

    public OrderAdapter(Context context, List<OrderBean> list) {
        super(context, list);
    }

    public OrderAdapter(Context context, int orderType, List<OrderBean> list){
        this(context, list);
        mOrderType = orderType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        OrderBean bean = mList.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_order, parent, false);
            holder = new ViewHolder();
            holder.tvOrderNum = (TextView) convertView.findViewById(R.id.tv_order_num);
            holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.tvRentplace = (TextView) convertView.findViewById(R.id.tv_rent_place);
            holder.tvRentMoney = (TextView) convertView.findViewById(R.id.tv_rent_money);
            holder.btPay = (Button) convertView.findViewById(R.id.bt_pay);
            if(mOrderType == 2){
                holder.btPay.setVisibility(View.VISIBLE);
                addListener(holder.btPay, bean);
            }
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvOrderNum.setText(mContext.getString(R.string.order_num, bean.getId() +""));
        holder.tvOrderTime.setText(format.format(bean.getStart_time()));
        holder.tvRentplace.setText(bean.getStatus() + "");
        holder.tvRentMoney.setText(bean.getAccounts_price() + "元");
        return convertView;
    }

    public void addListener(View v, final OrderBean bean){
       v.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mShareIfiSdk.pay(bean.getId() + "");
           }
       });
    }


    public class ViewHolder{
        public TextView tvOrderNum;
        public TextView tvOrderTime;
        public TextView tvRentplace;
        public TextView tvRentMoney;
        public Button btPay;
    }

}
