package com.keyhua.renameyourself.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BackHandledInterface;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.le.BleCommon;
import com.keyhua.renameyourself.util.CommonUtility;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import org.litepal.crud.DataSupport;

public class MainActivity extends BaseActivity implements
        BackHandledInterface, OnItemClickListener, OnDismissListener {

    private BaseFragment mBackHandedFragment;
    private Fragment frgContent = null;// 容器
    private String title = "";
    //DrawerLayout控件
    private DrawerLayout mDrawerLayout;
    private int choiceInt = 1;//1为途中界面，2为领队工具界面
    private RadioGroup rg_button = null;
    private LinearLayout ll_shenfen = null;
    private RadioButton tv_tuzhong = null;//途中
    private RadioButton tv_lxdt = null;//离线地图
    private RadioButton tv_gongju = null;//领队工具
    private TextView tv_shenfen = null;//领队身份
    private TextView tv_clean = null;//清除数据
    private SVProgressHUD mSVProgressHUD;
    private AlertView tiplertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    private TextView left_banbenhao;// 版本号


    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    public void switchConent(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        toolbar_title.setText(title);
    }
    private int status = 0;//对应对话框
    //提示框
    public void showTipDialog(String str) {

        if (tiplertView != null) {
            tiplertView = null;
        }
        tiplertView = new AlertView("温馨提示", str, "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, this).setCancelable(false).setOnDismissListener(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                tiplertView.show();
            }
        }, 1000);
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == tiplertView && position != AlertView.CANCELPOSITION) {
            switch (status) {
                case 1:
                    status=0;
                    int deleteInt = 0;
                    switch (App.getInstance().getIs_leader()) {
                        case CommonUtility.LINGDUI:
                            if (LitepalUtil.getDuiyuan().size() > 0) {
                                mSVProgressHUD.showWithStatus("数据清除中..");
                                deleteInt = CommonUtility.DUIYUAN;
                                LitepalUtil.deleteUser(deleteInt);
                                mSVProgressHUD.dismiss();
                                showTipDialog("数据已清除");
                            } else {
                                showTipDialog("当前没有队员");
                            }
                            break;
                        case CommonUtility.DUIYUAN:
                            if (LitepalUtil.getAllUser().size() > 0) {
                                mSVProgressHUD.showWithStatus("数据清除中..");
                                LitepalUtil.deleteAll();
                                mSVProgressHUD.dismiss();
                                showTipDialog("数据已清除");
                            } else {
                                showTipDialog("当前没有队员");
                            }
                            break;
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        choiceInt = 0;

    }

    private RadioButton radiobutton_select_one = null;
    private RadioButton radiobutton_select_two = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHeaderOther("", "途中", true, false, false);
        init();
        // 打开页面显示的fragment
        frgContent = new DiTuFragment();
        switchConent(frgContent, "途中");
    }

    @Override
    protected void onInitData() {
        left_banbenhao = (TextView) findViewById(
                R.id.left_banbenhao);
        mSVProgressHUD = new SVProgressHUD(this);
        rg_button = (RadioGroup) findViewById(R.id.rg_button);
        ll_shenfen = (LinearLayout) findViewById(R.id.ll_shenfen);
        tv_tuzhong = (RadioButton) findViewById(R.id.tv_tuzhong);
        tv_lxdt = (RadioButton) findViewById(R.id.tv_lxdt);
        tv_gongju = (RadioButton) findViewById(R.id.tv_gongju);
        tv_shenfen = (TextView) findViewById(R.id.tv_shenfen);
        tv_clean = (TextView) findViewById(R.id.tv_clean);
        toolbar_bac.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.enter_menu_icon, 0, 0, 0);
        radiobutton_select_one = (RadioButton) findViewById(R.id.radiobutton_select_one);
        radiobutton_select_two = (RadioButton) findViewById(R.id.radiobutton_select_two);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //开启应用默认打开DrawerLayout
        mDrawerLayout.openDrawer(ll_shenfen);
    }

    @Override
    protected void onResload() {
        switch (App.getInstance().getIs_leader()) {
            case CommonUtility.LINGDUI:
                tv_gongju.setVisibility(View.VISIBLE);
                tv_shenfen.setText("领队");
                break;
            case CommonUtility.DUIYUAN:
                tv_gongju.setVisibility(View.GONE);
                tv_shenfen.setText("队员");
                break;
        }
        //默认显示为
        rightButton(View.VISIBLE, "队员");
        left_banbenhao.setText("V" + getVersion(this));

    }

    @Override
    protected void setMyViewClick() {
        radiobutton_select_one.setOnClickListener(this);
        tv_tuzhong.setOnClickListener(this);
        tv_gongju.setOnClickListener(this);
        tv_shenfen.setOnClickListener(this);
        tv_lxdt.setOnClickListener(this);
        radiobutton_select_two.setOnClickListener(this);
        toolbar_tv_right_cancle.setOnClickListener(MainActivity.this);
        tv_clean.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {

        frgContent = null;
        switch (v.getId()) {
            case R.id.radiobutton_select_one://1、地图2、队伍管理
                switch (choiceInt) {
                    case 1:
                        rightButton(View.VISIBLE, "队员");
                        frgContent = new DiTuFragment();
                        title = "途中";
                        break;
                    case 2:
                        frgContent = new DuiWuGuanLiFragment();
                        title = "队伍管理";
                        rightButton(View.VISIBLE, "同步");
                        break;
                }

                break;
            case R.id.radiobutton_select_two://1、同行宝2、活动设置
                switch (choiceInt) {
                    case 1:
                        switch (App.getInstance().getIs_leader()) {
                            case CommonUtility.LINGDUI:
                                showToast("领队无法进入同行宝界面");
                                radiobutton_select_one.setChecked(true);
//                                rightButton(View.GONE, "");
                                break;
                            case CommonUtility.DUIYUAN:
                                rightButton(View.VISIBLE, "获取数据");

                                frgContent = new TongXingBaoFragment();
                                title = "同行宝";
                                break;
                        }
                        break;
                    case 2:
                        rightButton(View.VISIBLE, "保存");
                        frgContent = new HuoDongSheZhiFragment();
                        title = "活动设置";
                        break;
                }

                break;
            case R.id.toolbar_bac:

                //开启应用默认打开DrawerLayout
                mDrawerLayout.openDrawer(ll_shenfen);
                break;
            case R.id.toolbar_tv_right_cancle:
                switch (choiceInt) {
                    case 1:

                        break;
                    case 2:
                        showToast("保存");
                        break;
                    case 3:
                        openActivity(DownOfflineActivity.class);
                        break;
                }

                break;
            case R.id.tv_tuzhong:
                radiobutton_select_one.setChecked(true);
                frgContent = new DiTuFragment();//默认进入地图界面
                title = "途中";
                choiceInt = 1;
                radiobutton_select_one.setText("地图");
                radiobutton_select_two.setText("同行宝");
                rg_button.setVisibility(View.VISIBLE);
                rightButton(View.VISIBLE, "队员");
                //关闭DrawerLayout回到主界面选中的tab的fragment页
                mDrawerLayout.closeDrawer(ll_shenfen);
                break;
            case R.id.tv_gongju:
                radiobutton_select_one.setChecked(true);
                frgContent = new DuiWuGuanLiFragment();
                title = "队伍管理";
                choiceInt = 2;
                radiobutton_select_one.setText("队伍管理");
                radiobutton_select_two.setText("活动设置");
                rightButton(View.VISIBLE, "同步");
                rg_button.setVisibility(View.VISIBLE);
                //关闭DrawerLayout回到主界面选中的tab的fragment页
                mDrawerLayout.closeDrawer(ll_shenfen);
                break;
            case R.id.tv_lxdt:
                frgContent = new LiXianDiTuFragment();
                title = "离线地图";
                rg_button.setVisibility(View.GONE);
                choiceInt = 3;
                rightButton(View.VISIBLE, "下载地图");
                //关闭DrawerLayout回到主界面选中的tab的fragment页
                mDrawerLayout.closeDrawer(ll_shenfen);
                break;
            case R.id.tv_shenfen:
                openActivity(IndexActivity.class);
                finish();
                break;
            case R.id.tv_clean:
                status=1;
                showTipDialog("是否清楚队员表数据？");
                break;

        }
        if (frgContent != null) {
            switchConent(frgContent, title);
        }

    }


    //右上角按钮是否显示

    public void rightButton(int showInt, String str) {
        toolbar_tv_right_cancle.setVisibility(showInt);
        toolbar_tv_right_cancle.setText(str);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (frgContent instanceof HuoDongSheZhiFragment) {
            HuoDongSheZhiFragment.onKeyDown(keyCode, event);
        } else if (frgContent instanceof DuiWuGuanLiFragment) {
            DuiWuGuanLiFragment.onKeyDown(keyCode, event);
        } else if (frgContent instanceof TongXingBaoFragment) {
            TongXingBaoFragment.onKeyDown(keyCode, event);
        }
        //只要点返回就取消发送或接收数据
        BleCommon.getInstance().setCharacteristic(false);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                twoToDefinish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}
