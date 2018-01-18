package com.kunulo.share_ifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.kunulo.lib_share_ifi.jsonBen.DevicePoint;
import com.kunulo.share_ifi.R;
import com.kunulo.share_ifi.activity.listener.MyOnMapStatusChangeListener;
import com.kunulo.share_ifi.sensor.MyOrientationListener;
import com.kunulo.share_ifi.utils.IntentUtils;
import com.kunulo.share_ifi.utils.TitlebarHelper;
import com.kunulo.share_ifi.utils.Utils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    /*
    sliding
    */
    private  ImageView ivUserIcon;
    private TextView tvUserName;
    private View layWallet;
    private View layDevices;
    private View layOrder;
    private View layCoupon;
    private View layAbout;
    private View layFeedback;
    private Button btLoginOut;

    private TitlebarHelper titlebarHelper;
    private DrawerLayout drawerLayout;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button btUnLock;
    private ImageView ivRestartDingWei;

    private MyOrientationListener orientationListener; // 传感器

    private float mCurrentX = 0; // 传感器方向
//    private boolean isFristIn;

    private BitmapDescriptor mCurrentMarkerBmp; // 当前位置的图片
    private BitmapDescriptor mMarkerBmp; // 标记设备坐标的图片
    private List<Marker> mMarkers = new ArrayList<>(); // 所有标记点集合（仅包含标记设备）
    private Marker mCurMark;  // 当前拖动位置中心标注点
    private BitmapDescriptor mDingWeiMarkerBmp; //  当前拖动位置中心的图片

    private LatLng mCurLatLng; // 上一次定位坐标
    private LocationMode mCurrentMode = LocationMode.NORMAL; // 当前定位模式
    private MyLocationConfiguration mLocationConfiguration; // 定位点配置信息
    float mZoom = 18; // 当前缩放比例


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        findView();
        init();
        initLocation();
        initTitle();
        // 初始化传感器
        initSensor();
        addListener();

    }


    private void findView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        btUnLock = (Button) findViewById(R.id.bt_unlock);
        ivRestartDingWei = (ImageView) findViewById(R.id.iv_restart_dingwei);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivUserIcon = (ImageView) findViewById(R.id.iv_user_icon);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        layWallet = findViewById(R.id.lay_wallet);
        layDevices = findViewById(R.id.lay_devices);
        layOrder = findViewById(R.id.lay_order);
        layCoupon = findViewById(R.id.lay_coupon);
        layAbout = findViewById(R.id.lay_about);
        layFeedback = findViewById(R.id.lay_feedback);
        btLoginOut = (Button) findViewById(R.id.bt_login_out);

    }

    private void init() {
        tvUserName.setText(mShareIfiSdk.getUserNumber());

    }


    /**
     * 路径规划搜索接口
     * */
//    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    private void initLocation(){
        // 隐藏百度的LOGO
        View child = mMapView.getChildAt(1);
        mBaiduMap = mMapView.getMap();
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mMapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mMapView.showZoomControls(false);

        // 解决第一次可能定位失败
        mShareIfiSdk.startLocationDingWei();
        mCurrentMarkerBmp = BitmapDescriptorFactory.fromResource(R.mipmap.ic_current_mark);
        mCenterMarkerOption = new MarkerOptions()
                .icon(mCurrentMarkerBmp)
                .draggable(true);

        mDingWeiMarkerBmp = BitmapDescriptorFactory.fromResource(R.mipmap.ic_dingwei);
        mLocationConfiguration = new MyLocationConfiguration(mCurrentMode, true, mDingWeiMarkerBmp);
        mMarkerBmp = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mark);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        mBaiduMap.setMyLocationConfiguration(mLocationConfiguration);
        // 标记点的点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                String content = bundle.getString("content");
                ToastUtils.showShortToast(marker.getTitle() +" : " + content );
//                LatLng toLatLng = marker.getPosition();
//                WalkingRoutePlanOption option = new WalkingRoutePlanOption();
//                PlanNode toPlanNode =  PlanNode.withLocation(toLatLng);
//                PlanNode fromePlanNode =  PlanNode.withLocation(mCurLatLng);
//                option.from(fromePlanNode).to(toPlanNode);
//                mSearch.walkingSearch(option);
                return false;
            }
        });
        // 地图拖动时间
        mBaiduMap.setOnMapStatusChangeListener(new MyOnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LogUtils.v("longitude :  " + mapStatus.target.longitude + " latitude : " + mapStatus.target.latitude);
//                averlayDingWeiMaker(mapStatus.target.latitude, mapStatus.target.longitude);
                mShareIfiSdk.getNearbyDevice(mapStatus.target.longitude, mapStatus.target.latitude);
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                super.onMapStatusChange(mapStatus);
                averlayCenterMark(mapStatus.target);
            }
        });

        UiSettings settings = mBaiduMap.getUiSettings();
//        settings.setAllGesturesEnabled(false);   //关闭一切手势操作
//        settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
        settings.setRotateGesturesEnabled(false);//屏蔽旋转
//        settings.setZoomGesturesEnabled(false);//获取是否允许缩放手势返回:是否允许缩放手势
//        settings.setCompassEnabled(false); //设置是否允许拖拽手势
//        settings.setScrollGesturesEnabled(false); //设置是否允许指南针
    }

    private void initTitle() {
        titlebarHelper = new TitlebarHelper(getWindow().getDecorView());
        titlebarHelper.ivLeft.setOnClickListener(this);
    }



    /**
     * @author WP
     * 定位结合方向传感器，从而可以实时监测到X轴坐标的变化，从而就可以检测到
     * 定位图标方向变化，只需要将这个动态变化的X轴的坐标更新myCurrentX值，
     * 最后在MyLocationData data.driection(myCurrentX);
     * */
    private void initSensor(){
        orientationListener = new MyOrientationListener(getApplicationContext());
        orientationListener.start();
        orientationListener.setMyOrientationListener(new MyOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
                setDirection(mCurrentX);
            }
        });
    }

    private void addListener() {
        btUnLock.setOnClickListener(this);
        ivRestartDingWei.setOnClickListener(this);
        layWallet.setOnClickListener(this);
        layDevices.setOnClickListener(this);
        layOrder.setOnClickListener(this);
        layCoupon.setOnClickListener(this);
        layAbout.setOnClickListener(this);
        layFeedback.setOnClickListener(this);
        btLoginOut.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(orientationListener != null){
            orientationListener.stop();
            orientationListener = null;
        }
        mMapView.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    /**
     * 标记附近坐标
     * @param isDrag 是否拖动
     * @param latLngs 需要标记的坐标点
     * @return
     */
    private void averlayMap(boolean isDrag, List<LatLng> latLngs) {
        for (Marker marker :mMarkers){
            marker.remove();
        }
        mMarkers.clear();
//        options.clear();
        //构建Marker图标
        //构建MarkerOption，用于在地图上添加Marker
        if(latLngs != null) {
            for (LatLng latlng :latLngs) {
                OverlayOptions option = new MarkerOptions()
                        .position(latlng)
                        .icon(mMarkerBmp)
                        .draggable(isDrag);
//                options.add(option);
                Marker marker = (Marker) mBaiduMap.addOverlay(option);
                marker.setTitle("title");
                Bundle bundle = new Bundle();
                bundle.putString("content", "content " );
                marker.setExtraInfo(bundle);
                mMarkers.add(marker);
            }
        }
    }

    private void clearMarkers(){
        for (Marker marker : mMarkers){
            marker.remove();
        }
        mMarkers.clear();
    }

    private MarkerOptions mCenterMarkerOption;

    private void averlayCenterMark(LatLng latLng){
        if(mCurMark != null){
//            mCurMark.remove();
            mCurMark.setPosition(latLng);
            return;
        }
        if(mCenterMarkerOption != null) {
            mCenterMarkerOption.position(latLng);
        } else{
            mCenterMarkerOption = new MarkerOptions()
                    .position(latLng)
                    .icon(mCurrentMarkerBmp)
                    .draggable(true);
        }
        mCurMark = (Marker) mBaiduMap.addOverlay(mCenterMarkerOption);
    }

    private void  averlayDingWeiMaker(double latitude, double longitude){
        if(mBaiduMap == null)
            return;
        MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentX)
//                .direction(0)
                .latitude(latitude)
                .longitude(longitude).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
    }

    private void setDirection(float direction){
        if(mBaiduMap == null)
            return;
        MyLocationData locationData = mBaiduMap.getLocationData();
        if(locationData == null)
            return;
        MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(0)
                .direction(direction)
                .latitude(locationData.latitude)
                .longitude(locationData.longitude).build();

        mBaiduMap.setMyLocationData(locData);

    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        super.onReceiveLocation(location);
        clearMarkers();
        if (location == null)
            return;
        LatLng center = new LatLng(location.getLatitude(), location.getLongitude());
        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(center, mZoom);
        mBaiduMap.setMapStatus(mapStatus);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        averlayDingWeiMaker(location.getLatitude(), location.getLongitude());

        mShareIfiSdk.getNearbyDevice(location.getLongitude(), location.getLatitude());
        mCurLatLng = center;
        mShareIfiSdk.stopLocationDingWei();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_unlock:
                mShareIfiSdk.isPayDeposit();
                break;
            case R.id.iv_restart_dingwei:
                mZoom = mBaiduMap.getMapStatus().zoom;
                mShareIfiSdk.startLocationDingWei();
                break;
            case R.id.iv_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.lay_wallet:
                intent = new Intent(this, MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_devices:
                intent = new Intent(this, MyDevicesActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_order:
                intent = new Intent(this, MyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.lay_coupon:
                break;
            case R.id.lay_about:
                break;
            case R.id.lay_feedback:
                break;
            case R.id.bt_login_out:
                mShareIfiSdk.loginOut();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentUtils.REQUEST_QR_CODE && resultCode == RESULT_OK && data != null){
            Bundle bundle = data.getExtras();
            int result = bundle.getInt(CodeUtils.RESULT_TYPE);
            String msg = bundle.getString(CodeUtils.RESULT_STRING);
            String sb;
            if(result == CodeUtils.RESULT_SUCCESS){
                sb = "解析成功：" + msg;
            } else {
                sb = "解析失败：" + msg;
            }
            ToastUtils.showShortToast(sb);
        }
    }

    @Override
    public void didGetNearbyDevice(boolean result, String code, String message,List<DevicePoint> devicePoints) {
        super.didGetNearbyDevice(result, code, message,devicePoints);
        if(result){
            clearMarkers();
            averlayMap(true, Utils.change2LatLans(devicePoints));
        } else {
            ToastUtils.showShortToast(message);
        }
    }

    @Override
    public void didIspayDeposit(boolean result, String code, String message) {
        super.didIspayDeposit(result, code, message);
        if(result){
            mShareIfiSdk.isCanBorrow();
        } else {
            ToastUtils.showShortToast(message);
        }
    }

    @Override
    public void didIsCanBorrow(boolean result, String code, String message) {
        super.didIsCanBorrow(result, code, message);
        if(result){
            Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            startActivityForResult(intent, IntentUtils.REQUEST_QR_CODE);
        } else {
            ToastUtils.showShortToast(message);
        }
    }

    @Override
    public void didLoginOut(boolean result, String message) {
        super.didLoginOut(result, message);
        if(result){
            pageManager.finishAllActivity();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
