package com.keyhua.renameyourself.main.personCenter.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.MainActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.json.JSONException;
import org.json.JSONObject;

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

    private String newPass = "";

    private String code = "";

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
        tv_yzm.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_itv_back:
                finish();
                break;
            case R.id.tv_yzm:
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
                JSONObject obj = new JSONObject();
                try {
                    obj.put("phone", "18202811358");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //验证码
                sendDataToServer(CommonUtility.SERVEROK1, CommonUtility.URLsendPhoneCode, obj.toString());
                break;
            case R.id.tv_login:
                newPass = ctv_pwd.getText().toString();
                code = ctv_yzm.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                } else if (TextUtils.isEmpty(newPass)) {
                    showToast("请输入新密码");
                } else {

                    JSONObject obj1 = new JSONObject();
                    try {
                        obj1.put("code", code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //用户中心检测手机验证码是否正确接口
                    sendDataToServer(CommonUtility.SERVEROK3, CommonUtility.URLcheckPhoneCode, obj1.toString());
                }

                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }

    /**
     * 发送数据到后台
     *
     * @param which   具体哪个按钮的操作
     * @param strdata 发送给后台的数据
     * @param url     对应的url
     */
    public void sendDataToServer(final int which, String url, String strdata) {

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(url + strdata, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        switch (which) {
                            case CommonUtility.SERVEROK1://验证码
                                showToast("发送成功");
                                break;
                            case CommonUtility.SERVEROK2:
                                showToast("修改成功");
                                finish();
                                openActivity(MainActivity.class);
                                break;
                            case CommonUtility.SERVEROK3:
                                JSONObject obj1 = new JSONObject();
                                try {
                                    obj1.put("userPhone", "18202811358");
                                    obj1.put("userPwd", newPass);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //修改密码
                                sendDataToServer(CommonUtility.SERVEROK2, CommonUtility.URLsaveUser, obj1.toString());
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                switch (which) {
                    case CommonUtility.SERVEROK1:
                        showToast("发送失败");
                        break;
                    case CommonUtility.SERVEROK2:
                        showToast("修改失败，请重试");
                        break;
                    case CommonUtility.SERVEROK3:
                        showToast("验证码错误，请重新获取");
                        break;
                }
            }
        });
        requestQueue.add(jsonObjectRequest1);
    }


}
