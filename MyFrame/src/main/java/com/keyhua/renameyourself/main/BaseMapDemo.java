package com.keyhua.renameyourself.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;

/**
 * 演示MapView的基本用法
 */
public class BaseMapDemo extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String LTAG = BaseMapDemo.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basemap);
        initHeaderOther("", "离线地图", true, false, false);
        init();
        Intent intent = getIntent();
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 隐藏自带的地图缩放控件
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        if (intent.hasExtra("x") && intent.hasExtra("y")) {
            // 当用intent参数时，设置中心点为指定点
            Bundle b = intent.getExtras();
            LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
            // mMapView = new MapView(this,
            // new BaiduMapOptions().mapStatus(new MapStatus.Builder()
            // .target(p).build()));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p);
            mBaiduMap.animateMapStatus(u);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onInitData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResload() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void setMyViewClick() {
        // TODO Auto-generated method stub
    }

}
