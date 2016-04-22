package com.keyhua.renameyourself.main;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
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
import com.keyhua.renameyourself.main.eventBusBean.ChangeAddIn;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.GuiDuiBean;
import com.keyhua.renameyourself.main.eventBusBean.LidDUIBean;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.Arrays;
import java.util.List;

public class ChangeDuiYuanActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    private TextView ctv_event = null;//队员名
    private TextView ctv_lijin = null;//队员电话
    private TextView tv_snnum = null;//sn添加
    private TextView tv_snbsc = null;//sn删除
    private TextView tv_snbtn = null;//sn
    private TextView tv_lidui = null;//离队显示
    private CleareditTextView ctv_bianhao = null;//组内编号
    private TextView tv_liduibtn = null;//离队按钮
    private TextView tv_guiduibtn = null;//归队按钮
    private TextView tv_ok = null;
    private int act_isleave = 0;// 是否离队(1)或归队(0)
    private long id = 0;
    private SVProgressHUD mSVProgressHUD;
    // 蓝牙
    private String mDeviceName = null;// 蓝牙名
    private String SNStr = null; // SN号
    private String mDeviceAddress = null;// 蓝牙地址
    private int isAddin = 0;//是否需要执行添加入组请求
    private String numStr = "";//从输入框获取的snNum
    private AlertView tiplertView;//是否添加入组成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_duiyuan);

        id = getIntent().getLongExtra("id", 0);
        initHeaderOther(bacStr, "修改队员信息", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        isAddin = 0;
        mSVProgressHUD = new SVProgressHUD(this);
        EventBus.getDefault().register(this);
        ctv_event = (TextView) findViewById(R.id.ctv_event);
        ctv_lijin = (TextView) findViewById(R.id.ctv_lijin);
        tv_snnum = (TextView) findViewById(R.id.tv_snnum);
        tv_snbsc = (TextView) findViewById(R.id.tv_snbsc);
        ctv_bianhao = (CleareditTextView) findViewById(R.id.ctv_bianhao);
        tv_snbtn = (TextView) findViewById(R.id.tv_snbtn);
        tv_lidui = (TextView) findViewById(R.id.tv_lidui);
        tv_liduibtn = (TextView) findViewById(R.id.tv_liduibtn);
        tv_guiduibtn = (TextView) findViewById(R.id.tv_guiduibtn);
        tv_ok = (TextView) findViewById(R.id.tv_ok);

    }

    @Override
    protected void onResload() {
        SignUpUser s = LitepalUtil.getAllUserByid(id);
        ctv_event.setText(s.getU_nickname());
        ctv_lijin.setText(s.getPhonenum());
        String deviceSN = s.getStrDeviceSN();
        if (TextUtils.isEmpty(deviceSN)) {
            tv_snbtn.setText("添加");
        } else {
            tv_snnum.setText("SN号:" + deviceSN);
            tv_snbtn.setText("修改");
        }
        ctv_bianhao.setText(s.getTps_id() + "");
        int act_isleave = s.getAct_isleave();
        switch (act_isleave) {
            case CommonUtility.LIDUI:
                tv_liduibtn.setVisibility(View.GONE);
                tv_guiduibtn.setVisibility(View.VISIBLE);
                tv_lidui.setText(CommonUtility.GUIDUISTR);
                break;
            case CommonUtility.GUIDUI:
                tv_liduibtn.setVisibility(View.VISIBLE);
                tv_guiduibtn.setVisibility(View.GONE);
                tv_lidui.setText(CommonUtility.LINGDUISTR);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setMyViewClick() {
        tv_ok.setOnClickListener(this);
        tv_snbtn.setOnClickListener(this);
        tv_liduibtn.setOnClickListener(this);
        tv_guiduibtn.setOnClickListener(this);
        tv_snbsc.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                //姓名
                String eventStr = ctv_event.getText().toString();
                //电话
                String giftStr = ctv_lijin.getText().toString();
                //sn
                String locationStr = tv_snnum.getText().toString();
                int numInt = Integer.valueOf(ctv_bianhao.getText().toString());
                //当前队员的id
                long numid = 0;
                List<SignUpUser> l = LitepalUtil.getAllUser();
                for (int i = 0; i < l.size(); i++) {
                    if (l.get(i).getTps_id() == numInt && l.get(i).getId() != id) {//当前的组内id等于输入的组内id并且当前的自增id不等于数据库中的自增id
                        numInt = -1;
                    }
                }
                //离队或归队
                if (TextUtils.isEmpty(eventStr)) {
                    showToast("队员姓名为必填项");
                } else if (numInt == 0 || numInt == 1) {
                    showToast("请填入有效的编号");
                } else if (numInt == -1 && numid != id) {
                    showToast("当前存在该编号的队员");
                } else {
                    SignUpUser event = new SignUpUser();
//                    event.setU_nickname(eventStr);
//                    event.setPhonenum(giftStr);
//                    event.setStrDeviceSN(locationStr);
                    event.setAct_isleave(act_isleave);
                    event.setTps_id(numInt);
                    event.update(id);
                    showToast("修改成功");
                    finish();
                }

                break;
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.tv_snbsc://删除sn号
                if (!TextUtils.isEmpty(numStr)) {
                    SignUpUser s = new SignUpUser();
                    s.setStrDeviceSN("");
                    s.update(id);
                    tv_snnum.setText("添加同行宝设备");
                } else {
                    showToast(CommonUtility.DUIWUGUANLI);
                }
                break;
            case R.id.tv_snbtn://添加sn号
                numStr = tv_snnum.getText().toString();
                SignUpUser s = LitepalUtil.getLeader();
                if (!TextUtils.isEmpty(s.getStrDeviceSN())) {
                    isAddin = 1;
                    Bundle b = new Bundle();
                    //告诉关联界面是由该界面跳入
                    b.putBoolean("fromTuZhong", false);
                    openActivity(ContactMyGuardianAcitivty.class, b);
                } else {
                    showToast(CommonUtility.DUIWUGUANLI);
                }

                break;
            case R.id.tv_liduibtn:
                numStr = tv_snnum.getText().toString();
                if (!TextUtils.isEmpty(LitepalUtil.getLeader().getStrDeviceSN())) {
                    mSVProgressHUD.showWithStatus("离队中...");
                    BleCommon.getInstance().liDui(numStr.substring(4));

                } else {
                    showToast(CommonUtility.DUIWUGUANLI);
                }

                break;
            case R.id.tv_guiduibtn:
                numStr = tv_snnum.getText().toString();
                if (!TextUtils.isEmpty(numStr)) {
                    mSVProgressHUD.showWithStatus("归队中...");
                    BleCommon.getInstance().guiDui(numStr.substring(4));
                } else {
                    showToast(CommonUtility.DUIWUGUANLI);
                }

                break;
        }
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {

    }

    //蓝牙start------------------------------------------------------------------------
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 提示设备已断开连接
     */
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

    /**
     * 添加入组
     */
    @Subscribe
    public void onEventMainThread(AddInBean event) {
        mSVProgressHUD.dismiss();
        isAddSuccess = true;
        byte[] mode = event.getMode();
        byte[] byt0 = {0};
        byte[] byt1 = {1};
        if (Arrays.equals(mode, byt0)) {// 入组失败
            tv_snbtn.setText("添加");
            showTipDialog("添加入组失败，请重试");
        } else if (Arrays.equals(mode, byt1)) {// 入组成功
            SignUpUser s = new SignUpUser();
            s.setStrDeviceSN(SNStr);
            s.update(id);
            tv_snnum.setText("SN号:" + SNStr);
            tv_snbtn.setText("修改");
            showTipDialog("添加入组成功");
        }
        //清空当前队员地址
        App.getInstance().setContactBleDuiYuanAddress(
                "");
        App.getInstance().setBleDuiYuanAddress("");
        App.getInstance().setBleDuiYuanName("");
    }

    /**
     * 离队返回值
     */
    @Subscribe
    public void onEventMainThread(LidDUIBean event) {
        mSVProgressHUD.dismiss();
        byte[] mode = event.getMode();
        byte[] byt0 = {0};
        byte[] byt1 = {1};
        if (Arrays.equals(mode, byt0)) {// 入组失败
            showTipDialog("离队操作失败，请重试");
        } else if (Arrays.equals(mode, byt1)) {// 入组成功
            act_isleave = CommonUtility.LIDUI;
            tv_liduibtn.setVisibility(View.GONE);
            tv_guiduibtn.setVisibility(View.VISIBLE);
            SignUpUser s = new SignUpUser();
            s.setAct_isleave(act_isleave);
            s.update(id);
            tv_lidui.setText(CommonUtility.GUIDUISTR);
            showTipDialog("离队成功");

        }
    }

    /**
     * 归队返回值
     */
    @Subscribe
    public void onEventMainThread(GuiDuiBean event) {
        mSVProgressHUD.dismiss();
        byte[] mode = event.getMode();
        byte[] byt0 = {0};
        byte[] byt1 = {1};
        if (Arrays.equals(mode, byt0)) {// 入组失败
            showTipDialog("归队操作失败，请重试");
        } else if (Arrays.equals(mode, byt1)) {// 入组成功
            act_isleave = CommonUtility.GUIDUI;
            tv_guiduibtn.setVisibility(View.GONE);
            tv_liduibtn.setVisibility(View.VISIBLE);
            SignUpUser s = new SignUpUser();
            s.setAct_isleave(act_isleave);
            s.update(id);
            tv_lidui.setText(CommonUtility.LINGDUISTR);
            showTipDialog("归队成功");
        }
    }

    private boolean isAddSuccess = false;

    /**
     * 告诉手机去添加入组
     */
    @Subscribe
    public void onEventMainThread(ChangeAddIn event) {
        isAddSuccess = false;
        // 队员
        mDeviceName = App.getInstance().getBleDuiYuanName();
        mDeviceAddress = App.getInstance().getBleDuiYuanAddress();
        SNStr = App.getInstance()
                .getContactBleDuiYuanAddress();//添加入组时的操作
        // 与设备交互
        if (!TextUtils.isEmpty(SNStr) && isAddin == 1) {
            mSVProgressHUD.showWithStatus("添加入组中...");
            BleCommon.getInstance().addIn(SNStr);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (isAddSuccess == false) {
                        showTipDialog("添加入组失败，请重试");
                    }
                }
            }, 60 * 1000);
        } else {
        }
    }

    //蓝牙END--------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        if (mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismiss();
        } else {
            super.onBackPressed();
        }
    }

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

