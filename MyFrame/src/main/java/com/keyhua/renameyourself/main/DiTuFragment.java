package com.keyhua.renameyourself.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
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
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.GpsInfo;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.eventBusBean.ActivitySetBean;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.GetMemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.GpsBean;
import com.keyhua.renameyourself.main.eventBusBean.InitBluetoothBean;
import com.keyhua.renameyourself.main.eventBusBean.LengthZero;
import com.keyhua.renameyourself.main.eventBusBean.QueryModeBean;
import com.keyhua.renameyourself.main.eventBusBean.TestGps;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.main.le.BluetoothLeService;
import com.keyhua.renameyourself.main.protocol.HwtxCommandUtility;
import com.keyhua.renameyourself.main.protocol.HwtxDataGpsInfoDataComp;
import com.keyhua.renameyourself.main.protocol.HwtxDataGrpGpsInfoItem;
import com.keyhua.renameyourself.main.service.GpsInfoCollectionService;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.NetUtil;
import com.keyhua.renameyourself.util.ParseOject;
import com.keyhua.renameyourself.util.SPUtils;
import com.keyhua.renameyourself.util.TimeUtil;
import com.keyhua.renameyourself.view.CircleImageView;
import com.keyhua.renameyourself.view.CleareditTextView;
import com.keyhua.renameyourself.view.CustomDialog;
import com.keyhua.renameyourself.view.MyListView;
import com.keyhua.renameyourself.view.ReboundScrollView;

import org.apache.http.util.EncodingUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

/**
 * @author 曾金叶
 * @2015-8-5 @上午10:15:40
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DiTuFragment extends BaseFragment implements
        OnCheckedChangeListener, OnItemClickListener, OnDismissListener {
    protected TextView toolbar_tv_right_cancle = null;// 右边文字
    // int count=-0;
    private MapView mMapView = null;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    // double distance=0;
    private BaiduMap mBaiduMap;
    private LatLng p2;
    // UI相关
    private ImageView requestLocButton;
    // 开始记录轨迹
    private ImageView iv_kaishihuaguiji;
    // 记录中轨迹
    private ImageView iv_jilutinghuaguiji;
    // 暂停记录轨迹
    private ImageView iv_zantinghuaguiji;
    // 停止记录轨迹
    private ImageView iv_tingzhi;
    // 下载离线地图
    private ImageView iv_xiazai;
    // 定位所有人员
    private ImageView iv_xianshisuoyouren;
    // 地图图层显示
    private ImageView iv_dituleixing;
    boolean isFirstLoc = true;// 是否首次定位
    // 自定义放大缩小控件
    private Button zoomInBtn;
    private Button zoomOutBtn;
    private float currentZoomLevel = 0;
    List<GpsInfo> infos;
    List<GpsInfo> infosMy;
    // 轨迹数据库操作
    private List<GpsInfo> mGpSinfoDao;
    private GpsInfo info = null;
    // 选择图层类别
    private LinearLayout ll_dtlx = null;
    // 参考轨迹的显示与隐藏
    private LinearLayout ll_ckgj = null;
    // 刷新按钮
    private ImageView tv_refresh = null;
    // 失联提示
    private TextView tv_tishi = null;
    // 选择当前参考轨迹
    private TextView tv_ckgj = null;
    // 普通地图
    private TextView tv_ptdt = null;
    // 卫星地图
    private TextView tv_wxdt = null;
    // 交通地图
    private TextView tv_close = null;
    // 计划轨迹
    private ToggleButton tb_jhgj = null;
    // 我的轨迹
    private ToggleButton tb_wdgj = null;
    // 交通图
    private ToggleButton tb_jtdt = null;
    // 点击右上角显示人员列表
    private MyListView lv = null;
    // 失联队员列表
    private MyListView lv_shilian = null;
    private ReboundScrollView rsv = null;
    private RelativeLayout rl = null;
    // lv中数据,需更改bean
    private MyListAdpter listadapter = null;
    private List<SignUpUser> tuZhongUserListGet = null;
    private SignUpUser zhongUser = null;
    //
    private String mDeviceName = null;// 蓝牙名
    private String mDeviceAddress = null;// 蓝牙地址
    // 展示所有人员信息
    // 初始化全局 bitmap 信息，不用时及时 recycle
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor bdStart = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_st);
    private BitmapDescriptor bdEnd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_en);
    //自己
    private BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.dt_me);
    //领队
    private BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.mipmap.dt_ld);
    //其他人
    private BitmapDescriptor bdC = BitmapDescriptorFactory
            .fromResource(R.mipmap.dt_dy);
    // 实时获取头像
    private BitmapDescriptor bdD = null;
    private List<LatLng> latLngs = null;
    private List<String> latLngsStr = null;
    private boolean showDuiyuan = true;

    // 参考轨迹
    private String TempTrace_data = null;//
    private JSONArray arrayTempTrace_data = null;
    // 蓝牙设备配置
    private JSONObject hwtxCommandjsonObject = null;
    /**
     * GPS工作间隔[1,60]
     */
    private Integer bGpsInterval = null;
    /**
     * 广播时间间隔[5,60]
     */
    private Integer bHktAtInterval = null;
    /**
     * 失联次数 [1-10]
     */
    private Integer bLostContactNum = null;
    /**
     * 黄色警告距离 范围[10, 5000]
     */
    private Integer wWarningDistance1 = 1000;
    /**
     * 红色警告距离
     */
    private Integer wWarningDistance2 = 1500;
    /**
     * 失联警告距离
     */
    private Integer wWarningDistance3 = 2000;
    /**
     * 电量告警
     */
    private Integer bWarningBatteryPercent = null;
    private static SVProgressHUD mSVProgressHUD;
    //需要更新的对象，1两个都更新，2设备参数表，3队员信息表
    private int status = 0;

    /**
     * 数据的初始化
     */
    public void initDao() {

        // 获取队员数据库
        signUpUserGet = new SignUpUser();
        // 拿取数据
        signUpUserListGet = LitepalUtil.getAllUser();
        // 轨迹信息数据库
        info = new GpsInfo();
        mGpSinfoDao = DataSupport.findAll(GpsInfo.class);
        if (LitepalUtil.getpg() != null) {
            TempTrace_data = LitepalUtil.getpg().getLocationInfo();
            try {
                arrayTempTrace_data = new JSONArray(TempTrace_data);
            } catch (JSONException e) {
                //
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().setBottonChoice(CommonUtility.TUZHONG);
        initDao();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leftfrag_tuzhong, null);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        getActivity().unregisterReceiver(broadcastReceiver);
        App.getInstance().setMyGuiJi(false);
        App.getInstance().setJiHuaGuiJi(false);
        super.onDestroy();
    }

    /**
     * Dialog 暂停
     */
    public void showAlertDialog() {
        final CustomDialog dialog = new CustomDialog(getActivity(),
                R.style.Dialog);
        dialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dt_dialog_view, null);
        dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        TextView tv_dialog_btn1 = (TextView) view
                .findViewById(R.id.tv_dialog_btn1);
        tv_dialog_btn1.setText("暂停记录当前轨迹");
        TextView tv_dialog_btn2 = (TextView) view
                .findViewById(R.id.tv_dialog_btn2);
        TextView tv_dialog_btn3 = (TextView) view
                .findViewById(R.id.tv_dialog_btn3);
        dialog.show();
        tv_dialog_btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 标记为暂停
                SPUtils.put(getActivity(), "dt_status", ZANTING);
                dialog.dismiss();
                // 暂停状态
                iv_zantinghuaguiji.setVisibility(View.VISIBLE);
                iv_jilutinghuaguiji.setVisibility(View.GONE);
                // 关闭服务
                Intent intent2 = new Intent(
                        GpsInfoCollectionService.actionToStop);
                getActivity().sendBroadcast(intent2);
            }
        });
        // 停止
        tv_dialog_btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 标记为未开始
                SPUtils.put(getActivity(), "dt_status", WEIKAISHI);
                // 停止时间
                String end_time = TimeUtil.getDatetime();
                GpsInfo s = new GpsInfo();
                s.setEnd_time(end_time);
                s.updateAll("start_time = ?", (String) SPUtils.get(
                        getActivity(), "gps_start_time", ""));
                dialog.dismiss();
                // 恢复到最初状态
                iv_jilutinghuaguiji.setVisibility(View.GONE);
                iv_kaishihuaguiji.setVisibility(View.VISIBLE);
                // 关闭服务
                Intent intent2 = new Intent(
                        GpsInfoCollectionService.actionToStop);
                getActivity().sendBroadcast(intent2);
                for (int i = 0; i < resultPoints.size(); i++) {
                    if (i == resultPoints.size() - 1) {
                        overlayOptions = new MarkerOptions()
                                .position(resultPoints.get(i)).icon(bdEnd)
                                .draggable(false).perspective(true);

                    }
                    mBaiduMap.addOverlay(overlayOptions);
                }
                //
                showAlertDialogInputGpsName();
            }

        });
        // 取消
        tv_dialog_btn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    AlertDialog dialog2 = null;

    /**
     * Dialog 暂停状态下弹出的对话框
     */
    public void showAlertDialogCancle() {

        final CustomDialog dialog = new CustomDialog(getActivity(),
                R.style.Dialog);
        dialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dt_dialog_view, null);
        dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        TextView tv_dialog_btn1 = (TextView) view
                .findViewById(R.id.tv_dialog_btn1);
        tv_dialog_btn1.setText("继续记录当前轨迹");
        TextView tv_dialog_btn2 = (TextView) view
                .findViewById(R.id.tv_dialog_btn2);
        TextView tv_dialog_btn3 = (TextView) view
                .findViewById(R.id.tv_dialog_btn3);
        dialog.show();
        // 开始记录
        tv_dialog_btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 标记为记录中
                SPUtils.put(getActivity(), "dt_status", JILUZHONG);
                dialog.dismiss();
                iv_zantinghuaguiji.setVisibility(View.GONE);
                // 记录中按钮显示出来
                iv_jilutinghuaguiji.setVisibility(View.VISIBLE);
                // iv_jilutinghuaguiji.setVisibility(View.VISIBLE);
                // 开启服务，记录轨迹到数据库
                getActivity().startService(
                        new Intent(getActivity(),
                                GpsInfoCollectionService.class));
                showTrack();//
            }
        });
        // 停止
        tv_dialog_btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 标记为未开始
                SPUtils.put(getActivity(), "dt_status", WEIKAISHI);
                // 停止时间
                String end_time = TimeUtil.getDatetime();
                GpsInfo s = new GpsInfo();
                s.setEnd_time(end_time);
                s.updateAll("start_time = ?", (String) SPUtils.get(
                        getActivity(), "gps_start_time", ""));
                dialog.dismiss();
                // 恢复到最初状态
                iv_zantinghuaguiji.setVisibility(View.GONE);
                iv_kaishihuaguiji.setVisibility(View.VISIBLE);
                // 关闭服务
                Intent intent2 = new Intent(
                        GpsInfoCollectionService.actionToStop);
                getActivity().sendBroadcast(intent2);
                for (int i = 0; i < resultPoints.size(); i++) {
                    if (i == resultPoints.size() - 1) {
                        overlayOptions = new MarkerOptions()
                                .position(resultPoints.get(i)).icon(bdEnd)
                                .draggable(false).perspective(true);

                    }
                    mBaiduMap.addOverlay(overlayOptions);
                }
                //
                showAlertDialogInputGpsName();
            }
        });
        // 取消
        tv_dialog_btn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Dialog 输入轨迹名称对话框 名称(输入)+时间(默认)
     */
    public void showAlertDialogInputGpsName() {
        final CustomDialog dialog = new CustomDialog(getActivity(),
                R.style.Dialog);
        dialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dt_dialog_view_input, null);
        dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        TextView tv_dialog_btn1 = (TextView) view
                .findViewById(R.id.tv_dialog_btn1);
        final CleareditTextView tv_dialog_ctv = (CleareditTextView) view
                .findViewById(R.id.tv_dialog_ctv);
        tv_dialog_btn1.setText("确认");
        TextView tv_dialog_btn3 = (TextView) view
                .findViewById(R.id.tv_dialog_btn3);
        dialog.show();
        // 保存轨迹名称
        tv_dialog_btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String tempStr = tv_dialog_ctv.getText().toString();
                if (!TextUtils.isEmpty(tempStr)) {
                    if (tempStr.length() < 50) {//
                        String timeStr = (String) SPUtils.get(
                                getActivity(), "gps_start_time", "");
                        GpsInfo s = new GpsInfo();
                        s.setName(tempStr + "-" + timeStr);
                        s.updateAll("start_time = ?", timeStr);
                        dialog.dismiss();
                    } else {
                        showToast("您输入的标题过长！");
                    }
                } else {
                    showToast("请输入轨迹标题！");
                }

            }
        });
        // 取消
        tv_dialog_btn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String timeStr = (String) SPUtils.get(
                        getActivity(), "gps_start_time", "");
                try {
                    List<GpsInfo> dao = DataSupport.where("start_time=?", gps_start_time).find(GpsInfo.class);
                    if (dao.size() > 0) {
                        GpsInfo s = new GpsInfo();
                        s.setName(timeStr);
                        s.updateAll("start_time = ?", timeStr);
                    }
                } catch (Exception e) {

                }

                dialog.dismiss();
            }
        });
    }

    private final int WEIKAISHI = 1;// 未开始状态
    private final int JILUZHONG = 2;// 记录状态
    private final int ZANTING = 3;// 暂停状态
    private int STATUS = 1;
    private boolean showAllPeople = false;// 是否需要显示所有队员在地图中

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_kaishihuaguiji:// 开始绘制轨迹 1为未开始状态 ，2为记录状态，3为暂停状态
                if (TextUtils.isEmpty(mDeviceAddress)) {
                    showToast("请先关联设备");
                } else {
                    // if (mGpSinfoDao.queryAll().size() < 5) {
                    SPUtils.put(getActivity(), "dt_status", JILUZHONG);
                    // 开始时间,每次根据当前这个时间来操作数据库
                    String start_time = TimeUtil.getDatetime();
                    SPUtils.put(getActivity(), "gps_start_time", start_time);
                    info = new GpsInfo();
                    info.setStart_time(start_time);
                    info.save();
                    /** 每次点开始的时候都是新增一个完整的轨迹对象 */
                    iv_kaishihuaguiji.setVisibility(View.GONE);
                    // 记录中按钮显示出来
                    iv_jilutinghuaguiji.setVisibility(View.VISIBLE);
                    // iv_jilutinghuaguiji.setVisibility(View.VISIBLE);
                    // 开启服务，记录轨迹到数据库
                    getActivity().startService(
                            new Intent(getActivity(), GpsInfoCollectionService.class));
                    showToast("开始记录轨迹");
                    showTrack();//
                }
                break;
            case R.id.iv_zantinghuaguiji:// 可暂停绘制轨迹
                // 关闭服务
                showAlertDialogCancle();
                break;
            case R.id.iv_xiazai:// 下载离线地图
                openActivity(DownOfflineActivity.class);
                break;
            case R.id.iv_jilutinghuaguiji:// 状态为记录中，点击展示继续、暂停、停止按钮
                showAlertDialog();
                break;
            case R.id.iv_tingzhi:// 停止绘制轨迹
                break;
            case R.id.iv_dituleixing:// 地图类型
                if (ll_dtlx.getVisibility() == View.GONE) {
                    ll_dtlx.setVisibility(View.VISIBLE);
                } else {
                    ll_dtlx.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_dtlx:// 点击pop框消失
                // ll_dtlx.setVisibility(View.GONE);
                break;
            case R.id.iv_xianshisuoyouren:
                tuZhongUserListGet = LitepalUtil.getAllUserByActisleave();
                if (tuZhongUserListGet.size() <= 1) {
                    showToast("当前没有队员，请添加队员后重试");
                } else {
                    showAllPeople();
                }

                break;
            case R.id.tv_ptdt:// 普通地图
                // ll_dtlx.setVisibility(View.GONE);
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                // sousuo_c
                tv_ptdt.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.mipmap.sousuo_c, 0);
                tv_wxdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case R.id.tv_ckgj:// 参考轨迹
                break;
            case R.id.tv_refresh:// 刷新
                //
                /**
                 * --------------------------------------------------《GPS 数据测试》-----------------------------------------------------------
                 */
//                byte[] buffer = getFromAssets("HWTX_GroupGPSInfo_Table1.bin");
//                BleCommon.getInstance().displayData(buffer);
                if (TextUtils.isEmpty(mDeviceAddress)) {
                    showToast("请先关联设备");
                } else {
                    BleCommon.getInstance().getGPSDataTable();
                }

                break;
            case R.id.tv_wxdt:// 卫星地图
                // ll_dtlx.setVisibility(View.GONE);
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                tv_wxdt.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.mipmap.sousuo_c, 0);
                tv_ptdt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case R.id.tv_close:// 交通地图
                ll_dtlx.setVisibility(View.GONE);
                // mBaiduMap.g
                // mBaiduMap.setTrafficEnabled(true);
                break;
            case R.id.toolbar_tv_right_cancle:
                // 展示人员列表，这个列表对于队员来讲是来自蓝牙设备的，通过领队宝同步到同行宝 TODO
                tuZhongUserListGet = LitepalUtil.getAllUserByActisleaveDrderByDistance();
                if (tuZhongUserListGet.size() <= 1) {
                    showToast("当前没有队员，请添加队员后重试");
                } else {
                    if (rsv.getVisibility() == View.GONE) {
                        //
                        rsv.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.GONE);
                        toolbar_tv_right_cancle.setText("地图");
                        handlerlistNet.sendEmptyMessage(CommonUtility.SERVEROK3);
                    } else {
                        toolbar_tv_right_cancle.setText("队员");
                        rsv.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onInitData() {
        mSVProgressHUD = new SVProgressHUD(getActivity());
        EventBus.getDefault().register(this);
        toolbar_tv_right_cancle = (TextView) getActivity().findViewById(R.id.toolbar_tv_right_cancle);
        //
        latLngs = new ArrayList<LatLng>();
        latLngsStr = new ArrayList<String>();
        //
        lv = (MyListView) getActivity().findViewById(R.id.lv);
        lv_shilian = (MyListView) getActivity().findViewById(R.id.lv_shilian);
        rsv = (ReboundScrollView) getActivity().findViewById(R.id.rsv);
        //
        rl = (RelativeLayout) getActivity().findViewById(R.id.rl);
        ll_dtlx = (LinearLayout) getActivity().findViewById(R.id.ll_dtlx);
        ll_ckgj = (LinearLayout) getActivity().findViewById(R.id.ll_ckgj);
        tv_ptdt = (TextView) getActivity().findViewById(R.id.tv_ptdt);
        tv_ckgj = (TextView) getActivity().findViewById(R.id.tv_ckgj);
        tv_refresh = (ImageView) getActivity().findViewById(R.id.tv_refresh);
        tv_tishi = (TextView) getActivity().findViewById(R.id.tv_tishi);
        tv_wxdt = (TextView) getActivity().findViewById(R.id.tv_wxdt);
        tv_close = (TextView) getActivity().findViewById(R.id.tv_close);
        tb_jhgj = (ToggleButton) getActivity().findViewById(R.id.tb_jhgj);
        tb_wdgj = (ToggleButton) getActivity().findViewById(R.id.tb_wdgj);
        tb_jtdt = (ToggleButton) getActivity().findViewById(R.id.tb_jtdt);

        requestLocButton = (ImageView) getActivity()
                .findViewById(R.id.iv_local);
        iv_kaishihuaguiji = (ImageView) getActivity().findViewById(
                R.id.iv_kaishihuaguiji);
        iv_jilutinghuaguiji = (ImageView) getActivity().findViewById(
                R.id.iv_jilutinghuaguiji);
        iv_tingzhi = (ImageView) getActivity().findViewById(R.id.iv_tingzhi);
        iv_xiazai = (ImageView) getActivity().findViewById(R.id.iv_xiazai);
        iv_zantinghuaguiji = (ImageView) getActivity().findViewById(
                R.id.iv_zantinghuaguiji);
        iv_xianshisuoyouren = (ImageView) getActivity().findViewById(
                R.id.iv_xianshisuoyouren);
        iv_dituleixing = (ImageView) getActivity().findViewById(
                R.id.iv_dituleixing);
        // 2地图初始化
        mMapView = (MapView) getActivity().findViewById(R.id.bmapView);
        // 隐藏自带的地图缩放控件
        mMapView.showZoomControls(false);
        // 删除百度地图logo
        // mMapView.removeViewAt(1);
        zoomInBtn = (Button) getActivity().findViewById(R.id.zoomin);
        zoomOutBtn = (Button) getActivity().findViewById(R.id.zoomout);

        tuZhongUserListGet = new ArrayList<>();
        listadapter = new MyListAdpter();
        lv.setAdapter(listadapter);
//        handlerlistNet.sendEmptyMessage(CommonUtility.SERVEROK3);
        // 默认配置
//        handlerlistNet.sendEmptyMessage(CommonUtility.SERVEROK5);
//        if (!App.getInstance().isTb_phonelocation()) {// 未开启状态
//            BleCommon.getInstance().getGPSDataTable();
//        }

    }

    @Override
    protected void onResload() {
        mapViewControl();
        // 地图记录状态
        switch ((Integer) SPUtils.get(getActivity(), "dt_status", 1)) {
            case WEIKAISHI:// 未开始状态
                iv_kaishihuaguiji.setVisibility(View.VISIBLE);
                iv_jilutinghuaguiji.setVisibility(View.GONE);
                iv_zantinghuaguiji.setVisibility(View.GONE);
                break;
            case JILUZHONG:// 记录状态
                // 开启服务，记录轨迹到数据库
                getActivity().startService(
                        new Intent(getActivity(), GpsInfoCollectionService.class));
                showTrack();
                iv_kaishihuaguiji.setVisibility(View.GONE);
                iv_jilutinghuaguiji.setVisibility(View.VISIBLE);
                iv_zantinghuaguiji.setVisibility(View.GONE);
                break;
            case ZANTING:// 暂停状态
                showTrack();
                iv_kaishihuaguiji.setVisibility(View.GONE);
                iv_jilutinghuaguiji.setVisibility(View.GONE);
                iv_zantinghuaguiji.setVisibility(View.VISIBLE);
                break;
            default:
                iv_kaishihuaguiji.setVisibility(View.VISIBLE);
                iv_jilutinghuaguiji.setVisibility(View.GONE);
                iv_zantinghuaguiji.setVisibility(View.GONE);
                break;
        }
        // 开启广播来绘制地图
        IntentFilter filter = new IntentFilter(GpsInfoCollectionService.action);
        getActivity().registerReceiver(broadcastReceiver, filter);

    }

    @Override
    protected void setMyViewClick() {
        iv_kaishihuaguiji.setOnClickListener(this);
        iv_zantinghuaguiji.setOnClickListener(this);
        iv_jilutinghuaguiji.setOnClickListener(this);
        iv_tingzhi.setOnClickListener(this);
        iv_xiazai.setOnClickListener(this);
        iv_dituleixing.setOnClickListener(this);
        iv_xianshisuoyouren.setOnClickListener(this);
        tv_ptdt.setOnClickListener(this);
        tv_ckgj.setOnClickListener(this);
        tv_refresh.setOnClickListener(this);
        tv_wxdt.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        ll_dtlx.setOnClickListener(this);
        toolbar_tv_right_cancle.setOnClickListener(this);

        tb_jhgj.setOnCheckedChangeListener(this);
        tb_wdgj.setOnCheckedChangeListener(this);
        tb_jtdt.setOnCheckedChangeListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void headerOrFooterViewControl() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // 队员
        mDeviceName = App.getInstance().getBleDuiYuanName();
        mDeviceAddress = App.getInstance().getBleDuiYuanAddress();
        if (TextUtils.isEmpty(mDeviceAddress)) {
            mDeviceAddress = App.getInstance().getBleLingDuiDuiAddress();
        }
        // 当前界面的广播接收器
        if (!TextUtils.isEmpty(mDeviceName)) {
            // tv_contact.setText("已关联同行宝:" + mDeviceName + "\t\t点击断开");
        } else {
            // tv_contact.setText("关联同行宝");
            // showToast("未关联同行宝");
        }
    }

    // ----------------------------------------------------百度地图

    /**
     * 定位SDK监听函数
     * <p/>
     * double gpsLatitude = 0; double gpsLongitude = 0;
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (App.getInstance().isTb_phonelocation()) {// 开启状态
                // map view 销毁后不在处理新接收的位置
                if (location == null || mMapView == null)
                    return;
                logzjy.i("服务中的请求同样返回到这里来了！");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(latitude)
                        .longitude(longitude).build();
                mBaiduMap.setMyLocationData(locData);
                p2 = new LatLng(latitude, longitude);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p2);
                mBaiduMap.animateMapStatus(u);
                //更新使用当前手机的队员（或领队）的gps数据
                SignUpUser s = new SignUpUser();
                s.setUser_latitude(String.valueOf(latitude));
                s.setUser_longitude(String.valueOf(longitude));
                s.updateAll("isUsedByCurrentDevice = ?", String.valueOf(CommonUtility.SELF));
//                }
            } else {// GPS关闭状态

                if (TextUtils.isEmpty(mDeviceAddress)) {
                } else {
                    BleCommon.getInstance().getGPSDataTable();
                }

                // getGPSDataTable();
                // map view 销毁后不在处理新接收的位置
                latitude = App.getInstance().getGpsLatitude();
                longitude = App.getInstance().getGpsLongitude();
                int latitudeInt = (int) latitude;
                int longitudeInt = (int) longitude;
                if (latitudeInt != 0 && longitudeInt != 0) {//当拿到的gps数据是空的时候还是拿当前的
                    if (mMapView == null)
                        return;
                    logzjy.i("服务中的请求同样返回到这里来了！");
                    MyLocationData locData = new MyLocationData.Builder()
                            // .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(latitude)
                            .longitude(longitude).build();
                    mBaiduMap.setMyLocationData(locData);
                    p2 = new LatLng(latitude, longitude);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p2);
                    mBaiduMap.animateMapStatus(u);
//                    更新使用当前手机的队员（或领队）的gps数据
                    SignUpUser s = new SignUpUser();
                    s.setUser_latitude(String.valueOf(latitude));
                    s.setUser_longitude(String.valueOf(longitude));
                    s.updateAll("isUsedByCurrentDevice = ?", String.valueOf(CommonUtility.SELF));
                } else {
                    // map view 销毁后不在处理新接收的位置
                    if (location == null || mMapView == null)
                        return;
                    logzjy.i("服务中的请求同样返回到这里来了！");
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                                    // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(latitude)
                            .longitude(longitude).build();
                    mBaiduMap.setMyLocationData(locData);
                    p2 = new LatLng(latitude, longitude);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p2);
                    mBaiduMap.animateMapStatus(u);
                    //更新使用当前手机的队员（或领队）的gps数据
                    SignUpUser s = new SignUpUser();
                    s.setUser_latitude(String.valueOf(latitude));
                    s.setUser_longitude(String.valueOf(longitude));
                    s.updateAll("isUsedByCurrentDevice = ?", String.valueOf(CommonUtility.SELF));
                }
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 服务中发送广播过来进行绘图
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            showTrack();//
            /**
             * 在广播返回的时候从设备获取gps（领队宝） 获取GPS数据表
             * 每个从硬件获取数据的操作，1硬件都会先返回数据的大小，2再返回数据TODO
             */
            if (App.getInstance().isTb_phonelocation()) {// 开启状态

            } else {// GPS关闭状态
                BleCommon.getInstance().getGPSDataTable();
            }
        }
    };
    private List<LatLng> resultPoints;
    private Marker marker = null;
    private OverlayOptions overlayOptions = null;
    private JSONArray array = null;

    /**
     * 显示轨迹
     */
    private void showTrack() {

        if (mBaiduMap != null) {
            mBaiduMap.clear();
            // 关联地图中的默认值，应用退出时未停止画轨迹的情况
            // SPUtils.put(getActivity(), "dt_status", 0);

        }
        if ((Boolean) SPUtils.get(getActivity(), "showAllPeople", false)) {// 是否需要将所有队员显示
            allPeople();
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (getActivity() != null) {
                    resultPoints = new ArrayList<LatLng>();
                    String gps_start_time = (String) SPUtils.get(
                            getActivity(), "gps_start_time", "");
                    // 获取数据库的数据
                    List<GpsInfo> dao = DataSupport.where("start_time=?", gps_start_time).find(GpsInfo.class);
                    infos = new ArrayList<GpsInfo>();
                    if (dao.size() > 0) {


                        infos.add(dao.get(0));
                        if (infos.size() != 0 && infos.get(0) != null) {
                            if (!TextUtils.isEmpty(infos.get(0).getLocationInfo())) {
                                try {
                                    array = new JSONArray(infos.get(0)
                                            .getLocationInfo());
                                    for (int i = 0; i < array.length(); i++) {
                                        double myLongitude = array.getJSONObject(i)
                                                .getDouble("longitude");
                                        double myLatitude = array.getJSONObject(i)
                                                .getDouble("latitude");
                                        LatLng point = new LatLng(myLatitude,
                                                myLongitude);
                                        resultPoints.add(point);
                                        point = null;
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            // for (GpsInfo info : infos) {
                            // double myLatitude =
                            // Double.parseDouble(info.getLatitude());
                            // double myLongitude = Double
                            // .parseDouble(info.getLongitude());
                            // LatLng point = new LatLng(myLatitude, myLongitude);
                            // resultPoints.add(point);
                            // // latLngs.add(point);
                            // point = null;
                            //
                            // }
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // 数据库最后一个坐标的位置
                if (array != null) {

                    if (array.length() >= 2) {
                        MapStatusUpdate u = null;
                        try {
                            u = MapStatusUpdateFactory.newLatLng(new LatLng(
                                    array.getJSONObject(array.length() - 1)
                                            .getDouble("latitude"), array
                                    .getJSONObject(array.length() - 1)
                                    .getDouble("longitude")));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mBaiduMap.setMapStatus(u);
                        // 折线显示
                        if (resultPoints.size() >= 2) {
                            OverlayOptions ooPolyline = new PolylineOptions()
                                    .width(10).color(0xAAFF0000)
                                    .points(resultPoints); //
                            mBaiduMap.addOverlay(ooPolyline);
                        }
                        Log.i("onPostExecute", "onPostExecute()");
                        // 得到了所有信息之后才可点击显示队员按钮

                    }
                    // 显示出起点
                    for (int i = 0; i < resultPoints.size(); i++) {
                        if (i == 0) {
                            overlayOptions = new MarkerOptions()
                                    .position(resultPoints.get(i)).icon(bdStart)
                                    .draggable(false).perspective(true);
                            //
                            marker = (Marker) (mBaiduMap
                                    .addOverlay(overlayOptions));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("info", "测试");
                            marker.setExtraInfo(bundle);

                            mBaiduMap.addOverlay(overlayOptions);
                        }
                    }
                }
                super.onPostExecute(result);
            }

        }.execute();

    }

    // 参考轨迹--------------------------------------------------------------------------------------
    private List<LatLng> resultPointsTemp;
    private Marker markerTemp = null;
    private OverlayOptions overlayOptionsTemp = null;

    /**
     * 显示参考轨迹
     */
    private void showTrackTemp() {
        resultPointsTemp = new ArrayList<LatLng>();
        if (arrayTempTrace_data.length() != 0) {
            try {
                for (int i = 0; i < arrayTempTrace_data.length(); i++) {
                    double TempLongitude = arrayTempTrace_data.getJSONObject(i)
                            .getDouble("longitude");
                    double TempLatitude = arrayTempTrace_data.getJSONObject(i)
                            .getDouble("latitude");
                    LatLng point = new LatLng(TempLatitude, TempLongitude);
                    resultPointsTemp.add(point);
                    point = null;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // 数据库最后一个坐标的位置
        if (arrayTempTrace_data != null) {
            if (arrayTempTrace_data.length() >= 2) {
                MapStatusUpdate u = null;
                try {
                    u = MapStatusUpdateFactory.newLatLng(new LatLng(
                            arrayTempTrace_data.getJSONObject(0).getDouble(
                                    "latitude"), arrayTempTrace_data
                            .getJSONObject(0).getDouble("longitude")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mBaiduMap.setMapStatus(u);
                // 折线显示
                if (resultPointsTemp.size() >= 2) {
                    OverlayOptions ooPolyline = new PolylineOptions().width(10)
                            .color(0xAAFF0000).points(resultPointsTemp); //
                    mBaiduMap.addOverlay(ooPolyline);
                }
                Log.i("onPostExecute", "onPostExecute()");
                // 得到了所有信息之后才可点击显示队员按钮

            }
            // 显示出起点
            for (int i = 0; i < resultPointsTemp.size(); i++) {
                if (i == 0) {
                    overlayOptionsTemp = new MarkerOptions()
                            .position(resultPointsTemp.get(i)).icon(bdStart)
                            .draggable(false).perspective(true);
                    //
                    markerTemp = (Marker) (mBaiduMap
                            .addOverlay(overlayOptionsTemp));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", "测试");
                    markerTemp.setExtraInfo(bundle);

                    mBaiduMap.addOverlay(overlayOptionsTemp);
                }
                // 显示出终点
                if (i == resultPointsTemp.size() - 1) {
                    overlayOptionsTemp = new MarkerOptions()
                            .position(resultPointsTemp.get(i)).icon(bdEnd)
                            .draggable(false).perspective(true);
                    //
                    markerTemp = (Marker) (mBaiduMap
                            .addOverlay(overlayOptionsTemp));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", "测试");
                    markerTemp.setExtraInfo(bundle);

                    mBaiduMap.addOverlay(overlayOptionsTemp);
                }
            }
            // 显示出终点
        }
    }

    /**
     * 对地图的控制
     */
    private void mapViewControl() {
        mCurrentMode = LocationMode.NORMAL;
        OnClickListener btnClickListener = new OnClickListener() {
            public void onClick(View v) {
                //
                // if (mConnected) {
                // getGPSDataTable();
                // }
                switch (mCurrentMode) {
                    case NORMAL:
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        mCurrentMode = LocationMode.NORMAL;

                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        mCurrentMode = LocationMode.NORMAL;
                        mBaiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;

                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);
        // 2
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 开启罗盘，不显示
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        // 开启交通图
        mBaiduMap.setTrafficEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);// 不允许旋转
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newMapStatus(new MapStatus.Builder().zoom(17).build()));// 设置缩放级别
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(60 * 1000); // 不设置或者小于1000，手动调用
        // locationClient.requestLocation();就会进行一次定位。
        // 设置定时定位的时间间隔。单位毫秒

        mLocClient.setLocOption(option);
        mLocClient.start();
        // 按钮缩放
        zoomInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = mBaiduMap.getMapStatus().zoom;
                if (zoomLevel < mBaiduMap.getMaxZoomLevel()) {
                    // MapStatusUpdateFactory.zoomIn();
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                    zoomOutBtn.setEnabled(true);
                } else {
                    // showToast("已经放至最大！");
                    zoomInBtn.setEnabled(false);
                }
            }
        });
        zoomOutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float zoomLevel = mBaiduMap.getMapStatus().zoom;
                if (zoomLevel > mBaiduMap.getMinZoomLevel()) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                    zoomInBtn.setEnabled(true);
                } else {
                    zoomOutBtn.setEnabled(false);
                    showToast("已经缩至最小！");
                }
            }
        });
        // 覆盖物点击
        mBaiduMap.setOnMarkerClickListener(clickListener);
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                logzjy.e("*******onMapStatusChange**********");
                currentZoomLevel = arg0.zoom;
                if (currentZoomLevel == mBaiduMap.getMaxZoomLevel()) {
                    zoomInBtn.setEnabled(false);
                    // showToast("已经放至最大！");
                } else if (currentZoomLevel == mBaiduMap.getMinZoomLevel()) {
                    zoomOutBtn.setEnabled(false);
                    showToast("已经缩至最小！");
                } else {
                    zoomInBtn.setEnabled(true);
                    zoomOutBtn.setEnabled(true);
                }
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                logzjy.e("*********onMapStatusChangeFinish********");
            }

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

                logzjy.e("*****************" + Float.toString(currentZoomLevel));
            }
        });

    }

    // 失联人员
    int lichengOtherTempLong = 0;
    String nameTempLong = null;
    List<Shilianbean> listShilianbean = null;
    // SPUtils.put(getActivity(), "Leader_latitude",
    // LeaderLocationItem.getLeader_latitude());
    // SPUtils.put(getActivity(), "Leader_latitude",
    // LeaderLocationItem.getLeader_longitude());
    // SPUtils.put(getActivity(), "Leadrid",
    // LeaderLocationItem.getLeadrid());
    /**
     * 地图中点击某个人获取信息
     */
    private OnMarkerClickListener clickListener = new OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {
            //
//            List<SignUpUser> tuZhongUserListGet = LitepalUtil.getAllUserByActisleave();
            for (int j = 0; j < tuZhongUserListGet.size(); j++) {
                Integer tps_type = tuZhongUserListGet.get(j).getTps_type();// 人员类型：1普通队员，2领队
                Integer tps_id = tuZhongUserListGet.get(j).getTps_id();// 物理id
                String phonenum = tuZhongUserListGet.get(j).getPhonenum();// 队员手机号
                String strDeviceSN = tuZhongUserListGet.get(j).getStrDeviceSN();// 硬件sn号码
                String u_nickname = tuZhongUserListGet.get(j).getU_nickname(); // 用户昵称
                String u_head = ""; // 用户头像
                Integer act_isleave = tuZhongUserListGet.get(j).getAct_isleave();// 是否离队(1)或归队(2)
                String user_latitude = tuZhongUserListGet.get(j).getUser_latitude();
                String user_longitude = tuZhongUserListGet.get(j).getUser_longitude();
                double distance = tuZhongUserListGet.get(j).getDistance();
                String location_time = tuZhongUserListGet.get(j).getLocation_time();
                Integer isUsedByCurrentDevice = tuZhongUserListGet.get(j).getIsUsedByCurrentDevice();//判断该tps_id是否是当前手机使用,列表中代表自己,1为自己，整个数据表中只会存在一个为1,0为其他人.每台手机只会指定一个用户

                if (!TextUtils.isEmpty(user_latitude)) {
                    if (marker
                            .getPosition()
                            .toString()
                            .equals(new LatLng(Double
                                    .valueOf(user_latitude), Double
                                    .valueOf(user_longitude)).toString())) {// 两相等时，获取当前点击的item

                        // 1 生成一个TextView用户在地图中显示InfoWindow
                        TextView location = new TextView(
                                getActivity()
                                        .getApplicationContext());
                        location.setGravity(Gravity.CENTER_VERTICAL);
                        location.setTextColor(getResources()
                                .getColor(R.color.white));
                        location.setBackgroundResource(R.mipmap.btn_back_nor);
                        location.setPadding(30, 20, 30, 50);
                        // 自己的点
                        LatLng myLatLng = new LatLng(
                                Double.valueOf(marker.getPosition().latitude),
                                Double.valueOf(marker.getPosition().longitude));
                        // 与自己之间的距离
                        double licheng = DistanceUtil.getDistance(
                                p2, myLatLng);
                        //找到领队
                        SignUpUser s = LitepalUtil.getLeader();
                        // 算出与领队的距离
                        double lichengOther = 0;
                        // 算出当前点与领队的距离
                        if (!TextUtils.isEmpty(s.getUser_latitude())) {
                            lichengOther = DistanceUtil
                                    .getDistance(
                                            myLatLng,
                                            new LatLng(
                                                    Double.valueOf(s.getUser_latitude()),
                                                    Double.valueOf(s.getUser_longitude())));
                        }

                        String titleStr = "";
                        if (tps_type == CommonUtility.LINGDUI && isUsedByCurrentDevice == CommonUtility.SELF) {//当前手机为领队使用
                            if (isUsedByCurrentDevice == CommonUtility.SELF) {//自己
                                titleStr = u_nickname
                                        + "\n最近上传时间:"
                                        + location_time;
                            } else if (tps_type == CommonUtility.LINGDUI) {//领队
                            } else if (act_isleave == CommonUtility.LIDUI) {//离队的
                            } else {//其他队员
                                titleStr = u_nickname
                                        + "\n离我距离:"
                                        + ParseOject
                                        .StringToDouble(licheng)
                                        + "m"
                                        + "\n最近上传时间:"
                                        + location_time;
                            }
                        } else {//队员身份
                            if (isUsedByCurrentDevice == CommonUtility.SELF) {//自己
                                titleStr = u_nickname
                                        + "\n距离领队:"
                                        + "("
                                        + ParseOject
                                        .StringToDouble(lichengOther)
                                        + "m"
                                        + ")"
                                        + "\n最近上传时间:"
                                        + location_time;
                            } else if (tps_type == CommonUtility.LINGDUI) {//领队
                                titleStr = u_nickname
                                        + "\n离我距离:"
                                        + ParseOject
                                        .StringToDouble(licheng)
                                        + "m"
                                        + "\n最近上传时间:"
                                        + location_time;
                            } else if (act_isleave == CommonUtility.LIDUI) {//离队的
                            } else {//其他队员
                                titleStr = u_nickname
                                        + "\n离我距离:"
                                        + ParseOject
                                        .StringToDouble(licheng)
                                        + "m"
                                        + "\n距离领队:"
                                        + "("
                                        + ParseOject
                                        .StringToDouble(lichengOther)
                                        + "m"
                                        + ")"
                                        + "\n最近上传时间:"
                                        + location_time;
                            }
                        }
                        location.setText(titleStr);
                        // btn 变成 View 图片
                        BitmapDescriptor descriptor = BitmapDescriptorFactory
                                .fromView(location);
                        // 2 将marker所在的经纬度的信息转化成屏幕上的坐标
                        final LatLng ll = marker.getPosition();
                        Point p = mBaiduMap.getProjection()
                                .toScreenLocation(ll);
                        p.y -= 47;
                        LatLng llInfo = mBaiduMap.getProjection()
                                .fromScreenLocation(p);
                        // 3 为弹出的InfoWindow添加点击事件
                        /**
                         * 弹窗的点击事件： - InfoWindow 展示的bitmap position
                         * - InfoWindow 显示的地理位置 - InfoWindow Y 轴偏移量
                         * listener - InfoWindow 点击监听者 InfoWindow
                         * 点击的时候 消失。
                         */
                        InfoWindow mInfoWindow = new InfoWindow(
                                descriptor, llInfo, -60,
                                new OnInfoWindowClickListener() {

                                    public void onInfoWindowClick() {
                                        // 当用户点击 弹窗 触发：
                                        // 开启 POI 检索、 开启 路径规矩, 跳转界面！
                                        // 1 隐藏 弹窗！
                                        mBaiduMap.hideInfoWindow();
                                    }
                                });
                        // 显示InfoWindow
                        mBaiduMap.showInfoWindow(mInfoWindow);
                        return true;
                    }
                }
            }

            return false;
        }
    };


    /**
     * @param data
     * 获取需要展示的值 。拿到所有的数据之后再进行其他操作，这里需要一个值来判断是否接收完成
     */
// 队员
    private SignUpUser signUpUserGet = null;
    private List<SignUpUser> signUpUserListGet = null;

    private List<Integer> errorNumList = null;// 未同步成功的队员
    private List<byte[]> tempByte = null;

    // 从设备拿到经纬度等数据信息
    double longitude = 0;
    double latitude = 0;

    JSONArray arrayLocationInfo = null;
    String gps_start_time = null;

    /**
     * Dialog
     */
    public void showAlertDialog(String title) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setCancelable(false);// 点击对话框外部不关闭对话框
        builder.setMessage(title);
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private int showTrackMyInt = 0;
    private boolean showTrackMyboolean = true;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_jhgj:// 计划轨迹
                if (isChecked) {
                    // ll_ckgj.setVisibility(View.VISIBLE);
                    showTrackTemp();
                    App.getInstance().setJiHuaGuiJi(true);
                } else {
                    // ll_ckgj.setVisibility(View.GONE);
                    getNowTrack();
                    App.getInstance().setJiHuaGuiJi(false);
                }
                // 当已勾选了我的轨迹时，需显示
                if (App.getInstance().getMyGuiJi() && infosMy != null) {
                    showTrackMyInt = 0;
                    App.getInstance().setMyGuiJi(true);
                    // 获取数据库的数据
                    List<GpsInfo> dao = DataSupport.findAll(GpsInfo.class);
                    infosMy = dao;
                    while (showTrackMyboolean) {
                        if (showDuiyuan) {
                            showTrackMyInt++;
                            if (showTrackMyInt == 5
                                    || infosMy.size() == showTrackMyInt) {// 最多画5条轨迹
                                showTrackMyboolean = false;
                            }
                            showTrackMy();
                        }
                    }
                    showDuiyuan = true;
                    showTrackMyboolean = true;
                    showTrackMyInt = 0;
                }
                break;
            case R.id.tb_wdgj:// 我的轨迹

                if (isChecked) {// showDuiyuan
                    App.getInstance().setMyGuiJi(true);
                    // 获取数据库的数据
                    List<GpsInfo> dao = DataSupport.findAll(GpsInfo.class);
                    infosMy = dao;
                    while (showTrackMyboolean) {
                        if (showDuiyuan) {
                            showTrackMyInt++;
                            if (showTrackMyInt == 5
                                    || infosMy.size() == showTrackMyInt) {// 最多画5条轨迹
                                showTrackMyboolean = false;
                            }
                            showTrackMy();
                        }
                    }
                    showDuiyuan = true;
                    showTrackMyboolean = true;
                    showTrackMyInt = 0;
                } else {
                    App.getInstance().setMyGuiJi(false);
                    getNowTrack();
                }

                break;
            case R.id.tb_jtdt:// 交通图
                if (isChecked) {
                    mBaiduMap.setTrafficEnabled(true);
                } else {
                    mBaiduMap.setTrafficEnabled(false);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 获取当前状态的轨迹情况
     */
    public void getNowTrack() {
        // 地图记录状态
        switch ((Integer) SPUtils.get(getActivity(), "dt_status", 1)) {
            case WEIKAISHI:// 未开始状态
                if (mBaiduMap != null) {
                    mBaiduMap.clear();
                    // 关联地图中的默认值，应用退出时未停止画轨迹的情况
                    // SPUtils.put(getActivity(), "dt_status", 0);
                }
                break;
            case JILUZHONG:// 记录状态
                // 开启服务，记录轨迹到数据库
                showTrack();
                break;
            case ZANTING:// 暂停状态
                showTrack();
                break;
            default:
                break;
        }

    }

    private List<SignUpUser> tuZhongUserDaoSet = null;
    // 服务器返回提示信息
    private String msgString = null;
    private int State = 0;
    @SuppressLint("HandlerLeak")
    Handler handlerlistNet = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1:// TODO 需要修改这里，更新对应的经纬度

                    // 判断当前是手机定位优先还是设备定位优先，更新指定的经纬度
                case CommonUtility.SERVEROK3://
                    // 数据库中拿取数据
                    if (tuZhongUserListGet != null) {
                        tuZhongUserListGet.clear();
                    }
                    //这里要显示所有
                    tuZhongUserListGet = LitepalUtil.getAllUserByActisleaveDrderByDistance();
                    listadapter.notifyDataSetChanged();
                    break;
                case CommonUtility.SERVERERRORLOGIN:
                    showToastLogin();
                    App.getInstance().setAut("");
                    openActivity(LoginActivity.class);
                    break;
                case CommonUtility.SERVERERROR:
                    break;
                case CommonUtility.KONG:
                    break;
                case CommonUtility.SERVEROK2:
                    break;
                case CommonUtility.SERVEROK4:// 拿到所有的设备参数
                    try {
                        bGpsInterval = hwtxCommandjsonObject.getInt("bGpsInterval");
                        bHktAtInterval = hwtxCommandjsonObject
                                .getInt("bLostContactNum");
                        bLostContactNum = hwtxCommandjsonObject
                                .getInt("bLostContactNum");
                        wWarningDistance1 = hwtxCommandjsonObject
                                .getInt("wWarningDistance1");
                        wWarningDistance2 = hwtxCommandjsonObject
                                .getInt("wWarningDistance2");
                        wWarningDistance3 = hwtxCommandjsonObject
                                .getInt("wWarningDistance3");
                        bWarningBatteryPercent = hwtxCommandjsonObject
                                .getInt("bWarningBatteryPercent");
                    } catch (JSONException e) {
                        //
                        e.printStackTrace();
                    }
                    break;
                case CommonUtility.SERVEROK5:
                    wWarningDistance1 = 1000;
                    wWarningDistance2 = 2000;
                    wWarningDistance3 = 5000;
                    break;
                default:
                    break;
            }
        }

        ;
    };

    class MyListAdpter extends BaseAdapter {

        @Override
        public int getCount() {
            return tuZhongUserListGet != null ? tuZhongUserListGet.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_tuzhong, null);
                holder = new ViewHolder();
                holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
                holder.civ_icon = (CircleImageView) convertView
                        .findViewById(R.id.civ_icon);
                holder.tv_name = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.tv_juli = (TextView) convertView
                        .findViewById(R.id.tv_juli);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Integer tps_type = tuZhongUserListGet.get(position).getTps_type();// 人员类型：1普通队员，2领队
            Integer tps_id = tuZhongUserListGet.get(position).getTps_id();// 物理id
            String phonenum = tuZhongUserListGet.get(position).getPhonenum();// 队员手机号
            String strDeviceSN = tuZhongUserListGet.get(position).getStrDeviceSN();// 硬件sn号码
            String u_nickname = tuZhongUserListGet.get(position).getU_nickname(); // 用户昵称
            String u_head = ""; // 用户头像
            Integer act_isleave = tuZhongUserListGet.get(position).getAct_isleave();// 是否离队(1)或归队(2)
            String user_latitude = tuZhongUserListGet.get(position).getUser_latitude();
            String user_longitude = tuZhongUserListGet.get(position).getUser_longitude();
            double distance = tuZhongUserListGet.get(position).getDistance();
            String location_time = tuZhongUserListGet.get(position).getLocation_time();
            Integer isUsedByCurrentDevice = tuZhongUserListGet.get(position).getIsUsedByCurrentDevice();//判断该tps_id是否是当前手机使用,列表中代表自己,1为自己，整个数据表中只会存在一个为1,0为其他人.每台手机只会指定一个用户

            if (TextUtils.equals(user_latitude, "")
                    || TextUtils.equals(user_longitude, "")) {
                holder.tv_juli.setText("该队员尚未定位成功");
            } else {
                // 显示距离
                int licheng = (int) distance;
                if (licheng > wWarningDistance1 && wWarningDistance2 > licheng) {//
                    holder.tv_juli.setText(licheng + "m");
                    holder.tv_juli.setTextColor(getResources().getColor(
                            android.R.color.holo_red_light));
                } else if (licheng > wWarningDistance2
                        && licheng < wWarningDistance3) {
                    holder.tv_juli.setText(licheng + "m");
                    holder.tv_juli.setTextColor(getResources().getColor(
                            R.color.app_green));
                } else if (licheng > wWarningDistance3) {
                    // holder.tv_juli.setText(ParseOject
                    // .StringToDouble(licheng / 1000) + "km");
                    holder.tv_juli.setText("距离过大");
                    holder.tv_juli.setTextColor(getResources().getColor(
                            android.R.color.holo_blue_light));
                } else {
                    holder.tv_juli.setText(licheng
                            + "m");
                    holder.tv_juli.setTextColor(getResources().getColor(
                            android.R.color.black));
                }

            }
            holder.tv_name.setTextColor(getResources().getColor(
                    android.R.color.black));
            if (tps_type == CommonUtility.LINGDUI) {//领队
                holder.tv_name.setText(tuZhongUserListGet.get(position)
                        .getU_nickname() + "(领队)");
                holder.civ_icon.setImageResource(R.mipmap.dt_ld);
                if (isUsedByCurrentDevice == CommonUtility.SELF) {//自己
                    holder.tv_juli.setText("");
                    holder.tv_name.setText(tuZhongUserListGet.get(position)
                            .getU_nickname() + "(自己)");
                }
            } else if (isUsedByCurrentDevice == CommonUtility.SELF) {//自己
                holder.tv_name.setText(tuZhongUserListGet.get(position)
                        .getU_nickname() + "(自己)");
                holder.civ_icon.setImageResource(R.mipmap.dt_me);
                holder.tv_juli.setText("");
            } else if (act_isleave == CommonUtility.LIDUI) {//领队与自己是不存在离队的
                holder.tv_name.setText(tuZhongUserListGet.get(position)
                        .getU_nickname() + "(离队)");
                holder.tv_name.setTextColor(getResources().getColor(
                        android.R.color.holo_red_light));
                holder.civ_icon.setImageResource(R.mipmap.dt_dy);
            } else {
                holder.tv_name.setText(tuZhongUserListGet.get(position)
                        .getU_nickname());
                holder.civ_icon.setImageResource(R.mipmap.dt_dy);
            }
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer act_isleave = tuZhongUserListGet.get(position).getAct_isleave();// 是否离队(1)或归队(2)
                    String user_latitude = tuZhongUserListGet.get(position).getUser_latitude();
                    String user_longitude = tuZhongUserListGet.get(position).getUser_longitude();
                    if (act_isleave == CommonUtility.LIDUI) {//离队不可点击
                        showToast("该队员已离队");
                    } else if (TextUtils.equals(user_latitude, "")
                            || TextUtils.equals(user_longitude, "")) {
                        showToast("该队员未使用同行宝");
                    } else {
                        rsv.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // //
                                try {
                                    if (tuZhongUserListGet.get(position).getIsUsedByCurrentDevice() == CommonUtility.SELF) {//自己
                                        bdD = bdA;
                                    } else if (tuZhongUserListGet.get(position).getTps_type() == CommonUtility.LINGDUI) {//领队
                                        bdD = bdB;
                                    } else {
                                        bdD = bdC;
                                    }

                                } catch (Exception e) {
                                    //
                                    e.printStackTrace();
                                }
                                if (!tuZhongUserListGet.get(position)
                                        .getUser_latitude().equals("")
                                        && !tuZhongUserListGet.get(position)
                                        .getUser_longitude().equals("")
                                        && bdD != null) {
                                    overlayOptions = new MarkerOptions()
                                            .position(
                                                    new LatLng(
                                                            Double.valueOf(tuZhongUserListGet
                                                                    .get(position)
                                                                    .getUser_latitude()),
                                                            Double.valueOf(tuZhongUserListGet
                                                                    .get(position)
                                                                    .getUser_longitude())))
                                            .icon(bdD).draggable(false)
                                            .perspective(true);
                                    mBaiduMap.addOverlay(overlayOptions);
                                }
                            }

                        }).start();
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private LinearLayout ll;
            private CircleImageView civ_icon;
            private TextView tv_name, tv_juli;
        }

    }

    // 我的最近五条轨迹--------------------------------------------------------------------------------------
    private List<LatLng> resultPointsMy;
    private Marker markerMy = null;
    private OverlayOptions overlayOptionsMy = null;
    private JSONArray arrayMy = null;

    /**
     * 显示轨迹
     */
    private void showTrackMy() {
        showDuiyuan = false;
        resultPointsMy = new ArrayList<LatLng>();

        if (infosMy.size() != 0
                && infosMy.size() - showTrackMyInt < infosMy.size()) {
            if (!TextUtils.isEmpty(infosMy.get(infosMy.size() - showTrackMyInt)
                    .getLocationInfo())) {
                try {
                    arrayMy = new JSONArray(infosMy.get(
                            infosMy.size() - showTrackMyInt).getLocationInfo());
                    for (int i = 0; i < arrayMy.length(); i++) {
                        double myLongitude = arrayMy.getJSONObject(i)
                                .getDouble("longitude");
                        double myLatitude = arrayMy.getJSONObject(i).getDouble(
                                "latitude");
                        LatLng point = new LatLng(myLatitude, myLongitude);
                        resultPointsMy.add(point);
                        point = null;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // 数据库最后一个坐标的位置
        if (arrayMy != null) {

            if (arrayMy.length() >= 2) {
                MapStatusUpdate u = null;
                try {
                    u = MapStatusUpdateFactory.newLatLng(new LatLng(arrayMy
                            .getJSONObject(arrayMy.length() - 1).getDouble(
                                    "latitude"), arrayMy.getJSONObject(
                            arrayMy.length() - 1).getDouble("longitude")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mBaiduMap.setMapStatus(u);
                // 折线显示
                if (resultPointsMy.size() >= 2) {
                    OverlayOptions ooPolyline = new PolylineOptions().width(10)
                            .color(0xAAFF0000).points(resultPointsMy); //
                    mBaiduMap.addOverlay(ooPolyline);
                }
                Log.i("onPostExecute", "onPostExecute()");
                // 得到了所有信息之后才可点击显示队员按钮

            }
            // 显示出起点
            for (int i = 0; i < resultPointsMy.size(); i++) {
                if (i == 0) {
                    overlayOptionsMy = new MarkerOptions()
                            .position(resultPointsMy.get(i)).icon(bdStart)
                            .draggable(false).perspective(true);
                    //
                    markerMy = (Marker) (mBaiduMap.addOverlay(overlayOptionsMy));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", "测试");
                    markerMy.setExtraInfo(bundle);

                    mBaiduMap.addOverlay(overlayOptionsMy);
                }
                if (i == resultPointsMy.size() - 1) {
                    overlayOptionsMy = new MarkerOptions()
                            .position(resultPointsMy.get(i)).icon(bdEnd)
                            .draggable(false).perspective(true);
                    //
                    markerMy = (Marker) (mBaiduMap.addOverlay(overlayOptionsMy));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", "测试");
                    markerMy.setExtraInfo(bundle);

                    mBaiduMap.addOverlay(overlayOptionsMy);
                }
            }
        }
        showDuiyuan = true;
    }

    private void showAllPeople() {
        // 判断当前是手机定位优先还是设备定位优先，更新指定的经纬度
        //----------------------------------------------《队员APP不需要显示失联START》---------------------------------------------------------
//        for (int i = 0; i < indexGpsInfoArray.size(); i++) {
//            int tps_id = indexGpsInfoArray
//                    .get(i).getNewFlaguDevNumInGroupU().getDevNumInGroup();
//            SignUpUser s = LitepalUtil.getAllUserByTpsid(String.valueOf(tps_id));
//            String deviceSN = s.getStrDeviceSN();
//            String LingDuiNameSN = App.getInstance().getBleLingDuiName();
//            String DuiYuanNameSN = App.getInstance().getBleDuiYuanName();
//            //通过设备参数号找到对应的当前设备的队员
//            if ((TextUtils.equals(deviceSN, LingDuiNameSN))) {//领队------------------------------------------------
//
//            } else if ((TextUtils.equals(deviceSN, DuiYuanNameSN))) {//队员----------------------------------------------
//
//            }
//        }
        //----------------------------------------------《队员APP不需要显示失联END》---------------------------------------------------------
        // 获取失联信息表暂时放这里
        // getAlarmLostContactList();

        tv_tishi.setText("");
        // 失联队员列表
        listShilianbean = new ArrayList<Shilianbean>();
        // if (showDuiyuan) {// 先判断地图是否绘制完成
        // 算出与离其他队员最近的距离
        int lichengOther = 0;
        int lichengOtherTemp = 0;
        int muchUnLocal = 0;//多少个队员未定位
//        boolean xianshiBool = false;
        Shilianbean shilianbean = null;
        if (tuZhongUserListGet != null) {

            for (int i = 0; i < tuZhongUserListGet.size(); i++) {
//                if (tuZhongUserListGet.get(i).getIsUsedByCurrentDevice() == CommonUtility.SELF && tuZhongUserListGet.get(i).getTps_type() == CommonUtility.DUIYUAN) {//队员则不显示提示
//                    xianshiBool = false;
//                } else if (tuZhongUserListGet.get(i).getIsUsedByCurrentDevice() == CommonUtility.SELF && tuZhongUserListGet.get(i).getTps_type() == CommonUtility.LINGDUI) {
//                    xianshiBool = true;
//                }
                if (!TextUtils.isEmpty(tuZhongUserListGet.get(i)
                        .getUser_latitude())) {

                    lichengOther = (int) DistanceUtil.getDistance(
                            p2,
                            new LatLng(Double.valueOf(tuZhongUserListGet
                                    .get(i).getUser_latitude()), Double
                                    .valueOf(tuZhongUserListGet.get(i)
                                            .getUser_longitude())));
                    if (lichengOtherTemp == 0) {
                        // 最近的距离
                        lichengOtherTemp = lichengOther;
                        lichengOtherTempLong = lichengOther;
                    }
                    // 假设大于1000就报失联
                    if (lichengOther > wWarningDistance3) {
                        shilianbean = new Shilianbean();
                        lichengOtherTemp = lichengOther;
                        nameTempLong = tuZhongUserListGet.get(i)
                                .getU_nickname();
                        shilianbean
                                .setLichengOtherTempLong(lichengOtherTemp);
                        shilianbean.setNameTempLong(nameTempLong);
                        listShilianbean.add(shilianbean);
                    }

                } else {
                    muchUnLocal++;
                }
            }
            //得到最大的那个
            double doubleL = 0;
            int positionL = 0;
            for (int i = 0; i < listShilianbean.size(); i++) {
                double l = listShilianbean
                        .get(i).getLichengOtherTempLong();
                if (doubleL < l) {
                    doubleL = l;
                    positionL = i;
                }

            }
            // 展示所有失联人员
            if (listShilianbean.size() > 0) {//当有队员处于失联，与目前失联提示一样，多个队员失联的时候显示最严重的。
                tv_tishi.setText("\n【失联】\t"
                        + listShilianbean.get(positionL).getNameTempLong()
                        + " \t距离:"
                        + ParseOject.StringToDouble(listShilianbean
                        .get(positionL).getLichengOtherTempLong())
                        + "m\n");
            } else if (muchUnLocal != 0) {//领队刚刚切换过来，如果还没有获得过队员信息，则提示“x个队员尚未定位”；
                tv_tishi.setText(muchUnLocal + "个队员尚未定位");
            } else {//只有获得过所有队员的位置信息了，则显示距离领队最远的队员：“x ：300m”；
                if (tuZhongUserListGet.size() > 0) {
                    tv_tishi.setText("距离领队最远的队员：" + tuZhongUserListGet.get(0).getU_nickname() + ":" + tuZhongUserListGet.get(0).getDistance());
                } else {
                    tv_tishi.setText("状态良好");
                }
            }
            if (tv_tishi.getVisibility() == View.GONE) {
                iv_xianshisuoyouren
                        .setBackgroundResource(R.mipmap.select_dt_31);
                showAllPeople = true;
                SPUtils.put(getActivity(), "showAllPeople", true);

                tv_tishi.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < tuZhongUserListGet.size(); i++) {
                            try {
                                if (tuZhongUserListGet.get(i).getTps_type() == CommonUtility.LINGDUI) {//领队
                                    bdD = bdB;
                                } else if (tuZhongUserListGet.get(i).getIsUsedByCurrentDevice() == CommonUtility.SELF) {//自己
                                    bdD = bdA;
                                } else {
                                    bdD = bdC;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!tuZhongUserListGet.get(i)
                                    .getUser_latitude().equals("")
                                    && !tuZhongUserListGet.get(i)
                                    .getUser_longitude().equals("")
                                    && bdD != null) {//
                                // &&
                                // !TextUtils.equals(tuZhongUserListGet
                                // .get(i).getUserid(), App
                                // .getInstance().getUserid())
                                overlayOptions = new MarkerOptions()
                                        .position(
                                                new LatLng(
                                                        Double.valueOf(tuZhongUserListGet
                                                                .get(i)
                                                                .getUser_latitude()),
                                                        Double.valueOf(tuZhongUserListGet
                                                                .get(i)
                                                                .getUser_longitude())))
                                        .icon(bdD).draggable(false)
                                        .perspective(true);
                                if (overlayOptions != null
                                        && mBaiduMap != null) {
                                    mBaiduMap.addOverlay(overlayOptions);
                                }

                            }
                        }
                    }
                }).start();

            } else {
                showAllPeople = false;
                SPUtils.put(getActivity(), "showAllPeople", false);
                // 清除覆盖物之后再重新绘制轨迹
                // 地图记录状态
                switch ((Integer) SPUtils
                        .get(getActivity(), "dt_status", 1)) {
                    case WEIKAISHI:// 未开始状态
                        if (mBaiduMap != null) {
                            mBaiduMap.clear();
                            // 关联地图中的默认值，应用退出时未停止画轨迹的情况
                            // SPUtils.put(getActivity(), "dt_status", 0);
                        }
                        break;
                    case JILUZHONG:// 记录状态
                        // 开启服务，记录轨迹到数据库
                        showTrack();
                        break;
                    case ZANTING:// 暂停状态
                        showTrack();
                        break;
                    default:
                        if (mBaiduMap != null) {
                            mBaiduMap.clear();
                            // 关联地图中的默认值，应用退出时未停止画轨迹的情况
                            // SPUtils.put(getActivity(), "dt_status", 0);
                        }
                        break;
                }
                tv_tishi.setVisibility(View.GONE);
                iv_xianshisuoyouren
                        .setBackgroundResource(R.drawable.select_dt_3);
            }
            // } else {
            // showToast("正在加载数据，请稍等");
            // }

        }

    }

    private void allPeople() {
        iv_xianshisuoyouren
                .setBackgroundResource(R.mipmap.select_dt_31);
        // 获取失联信息表暂时放这里
        // getAlarmLostContactList();
        tuZhongUserListGet = LitepalUtil.getAllUserByActisleave();
        tv_tishi.setText("");
        // 失联队员列表
        listShilianbean = new ArrayList<Shilianbean>();
        // if (showDuiyuan) {// 先判断地图是否绘制完成
        // 算出与离其他队员最近的距离
        int lichengOther = 0;
        int lichengOtherTemp = 0;
        int muchUnLocal = 0;//多少个队员未定位
        Shilianbean shilianbean = null;
        if (tuZhongUserListGet != null) {

            for (int i = 0; i < tuZhongUserListGet.size(); i++) {

                if (!TextUtils.isEmpty(tuZhongUserListGet.get(i)
                        .getUser_latitude())) {
                    lichengOther = (int) DistanceUtil.getDistance(
                            p2,
                            new LatLng(Double.valueOf(tuZhongUserListGet
                                    .get(i).getUser_latitude()), Double
                                    .valueOf(tuZhongUserListGet.get(i)
                                            .getUser_longitude())));
                    if (lichengOtherTemp == 0) {
                        // 最近的距离
                        lichengOtherTemp = lichengOther;
                        lichengOtherTempLong = lichengOther;
                    }
                    // 假设大于1000就报失联
                    if (lichengOther > wWarningDistance3) {
                        shilianbean = new Shilianbean();
                        lichengOtherTemp = lichengOther;
                        nameTempLong = tuZhongUserListGet.get(i)
                                .getU_nickname();
                        shilianbean
                                .setLichengOtherTempLong(lichengOtherTemp);
                        shilianbean.setNameTempLong(nameTempLong);
                        listShilianbean.add(shilianbean);
                    }

                } else {
                    muchUnLocal++;
                }
            }
            //得到最大的那个
            double doubleL = 0;
            int positionL = 0;
            for (int i = 0; i < listShilianbean.size(); i++) {
                double l = listShilianbean
                        .get(i).getLichengOtherTempLong();
                if (doubleL < l) {
                    doubleL = l;
                    positionL = i;
                }

            }
            // 展示所有失联人员
            if (listShilianbean.size() > 0) {//当有队员处于失联，与目前失联提示一样，多个队员失联的时候显示最严重的。
                tv_tishi.setText("\n【失联】\t"
                        + listShilianbean.get(positionL).getNameTempLong()
                        + " \t距离:"
                        + ParseOject.StringToDouble(listShilianbean
                        .get(positionL).getLichengOtherTempLong())
                        + "m\n");
            } else if (muchUnLocal != 0) {//领队刚刚切换过来，如果还没有获得过队员信息，则提示“x个队员尚未定位”；
                tv_tishi.setText(muchUnLocal + "个队员尚未定位");
            } else {//只有获得过所有队员的位置信息了，则显示距离领队最远的队员：“x ：300m”；
                if (tuZhongUserListGet.size() > 0) {
                    tv_tishi.setText("距离领队最远的队员：" + tuZhongUserListGet.get(0).getU_nickname() + ":" + tuZhongUserListGet.get(0).getDistance());
                } else {
                    tv_tishi.setText("状态良好");
                }
            }

            tv_tishi.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < tuZhongUserListGet.size(); i++) {
                        try {
                            if (tuZhongUserListGet.get(i).getTps_type() == CommonUtility.LINGDUI) {//领队
                                bdD = bdB;
                            } else if (tuZhongUserListGet.get(i).getIsUsedByCurrentDevice() == CommonUtility.SELF) {//自己
                                bdD = bdA;
                            } else {
                                bdD = bdC;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!tuZhongUserListGet.get(i)
                                .getUser_latitude().equals("")
                                && !tuZhongUserListGet.get(i)
                                .getUser_longitude().equals("")
                                && bdD != null) {// 不显示自己的
                            // &&
                            // !TextUtils.equals(tuZhongUserListGet
                            // .get(i).getUserid(), App
                            // .getInstance().getUserid())
                            overlayOptions = new MarkerOptions()
                                    .position(
                                            new LatLng(
                                                    Double.valueOf(tuZhongUserListGet
                                                            .get(i)
                                                            .getUser_latitude()),
                                                    Double.valueOf(tuZhongUserListGet
                                                            .get(i)
                                                            .getUser_longitude())))
                                    .icon(bdD).draggable(false)
                                    .perspective(true);
                            if (overlayOptions != null
                                    && mBaiduMap != null) {
                                mBaiduMap.addOverlay(overlayOptions);
                            }

                        }
                    }
                }
            }).start();
        }

        // 地图记录状态
//        switch ((Integer) SPUtils
//                .get(getActivity(), "dt_status", 1)) {
//            case WEIKAISHI:// 未开始状态
//                if (mBaiduMap != null) {
//                    mBaiduMap.clear();
//                    // 关联地图中的默认值，应用退出时未停止画轨迹的情况
//                    // SPUtils.put(getActivity(), "dt_status", 0);
//                }
//                break;
//            case JILUZHONG:// 记录状态
//                // 开启服务，记录轨迹到数据库
//                showTrack();
//                break;
//            case ZANTING:// 暂停状态
//                showTrack();
//                break;
//            default:
//                if (mBaiduMap != null) {
//                    mBaiduMap.clear();
//                    // 关联地图中的默认值，应用退出时未停止画轨迹的情况
//                    // SPUtils.put(getActivity(), "dt_status", 0);
//                }
//                break;
//        }
    }

    //获取到的gps数据
    ArrayList<HwtxDataGrpGpsInfoItem> indexGpsInfoArray = null;

    @Subscribe
    public void onEventMainThread(GpsBean event) {
        if (mBaiduMap != null) {
            mBaiduMap.clear();
            // 关联地图中的默认值，应用退出时未停止画轨迹的情况
            // SPUtils.put(getActivity(), "dt_status", 0);
        }

        this.indexGpsInfoArray = event.getIndexGpsInfoArray();
        String gpsTime = event.getGpsTime();
        double gpsLatitude = event.getGpsLatitude();
        double gpsLongitude = event.getGpsLongitude();
        App.getInstance().setGpsLatitude(gpsLatitude);
        App.getInstance().setGpsLongitude(gpsLongitude);
        //拿到经纬度之后进行地图的绘制
        int latitudeInt = (int) gpsLatitude;
        int longitudeInt = (int) gpsLongitude;
        if (latitudeInt != 0 && longitudeInt != 0) {//当拿到的gps数据有效的时候还是拿当前的
            MyLocationData locData = new MyLocationData.Builder()
                    // // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(gpsLatitude)
                    .longitude(gpsLongitude).build();
            mBaiduMap.setMyLocationData(locData);
            p2 = new LatLng(gpsLatitude, gpsLongitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(p2);
            mBaiduMap.animateMapStatus(u);
        }

        gps_start_time = (String) SPUtils.get(getActivity(),
                "gps_start_time", "");
        mGpSinfoDao = DataSupport.findAll(GpsInfo.class);
        // 根据开始时间来查gps数据
        mGpSinfoDao = DataSupport.where("start_time=?", gps_start_time).find(GpsInfo.class);
        if (mGpSinfoDao.size() > 0) {
            try {
                if (mGpSinfoDao != null) {
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
            // Receive Location
            if (longitude == gpsLongitude || latitude == gpsLatitude) {
                // 只要有一个参数相同时不保存
            } else {
                longitude = gpsLongitude;
                latitude = gpsLatitude;
                info = new GpsInfo();
                //当坐标不为0时保存
                int longitudeTmp = (int) longitude;
                if (longitudeTmp != 0) {
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
            }
        }

        /**-------------------------《GPS数据测试》--------------------------*/
//        if ((Boolean) SPUtils.get(getActivity(), "showAllPeople", false)) {// 是否需要将所有队员显示
//
//        }else{
//
//        }
        // 配置表更新标志
        boolean hasNewConfig = event.isHasNewConfig();
        //大表2更新标志
        boolean hasNewBt = event.isHasNewBt();
        if (hasNewBt && hasNewConfig) {//都有更新
            showTipDialog("队员表及配置表有更新");
            status = 1;
        } else if (hasNewConfig) {//配置表有更新
            showTipDialog("配置表有更新");
            status = 2;
        } else if (hasNewBt) {//队员表有更新
            showTipDialog("队员表有更新");
            status = 3;
        }
        updataAllpeople();
        if ((Boolean) SPUtils.get(getActivity(), "showAllPeople", false)) {// 是否需要将所有队员显示
            allPeople();
        }
//        showAllPeople();
        App.getInstance().setTb_phonelocation(false);
    }

    public void updataAllpeople() {
        //查找所有未离队的队员
        tuZhongUserListGet = LitepalUtil.getAllUserByActisleave();
        if (App.getInstance().isTb_phonelocation()) {// 开启状态
        } else {// GPS关闭状态
            // getGPSDataTable();
            // // 当前队员的组id
            if (indexGpsInfoArray != null && tuZhongUserListGet != null
                    && tuZhongUserListGet.size() != 0) {
                for (int i = 0; i < indexGpsInfoArray.size(); i++) {
                    double gpsLatitude = 0;
                    double gpsLongitude = 0;
                    int tps_id = indexGpsInfoArray
                            .get(i).getNewFlaguDevNumInGroupU().getDevNumInGroup();
                    //离队标志
                    boolean hasLeave = indexGpsInfoArray
                            .get(i).getNewFlaguDevNumInGroupU().getuHasLeaveGroup();
                    // 配置表更新标志
                    boolean hasNewConfig = indexGpsInfoArray
                            .get(i).getNewFlaguDevNumInGroupU().getuHasNewConfig();
                    //大表2更新标志
                    boolean hasNewBt = indexGpsInfoArray
                            .get(i).getNewFlaguDevNumInGroupU().getuHasNewBtApp2();
//                    if (hasNewBt && hasNewConfig) {//都有更新
//                        showTipDialog("队员表及配置表有更新");
//                        status = 1;
//                    } else if (hasNewConfig) {//配置表有更新
//                        showTipDialog("配置表有更新");
//                        status = 2;
//                    } else if (hasNewBt) {//队员表有更新
//                        showTipDialog("队员表有更新");
//                        status = 3;
//                    }


                    //根据硬件数据中查到的组id找到对应的队员
                    SignUpUser siU = LitepalUtil.getAllUserByTpsid(String.valueOf(tps_id));
                    // 拿到该队员的自增id，用来更新第i个队员信息
//                    long uidStr = siU.getId();
                    HwtxDataGpsInfoDataComp gpsInfoDataComp = indexGpsInfoArray
                            .get(i)
                            .getGpsInfoDataComp();

                    byte[] byt0 = {0};
                    byte[] byt1 = {1};
                    if (mMapView == null)
                        return;
                    byte[] testbyte = gpsInfoDataComp.toBytes();
                    byte[] gpsMisc = gpsInfoDataComp.getGpsMiscRaw();
                    byte[] gpsTimebyte = gpsInfoDataComp
                            .getGpsTimeRaw();
                    byte[] gpsLatitudebyte = gpsInfoDataComp
                            .getGpsLatitudeRaw();
                    byte[] gpsLongitudebyte = gpsInfoDataComp
                            .getGpsLongitudeRaw();
                    //判断是否有效
                    byte[] valid = HwtxCommandUtility
                            .extractBitsFromBytes(gpsMisc, 1,
                                    1);  //判断是否有效
                    //秒
                    byte[] Second = HwtxCommandUtility
                            .extractBitsFromBytes(gpsMisc, 2,
                                    6);
                    String validStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(valid));
                    String secondStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(Second));
                    // unsigned Year : 12; //00~2xxx.(max4095)
                    // unsigned Month : 4; //01~12
                    // unsigned Day : 5; //01~31
                    // unsigned Hour : 5; //00~23
                    // unsigned Minute : 6; //00~59

                    // unsigned Sign:1 ; //符号位
                    // unsigned Integral : 10; //整数部分: 0~511
                    // unsigned Decimal : 21; //小数部分: 0~2097152

                    // 时间
                    byte[] gpsTimeYear = HwtxCommandUtility
                            .extractBitsFromBytes(gpsTimebyte, 0,
                                    12);

                    byte[] gpsTimeMonth = HwtxCommandUtility
                            .extractBitsFromBytes(gpsTimebyte, 12,
                                    4);
                    byte[] gpsTimeDay = HwtxCommandUtility
                            .extractBitsFromBytes(gpsTimebyte, 16,
                                    5);
                    byte[] gpsTimeHour = HwtxCommandUtility
                            .extractBitsFromBytes(gpsTimebyte, 21,
                                    5);
                    byte[] gpsTimeMinute = HwtxCommandUtility
                            .extractBitsFromBytes(gpsTimebyte, 26,
                                    6);

                    String gpsTimeYearStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsTimeYear));
                    String gpsTimeMonthStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsTimeMonth));
                    String gpsTimeDayStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsTimeDay));
                    String gpsTimeHourStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsTimeHour) + 8);
                    String gpsTimeMinuteStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsTimeMinute));
                    String gpsTime = gpsTimeYearStr + "年"
                            + gpsTimeMonthStr + "月" + gpsTimeDayStr
                            + "日" + gpsTimeHourStr + "点"
                            + gpsTimeMinuteStr + "分" + secondStr + "秒";
                    // unsigned Sign:1 ; //符号位
                    // unsigned Integral : 10; //整数部分: 0~511
                    // unsigned Decimal : 21; //小数部分: 0~2097152
                    // gpsLatitude
                    byte[] gpsLatitudeSign = HwtxCommandUtility
                            .extractBitsFromBytes(gpsLatitudebyte,
                                    0, 1);
                    byte[] gpsLatitudeIntegral = HwtxCommandUtility
                            .extractBitsFromBytes(gpsLatitudebyte,
                                    1, 10);
                    byte[] gpsLatitudeDecimal = HwtxCommandUtility
                            .extractBitsFromBytes(gpsLatitudebyte,
                                    11, 21);

                    String gpsLatitudeDecimalStr = String
                            .valueOf(String.valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsLatitudeDecimal)));
                    switch (gpsLatitudeDecimalStr.length()) {
                        case 5:
                            gpsLatitudeDecimalStr = "0"
                                    + gpsLatitudeDecimalStr;
                            break;
                        case 4:
                            gpsLatitudeDecimalStr = "00"
                                    + gpsLatitudeDecimalStr;
                            break;
                        case 3:
                            gpsLatitudeDecimalStr = "000"
                                    + gpsLatitudeDecimalStr;
                            break;
                        case 2:
                            gpsLatitudeDecimalStr = "0000"
                                    + gpsLatitudeDecimalStr;
                            break;
                        case 1:
                            gpsLatitudeDecimalStr = "00000"
                                    + gpsLatitudeDecimalStr;
                            break;

                        default:
                            break;
                    }
                    double gpsLatitudeTemp = Double
                            .parseDouble(String
                                    .valueOf(
                                            HwtxCommandUtility
                                                    .bytesToInt32(gpsLatitudeIntegral))
                                    .concat(".")
                                    .concat(gpsLatitudeDecimalStr));
                    if (Arrays.equals(gpsLatitudeSign, byt0)) {// 正，否则负
                        gpsLatitude = gpsLatitudeTemp;
                    } else {
                        gpsLatitude = -gpsLatitudeTemp;
                    }

                    // gpsLongitude
                    byte[] gpsLongitudeSign = HwtxCommandUtility
                            .extractBitsFromBytes(gpsLongitudebyte,
                                    0, 1);
                    byte[] gpsLongitudeIntegral = HwtxCommandUtility
                            .extractBitsFromBytes(gpsLongitudebyte,
                                    1, 10);
                    byte[] gpsLongitudeDecimal = HwtxCommandUtility
                            .extractBitsFromBytes(gpsLongitudebyte,
                                    11, 21);
                    String gpsLongitudeIntegralStr = String
                            .valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsLongitudeIntegral));
                    String LongitudeDecimalStr = String
                            .valueOf(String.valueOf(HwtxCommandUtility
                                    .bytesToInt32(gpsLongitudeDecimal)));
                    switch (LongitudeDecimalStr.length()) {
                        case 5:
                            LongitudeDecimalStr = "0"
                                    + LongitudeDecimalStr;
                            break;
                        case 4:
                            LongitudeDecimalStr = "00"
                                    + LongitudeDecimalStr;
                            break;
                        case 3:
                            LongitudeDecimalStr = "000"
                                    + LongitudeDecimalStr;
                            break;
                        case 2:
                            LongitudeDecimalStr = "0000"
                                    + LongitudeDecimalStr;
                            break;
                        case 1:
                            LongitudeDecimalStr = "00000"
                                    + LongitudeDecimalStr;
                            break;

                        default:
                            break;
                    }
                    double gpsLongitudeTemp = Double
                            .parseDouble(gpsLongitudeIntegralStr
                                    .concat(".").concat(
                                            LongitudeDecimalStr));

                    if (Arrays.equals(gpsLongitudeSign, byt0)) {// 正，否则负
                        gpsLongitude = gpsLongitudeTemp;
                    } else {
                        gpsLongitude = -gpsLongitudeTemp;
                    }
                    // gpsTime
                    System.out.println("gpsTime:" + gpsTime
                            + "\ngpsLatitude:" + gpsLatitude
                            + "\ngpsLongitude:" + gpsLongitude);
                    try {
                        LatLng desLatLng = CommonUtility.convertGps(gpsLatitude, gpsLongitude);
                        gpsLatitude = desLatLng.latitude;
                        gpsLongitude = desLatLng.longitude;

                        // 距离
                        double distance = DistanceUtil
                                .getDistance(p2, new LatLng(
                                        (gpsLatitude), gpsLongitude));
                        if (TextUtils.equals(validStr, "1")) {//gps值有效

                            SignUpUser s = new SignUpUser();
                            s.setDistance((int) distance);
                            s.setUser_latitude(String.valueOf(gpsLatitude));
                            s.setUser_longitude(String.valueOf(gpsLongitude));
                            s.setLocation_time(String.valueOf(gpsTime));

                            if (hasLeave && siU.getTps_type() != CommonUtility.LINGDUI) {
                                s.setAct_isleave(CommonUtility.LIDUI);
                            } else {
                                s.setAct_isleave(CommonUtility.GUIDUI);
                            }
                            s.updateAll("tps_id = ?", String.valueOf(tps_id));
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    /**
     * 获取设备参数表
     */
    @Subscribe
    public void onEventMainThread(ActivitySetBean event) {
        Integer bGpsInterval = event.getbGpsInterval();
        Integer bHktAtInterval = event.getbHktAtInterval();
        Integer bLostContactNum = event.getbLostContactNum();
        Integer wWarningDistance1 = event.getwWarningDistance1();
        Integer wWarningDistance2 = event.getwWarningDistance2();
        Integer wWarningDistance3 = event.getwWarningDistance3();
        Integer bWarningBatteryPercent = event.getbWarningBatteryPercent();
        //
        SPUtils.put(getActivity(), "bGpsInterval", bGpsInterval);
        SPUtils.put(getActivity(), "bHktAtInterval", bHktAtInterval);
        SPUtils.put(getActivity(), "bLostContactNum",
                bLostContactNum);
        SPUtils.put(getActivity(), "wWarningDistance1",
                wWarningDistance1);
        SPUtils.put(getActivity(), "wWarningDistance2",
                wWarningDistance2);
        SPUtils.put(getActivity(), "wWarningDistance3",
                wWarningDistance3);
        SPUtils.put(getActivity(), "bWarningBatteryPercent",
                bWarningBatteryPercent);
        switch (status) {
            case 1:
                //下一步是获取队员信息表
                BleCommon.getInstance().getGroupInfoAllBtApp();
                break;
            case 2:
                mSVProgressHUD.dismiss();
                showTipSuccessDialog("获取设备参数表成功");
                break;
            case 3:
                mSVProgressHUD.dismiss();
                break;
        }

    }

    /**
     * 获取队员信息表
     */
    @Subscribe
    public void onEventMainThread(GetMemberInfoBean event) {
        //告诉用户获取所有数据完毕
        mSVProgressHUD.dismiss();
        switch (status) {
            case 1:
                showTipSuccessDialog("获取队员信息表和设备参数表成功");
                break;
            case 2:
                break;
            case 3:
                showTipSuccessDialog("获取队员信息表成功");
                break;
        }

    }

    private boolean isConnect = false;//是否关联成功
    private int connectTimes = 0;//重新关联次数

    /**
     * 提示设备已断开连接
     */
    @Subscribe
    public void onEventMainThread(ConnectBean event) {
        boolean mConnected = event.ismConnected();//是否关联成功
        mSVProgressHUD.dismiss();
        if (mConnected) {//关联成功
            isConnect = true;

            showTipSuccessDialog("设备已重新关联成功！");

        } else {//关联失败
            if (connectTimes == 2) {//自动重连两次，不成功就取消
                connectTimes = 0;
                isConnect = true;//防止10秒钟后天出关联对话框
                if (getActivity() != null) {
                    showTipSuccessDialog("自动重新关联失败，请手动关联");
                }
                cancleContact();

            } else if (connectTimes == 0) {
                connectTimes++;
                isConnect = false;
                //断开连接后自动重连
                showToast("设备已断开");
                //cancleContact();
                initBluetoothConnet();
                mSVProgressHUD.showWithStatus("正在尝试重新关联中..");
                new Handler().postDelayed(new Runnable() {//10秒钟关联失败则提示重新关联失败

                    @Override
                    public void run() {
                        if (isConnect == false) {
                            mSVProgressHUD.dismiss();
                            if (getActivity() != null) {
                                showTipSuccessDialog("自动重新关联失败，请手动关联");
                            }
                            cancleContact();

                        }
                    }
                }, 10000);
            } else {
                connectTimes++;
                isConnect = false;
                //断开连接后自动重连
                showToast("设备已断开");
                //cancleContact();
                initBluetoothConnet();
                mSVProgressHUD.showWithStatus("正在尝试重新关联中..");
            }
            mDeviceAddress = App.getInstance().getBleDuiYuanAddress();
            if (TextUtils.isEmpty(mDeviceAddress)) {
                mDeviceAddress = App.getInstance().getBleLingDuiDuiAddress();
            }
        }
    }

    /**
     * 提示设备已断开连接
     */
    @Subscribe
    public void onEventMainThread(LengthZero event) {
        mSVProgressHUD.dismiss();
        switch (event.getWhitchInt()) {//1为gps数据表为空，2为队员信息表为空
            case 1:
                showTipSuccessDialog("gps数据表为空");
                break;
            case 2:
                showTipSuccessDialog("队员信息表为空");
                break;
        }
    }

    /**
     * 初始化蓝牙相关
     */
    private void initBluetoothConnet() {
        if (!TextUtils.isEmpty(mDeviceAddress)) {
            //STEP 1
            // 蓝牙相关
            BleCommon.getInstance().setCharacteristic(mDeviceAddress);
            // 广播接收器
            getActivity().registerReceiver(BleCommon.getInstance().mGattUpdateReceiver,
                    BleCommon.getInstance().makeGattUpdateIntentFilter());
            Intent gattServiceIntent = new Intent(getActivity(),
                    BluetoothLeService.class);
            getActivity().bindService(gattServiceIntent, BleCommon.getInstance().mServiceConnection,
                    Context.BIND_AUTO_CREATE);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (isConnect == false) {
                        //STEP 2
                        try {
                            getActivity().unregisterReceiver(BleCommon.getInstance().mGattUpdateReceiver);
                            getActivity().unbindService(BleCommon.getInstance().mServiceConnection);
                            if (BleCommon.getInstance().mBluetoothLeService != null) {
                                BleCommon.getInstance().mBluetoothLeService.disconnect();
                                BleCommon.getInstance().mBluetoothLeService.close();
                                BleCommon.getInstance().mBluetoothLeService = null;
                            }
                        } catch (Exception e) {
                            //需要完全断开连接才能再次关联
                        }
                        //STEP 3
                        // 蓝牙相关
                        BleCommon.getInstance().setCharacteristic(mDeviceAddress);
                        // 广播接收器
                        getActivity().registerReceiver(BleCommon.getInstance().mGattUpdateReceiver,
                                BleCommon.getInstance().makeGattUpdateIntentFilter());
                        Intent gattServiceIntentAgin = new Intent(getActivity(),
                                BluetoothLeService.class);
                        getActivity().bindService(gattServiceIntentAgin, BleCommon.getInstance().mServiceConnection,
                                Context.BIND_AUTO_CREATE);
                    }

                }
            }, 6000);

        }
    }

    // ------------------------------------end------------------------

    /**
     * --------------------------------------------------《GPS 数据测试》-----------------------------------------------------------
     */
    //从assets 文件夹中获取文件并读取数据
    public byte[] getFromAssets(String fileName) {
        byte[] buffer = null;
        try {
            InputStream in = getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    private AlertView tiplertView;//

    //提示框
    public void showTipDialog(String str) {

        if (tiplertView != null) {
            tiplertView = null;
        }
        if (getActivity() != null) {
            tiplertView = new AlertView("温馨提示", str, null, new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    tiplertView.show();
                }
            }, 1000);
        }
    }

    private AlertView TipSuccesslertView;//

    //提示框
    public void showTipSuccessDialog(String str) {

        if (TipSuccesslertView != null) {
            TipSuccesslertView = null;
        }
        if (getActivity() != null) {

            TipSuccesslertView = new AlertView("温馨提示", str, null, new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    TipSuccesslertView.show();
                }
            }, 1000);
        }
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == tiplertView && position != AlertView.CANCELPOSITION) {
            mSVProgressHUD.showWithStatus("正在获取数据...");
            switch (status) {
                case 1:
                    BleCommon.getInstance().getSetingGetTable();
                    break;
                case 2:
                    BleCommon.getInstance().getSetingGetTable();
                    break;
                case 3:
                    BleCommon.getInstance().getGroupInfoAllBtApp();
                    break;
                default:
                    break;
            }
        }
    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {

            mSVProgressHUD.dismiss();
        }
        return true;
    }
}
