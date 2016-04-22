package com.keyhua.renameyourself.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.eventBusBean.AddInBean;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.InitBluetoothBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryModeBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryTXBBean;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.main.le.BluetoothLeService;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.SPUtils;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.Arrays;

public class InputActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    private TextView tv_num = null;
    private TextView tv_ok = null;
    private TextView tv_jia = null;
    private CleareditTextView ctv_bianhao = null;
    private String bluetoothdeviceName = null;// 名称
    private String mAddress = null;// 地址
    private static SVProgressHUD mSVProgressHUD;
    private int status = 0;
    private boolean isconnect = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        mSVProgressHUD = new SVProgressHUD(this);
        init();
    }

    @Override
    protected void onInitData() {
        EventBus.getDefault().register(this);
        initHeaderOther("", "烧录设备", true, false, false);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_jia = (TextView) findViewById(R.id.tv_jia);
        ctv_bianhao = (CleareditTextView) findViewById(R.id.ctv_bianhao);
        //关联领队机
        EventBus.getDefault().post(
                new InitBluetoothBean());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //连接界面点击关联之后开始初始化蓝牙
    @Subscribe
    public void onEventMainThread(InitBluetoothBean event) {
        initBluetooth();
        mSVProgressHUD.showWithStatus("关联中..");
    }

    //查询串号返回结果
    @Subscribe
    public void onEventMainThread(QueryTXBBean event) {
        String tcbSnNum = event.getTcbSnNum();
        String numStr = ctv_bianhao.getText().toString();
        tv_num.setText("当前SN号：" + tcbSnNum);
        showToast("查询成功，当前设备号：" + tcbSnNum);
        mSVProgressHUD.dismiss();
        switch (status) {
            case 1:

                break;
            case 2:
                if (TextUtils.equals(tcbSnNum, numStr)) {
                    isSuccess = true;
                    showTipDialog("烧录成功");
                } else {
                    isSuccess = false;
                    showTipDialog("烧录失败");
                }
                break;
        }

    }

    //连接成功之后进行查询模式
    @Subscribe
    public void onEventMainThread(ConnectBean event) {
        boolean mConnected = event.ismConnected();//是否关联成功
        mSVProgressHUD.dismiss();
        if (mConnected) {//关联成功
            isconnect = true;
            BleCommon.getInstance().queryMode();
            status = 1;
            mSVProgressHUD.showWithStatus("查询串号中..");
            showToast("设备已关联成功");
        } else {//关联失败
            isconnect = false;
            //
            cancleContact();
            //
            showToast("设备已断开，请重新连接");
            finish();
        }
    }

    /**
     * 添加入组
     */
    @Subscribe
    public void onEventMainThread(AddInBean event) {
        mSVProgressHUD.dismiss();
        byte[] mode = event.getMode();
        byte[] byt0 = {0};
        byte[] byt1 = {1};
        if (Arrays.equals(mode, byt0)) {// 入组失败
            showToast("烧录失败，请重试");
        } else if (Arrays.equals(mode, byt1)) {// 入组成功
            showToast("烧录完成");
            mSVProgressHUD.showWithStatus("查询串号中..");
            status = 2;
            BleCommon.getInstance().queryMode();
        }
    }

    @Override
    protected void onResload() {
        mAddress = App.getInstance().getBleLingDuiDuiAddress();
        bluetoothdeviceName = App.getInstance().getBleLingDuiName();
        tv_num.setText("当前SN号：");

        ctv_bianhao.setText((String) SPUtils.get(InputActivity.this, "numStr", ""));
    }

    @Override
    protected void setMyViewClick() {

        tv_ok.setOnClickListener(this);
        tv_jia.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.tv_jia:
                int defInt = Integer.parseInt((String) SPUtils.get(InputActivity.this, "numStr", "0"));
                if (defInt != 0) {
                    ctv_bianhao.setText(String.valueOf(defInt + 1));
                    String numStr = ctv_bianhao.getText().toString();
                    SPUtils.put(InputActivity.this, "numStr", numStr);
                } else {
                    showToast("不能为空");
                }
                break;
            case R.id.tv_ok:
                String numStr = ctv_bianhao.getText().toString();

                if (TextUtils.isEmpty(numStr)) {
                    showToast("不能为空");
                } else {//不为空时
                    int defInt2 = Integer.parseInt(numStr);
                    if (defInt2 == 0) {
                        showToast("不能为0");
                    }
                    SPUtils.put(InputActivity.this, "numStr", numStr);
                    BleCommon.getInstance().addIn(numStr);
                    mSVProgressHUD.showWithStatus("烧录中...");
                }
                break;
        }
    }

    /**
     * 初始化蓝牙相关
     */
    private void initBluetooth() {
        //STEP 1
        // 蓝牙相关
        BleCommon.getInstance().setCharacteristic(mAddress);
        // 广播接收器
        registerReceiver(BleCommon.getInstance().mGattUpdateReceiver,
                BleCommon.getInstance().makeGattUpdateIntentFilter());
        Intent gattServiceIntent = new Intent(this,
                BluetoothLeService.class);
        bindService(gattServiceIntent, BleCommon.getInstance().mServiceConnection,
                Context.BIND_AUTO_CREATE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //STEP 2
                if (isconnect == false) {
                    try {
                        unregisterReceiver(BleCommon.getInstance().mGattUpdateReceiver);
                        unbindService(BleCommon.getInstance().mServiceConnection);
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
                    BleCommon.getInstance().setCharacteristic(mAddress);
                    // 广播接收器
                    registerReceiver(BleCommon.getInstance().mGattUpdateReceiver,
                            BleCommon.getInstance().makeGattUpdateIntentFilter());
                    Intent gattServiceIntentAgin = new Intent(InputActivity.this,
                            BluetoothLeService.class);
                    bindService(gattServiceIntentAgin, BleCommon.getInstance().mServiceConnection,
                            Context.BIND_AUTO_CREATE);
                }
            }
        }, 2500);

    }

    @Override
    public void onDismiss(Object o) {

    }

    private boolean isSuccess = false;

    @Override
    public void onItemClick(Object o, int position) {
        if (o == tiplertView && position != AlertView.CANCELPOSITION) {
            if (isSuccess) {
                finish();
            }

        }
    }

    private AlertView tiplertView;//是否添加入组成功

    //提示框
    public void showTipDialog(String str) {

        if (tiplertView != null) {
            tiplertView = null;
        }
        tiplertView = new AlertView("温馨提示", str, null, new String[]{"确定"}, null, this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                tiplertView.show();
            }
        }, 1000);
    }
}
