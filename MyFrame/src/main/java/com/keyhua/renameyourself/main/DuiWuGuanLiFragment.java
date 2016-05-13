package com.keyhua.renameyourself.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.GpsInfo;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.PlanGpsInfo;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.eventBusBean.ConnectBean;
import com.keyhua.renameyourself.main.eventBusBean.GetMemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.InitBluetoothBean;
import com.keyhua.renameyourself.main.eventBusBean.LengthZero;
import com.keyhua.renameyourself.main.eventBusBean.MemberInfoBean;
import com.keyhua.renameyourself.main.eventBusBean.QueryModeBean;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.main.le.BluetoothLeService;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.DensityUtils;
import com.keyhua.renameyourself.util.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.loadmore.LoadMoreListViewContainer;
import in.srain.cube.ptr.PtrFrameLayout;

public class DuiWuGuanLiFragment extends BaseFragment implements OnItemClickListener, OnDismissListener {
    // 上拉下拉刷新MerchantsListAdpter
    private SwipeMenuListView lv_home = null;
    private MYAdpter listadapter = null;
    public int index = 0;
    public int count = 10;
    // 服务器返回提示信息
    private List<SignUpUser> mers = null;
    private List<SignUpUser> mersTemp = null;
    private FloatingActionButton fab = null;
    private AlertView deleteAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    private AlertView ldAlertView;//领队弹出框
    private AlertView dyAlertView;//队员弹出框
    private AlertView tblertView;//是否同步弹出框
    private TextView tv_dynums = null;//队伍人数
    private TextView tv_glldj = null;//关联领队机
    private TextView tv_dk = null;//断开领队机
    private TextView tv_hq = null;//获取数据
    private TextView tv_gl = null;//关联领队机
    private TextView tv_jhgj = null;//显示计划轨迹概略
    private TextView btn_jhgj = null;//选择计划轨迹的按钮
    private TextView btn_sd = null;//收队的按钮
    private long id = 0;//数据库中对应的id
    private String mDeviceAddress = null;// 蓝牙地址
    private String mDeviceName = null;// 蓝牙名
    private static SVProgressHUD mSVProgressHUD;
    protected TextView toolbar_tv_right_cancle = null;// 右边文字
    private AlertView tipAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    private AlertView tiplertView;//是否添加入组成功
    private int status = 0;//同步结果
    private int duiyuanMode = 0;
    private boolean isConnect = false;//是否关联成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_duiwuguanli, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        //领队机设备状态
        mDeviceName = LitepalUtil.getLeader().getStrDeviceSN();
        mDeviceAddress = App.getInstance().getBleLingDuiDuiAddress();
        mersTemp = DataSupport.where("tps_type=?", String.valueOf(CommonUtility.DUIYUAN)).find(SignUpUser.class);//找到所有队员
        tv_dynums.setText("队伍人数:" + (mersTemp.size() + 1) + "人");
        if (mersTemp.size() > 0) {
            mers.clear();
            mers.addAll(mersTemp);
            listadapter.notifyDataSetChanged();
        }
        PlanGpsInfo p = LitepalUtil.getpg();
        if (p != null) {
            tv_jhgj.setText("已选择轨迹：" + p.getName());
        } else {
            tv_jhgj.setText("点击按钮选择计划轨迹");
        }
    }

    //蓝牙start------------------------------------------------------------------------
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //查询模式返回结构
    @Subscribe
    public void onEventMainThread(QueryModeBean event) {


        byte[] mode = event.getMode();
        byte[] byt0 = {0};
        byte[] byt1 = {1};
        if (Arrays.equals(mode, byt0)) {// 队员模式
            if (duiyuanMode == 1) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                BleCommon.getInstance().queryMode();
//                    }
//                }, 6000);
            } else {
                mSVProgressHUD.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dyAlertView.show();
                    }
                }, 1000);
            }
        } else if (Arrays.equals(mode, byt1)) {// 领队模式
            mSVProgressHUD.dismiss();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    ldAlertView.show();
                }
            }, 1000);
            duiyuanMode = 0;
        }
    }

    //连接成功之后进行查询模式
    @Subscribe
    public void onEventMainThread(ConnectBean event) {
        boolean mConnected = event.ismConnected();//是否关联成功
        if (mConnected) {//关联成功
            isConnect = true;
            //领队机设备状态
            mDeviceName = App.getInstance().getBleLingDuiName();
            mDeviceAddress = App.getInstance().getBleLingDuiDuiAddress();
            String strDeviceSN = mDeviceName;// 硬件sn号码
            //更新领队的设备号
            SignUpUser s = new SignUpUser();
            s.setStrDeviceSN(strDeviceSN);
            s.setDeviceReady(1);
            s.updateAll("tps_type = ?", String.valueOf(CommonUtility.LINGDUI));
            tv_glldj.setText("领队机:" + strDeviceSN);
            tv_dk.setVisibility(View.VISIBLE);
            tv_gl.setVisibility(View.GONE);
            BleCommon.getInstance().queryMode();
            App.getInstance().setTb_phonelocation(false);
        } else {//关联失败
            isConnect = false;
            //清空领队的设备号
            SignUpUser s = new SignUpUser();
            s.setStrDeviceSN("");
            s.setDeviceReady(2);
            s.updateAll("tps_type = ?", String.valueOf(CommonUtility.LINGDUI));
            //
            cancleContact();
            //
            showToast("设备已断开，请重新连接");
            tv_gl.setVisibility(View.VISIBLE);
            tv_dk.setVisibility(View.GONE);
            mSVProgressHUD.dismiss();
            tv_glldj.setText("领队机");
            App.getInstance().setTb_phonelocation(true);
        }
    }

    //连接界面点击关联之后开始初始化蓝牙
    @Subscribe
    public void onEventMainThread(InitBluetoothBean event) {
        initBluetooth();
        mSVProgressHUD.showWithStatus("关联中..");
    }

    //同步队员信息表
    @Subscribe
    public void onEventMainThread(MemberInfoBean event) {
        boolean isFaild = event.isFaild();
        String tip = "";
        mSVProgressHUD.dismiss();
        if (isFaild) {
//            showToast("数据同步失败，请重试");
            tip = "数据同步失败，请重试";
            status = 1;
            showTipDialog(tip);
        } else {
            List<Integer> errorNumList = event.getErrorNumList();// 未同步成功的队员

            String str = "";
            for (int i = 0; i < errorNumList.size(); i++) {
                str += errorNumList.get(i) + "号";
            }
            if (TextUtils.isEmpty(str)) {
                SPUtils.put(getActivity(), "istongbu", true);//设备参数表需要在同步队员信息表之后才能进行
//                showToast("所有队员信息同步成功！");
                tip = "所有队员信息同步成功！";
                status = 2;
                showTipDialog(tip);
            } else {
//                tipAlertView = new AlertView("温馨提示", "队员：" + str + "未同步成功", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
//                tipAlertView.show();
                tip = "队员：" + str + "未同步成功";
                status = 3;
                showTipDialog(tip);
            }
        }

    }

    /**
     * 获取队员信息表
     */
    @Subscribe
    public void onEventMainThread(GetMemberInfoBean event) {
        //告诉用户获取所有数据完毕
        mSVProgressHUD.dismiss();
        status = 3;
        showTipDialog("数据获取完成");
        SPUtils.put(getActivity(), "istongbu", true);//设备参数表需要在同步队员信息表之后才能进行
        //刷新列表
        mersTemp = DataSupport.where("tps_type=?", String.valueOf(CommonUtility.DUIYUAN)).find(SignUpUser.class);//找到所有队员
        tv_dynums.setText("队伍人数:" + (mersTemp.size() + 1) + "人");
        if (mersTemp.size() > 0) {
            mers.clear();
            mers.addAll(mersTemp);
            listadapter.notifyDataSetChanged();
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
                status = 6;
                showTipDialog("gps数据表为空");
                break;
            case 2:
                status = 6;
                showTipDialog("队员信息表为空");
                break;
        }
    }

    /**
     * 初始化蓝牙相关
     */
    private void initBluetooth() {

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
                    if (getActivity() != null) {
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
            }
        }, 2500);

    }


    //蓝牙END--------------------------------------------------------------------------------------
    @Override
    protected void onInitData() {
        toolbar_tv_right_cancle = (TextView) getActivity().findViewById(R.id.toolbar_tv_right_cancle);
        mSVProgressHUD = new SVProgressHUD(getActivity());
        EventBus.getDefault().register(this);

        tv_dynums = (TextView) getActivity().findViewById(R.id.tv_dynums);
        tv_jhgj = (TextView) getActivity().findViewById(R.id.tv_jhgj);
        btn_jhgj = (TextView) getActivity().findViewById(R.id.btn_jhgj);
        btn_sd = (TextView) getActivity().findViewById(R.id.btn_sd);
        tv_hq = (TextView) getActivity().findViewById(R.id.tv_hq);
        tv_dk = (TextView) getActivity().findViewById(R.id.tv_dk);
        tv_gl = (TextView) getActivity().findViewById(R.id.tv_gl);
        tv_glldj = (TextView) getActivity().findViewById(R.id.tv_glldj);
        deleteAlertView = new AlertView("温馨提示", "确定要删除该条记录吗", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        ldAlertView = new AlertView("温馨提示", "同行宝当前已是领队模式", null, new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        dyAlertView = new AlertView("温馨提示", "同行宝当前模式为队员，是否设为领队", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        tblertView = new AlertView("温馨提示", "是否同步队员信息表", "取消", new String[]{"确定"}, null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mers = new ArrayList<>();
        mersTemp = new ArrayList<>();
        lv_home = (SwipeMenuListView) getActivity().findViewById(R.id.lv_home);
        listadapter = new MYAdpter(getActivity(), mers);
        lv_home.setAdapter(listadapter);

        //领队机设备状态
        mDeviceName = LitepalUtil.getLeader().getStrDeviceSN();
        mDeviceAddress = App.getInstance().getBleLingDuiDuiAddress();
        // 与设备交互
        if (!TextUtils.isEmpty(mDeviceName)) {
            tv_dk.setVisibility(View.VISIBLE);
            tv_gl.setVisibility(View.GONE);
            tv_glldj.setText("领队机:" + mDeviceName);

        } else {
            tv_gl.setVisibility(View.VISIBLE);
            tv_dk.setVisibility(View.GONE);
            tv_glldj.setText("领队机");
        }

    }

    @Override
    protected void onResload() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(DensityUtils.dp2px(getActivity(), 90));
                // set item title
                openItem.setTitle("修改");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xef,
                        0x56, 0x56)));
                // set item width
                deleteItem.setWidth((DensityUtils.dp2px(getActivity(), 90)));
                // set a icon
//                deleteItem.setIcon(R.mipmap.ic_launcher);
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lv_home.setMenuCreator(creator);
    }


    @Override
    protected void setMyViewClick() {
        tv_dk.setOnClickListener(this);
        tv_gl.setOnClickListener(this);
        fab.setOnClickListener(this);
        tv_hq.setOnClickListener(this);
        toolbar_tv_right_cancle.setOnClickListener(this);
        btn_jhgj.setOnClickListener(this);
        btn_sd.setOnClickListener(this);

        // Right
//        lv_home.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        lv_home.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                id = mers.get(position).getId();
                Bundle b = new Bundle();
                b.putLong("id", id);
                switch (index) {
                    case 0://修改
                        openActivity(ChangeDuiYuanActivity.class, b);
                        break;
                    case 1://删除
                        deleteAlertView.show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    protected void headerOrFooterViewControl() {

    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                openActivity(NewDuiYuanActivity.class);
                break;
            case R.id.toolbar_bac:
//                finish();
                break;
            case R.id.toolbar_right_l:
                showToast("查询");
                break;
            case R.id.toolbar_right_r:
                showToast("云同步");
                break;
            case R.id.tv_hq://从设备获取数据
                if (TextUtils.isEmpty(App.getInstance().getBleLingDuiDuiAddress())) {
                    showToast("请先关联领队机");
                } else {
                    status = 4;
                    showTipDialog("是否从领队机获取数据");
                }
                break;
            case R.id.toolbar_tv_right_cancle://同步队员信息表
                if (TextUtils.isEmpty(App.getInstance().getBleLingDuiDuiAddress())) {
                    showToast("请先关联领队机");
                } else {
                    int lInt = 0;
                    List<SignUpUser> l = DataSupport.where("tps_type=?", String.valueOf(CommonUtility.DUIYUAN)).find(SignUpUser.class);
                    for (int i = 0; i < l.size(); i++) {
                        if (TextUtils.isEmpty(l.get(i).getStrDeviceSN())) {
                            lInt++;
                        }
                    }
                    if (lInt != 0) {
//                        showToast("存在队员未添加设备，请先指定设备或删除该队员");
                        status = 5;
                        showTipDialog("存在队员未添加设备，请先指定设备或删除该队员");
                    } else {
                        tblertView.show();
                    }
                }
                break;
            case R.id.tv_dk:
                isConnect = false;
                //更新领队的设备号
                SignUpUser s = new SignUpUser();
                s.setStrDeviceSN("");
                s.updateAll("tps_type = ?", String.valueOf(CommonUtility.LINGDUI));
                tv_gl.setVisibility(View.VISIBLE);
                tv_dk.setVisibility(View.GONE);
                tv_glldj.setText("领队机");
                cancleContactSD();
                break;
            case R.id.tv_gl:
                Bundle b = new Bundle();
                //告诉关联界面是由该界面跳入
                b.putBoolean("fromTuZhong", true);
                openActivity(ContactMyGuardianAcitivty.class, b);
                break;
            case R.id.btn_jhgj:
                openActivity(PlanTrajectoryActivity.class);
                break;
            case R.id.btn_sd:
                openActivity(UploadTrajectoryActivity.class);
                break;
        }
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == deleteAlertView && position != AlertView.CANCELPOSITION) {
            DataSupport.delete(SignUpUser.class, id);
            mersTemp = LitepalUtil.getDuiyuan();//找到所有队员
            tv_dynums.setText("队伍人数:" + (mersTemp.size() + 1) + "人");
            mers.clear();
            mers.addAll(mersTemp);
            listadapter.notifyDataSetChanged();
        } else if (o == ldAlertView && position != AlertView.CANCELPOSITION) {

        } else if (o == dyAlertView && position != AlertView.CANCELPOSITION) {//设为领队操作
            duiyuanMode = 1;
            mSVProgressHUD.showWithStatus("设置领队中..");
            BleCommon.getInstance().setTeamLeader();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BleCommon.getInstance().queryMode();
                }
            }, 6000);
        } else if (o == tipAlertView && position != AlertView.CANCELPOSITION) {
        } else if (o == tblertView && position != AlertView.CANCELPOSITION) {//同步
            mSVProgressHUD.showWithStatus("同步中...");
            BleCommon.getInstance().synchronizationInformationTable();
        } else if (o == tiplertView && position != AlertView.CANCELPOSITION) {//比较数据，获取队员信息表
            switch (status) {
                case 1://同步失败
                    break;
                case 2://同步所有队员成功后再从硬件拿设备
//                    BleCommon.getInstance().getGroupInfoAllBtApp();
//                    mSVProgressHUD.showWithStatus("数据对比中...");
                    break;
                case 3://部分同步成功,数据对比完成
                    break;
                case 4://获取数据
                    mSVProgressHUD.showWithStatus("数据获取中..");
                    //先删除所有队员
                    LitepalUtil.deleteUser(CommonUtility.DUIYUAN);
                    BleCommon.getInstance().getGroupInfoAllBtApp();
                    break;
            }

        }
    }

    public class MYAdpter extends BaseAdapter {
        private Context context = null;
        public List<SignUpUser> mers = null;

        public MYAdpter(Context context, List<SignUpUser> mers) {
            this.context = context;
            this.mers = mers;
        }

        @Override
        public int getCount() {
            return mers != null ? mers.size() : 0;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lv, null);
                holder = new ViewHolder();
                holder.tv_bianhao = (TextView) convertView.findViewById(R.id.tv_bianhao);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_num = (TextView) convertView.findViewById(R.id.tv_nums);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            switch (mers.get(position).getAct_isleave()) {
                case CommonUtility.LIDUI:
                    holder.tv_bianhao.setText(mers.get(position).getTps_id() + "（离队）");
                    convertView.setBackgroundColor(getResources().getColor(R.color.videoview));
                    break;
                case CommonUtility.GUIDUI:
                    holder.tv_bianhao.setText(mers.get(position).getTps_id() + "");
                    convertView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    break;
            }

            holder.tv_name.setText(mers.get(position).getU_nickname());
            holder.tv_num.setText("SN号:" + (TextUtils.isEmpty(mers.get(position).getStrDeviceSN()) ? "未添加" : mers.get(position).getStrDeviceSN()));
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_bianhao = null;//队员组内编号
            private TextView tv_name = null;//队员名称
            private TextView tv_num = null;//队员SN号
        }
    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {

            mSVProgressHUD.dismiss();
        }
        return true;
    }

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