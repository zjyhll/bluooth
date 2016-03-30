package com.keyhua.renameyourself.main.personCenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.KeyBoardUtils;

public class FeedbackActivity extends BaseActivity {
    private EditText et_feedback = null;
    private TextView tv_commit = null;
    private ScrollView scroll_view = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initHeaderOther("", "意见反馈", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);
    }

    @Override
    protected void onResload() {
//        top_tv_title.setText("意见反馈");
//        top_tv_right.setVisibility(View.GONE);
    }

    @Override
    protected void setMyViewClick() {
//        top_itv_back.setOnClickListener(this);
//        tv_commit.setOnClickListener(this);
//        scroll_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_itv_back:
                finish();
                break;
            case R.id.tv_commit:
                content = et_feedback.getText().toString();
                sendAsyn();
                break;
            case R.id.scroll_view:
                showToast("点击");
                KeyBoardUtils.openKeybord(et_feedback, FeedbackActivity.this);
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }

    private Thread thread = null;

    public void sendAsyn() {
        thread = new Thread() {
            public void run() {
                Action();
            }
        };
        thread.start();
    }

    private String content = "";
    // 服务器返回提示信息
    private Integer state = Integer.valueOf(0);
    private String msgStr = "";

    public void Action() {
    }

    @SuppressLint("HandlerLeak")
    Handler handlerlist = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1:
                    // "state":0   // 和msg对应 1：成功，0：失败
                    showToast(msgStr);
                    switch (state) {
                        case 0:

                            break;
                        case 1:
                            finish();
                            break;
                    }
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
//                    App.getInstance().setAut("");
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
