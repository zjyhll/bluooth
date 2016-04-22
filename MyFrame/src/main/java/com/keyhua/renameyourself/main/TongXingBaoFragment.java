package com.keyhua.renameyourself.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.eventBusBean.ActivitySetBean;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.GetMemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.LengthZero;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.main.le.BluetoothLeService;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.SPUtils;
import com.keyhua.renameyourself.view.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.ptr.PtrFrameLayout;

public class TongXingBaoFragment extends BaseFragment implements OnItemClickListener, OnDismissListener {
    private MyListView lv = null;
    private TextView tv_tocontact_search = null;// 搜索按钮
    // pop
    private View parentView = null;
    private PopupWindow popContact = null;
    private LinearLayout ll_popup = null;
    private RelativeLayout parent = null;// 半透明背景色
    private Button btn_pop_ok = null;// 选择
    private Button btn_pop_cancle = null;// 取消

    private LeDeviceListAdapter mLeDeviceListAdapter;
    /**
     * 搜索BLE终端
     */
    private BluetoothAdapter mBluetoothAdapter;

    private boolean mScanning;
    private Handler mHandler;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 5000;
    // 判断由队员跳入、领队
    private boolean fromTuZhong = false;
    // 判断是否是关联硬件设备
    private boolean guanlianshebei = false;
    private View view = null;
    protected TextView toolbar_tv_right_cancle = null;// 右边文字
    private AlertView tblertView;//是否同步弹出框
    private static SVProgressHUD mSVProgressHUD;
    private AlertView tiplertView;//是否添加入组成功
    private TextView tv_glldj = null;//关联领队机
    private TextView tv_dk = null;//断开领队机
    private TextView tv_gl = null;//关联领队机
    private String mDeviceAddress = null;// 蓝牙地址
    private String mDeviceName = null;// 蓝牙名
    private boolean isConnect = false;//是否关联成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.leftfrag_myguardianfrg_tocontact, container, false);
        return parentView;
    }

    @Override
    protected void onInitData() {
        EventBus.getDefault().register(this);
        mSVProgressHUD = new SVProgressHUD(getActivity());
        tblertView = new AlertView("温馨提示", "是否获取队员信息表和设备参数表", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);

        toolbar_tv_right_cancle = (TextView) getActivity().findViewById(R.id.toolbar_tv_right_cancle);
        initPopwindow();
        initBluetooth();
        //
        lv = (MyListView) getActivity().findViewById(R.id.lv);
        tv_tocontact_search = (TextView) getActivity().findViewById(R.id.tv_tocontact_search);
        tv_dk = (TextView) getActivity().findViewById(R.id.tv_dk);
        tv_gl = (TextView) getActivity().findViewById(R.id.tv_gl);
        tv_glldj = (TextView) getActivity().findViewById(R.id.tv_glldj);
        //队员机设备状态
        mDeviceName = App.getInstance().getBleDuiYuanName();
        mDeviceAddress = App.getInstance().getBleDuiYuanAddress();
        // 与设备交互
        if (!TextUtils.isEmpty(mDeviceName)) {
            tv_dk.setVisibility(View.VISIBLE);
            tv_gl.setVisibility(View.GONE);
            tv_glldj.setText("队员机:" + mDeviceName);
//            initBluetoothConnet();
        } else {
            tv_gl.setVisibility(View.VISIBLE);
            tv_dk.setVisibility(View.GONE);
            tv_glldj.setText("队员机");
        }
    }

    protected void initPopwindow() {//
        popContact = new PopupWindow(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.pop_sos, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        popContact.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popContact.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popContact.setBackgroundDrawable(new BitmapDrawable());
        popContact.setFocusable(true);
        popContact.setOutsideTouchable(true);
        popContact.setContentView(view);
        // pop中的三个按钮
        // 此方法可防止5.0版本下面的banner挡住PopWindows
        popContact
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        parent = (RelativeLayout) view.findViewById(R.id.parent);
        btn_pop_ok = (Button) view.findViewById(R.id.btn_pop_ok);
        btn_pop_cancle = (Button) view.findViewById(R.id.btn_pop_cancle);
    }

    @Override
    protected void onResload() {
        mSVProgressHUD.showWithStatus("查找设备中...");
    }

    @Override
    protected void setMyViewClick() {
        btn_pop_ok.setOnClickListener(this);
        btn_pop_cancle.setOnClickListener(this);
        tv_tocontact_search.setOnClickListener(this);
        toolbar_tv_right_cancle.setOnClickListener(this);
        tv_dk.setOnClickListener(this);
//        tv_gl.setOnClickListener(this);
    }

    @Override
    protected void headerOrFooterViewControl() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mLeDeviceListAdapter = new LeDeviceListAdapter(getActivity());
        lv.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBluetooth() {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        mHandler = new Handler();
        if (!getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
        }
        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 开启蓝牙
        mBluetoothAdapter.enable();

    }


    /**
     * 初始化蓝牙相关
     */
    private void initBluetoothConnet() {
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
        }, 3000);

    }

    private int status = 0;//手动关联之后才能获取

    //连接成功之后进行查询模式
    @Subscribe
    public void onEventMainThread(ConnectBean event) {
        mSVProgressHUD.dismiss();
        boolean mConnected = event.ismConnected();//是否关联成功
        if (mConnected) {//关联成功
            App.getInstance().setBleLingDuiAddress("");
            App.getInstance().setBleLingDuiName("");
            App.getInstance().setTb_phonelocation(false);
            showToast("关联成功");
//            scanLeDevice(true);
            isConnect = true;
            if (status == 1) {
                status = 0;
                getInfo();
            }

        } else {//关联失败
            isConnect = false;
            showToast("设备已断开，请重新连接");
            cancleContact();
            //队员机设备状态
            mDeviceName = App.getInstance().getBleDuiYuanName();
            mDeviceAddress = App.getInstance().getBleDuiYuanAddress();
            // 与设备交互
            if (!TextUtils.isEmpty(mDeviceName)) {
                tv_dk.setVisibility(View.VISIBLE);
                tv_gl.setVisibility(View.GONE);
                tv_glldj.setText("队员机:" + mDeviceName);
//            initBluetoothConnet();
            } else {
                tv_gl.setVisibility(View.VISIBLE);
                tv_dk.setVisibility(View.GONE);
                tv_glldj.setText("队员机");
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
                showTipDialog("gps数据表为空");
                break;
            case 2:
                showTipDialog("队员信息表为空");
                break;
        }
    }

    private int bluetoothPosition = 0;
    private String bluetoothdeviceName = null;// 名称
    private String mAddress = null;// 地址

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tocontact_search:
                // 蓝牙设备的搜索，然后展示到listview中
                if (mScanning) {
                    scanLeDevice(false);
                } else {
                    scanLeDevice(true);
                }

                break;
            case R.id.btn_pop_ok:// TODO
                status = 1;
                popContact.dismiss();
                final BluetoothDevice device = mLeDeviceListAdapter
                        .getDevice(bluetoothPosition);
                if (device == null) {
                    return;
                }
                App.getInstance().setTb_phonelocation(false);
                App.getInstance().setContactBleDuiYuanAddress(
                        bluetoothdeviceName.substring(3));
                App.getInstance().setBleDuiYuanAddress(mAddress);
                App.getInstance().setBleDuiYuanName(bluetoothdeviceName.substring(3));
//                scanLeDevice(true);
                initBluetoothConnet();
//                initBluetooth();
                mSVProgressHUD.showWithStatus("设备关联中...");
                // 与设备交互
                mDeviceName = App.getInstance().getBleDuiYuanName();
                mDeviceAddress = App.getInstance().getBleDuiYuanAddress();
                if (!TextUtils.isEmpty(mDeviceName)) {
                    tv_dk.setVisibility(View.VISIBLE);
                    tv_gl.setVisibility(View.GONE);
                    tv_glldj.setText("队员机:" + mDeviceName);

                } else {
                    tv_gl.setVisibility(View.VISIBLE);
                    tv_dk.setVisibility(View.GONE);
                    tv_glldj.setText("队员机");
                }

                break;
            case R.id.btn_pop_cancle:
                popContact.dismiss();
                break;
            case R.id.toolbar_tv_right_cancle://获取设备参数表与同行队伍表
                if (TextUtils.isEmpty(App.getInstance().getBleDuiYuanName())) {
                    showToast("请先关联同行宝");
                } else {
                    tblertView.show();
                }

                break;
            case R.id.tv_dk:
                tv_gl.setVisibility(View.VISIBLE);
                tv_dk.setVisibility(View.GONE);
                tv_glldj.setText("队员机");
                cancleContact();
                //刷新列表
                scanLeDevice(true);
                isConnect = false;
                break;
            case R.id.tv_gl:
                Bundle b = new Bundle();
                //告诉关联界面是由该界面跳入
                b.putBoolean("fromTuZhong", true);
                openActivity(ContactMyGuardianAcitivty.class, b);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        //下一步是获取队员信息表
        BleCommon.getInstance().getGroupInfoAllBtApp();
    }

    /**
     * 获取队员信息表
     */
    @Subscribe
    public void onEventMainThread(GetMemberInfoBean event) {
        //告诉用户获取所有数据完毕
        mSVProgressHUD.dismiss();
        showTipDialog("获取队员信息表和设备参数表成功");
    }

    /**
     * 获取队员表等
     */
    public void getInfo() {
        mSVProgressHUD.showWithStatus("正在获取数据...");
//            initBluetoothConnet();
        //需先删除所有数据
        LitepalUtil.deleteAll();
        BleCommon.getInstance().getSetingGetTable();
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == tblertView && position != AlertView.CANCELPOSITION) {
            getInfo();
            /**
             * --------------------------------------------------《GPS 数据测试》-----------------------------------------------------------
             */
//            byte[] buffer = getFromAssets("BtAPP2.bin");
//            BleCommon.getInstance().displayData(buffer);
        } else if (o == tiplertView && position != AlertView.CANCELPOSITION) {

        }
    }

    public class LeDeviceListAdapter extends BaseAdapter {

        // Adapter for holding devices found through scanning.

        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;
        private Activity mContext;

        public LeDeviceListAdapter(Activity c) {
            super();
            mContext = c;
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = mContext.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_tocontact = (TextView) view
                        .findViewById(R.id.tv_tocontact);
                viewHolder.deviceName = (TextView) view
                        .findViewById(R.id.device_name);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (guanlianshebei) {
                viewHolder.tv_tocontact.setText("添加");
            } else {
                viewHolder.tv_tocontact.setText("关联");
            }
            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            } else
                viewHolder.deviceName.setText(R.string.unknown_device);

//            if (TextUtils.equals(App.getInstance().getBleDuiYuanName(), deviceName)) {
//                viewHolder.deviceName.setTextColor(getResources().getColor(R.color.colorPrimary));
//                viewHolder.tv_tocontact.setText("断开");
//            } else {
//                viewHolder.deviceName.setTextColor(getResources().getColor(R.color.content));
//                viewHolder.tv_tocontact.setText("关联");
//            }
            view.setOnClickListener(new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onClick(View v) {
//                    if (TextUtils.equals(App.getInstance().getBleDuiYuanName(), deviceName)) {
//                        cancleContact();
//                        scanLeDevice(true);
//                    } else {
                    if (mScanning) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }

                    bluetoothPosition = i;
                    bluetoothdeviceName = mLeDevices.get(i).getName();

                    logzjy.e("bluetoothdeviceName:" + bluetoothdeviceName);
                    mAddress = mLeDevices.get(i).getAddress();
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            getActivity(),
                            R.anim.activity_translate_in));
                    popContact.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                    btn_pop_ok.setText(deviceName);
                }
//                }
            });
            return view;
        }

        class ViewHolder {
            TextView deviceName;
            TextView tv_tocontact;
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (getActivity() != null) {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        getActivity().invalidateOptionsMenu();
                        System.out.println("scanLeDevice");
                        mSVProgressHUD.dismiss();
                    }
                }, SCAN_PERIOD);

                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
            getActivity().invalidateOptionsMenu();
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLeDeviceListAdapter.addDevice(device);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            mSVProgressHUD.dismiss();
        }
        return true;
    }

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

    //提示框
    public void showTipDialog(String str) {

        if (tiplertView != null) {
            tiplertView = null;
        }
        tiplertView = new AlertView("温馨提示", str, null, new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                tiplertView.show();
            }
        }, 1000);
    }
}
