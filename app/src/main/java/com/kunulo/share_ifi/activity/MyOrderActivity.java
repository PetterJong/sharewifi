package com.kunulo.share_ifi.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.utils.ToastUtils;
import com.kunulo.lib_share_ifi.jsonBen.MyOrderJsonBean.MyOrderBean.OrderBean;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.fragment.BaseOrderFragment;
import com.kunulo.share_ifi.fragment.HasCompleteFragment;
import com.kunulo.share_ifi.fragment.RentingFragment;
import com.kunulo.share_ifi.fragment.WaitPayFragment;
import com.kunulo.share_ifi.utils.TitlebarHelper;

import java.util.List;

public class MyOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private TitlebarHelper helper;
    private RadioGroup rgContent;
    private RadioButton rbRenting;
    private RadioButton rbWaitPay;
    private RadioButton rbHascomplete;
    private FrameLayout layContent;
    private BaseOrderFragment mRentingFragment;
    private BaseOrderFragment mCurFragment;
    private BaseOrderFragment mWaitPayFragment;
    private BaseOrderFragment mHasCompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findView();
        init();
        initTitlebar();
        addListener();
    }

    private void findView() {
        rgContent = (RadioGroup) findViewById(R.id.rg_content);
        rbRenting = (RadioButton) findViewById(R.id.rb_rent);
        rbRenting = (RadioButton) findViewById(R.id.rb_wait_pay);
        rbHascomplete = (RadioButton) findViewById(R.id.rb_has_complemet);
        layContent = (FrameLayout) findViewById(R.id.lay_content);
    }

    private  FragmentManager mFragmentManager;
    private void init() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mRentingFragment = new RentingFragment();
        mHasCompleteFragment = new HasCompleteFragment();
        mWaitPayFragment = new WaitPayFragment();
        fragmentTransaction.add(R.id.lay_content, mHasCompleteFragment).hide(mHasCompleteFragment)
                .add(R.id.lay_content, mWaitPayFragment).hide(mWaitPayFragment)
                .add(R.id.lay_content, mRentingFragment).show(mRentingFragment).commit();
        mCurFragment = mRentingFragment;
    }

    private void initTitlebar() {
        helper = new TitlebarHelper(getWindow().getDecorView());
        helper.setBackAndTitle(R.string.my_order);
        helper.ivLeft.setOnClickListener(this);
    }

    private void addListener() {
        rgContent.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_rent:
                fragmentTransaction.hide(mCurFragment);
                mCurFragment = mRentingFragment;
                fragmentTransaction.show(mCurFragment).commit();
                break;
            case R.id.rb_wait_pay:
                fragmentTransaction.hide(mCurFragment);
                mCurFragment = mWaitPayFragment;
                fragmentTransaction.show(mCurFragment).commit();
                break;
            case R.id.rb_has_complemet:
                fragmentTransaction.hide(mCurFragment);
                mCurFragment = mHasCompleteFragment;
                fragmentTransaction.show(mCurFragment).commit();
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void refreshFragment(){
        mShareIfiSdk.getMyOrder(1,1,10); // 请求租借中的订单
        mShareIfiSdk.getMyOrder(2,1,10); // 请求待支付的订单
        mShareIfiSdk.getMyOrder(3,1,10); // 请求已完成的订单
    }


    @Override
    public void didGetMyOrder(boolean result, String code, String message, int orderType, int nextPage, List<OrderBean> deviceBeens) {
        super.didGetMyOrder(result, code, message, orderType, nextPage, deviceBeens);
        if(result){
            if(orderType == 1 && mRentingFragment != null){
                mRentingFragment.onReciveOrderEvent(orderType, nextPage, deviceBeens);
            } else if(orderType == 2 && mWaitPayFragment != null) {
                mWaitPayFragment.onReciveOrderEvent(orderType, nextPage, deviceBeens);
            } else if(orderType == 3 && mHasCompleteFragment != null) {
                mHasCompleteFragment.onReciveOrderEvent(orderType, nextPage, deviceBeens);
            }
        } else {
            ToastUtils.showShortToast(message);
        }
    }

    @Override
    public void didPay(boolean result, String code, String message) {
        super.didPay(result, code, message);
        if(result){
            long orderId = Long.valueOf(message);
            mRentingFragment.onPayOrderEvent(orderId);
            mWaitPayFragment.onPayOrderEvent(orderId);
            mHasCompleteFragment.onPayOrderEvent(orderId);
        } else {
            ToastUtils.showShortToast(message);
        }
    }
}
