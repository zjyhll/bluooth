package com.keyhua.renameyourself.main.personCenter.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;

import java.util.Timer;
import java.util.TimerTask;

public class ForgetPwdActivity extends BaseActivity {
    private CleareditTextView ctv_yzm = null;
    private CleareditTextView ctv_pwd = null;
    private TextView tv_yzm = null;
    private TextView tv_login = null;
    // 手机验证码功能--------------------------------------------------
    private static Integer timerTicket = 0;
    private Handler handler = new Handler();
    private Timer delayTimer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initHeaderOther("找回密码", "", true, false, false);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel timer
        if (delayTimer != null) {
            delayTimer.cancel();
            delayTimer.purge();
            delayTimer = null;
        }
    }

    @Override
    protected void onInitData() {
        ctv_yzm = (CleareditTextView) findViewById(R.id.ctv_yzm);
        ctv_pwd = (CleareditTextView) findViewById(R.id.ctv_pwd);
        tv_yzm = (TextView) findViewById(R.id.tv_yzm);
        tv_login = (TextView) findViewById(R.id.tv_login);
    }

    @Override
    protected void onResload() {
//        top_tv_title.setText("找回密码");
//        top_tv_right.setVisibility(View.GONE);
    }

    @Override
    protected void setMyViewClick() {

//        top_itv_back.setOnClickListener(this);
//        tv_yzm.setOnClickListener(this);
//        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_itv_back:
                finish();
                break;
            case R.id.tv_yzm:
                username = "18202811358";
                tv_yzm.setClickable(false);
                tv_yzm.setBackgroundResource(R.drawable.btn_ok_selector_hui);

                delayTimer = new Timer();
                timerTicket = 60;
                TimerTask delayTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                if (timerTicket <= 0) {
                                    tv_yzm.setText("获取验证码");
                                    tv_yzm.setClickable(true);
                                    tv_yzm.setBackgroundResource(R.drawable.btn_ok_selector_huang);
                                    // Cancel Timer
                                    delayTimer.cancel();
                                    delayTimer.purge();
                                    delayTimer = null;
                                } else {
                                    tv_yzm.setText(timerTicket.toString() + "秒后再次获取");
                                    tv_yzm.setClickable(false);
                                }
                                timerTicket = timerTicket - 1;
                            }
                        });
                    }
                };
                delayTimer.schedule(delayTask, 0, 1000);
                sendAsynSendMSG();
                break;
            case R.id.tv_login:
                oldPass = ctv_pwd.getText().toString();
                code = ctv_yzm.getText().toString();
                if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(oldPass) && !TextUtils.isEmpty(newPass)) {
                    if (TextUtils.equals(oldPass, newPass)) {
                        sendAsyn();
                    } else {
                        showToast("密码不相同");
                    }

                } else {
                    showToast("密码或用户名不能为空");
                }

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

    private String oldPass = "";
    private String newPass = "";

    private String code = "";
    // 服务器返回提示信息
    private Integer state = Integer.valueOf(0);
    private String msgStr = "";

    public void Action() {
    }

    private String username = "";

    //请求服务器发送短信验证码
    public void sendAsynSendMSG() {
        thread = new Thread() {
            public void run() {
                ActionSendMSG();
            }
        };
        thread.start();
    }

    public void ActionSendMSG() {
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
//                            App.getInstance().setAut("");
                            finish();
                            break;
                    }
                    break;
                case CommonUtility.SERVEROK2:
                    // "state":0   // 和msg对应 1：成功，0：失败
                    showToast(msgStr);
                    switch (state) {
                        case 0:
                            break;
                        case 1:
                            break;
                    }
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
                case CommonUtility.SERVEROKYAN:
                    closedialog();
                    tv_yzm.setClickable(true);
                    tv_yzm.setBackgroundResource(R.drawable.btn_ok_selector_huang);
                    showToast("发送验证码上限，请与明日重试");
                    break;
                default:
                    break;
            }
        }
    };
}
