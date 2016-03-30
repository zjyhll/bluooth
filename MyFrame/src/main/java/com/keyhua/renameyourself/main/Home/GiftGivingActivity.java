package com.keyhua.renameyourself.main.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.Home.giftGiving.ChangeGiftGivingActivity;
import com.keyhua.renameyourself.main.Home.giftGiving.NewGiftGivingActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.DensityUtils;
import com.keyhua.renameyourself.util.NetUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import in.srain.cube.loadmore.LoadMoreContainer;
import in.srain.cube.loadmore.LoadMoreHandler;
import in.srain.cube.loadmore.LoadMoreListViewContainer;
import in.srain.cube.ptr.PtrDefaultHandler;
import in.srain.cube.ptr.PtrFrameLayout;
import in.srain.cube.ptr.PtrHandler;

/**
 * 送礼单
 */
public class GiftGivingActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {

    // 上拉下拉刷新MerchantsListAdpter
    LoadMoreListViewContainer loadMoreListViewContainer = null;
    private SwipeMenuListView lv_home = null;
    private MYAdpter listadapter = null;
    private PtrFrameLayout mPtrFrameLayout;
    public int index = 0;
    public int count = 10;
    // 服务器返回提示信息
    private ArrayList mers = null;
    private ArrayList mersTemp = null;
    private FloatingActionButton fab = null;
    private AlertView deleteAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    //选择条件，时间与金额
    private TextView tv_choice_time = null;
    private TextView tv_choice_money = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_giving);
        initHeaderOther(bacStr, "送礼单", true, true, true);
        init();
    }

    @Override
    protected void onInitData() {
        tv_choice_time = (TextView) findViewById(R.id.tv_choice_time);
        tv_choice_money = (TextView) findViewById(R.id.tv_choice_money);
        deleteAlertView = new AlertView("温馨提示", "确定要删除该条记录吗", "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        //end
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mers = new ArrayList<>();
        lv_home = (SwipeMenuListView) findViewById(R.id.lv_home);
        listadapter = new MYAdpter(this, mers);
        lv_home.setAdapter(listadapter);
        refreshAndLoadMore();
    }

    @Override
    protected void onResload() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(DensityUtils.dp2px(GiftGivingActivity.this, 90));
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
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xef,
                        0x56, 0x56)));
                // set item width
                deleteItem.setWidth((DensityUtils.dp2px(GiftGivingActivity.this, 90)));
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
        fab.setOnClickListener(this);
        tv_choice_time.setOnClickListener(this);
        tv_choice_money.setOnClickListener(this);

        // Right
//        lv_home.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        lv_home.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        openActivity(ChangeGiftGivingActivity.class);
                        break;
                    case 1:
                        // delete
                        deleteAlertView.show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == deleteAlertView) {
        } else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
//                showToast("添加事件");
                openActivity(NewGiftGivingActivity.class);
                break;
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.toolbar_right_l:
                showToast("查询");
                break;
            case R.id.toolbar_right_r:
                showToast("云同步");
                break;
            case R.id.tv_choice_time:
                showToast("通过时间进行筛选");
                break;
            case R.id.tv_choice_money:
                showToast("通过金额进行筛选");
                break;
        }
    }

    private void refreshAndLoadMore() {
        // 上下刷新START--------------------------------------------------------------------
        // 获取装载VIew的容器
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        // 获取view的引用
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        // 使用默认样式
        loadMoreListViewContainer.useDefaultHeader();
        // 加载更多数据，当列表滑动到最底部的时候，触发加载更多操作，
        // 这是需要从网络加载数据，或者是从数据库去读取数据
        // 给View 设置加载更多的Handler 去异步加载View需要显示的数据和VIew
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            // loadMoreListViewContainer调用onLoadMore传入loadMoreListViewContainer自身对象
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                if (NetUtil.isNetworkAvailable(GiftGivingActivity.this)) {// 有网
                    if (listadapter != null) {
                        index = listadapter.getCount();
                    } else {
                        index = 0;
                    }
                    sendAsyn();
                    mHandler.sendEmptyMessage(CommonUtility.ISLOADMORE);
                } else {// 无网
                    mHandler.sendEmptyMessage(CommonUtility.ISNETCONNECTEDInt);
                }
            }
        });
        // load至少刷新多少1秒
        mPtrFrameLayout.setLoadingMinTime(1000);
        // 容器设置异步线程检查是否可以下拉刷新，并且开始下拉刷新用户头
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_home, header);
            }

            // 开始刷新容器开头
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (NetUtil.isNetworkAvailable(GiftGivingActivity.this)) {// 有网
//                    mers.clear();
                    index = 0;
                    sendAsyn();
                    mHandler.sendEmptyMessage(CommonUtility.ISREFRESH);
                } else {// 无网
                    if (isshowdialog()) {
                        closedialog();
                    }
                    mHandler.sendEmptyMessage(CommonUtility.ISNETCONNECTEDInt);
                }
            }
        });
        // auto load data
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mPtrFrameLayout.autoRefresh(true);
                    // mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                }
            }
        }, 150);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 进行判断，是否存在数据
            // loadMoreListViewContainer.loadMoreFinish(mDataModel.getListPageInfo().isEmpty(),
            // mDataModel.getListPageInfo().hasMore());
            loadMoreListViewContainer.loadMoreFinish(false, true);
            switch (msg.what) {
                case CommonUtility.ISREFRESH:// 刷新
                    mPtrFrameLayout.refreshComplete();
                    break;
                case CommonUtility.ISLOADMORE:// 加载更多
                    mPtrFrameLayout.refreshComplete();
                    break;
                case CommonUtility.ISNETCONNECTEDInt:// 下拉刷新无网络时
                    mPtrFrameLayout.refreshComplete();
                    showToastNet();
                    break;
                default:
                    break;
            }
        }
    };

    // 刷新end------------------------------------------------------------------

    private Thread thread = null;

    public void sendAsyn() {
        thread = new Thread() {
            public void run() {
//                Action();
            }
        };
        thread.start();
    }


    @SuppressLint("HandlerLeak")
    Handler handlerlist = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1:
//                    mers.addAll(mersTemp);
                    listadapter.notifyDataSetChanged();
                    break;
                case CommonUtility.SERVEROK2:
                    break;
                case CommonUtility.SERVEROK3:
                    break;
                case CommonUtility.SERVEROK4:
                    break;
                case CommonUtility.ChANNELRSERVERERROR:
                    break;
                case CommonUtility.SERVERERRORLOGIN:
                    showToastLogin();
                    App.getInstance().setAut("");
                    break;
                case CommonUtility.SERVERERROR:
                    break;
                case CommonUtility.KONG:
                    break;
                default:
                    break;
            }
        }
    };


    public class MYAdpter extends BaseAdapter {
        private Context context = null;
        public ArrayList mDatas = null;
        //    private ImageLoader imageLoader = null;
//    private com.nostra13.universalimageloader.core.ImageLoader mImageLoader = null;
        private DisplayImageOptions options;

        public MYAdpter(Context context, ArrayList list) {
            this.context = context;
            this.mDatas = list;
//        this.imageLoader = imageLoader;
//        this.mImageLoader = mImageLoader;
//        this.options = options;
        }

        @Override
        public int getCount() {
//            return mDatas != null ? mDatas.size() : 0;
            return 3;
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
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_num = (TextView) convertView.findViewById(R.id.tv_nums);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.tv_name.setText((String) mDatas.get(position).getCompanyname());
//            holder.tv_time.setText((String) mDatas.get(position).getAddtime());
//            holder.tv_num.setText(mDatas.get(position).getBillnum() + "");
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    return true;
//                }
//            });
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_name = null;
            private TextView tv_time = null;
            private TextView tv_num = null;
        }
    }
}
