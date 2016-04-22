package com.keyhua.renameyourself.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseFragment;

import java.util.ArrayList;

public class LiXianDiTuFragment extends BaseFragment implements
        MKOfflineMapListener {
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private ArrayList<MKOLUpdateElement> localMapListTemp = null;
    private LocalMapAdapter lAdapter = null;
    private MKOfflineMap mOffline = null;
    private TextView news_title = null;
    protected TextView toolbar_tv_right_cancle = null;// 右边文字

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_offline, container, false);
    }

    @Override
    protected void onInitData() {
        toolbar_tv_right_cancle = (TextView) getActivity().findViewById(R.id.toolbar_tv_right_cancle);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
    }

    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        localMapListTemp = mOffline.getAllUpdateInfo();
        if (localMapListTemp != null) {
            localMapList = localMapListTemp;
            lAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResload() {
        news_title = (TextView) getActivity().findViewById(R.id.news_title);
        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }

        ListView localMapListView = (ListView) getActivity().findViewById(R.id.localmaplist);
        if (localMapList.size() == 0) {
            news_title.setVisibility(View.VISIBLE);
        } else {
            news_title.setVisibility(View.GONE);
            lAdapter = new LocalMapAdapter();
            localMapListView.setAdapter(lAdapter);
        }
    }

    @Override
    protected void setMyViewClick() {
        toolbar_tv_right_cancle.setOnClickListener(this);
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
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.top_itv_back:
                break;
            case R.id.top_tv_right:
                openActivity(DownOfflineActivity.class);
            case R.id.toolbar_tv_right_cancle:
                openActivity(DownOfflineActivity.class);
            default:
                break;
        }
    }

    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
            view = View.inflate(getActivity(),
                    R.layout.offline_localmap_list, null);
            initViewItem(view, e);
            return view;
        }

        void initViewItem(View view, final MKOLUpdateElement e) {
            TextView display = (TextView) view.findViewById(R.id.display);
            TextView remove = (TextView) view.findViewById(R.id.remove);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView update = (TextView) view.findViewById(R.id.update);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            ratio.setText(e.ratio + "%");
            title.setText(e.cityName);
            if (e.update) {
                update.setText("可更新");
            } else {
                update.setText("最新");
            }
            if (e.ratio != 100) {
                display.setEnabled(false);
            } else {
                display.setEnabled(true);
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mOffline.remove(e.cityID);
                    updateView();
                }
            });
            display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("x", e.geoPt.longitude);
                    intent.putExtra("y", e.geoPt.latitude);
                    intent.setClass(getActivity(), BaseMapDemo.class);
                    startActivity(intent);
                }
            });
        }

    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        lAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }

    }
}
