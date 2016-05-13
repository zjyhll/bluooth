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
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.GpsInfo;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.litepal.SignUpUser;
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
import com.keyhua.renameyourself.view.MyListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class UploadTrajectoryActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    private MyListView lv_home = null;
    private MYAdpter listadapter = null;
    // 服务器返回提示信息
    private ArrayList mers = null;
    private ArrayList mersTemp = null;
    // 轨迹数据库操作
    private List<GpsInfo> mGpSinfoDao;
    private static SVProgressHUD mSVProgressHUD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_trajectory);
        initHeaderOther("", "选择上传轨迹", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        mSVProgressHUD = new SVProgressHUD(this);
        lv_home = (MyListView) findViewById(R.id.lv_home);
        mers = new ArrayList<>();
        mGpSinfoDao = DataSupport.findAll(GpsInfo.class);
        if(mGpSinfoDao.size()==0){
            showTipDialog("当前没有轨迹，是否直接收队？");
        }else{
            listadapter = new MYAdpter(this, mGpSinfoDao);
            lv_home.setAdapter(listadapter);
        }

    }

    @Override
    protected void onResload() {
//        toolbar_right_r.sette
        //默认显示为
        rightButton(View.VISIBLE, "上传");
    }

    @Override
    protected void setMyViewClick() {
        toolbar_tv_right_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.toolbar_tv_right_cancle://上传
                showTipDialog("确认收队？");
                break;
        }
    }

    public class MYAdpter extends BaseAdapter {
        private Context context = null;
        public List<GpsInfo> mGpSinfoDao = null;

        public MYAdpter(Context context, List<GpsInfo> mGpSinfoDao) {
            this.context = context;
            this.mGpSinfoDao = mGpSinfoDao;
        }

        @Override
        public int getCount() {
            return mGpSinfoDao != null ? mGpSinfoDao.size() : 0;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_collection, null);
                holder = new ViewHolder();
                holder.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_tip = (TextView) convertView.findViewById(R.id.tv_tip);
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
            String locationInfo = mGpSinfoDao.get(position).getLocationInfo();// 轨迹信息
            String name = mGpSinfoDao.get(position).getName(); // 轨迹名称必填
            String start_time = mGpSinfoDao.get(position).getStart_time(); // 开始时间
            String end_time = mGpSinfoDao.get(position).getEnd_time(); // /结束时间
            String isChecked = mGpSinfoDao.get(position).getIsChecked();//是否被选择了
            holder.tv_name.setText( "开始时间："+start_time + "\n结束时间：" + end_time);
            holder.tv_tip.setText(name);
            if (!TextUtils.isEmpty(isChecked) && TextUtils.equals("1", isChecked)) {
                holder.iv_check.setImageResource(R.mipmap.collection_c);
            } else {
                holder.iv_check.setImageResource(R.mipmap.collection_n);
            }
            final ViewHolder finalHolder = holder;
            holder.iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String isChecked = mGpSinfoDao.get(position).getIsChecked();//是否被选择了
                    int id = mGpSinfoDao.get(position).getId();
                    if (!TextUtils.isEmpty(isChecked) && TextUtils.equals("1", isChecked)) {//已选
                        finalHolder.iv_check.setImageResource(R.mipmap.collection_n);
                        GpsInfo g = new GpsInfo();
                        g.setIsChecked("2");
                        g.update(id);
                    } else {
                        finalHolder.iv_check.setImageResource(R.mipmap.collection_c);
                        GpsInfo g = new GpsInfo();
                        g.setIsChecked("1");
                        g.update(id);
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private ImageView iv_check = null;//选择按钮
            private TextView tv_name = null;//婚庆名
            private TextView tv_tip = null;//描述
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

    private ArrayList<XUploadTraceDataItem> datalist = null;

    public void Action() {
        XUploadTraceRequest request = new XUploadTraceRequest();
        request.setAuthenticationToken(App.getInstance().getAut());
        XUploadTraceRequestParameter parameter = new XUploadTraceRequestParameter();
        parameter.setDataList(datalist);
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
                    XUploadTraceResponse response = new XUploadTraceResponse();
                    try {
                        response.fromJSONString(responseObject.toString());
                    } catch (ProtocolInvalidMessageException e) {
                        e.printStackTrace();
                    } catch (ProtocolMissingFieldException e) {
                        e.printStackTrace();
                    }
                    // 处理返回的参数，需要强制类型转换
                    XUploadTraceResponsePayload payload = (XUploadTraceResponsePayload) response
                            .getPayload();
                    handlerlist.sendEmptyMessage(CommonUtility.SERVEROK1);
                } else if (ret == 500) {
                    handlerlist.sendEmptyMessage(CommonUtility.KONG);
                }else if (ret == 550) {
                    handlerlist.sendEmptyMessage(CommonUtility.SERVERERROR);
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
                    LitepalUtil.deleteAllBesideLeader();
                    mSVProgressHUD.dismiss();
                    showToast("收队成功");
                    finish();
                    break;
                case CommonUtility.ChANNELRSERVERERROR:
                    showToast("登录失败，请重新登录");
                    if (isshowdialog()) {
                        closedialog();
                    }
                    break;
                case CommonUtility.SERVERERRORLOGIN:
                    mSVProgressHUD.dismiss();
                    break;
                case CommonUtility.SERVERERROR:
                    LitepalUtil.deleteAllBesideLeader();
                    mSVProgressHUD.dismiss();
                    showToast("收队成功");
                    finish();
                    break;
                case CommonUtility.KONG:

                    break;
                default:
                    break;
            }
        }
    };
    private AlertView tiplertView;//是否添加入组成功

    //提示框
    public void showTipDialog(String str) {
        if (tiplertView != null) {
            tiplertView = null;
        }
        tiplertView = new AlertView("温馨提示", str, "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                tiplertView.show();
            }
        }, 0);

    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == tiplertView && position != AlertView.CANCELPOSITION) {
            mSVProgressHUD.showWithStatus("收队中..");
            GpsInfo info = new GpsInfo();
            XUploadTraceDataItem dataDatalistItem = null;
            datalist=new ArrayList<>();
            List<GpsInfo> infos = DataSupport.where("isChecked=?", "1").find(GpsInfo.class);
            for (int i = 0; i < infos.size(); i++) {
                dataDatalistItem = new XUploadTraceDataItem();
                dataDatalistItem.setEnd_time(infos.get(i).getEnd_time());
                dataDatalistItem.setName(infos.get(i).getName());
                dataDatalistItem.setStart_time(infos.get(i).getStart_time());
                dataDatalistItem.setTrace_data(infos.get(i).getLocationInfo());
                datalist.add(dataDatalistItem);
            }
            if (datalist.size() > 0) {
                sendAsyn();
            } else {//为0就直接收队
                handlerlist.sendEmptyMessage(CommonUtility.SERVEROK1);
            }
        }
    }

}
