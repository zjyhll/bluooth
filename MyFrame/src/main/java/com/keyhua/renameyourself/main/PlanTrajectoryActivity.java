package com.keyhua.renameyourself.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.example.importotherlib.R;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.PlanGpsInfo;
import com.keyhua.outdoor.protocol.XGetUploadTraceListAction.XGetUploadTraceListDatalistItem;
import com.keyhua.outdoor.protocol.XGetUploadTraceListAction.XGetUploadTraceListRequest;
import com.keyhua.outdoor.protocol.XGetUploadTraceListAction.XGetUploadTraceListRequestParameter;
import com.keyhua.outdoor.protocol.XGetUploadTraceListAction.XGetUploadTraceListResponse;
import com.keyhua.outdoor.protocol.XGetUploadTraceListAction.XGetUploadTraceListResponsePayload;
import com.keyhua.outdoor.protocol.XUploadTraceAction.XUploadTraceDataItem;
import com.keyhua.outdoor.protocol.XUploadTraceAction.XUploadTraceRequest;
import com.keyhua.outdoor.protocol.XUploadTraceAction.XUploadTraceRequestParameter;
import com.keyhua.outdoor.protocol.XUploadTraceAction.XUploadTraceResponse;
import com.keyhua.outdoor.protocol.XUploadTraceAction.XUploadTraceResponsePayload;
import com.keyhua.protocol.exception.ProtocolInvalidMessageException;
import com.keyhua.protocol.exception.ProtocolMissingFieldException;
import com.keyhua.protocol.json.JSONException;
import com.keyhua.protocol.json.JSONObject;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.client.JSONRequestSender;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;
import com.keyhua.renameyourself.view.MyListView;

import java.util.ArrayList;

public class PlanTrajectoryActivity extends BaseActivity {
    private CleareditTextView search_edit = null;
    private TextView search = null;
    private MyListView lv_home = null;
    private MYAdpter listadapter = null;
    private PhotoView pv = null;
    private FrameLayout parent_pic = null;
    PlanGpsInfo p=null;//已选计划轨迹
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trajectory);
        initHeaderOther("", "选择计划轨迹", true, false, false);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        p = LitepalUtil.getpg();
//        updateList();
    }

    public void updateList() {
        mers.addAll(mersTemp);
        nameStr = "";
        search_edit.setText("");
        listadapter.notifyDataSetChanged();
    }

    @Override
    protected void onInitData() {
        search_edit = (CleareditTextView) findViewById(R.id.search_edit);
        search = (TextView) findViewById(R.id.search);
        pv = (PhotoView) findViewById(R.id.pv);
        parent_pic = (FrameLayout) findViewById(R.id.parent_pic);
        lv_home = (MyListView) findViewById(R.id.lv_home);
        mers = new ArrayList<>();
        listadapter = new MYAdpter(this, mers);
        lv_home.setAdapter(listadapter);
        sendAsyn();
    }

    @Override
    protected void onResload() {
// 启用图片缩放功能
        pv.enable();
    }

    @Override
    protected void setMyViewClick() {
        search.setOnClickListener(this);
        pv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.pv:
                parent_pic.setVisibility(View.GONE);
                break;
            case R.id.search:
                String search_editStr = search_edit.getText().toString();
                if (TextUtils.isEmpty(search_editStr)) {
                    nameStr = "";
                }
                mers.clear();
                nameStr = search_editStr;
                sendAsyn();
                break;
        }
    }

    public class MYAdpter extends BaseAdapter {
        private Context context = null;
        public ArrayList<XGetUploadTraceListDatalistItem> mers = null;

        public MYAdpter(Context context, ArrayList<XGetUploadTraceListDatalistItem> mers) {
            this.context = context;
            this.mers = mers;
        }

        @Override
        public int getCount() {
            return mers != null ? mers.size() : 0;
//            return 3;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_plan, null);
                holder = new ViewHolder();
                holder.tv_gjm = (TextView) convertView.findViewById(R.id.tv_gjm);
                holder.btn_xz = (TextView) convertView.findViewById(R.id.btn_xz);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_gjm.setText(mers.get(position).getName());
            if(p!=null&&p.getTrace_id()!=null&&mers.get(position).getTrace_id()==p.getTrace_id()){
                holder.btn_xz.setText("已选择");
                holder.btn_xz.setClickable(false);
                holder.btn_xz.setBackgroundResource(R.drawable.btn_ok_selector_hui);
            }else{
                holder.btn_xz.setText("选择");
                holder.btn_xz.setBackgroundResource(R.drawable.btn_ok_selector);
                holder.btn_xz.setClickable(true);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent_pic.setVisibility(View.VISIBLE);
                    String picture_url = mers.get(position).getPicture_url();
                    Glide.with(context)
                            .load(picture_url)
                            .into(pv);

                }
            });
            holder.btn_xz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LitepalUtil.deletePlanGps();
                    String picture_url = mers.get(position).getPicture_url();
                    String end_time = mers.get(position).getEnd_time();
                    String name = mers.get(position).getName();
                    String trace_data = mers.get(position).getTrace_data();
                    String start_time = mers.get(position).getStart_time();
                    Integer trace_id = mers.get(position).getTrace_id();
                    PlanGpsInfo p = new PlanGpsInfo();
                    p.setLocationInfo(trace_data);
                    p.setName(name);
                    p.setEnd_time(end_time);
                    p.setPicture_url(picture_url);
                    p.setStart_time(start_time);
                    p.setTrace_id(trace_id);
                    p.save();
                    finish();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView tv_gjm = null;//婚庆名
            private TextView btn_xz = null;//描述
        }
    }

    private Thread thread = null;

    // 用户上传轨迹文件
    public void sendAsyn() {
        thread = new Thread() {
            public void run() {
                Action();
            }
        };
        thread.start();
    }

    // 服务器返回提示信息
    private ArrayList<XGetUploadTraceListDatalistItem> mers = null;
    private ArrayList<XGetUploadTraceListDatalistItem> mersTemp = null;
    private String nameStr = "";

    public void Action() {
        XGetUploadTraceListRequest request = new XGetUploadTraceListRequest();
        request.setAuthenticationToken(App.getInstance().getAut());
        XGetUploadTraceListRequestParameter parameter = new XGetUploadTraceListRequestParameter();
        parameter.setIndex(0);
        parameter.setCount(100);
        parameter.setName(nameStr);
        request.setParameter(parameter);

        String requestString = null;
        try {
            requestString = request.toJSONString();
        } catch (ProtocolMissingFieldException e) {
            e.printStackTrace();
        }
        String requestUrl = CommonUtility.URL;
        JSONRequestSender sender = new JSONRequestSender(requestUrl);
        JSONObject responseObject = null;
        try {
            responseObject = sender.send(new JSONObject(requestString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (responseObject != null) {
            try {
                int ret = responseObject.getInt("ret");
                if (ret == 0) {
                    XGetUploadTraceListResponse response = new XGetUploadTraceListResponse();
                    try {
                        response.fromJSONString(responseObject.toString());
                    } catch (ProtocolInvalidMessageException e) {
                        e.printStackTrace();
                    } catch (ProtocolMissingFieldException e) {
                        e.printStackTrace();
                    }
                    // 处理返回的参数，需要强制类型转换
                    XGetUploadTraceListResponsePayload payload = (XGetUploadTraceListResponsePayload) response
                            .getPayload();
                    mersTemp = payload.getDatalist();
                    handlerlist.sendEmptyMessage(CommonUtility.SERVEROK1);
                } else if (ret == 500) {
                    handlerlist.sendEmptyMessage(CommonUtility.KONG);
                } else if (ret == 5011) {
                    handlerlist
                            .sendEmptyMessage(CommonUtility.SERVERERRORLOGIN);
                } else {
                    handlerlist.sendEmptyMessage(CommonUtility.SERVERERROR);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } else {
            handlerlist.sendEmptyMessage(CommonUtility.SERVERERROR);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handlerlist = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1:
                    updateList();
                    break;
                case CommonUtility.ChANNELRSERVERERROR:

                    break;
                case CommonUtility.SERVERERRORLOGIN:
                    showToast("登录失败，请重新登录");
                    if (isshowdialog()) {
                        closedialog();
                    }
                    Bundle b = new Bundle();
                    b.putString("autnotok", "1");
                    openActivity(LoginActivity.class, b);
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
}
