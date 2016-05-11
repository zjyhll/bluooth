package com.keyhua.renameyourself.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.eventBusBean.AddInBean;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.InitBluetoothBean;
import com.keyhua.renameyourself.main.eventBusBean.LengthZero;
import com.keyhua.renameyourself.main.eventBusBean.QueryLOGBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryModeBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryTXBBean;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.main.le.BluetoothLeService;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.SPUtils;
import com.keyhua.renameyourself.util.TimeUtil;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class InputActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    private TextView tv_num = null;
    private ScrollView scrollView = null;
    private TextView tv_ok = null;
    private TextView tv_jia = null;
    private TextView tv_end = null;
    private TextView ctv_bianhao = null;
    private CleareditTextView ctv_time = null;
    private String bluetoothdeviceName = null;// 名称
    private String mAddress = null;// 地址
    private static SVProgressHUD mSVProgressHUD;
    private int status = 0;

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
        initHeaderOther("", "log", true, false, false);
        tv_num = (TextView) findViewById(R.id.tv_num);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_jia = (TextView) findViewById(R.id.tv_jia);
        tv_end = (TextView) findViewById(R.id.tv_end);
        ctv_bianhao = (TextView) findViewById(R.id.ctv_bianhao);
        ctv_time = (CleareditTextView) findViewById(R.id.ctv_time);
        //关联领队机
        EventBus.getDefault().post(
                new InitBluetoothBean());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ctv_time.setText((String) SPUtils.get(InputActivity.this, "time", ""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        pauseTimer();
    }

    //连接界面点击关联之后开始初始化蓝牙
    @Subscribe
    public void onEventMainThread(InitBluetoothBean event) {
        initBluetooth();
        mSVProgressHUD.showWithStatus("关联中..");
    }

    int times = 0;

    //查询串号返回结果
    @Subscribe
    public void onEventMainThread(QueryLOGBean event) {
        String strLog = "";
        String start_timeH = TimeUtil.getDatetimeLog("HH:mm:ss") + "\n";
        String start_timeY = TimeUtil.getDatetimeLog("yyyyMMdd-HHmmss");
        String logStr = start_timeH + "\t\t" + strLog;
        mSVProgressHUD.dismiss();
        switch (event.getStatus()) {
            case 1://strMCUFW = "";//1
                start_timeH += "MCU FwVersion data(Hex):";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                strLog = event.getStrMCUFW();
                mSVProgressHUD.showWithStatus("获取BT FW版本号中..");
                BleCommon.getInstance().queryBTFW();
                break;
            case 2:// strBTFW = "";//2
                start_timeH += "BT FwVersion data(Hex):";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                strLog = event.getStrBTFW();
                mSVProgressHUD.showWithStatus("获取工作模式中..");
                BleCommon.getInstance().queryMode();
                break;
            case 3:// bWorkMode = "";//3
                start_timeH += "bWorkMode:";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                strLog = event.getbWorkMode();
                mSVProgressHUD.showWithStatus("获取设备串号中..");
                BleCommon.getInstance().querySNNUM();
                break;
            case 4://devSN = "";//4
                start_timeH += "DevSN:";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                strLog = event.getDevSN();
                mSVProgressHUD.showWithStatus("获取队伍表2中..");
                BleCommon.getInstance().getGroupInfoAllBtApp();
                break;
            case 5://strHWTX_GroupInfoAll_BtAPP2 = "";//5
                start_timeH += "HWTX_GroupInfoAll_BtAPP2 data(Hex):";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                strLog = event.getStrHWTX_GroupInfoAll_BtAPP2();
                mSVProgressHUD.showWithStatus("获取参数表中..");
                BleCommon.getInstance().getSetingGetTable();
                break;
            case 6://strHWTX_FW_Config = "";//6
                start_timeH += "HWTX_FW_Config data(Hex):";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                strLog = event.getStrHWTX_FW_Config();
//                mSVProgressHUD.showWithStatus("获取LOG中..");
                showToast("获取LOG中..");
                /**------------------------------------------------*/
                BleCommon.getInstance().queryLOGO();
                //每次发数据时打印时间
                start_timeH = "\n"+ TimeUtil.getDatetimeLog("HH:mm:ss") + "\n";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);

                String time = ctv_time.getText().toString();
                int defInt = 0;
                if (!TextUtils.isEmpty(time)) {
                    defInt = Integer.parseInt(time);

                    startTimer(defInt);
                    SPUtils.put(InputActivity.this, "time", time);
                }
                break;
            case 7://strLog = "";//7
                strLog = event.getStrLog();
//                if (times == 0) {
//                    ctv_bianhao.append(start_timeH);
//                    writeTxtToFile(start_timeH, filePath, fileName);
//                    times++;
//                } else if (times == 30) {//每30次打一次时间
//                    times = 0;
//                } else {
//                    times++;
//                }
                break;
            case 8:
                BleCommon.getInstance().queryLOGO();
                //每次发数据时打印时间
                start_timeH ="\n"+ TimeUtil.getDatetimeLog("HH:mm:ss") + "\n";
                ctv_bianhao.append(start_timeH);
                writeTxtToFile(start_timeH, filePath, fileName);
                break;
        }
        ctv_bianhao.append(strLog);
        //写入文件中
        writeTxtToFile(strLog, filePath, fileName);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    //连接成功
    @Subscribe
    public void onEventMainThread(ConnectBean event) {
        boolean mConnected = event.ismConnected();//是否关联成功
        mSVProgressHUD.dismiss();
        if (mConnected) {//关联成功
            status = 1;
            showToast("设备已关联成功");
        } else {//关联失败
            //
            cancleContact();
            //
            showToast("设备已断开，请重新连接");
//            finish();
        }
    }

    /**
     * 提示返回长度为0
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

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == tiplertView && position != AlertView.CANCELPOSITION) {

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

    @Override
    protected void onResload() {
        mAddress = App.getInstance().getBleLingDuiDuiAddress();
        bluetoothdeviceName = App.getInstance().getBleLingDuiName();
        tv_num.setText("当前SN号：" + bluetoothdeviceName);

        ctv_bianhao.setText((String) SPUtils.get(InputActivity.this, "numStr", ""));
    }

    @Override
    protected void setMyViewClick() {

        tv_ok.setOnClickListener(this);
        tv_jia.setOnClickListener(this);
        tv_end.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.tv_jia://开始记录
                times = 0;
                //获取MCU FW版本号
                mSVProgressHUD.showWithStatus("获取MCU FW版本号中..");
                BleCommon.getInstance().queryMCUFW();
                tv_end.setVisibility(View.VISIBLE);
                tv_jia.setVisibility(View.GONE);
                /***/
                String start_timeH = TimeUtil.getDatetimeLog("HH:mm:ss") + "\n";
                String start_timeY = TimeUtil.getDatetimeLog("yyyyMMdd-HHmmss");
                String start_timeY2 = TimeUtil.getDatetimeLog("yyyyMMdd_HHmmss");
                String logStr = start_timeH + start_timeY + "," + bluetoothdeviceName + "\n";
                ctv_bianhao.append(logStr);
                fileName = "log_sn" + bluetoothdeviceName + "_" + start_timeY2 + ".txt";
                //写入文件中
                writeTxtToFile(logStr, filePath, fileName);
                break;
            case R.id.tv_end://结束记录
                tv_jia.setVisibility(View.VISIBLE);
                tv_end.setVisibility(View.GONE);
                pauseTimer();
                break;
            case R.id.tv_ok:
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
        }, 2500);

    }

    @SuppressLint("HandlerLeak")
    Handler handlerlistNet = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1:// TODO
                    EventBus.getDefault().post(
                            new QueryLOGBean(8));
                    break;
                default:
                    break;
            }
        }
    };
    private Timer timer = new Timer();
    private TimerTask task;

    // 一个TimerTask 通过schedule方法使用之后，不能通过schedule方法调用第二次，想重复使用是不行的，是一次性用品
    public void startTimer(int defInt) {
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
                    handlerlistNet.sendEmptyMessage(CommonUtility.SERVEROK1);

                }
            };
        }
        if (timer != null && task != null) {
            timer.schedule(task, defInt * 1000 * 60, defInt * 1000 * 60);
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

    private String filePath = "/sdcard/TxbLog/";
    private String fileName = "txbLog.txt";

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath,
                               String fileName) {
        // 生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
}
