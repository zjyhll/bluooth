package com.keyhua.renameyourself.main;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.eventBusBean.ChangeAddIn;
import com.keyhua.renameyourself.main.eventBusBean.InitBluetoothBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryModeBean;
import com.keyhua.renameyourself.view.MyListView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author 曾金叶 关联领队宝 @2015-8-12
 * @上午11:38:47
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ContactMyGuardianAcitivty extends BaseActivity {
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(
                R.layout.leftfrag_myguardianfrg_tocontact_a, null);
        setContentView(parentView);
        init();
        initBluetooth();
    }

    private void initPopwindow() {//
        popContact = new PopupWindow(ContactMyGuardianAcitivty.this);
        view = getLayoutInflater().inflate(R.layout.pop_sos, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        popContact.setWidth(LayoutParams.MATCH_PARENT);
        popContact.setHeight(LayoutParams.WRAP_CONTENT);
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

    private void initBluetooth() {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // 开启蓝牙
        mBluetoothAdapter.enable();

    }

    private int bluetoothPosition = 0;
    private String bluetoothdeviceName = null;// 名称
    private String mAddress = null;// 地址

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.tv_tocontact_search:
                // 蓝牙设备的搜索，然后展示到listview中
                if (mScanning) {
                    scanLeDevice(false);
                } else {
                    scanLeDevice(true);
                }

                break;
            case R.id.btn_pop_ok:// TODO
                popContact.dismiss();
                final BluetoothDevice device = mLeDeviceListAdapter
                        .getDevice(bluetoothPosition);
                if (device == null) {
                    return;
                }
                if (guanlianshebei) {
                    App.getInstance().setContactBleDuiYuanAddress(
                            bluetoothdeviceName.substring(3));
                } else {
                    if (fromTuZhong) {//关联领队机
                        App.getInstance().setBleLingDuiAddress(mAddress);
                        App.getInstance().setBleLingDuiName(bluetoothdeviceName.substring(3));
                        //关联领队机
                        EventBus.getDefault().post(
                                new InitBluetoothBean());
                    } else {///添加队员设备
                        App.getInstance().setContactBleDuiYuanAddress(
                                bluetoothdeviceName.substring(3));
                        App.getInstance().setBleDuiYuanAddress(mAddress);
                        App.getInstance().setBleDuiYuanName(bluetoothdeviceName.substring(3));
                        //添加入组
                        EventBus.getDefault().post(
                                new ChangeAddIn());
                    }
                }
                finish();
                break;
            case R.id.btn_pop_cancle:
                popContact.dismiss();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onInitData() {
        initHeaderOther("", "关联设备", true, false, false);
        initPopwindow();
        //
        lv = (MyListView) findViewById(R.id.lv);
        tv_tocontact_search = (TextView) findViewById(R.id.tv_tocontact_search);

    }

    @Override
    protected void onResload() {
        fromTuZhong = getIntent().getBooleanExtra("fromTuZhong", false);
        guanlianshebei = getIntent().getBooleanExtra("guanlianshebei", false);
    }

    @Override
    protected void setMyViewClick() {
        tv_tocontact_search.setOnClickListener(this);
        btn_pop_ok.setOnClickListener(this);
        btn_pop_cancle.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
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
    public void onDestroy() {
        super.onDestroy();
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
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mScanning) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }
                    bluetoothPosition = i;
                    bluetoothdeviceName = mLeDevices.get(i).getName();
                    logzjy.e("bluetoothdeviceName:" + bluetoothdeviceName);
                    mAddress = mLeDevices.get(i).getAddress();
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            ContactMyGuardianAcitivty.this,
                            R.anim.activity_translate_in));
                    popContact.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                    btn_pop_ok.setText(deviceName);
                }
            });
            return view;
        }

        class ViewHolder {
            TextView deviceName;
            TextView tv_tocontact;
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

}
