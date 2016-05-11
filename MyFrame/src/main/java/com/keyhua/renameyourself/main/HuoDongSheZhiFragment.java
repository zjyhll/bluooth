package com.keyhua.renameyourself.main;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.MemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.SetBean;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.main.le.BluetoothLeService;
import com.keyhua.renameyourself.main.le.ByteUtil;
import com.keyhua.renameyourself.main.le.Utile;
import com.keyhua.renameyourself.main.protocol.HwtxCommandException;
import com.keyhua.renameyourself.main.protocol.HwtxCommandReceiveSetting;
import com.keyhua.renameyourself.main.protocol.HwtxCommandUtility;
import com.keyhua.renameyourself.main.protocol.HwtxDataFirmwareConfig;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.SPUtils;
import com.keyhua.renameyourself.view.CustomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HuoDongSheZhiFragment extends BaseFragment implements OnItemClickListener, OnDismissListener {
    /**
     * 同行宝编号
     */
    private TextView tv_num = null;
    /**
     * GPS工作间隔[1,60]
     */
    private EditText et2 = null;
    /**
     * GPS工作间隔[1,60]
     */
    private Integer bGpsInterval = null;
    /**
     * 广播时间间隔[5,60]
     */
    private EditText et3 = null;
    /**
     * 广播时间间隔[5,60]
     */
    private Integer bHktAtInterval = null;
    /**
     * 失联次数 [1-10]
     */
    private EditText et5 = null;
    /**
     * 失联次数 [1-10]
     */
    private Integer bLostContactNum = null;
    /**
     * 黄色警告距离 范围[10, 5000]
     */
    private EditText et6 = null;
    /**
     * 黄色警告距离 范围[10, 5000]
     */
    private Integer wWarningDistance1 = null;
    /**
     * 红色警告距离
     */
    private EditText et61 = null;
    /**
     * 红色警告距离
     */
    private Integer wWarningDistance2 = null;
    /**
     * 失联警告距离
     */
    private EditText et62 = null;
    /**
     * 失联警告距离
     */
    private Integer wWarningDistance3 = null;
    /**
     * 电量告警
     */
    private EditText et7 = null;
    /**
     * 电量告警
     */
    private Integer bWarningBatteryPercent = null;

    // 设备模式
    private String mDeviceName = null;// 蓝牙名
    private String mDeviceAddress = null;// 蓝牙地址
    // 同步参数
    private boolean isConnect = false;
    private AlertView deleteAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    protected TextView toolbar_tv_right_cancle = null;// 右边文字
    private static SVProgressHUD mSVProgressHUD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_set, container, false);
    }

    @Override
    protected void onInitData() {
        mSVProgressHUD = new SVProgressHUD(getActivity());
        EventBus.getDefault().register(this);
        toolbar_tv_right_cancle = (TextView) getActivity().findViewById(R.id.toolbar_tv_right_cancle);
        deleteAlertView = new AlertView("是否保存", null, "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        initPopwindow();
        tv_num = (TextView) getActivity().findViewById(R.id.tv_num);
        et2 = (EditText) getActivity().findViewById(R.id.et2);
        et3 = (EditText) getActivity().findViewById(R.id.et3);
        et5 = (EditText) getActivity().findViewById(R.id.et5);
        et6 = (EditText) getActivity().findViewById(R.id.et6);
        et61 = (EditText) getActivity().findViewById(R.id.et61);
        et62 = (EditText) getActivity().findViewById(R.id.et62);
        et7 = (EditText) getActivity().findViewById(R.id.et7);

        bGpsInterval = (Integer) SPUtils.get(getActivity(), "bGpsInterval",
                5);
        bHktAtInterval = (Integer) SPUtils.get(getActivity(),
                "bHktAtInterval", 5);
        bLostContactNum = (Integer) SPUtils.get(getActivity(),
                "bLostContactNum", 3);
        wWarningDistance1 = (Integer) SPUtils.get(getActivity(),
                "wWarningDistance1", 1000);
        wWarningDistance2 = (Integer) SPUtils.get(getActivity(),
                "wWarningDistance2", 1500);
        wWarningDistance3 = (Integer) SPUtils.get(getActivity(),
                "wWarningDistance3", 2000);
        bWarningBatteryPercent = (Integer) SPUtils.get(getActivity(),
                "bWarningBatteryPercent", 15);
        et2.setText(bGpsInterval + "");
        et3.setText(bHktAtInterval + "");
        et5.setText(bLostContactNum + "");
        et6.setText(wWarningDistance1 + "");
        et61.setText(wWarningDistance2 + "");
        et62.setText(wWarningDistance3 + "");
        et7.setText(bWarningBatteryPercent + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(ConnectBean event) {
        boolean mConnected = event.ismConnected();//是否关联成功
        if (mConnected) {//关联成功
        } else {//关联失败
            showToast("设备已断开，请重新连接");
            cancleContact();
            mSVProgressHUD.dismiss();
        }
    }

    //同步队员信息表
    @Subscribe
    public void onEventMainThread(SetBean event) {
        mSVProgressHUD.dismiss();
        boolean isFaild = event.isFaild();
        if (isFaild) {
            showTipDialog("数据同步失败，请重试");
        } else {
            List<Integer> errorNumList = event.getErrorNumList();// 未同步成功的队员
            String str = "";
            for (int i = 0; i < errorNumList.size(); i++) {
                str += errorNumList.get(i) + "号";
            }
            if (TextUtils.isEmpty(str)) {
                showTipDialog("所有队员信息同步成功！");
            } else {
                showTipDialog("队员：" + str + "未同步成功");
            }
        }
    }

    @Override
    protected void onResload() {
        tv_num.setText(mDeviceName);
    }

    @Override
    protected void setMyViewClick() {
        toolbar_tv_right_cancle.setOnClickListener(this);
    }

    @Override
    protected void headerOrFooterViewControl() {

    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == deleteAlertView && position != AlertView.CANCELPOSITION) {
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
            BleCommon.getInstance().hwtxCommandSendGpsPrepare(bGpsInterval, bHktAtInterval, bLostContactNum, wWarningDistance1, wWarningDistance2, wWarningDistance3, bWarningBatteryPercent);
            mSVProgressHUD.showWithStatus("保存中...");
        } else {
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_itv_back:// 返回按钮返回到上一个界面
                CommonUtility.gone_keyboard(getActivity());
                break;
            case R.id.toolbar_tv_right_cancle:// 保存
                if (TextUtils.isEmpty(App.getInstance().getBleLingDuiDuiAddress())) {
                    showToast("请先关联领队机");
                } else if ((boolean) SPUtils.get(getActivity(), "istongbu", false)==false) {
                    showToast("请先同步队员信息表");
                } else {
                    /**
                     * 2) 设备参数表：APP向UUID为0x0025特性写数据发送设备参数表，设备参数表数据格式参看hwtx_ext.
                     * h中的结构体HWTX_FW_Config，直接传输数据块
                     */
                    bGpsInterval = Integer
                            .valueOf(!TextUtils.isEmpty(et2.getText()) ? et2.getText()
                                    .toString() : "0");
                    bHktAtInterval = Integer
                            .valueOf(!TextUtils.isEmpty(et3.getText()) ? et3.getText()
                                    .toString() : "0");
                    bLostContactNum = Integer
                            .valueOf(!TextUtils.isEmpty(et5.getText()) ? et5.getText()
                                    .toString() : "0");
                    wWarningDistance1 = Integer.valueOf(!TextUtils.isEmpty(et6
                            .getText()) ? et6.getText().toString() : "0");
                    wWarningDistance2 = Integer.valueOf(!TextUtils.isEmpty(et61
                            .getText()) ? et61.getText().toString() : "0");
                    wWarningDistance3 = Integer.valueOf(!TextUtils.isEmpty(et62
                            .getText()) ? et62.getText().toString() : "0");
                    bWarningBatteryPercent = Integer.valueOf(!TextUtils.isEmpty(et7
                            .getText()) ? et7.getText().toString() : "0");
                    if (bGpsInterval != 0 && bHktAtInterval != 0
                            && bLostContactNum != 0 && wWarningDistance1 != 0
                            && wWarningDistance3 != 0 && wWarningDistance3 != 0
                            && bWarningBatteryPercent != 0) {
                        if (bGpsInterval < 1 && bGpsInterval > 60) {
                            showToast("GPS工作间隔范围为1到60");
                            return;
                        }
                        if (bHktAtInterval < 5 || bHktAtInterval > 60) {
                            showToast("广播时间间隔范围为5到60");// 广播时间间隔[5,60]
                            return;
                        }
                        if (bLostContactNum < 1 || bLostContactNum > 10) {
                            showToast("失联次数范围为1到10");// 广播时间间隔[1,10]
                            return;
                        }
                        if (wWarningDistance1 < 10 || wWarningDistance1 > 5000) {
                            showToast("黄色警告范围为10到5000");// 范围[10, 5000]
                            return;
                        }
                        if (wWarningDistance2 >= wWarningDistance1) {
                            if (wWarningDistance2 < 10 || wWarningDistance2 > 5000) {
                                showToast("红色警告范围为10到5000");// 范围[10, 5000]
                                return;
                            }
                        } else {
                            showToast("黄色警告范围大于红色警告范围");
                            return;
                        }
                        if (wWarningDistance3 >= wWarningDistance2) {
                            if (wWarningDistance3 < 10 || wWarningDistance3 > 5000) {
                                showToast("失联警告范围为10到5000");// 范围[10, 5000]
                                return;
                            }
                        } else {
                            showToast("红色警告范围大于失联警告范围");
                            return;
                        }
                        if (bWarningBatteryPercent < 0 || bWarningBatteryPercent > 100) {
                            showToast("电量百分比告警警告为10到5000");// 范围[0,100]
                            return;
                        }
                        deleteAlertView.show();


                    } else {
                        showToast("所有信息都为必填");
                    }
                }
                break;
            default:
                break;
        }
    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            mSVProgressHUD.dismiss();
        }
        return true;
    }

    private AlertView tiplertView;//是否添加入组成功

    //提示框
    public void showTipDialog(String str) {
        if (getActivity() != null) {
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
}
