package com.keyhua.renameyourself.main.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.keyhua.litepal.GpsInfo;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

public class GpsInfoCollectionService extends Service {
    //
    private Boolean D = true;
    // 轨迹数据库操作
    private List<GpsInfo> mGpSinfoDao;
    private GpsInfo info = null;

    public GpsInfoCollectionService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        if (D) {
            Log.i("GPS服务数据收集", "IBinder()");
        }
        return null;
    }

    String gps_start_time = null;

    @Override
    public void onCreate() {
        gps_start_time = (String) SPUtils.get(getApplicationContext(),
                "gps_start_time", "");
        if (D) {
            Log.i("GPS服务数据收集", "onCreate()");
        }
        info = new GpsInfo();
        mGpSinfoDao = DataSupport.findAll(GpsInfo.class);
        // 根据开始时间来查gps数据
        mGpSinfoDao = DataSupport.where("start_time=?", gps_start_time).find(GpsInfo.class);
        try {
            if (mGpSinfoDao != null&&mGpSinfoDao.size()>0) {
                if (mGpSinfoDao.get(0)
                        .getLocationInfo() != null) {
                    arrayLocationInfo = new JSONArray(mGpSinfoDao.get(0)
                            .getLocationInfo());
                } else {
                    arrayLocationInfo = new JSONArray();
                }
            } else {
                arrayLocationInfo = new JSONArray();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 接收关闭线程请求
        IntentFilter filter = new IntentFilter(
                GpsInfoCollectionService.actionToStop);
        registerReceiver(broadcastReceiver, filter);
        super.onCreate();
        // 在没有点停止记录轨迹之前都需要拿到之前存在数据库中的json，这里需要一个值在判断点击状态

    }

    /**
     * 百度地图Start
     * ----------------------------------------------------------------
     */
    private LocationClient mLocationClient;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    public MyLocationListener mMyLocationListener;

    // 经纬度
    public void getNowLocation() {
        // 百度地图
        // ,有时候没办法执行mLocationClient.requestLocation()回掉，getApplicationContext()与this换下就行了
        mLocationClient = new LocationClient(getApplicationContext());
        // 放于MyLocationListener()之前
        InitLocation();
    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mLocationClient.start();
        startTimer();
    }

    /**
     * 实现实位回调监听
     */
    double longitude = 0;
    double latitude = 0;
    JSONArray arrayLocationInfo = null;

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (App.getInstance().isTb_phonelocation()) {// 开启状态 ,默认就是true
                // 当数据库中有值的时候，使用上一次存的数据来比较
                if (arrayLocationInfo.length() != 0) {
                    try {
                        longitude = arrayLocationInfo.getJSONObject(
                                arrayLocationInfo.length() - 1).getDouble(
                                "longitude");
                        latitude = arrayLocationInfo.getJSONObject(
                                arrayLocationInfo.length() - 1).getDouble(
                                "latitude");
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                // Receive Location
                if (longitude == location.getLongitude()
                        || latitude == location.getLatitude()) {
                    // 只要有一个参数相同时不保存
                } else {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    info = new GpsInfo();

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("longitude", longitude);// 经度
                        jsonObject.put("latitude", latitude);// 纬度
                        jsonObject.put("name", "");// 地点名称
                        jsonObject.put("describe", "");// 地点描述
                        arrayLocationInfo.put(jsonObject);
                        /***
                         * 轨迹数据内容模板： [ { "name": "",//地点名称 "describe": "",地点描述
                         * "longitude": "",//经度 "latitude": ""//纬度 } ]
                         */
                        info.setLocationInfo(arrayLocationInfo.toString());
                        GpsInfo g = new GpsInfo();
                        g.setLocationInfo(arrayLocationInfo.toString());
                        try {//如果是第一条数据则会在异常中保存
                            g.update(mGpSinfoDao.get(0).getId());
                        } catch (Exception e) {
                            g.save();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    info = null;
                }
            } else {// GPS关闭状态
                // getGPSDataTable();
            }

        }
    }

    /**
     * 百度地图END
     * ------------------------------------------------------------------
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (D) {
            Log.i("GPS服务数据收集", "onStartCommand()");
        }
        getNowLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (D) {
            Log.i("GPS服务数据收集", "onDestroy()");
        }
        mGpSinfoDao = null;
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        // 关闭广播、服务时间计时器
        unregisterReceiver(broadcastReceiver);
        pauseTimer();
        super.onDestroy();
    }

    private Timer timer = new Timer();
    private TimerTask task;
    public static final String action = "com.hwacreate.outdoor.service.action";
    public static final String actionToStop = "com.hwacreate.outdoor.service.action.stopservice";

    // 一个TimerTask 通过schedule方法使用之后，不能通过schedule方法调用第二次，想重复使用是不行的，是一次性用品
    public void startTimer() {
        pauseTimer();
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Log.i("TimerTask", "TimerTask()");
                    mLocationClient.requestLocation();
                    // 发送广播绘制轨迹
                    Intent intent = new Intent(action);
                    sendBroadcast(intent);
                }
            };
        }
        if (timer != null && task != null) {
//			timer.schedule(task, 3000, 60 * 1000 * 5);
            timer.schedule(task, 3000, 60 * 1000);
        }
    }

    public void pauseTimer() {
        if (timer != null && task != null) {
            timer.cancel();
            timer = null;
            task.cancel();
            task = null;
        }
    }

    /**
     * 服务中发送广播过来进行绘图 ，暂停或停止
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };
}
