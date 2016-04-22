package com.keyhua.renameyourself.main;

import org.afinal.simplecache.ACache;
import org.litepal.crud.DataSupport;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.example.importotherlib.R;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.outdoor.protocol.GetUserInfo.GetUserInfoRequest;
import com.keyhua.outdoor.protocol.GetUserInfo.GetUserInfoRequestParameter;
import com.keyhua.outdoor.protocol.GetUserInfo.GetUserInfoResponse;
import com.keyhua.outdoor.protocol.GetUserInfo.GetUserInfoResponsePayload;
import com.keyhua.outdoor.protocol.SetUserChannelAction.SetUserChannelRequest;
import com.keyhua.outdoor.protocol.SetUserChannelAction.SetUserChannelRequestParameter;
import com.keyhua.outdoor.protocol.SetUserChannelAction.SetUserChannelResponse;
import com.keyhua.outdoor.protocol.SetUserChannelAction.SetUserChannelResponsePayload;
import com.keyhua.outdoor.protocol.UserLogin.UserLoginRequest;
import com.keyhua.outdoor.protocol.UserLogin.UserLoginRequestParameter;
import com.keyhua.outdoor.protocol.UserLogin.UserLoginResponse;
import com.keyhua.outdoor.protocol.UserLogin.UserLoginResponsePayload;
import com.keyhua.protocol.exception.ProtocolInvalidMessageException;
import com.keyhua.protocol.exception.ProtocolMissingFieldException;
import com.keyhua.protocol.json.JSONException;
import com.keyhua.protocol.json.JSONObject;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.client.JSONRequestSender;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.NetUtil;
import com.keyhua.renameyourself.view.CleareditTextView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
    // 登录页面
    private CleareditTextView yonghuming = null, mima = null;
    private TextView denglu = null;
    // 用户名
    private String yonghumingStr = null;
    // 密码
    private String mimaStr = null;
    // 关闭
    private ImageView _close = null;
    // login为1的时候是从欢迎界面跳过来的
    private int login = 0;
    private SVProgressHUD mSVProgressHUD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_login);
        mSVProgressHUD = new SVProgressHUD(this);
        init();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.denglu:// 登录
                if (NetUtil.isNetworkAvailable(LoginActivity.this)) {
                    yonghumingStr = yonghuming.getText().toString();
                    mimaStr = mima.getText().toString();
                    if (TextUtils.isEmpty(yonghumingStr)) {// 将参数yonghumingStr与mimaStr传入接口
                        showToast("请输入用户名");
                    } else if (TextUtils.isEmpty(mimaStr)) {
                        showToast("请输入密码");
                    } else {
                        mSVProgressHUD.showWithStatus("登录中...");
                        sendAsyn();
                    }
                } else {
                    showToast(CommonUtility.ISNETCONNECTED);
                }
//                openActivity(MainActivity.class);
//                finish();
                break;
            case R.id.yaoyaole_close:// 关闭页面
                openActivity(IndexActivity.class);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        openActivity(IndexActivity.class);
    }

    @Override
    protected void onInitData() {
        login = getIntent().getIntExtra("login", 0);
        yonghuming = (CleareditTextView) findViewById(R.id.edityonghuming);
        mima = (CleareditTextView) findViewById(R.id.editmima);
        denglu = (TextView) findViewById(R.id.denglu);
        _close = (ImageView) findViewById(R.id.yaoyaole_close);
    }

    @Override
    protected void onResload() {
        if (!TextUtils.isEmpty(App.getInstance().getPhonenum())) {
            yonghuming.setText(App.getInstance().getPhonenum());
        }
    }

    @Override
    protected void setMyViewClick() {
        denglu.setOnClickListener(this);
        _close.setOnClickListener(this);
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

    // 服务器返回提示信息
    private String msgString = null;
    private int State = 0;
    private UserLoginResponsePayload payload = null;

    public void Action() {
        UserLoginRequest request = new UserLoginRequest();
        UserLoginRequestParameter parameter = new UserLoginRequestParameter();
        parameter.setPhoneNumber(yonghumingStr);
        parameter.setPassword(mimaStr);
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
                    UserLoginResponse response = new UserLoginResponse();
                    try {
                        response.fromJSONString(responseObject.toString());
                    } catch (ProtocolInvalidMessageException e) {
                        e.printStackTrace();
                    } catch (ProtocolMissingFieldException e) {
                        e.printStackTrace();
                    }
                    // 处理返回的参数，需要强制类型转换
                    payload = (UserLoginResponsePayload) response
                            .getPayload();
                    msgString = payload.getMsg();
                    State = payload.getLoginState();
                    App.getInstance().setAut(payload.getAut());
                    App.getInstance().setPhonenum(payload.getPhonenum());
                    App.getInstance().setRole(payload.getRole());
                    App.getInstance().setUserid(payload.getUserid());
                    App.getInstance().setHeadurl(payload.getHead());
                    App.getInstance().setNickname(payload.getNickname());

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

    public void sendGetUserAsyn() {
        thread = new Thread() {
            public void run() {
                GetUserAction();
            }
        };
        thread.start();
    }

    public void GetUserAction() {
        GetUserInfoRequest request = new GetUserInfoRequest();
        request.setAuthenticationToken(App.getInstance().getAut());
        GetUserInfoRequestParameter parameter = new GetUserInfoRequestParameter();
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
                    GetUserInfoResponse response = new GetUserInfoResponse();
                    try {
                        response.fromJSONString(responseObject.toString());
                    } catch (ProtocolInvalidMessageException e) {
                        e.printStackTrace();
                    } catch (ProtocolMissingFieldException e) {
                        e.printStackTrace();
                    }
                    // 处理返回的参数，需要强制类型转换
                    GetUserInfoResponsePayload payload = (GetUserInfoResponsePayload) response
                            .getPayload();
                    int isL = payload.getIs_leader() != null ? payload
                            .getIs_leader() : 0;
                    if (isL == 0) {
                        handlerlist.sendEmptyMessage(CommonUtility.SERVEROK3);
                    } else {//领队
                        handlerlist.sendEmptyMessage(CommonUtility.SERVEROK4);
                    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);

    }

    @SuppressLint("HandlerLeak")
    Handler handlerlist = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1:
                    if (State == 1) {
                        sendGetUserAsyn();
                    } else {
                        showToast(msgString);
                    }
                    if (mSVProgressHUD.isShowing()) {
                        mSVProgressHUD.dismiss();
                    }
                    break;
                case CommonUtility.SERVEROK3:
                    showToast("您不是领队");
                    break;
                case CommonUtility.SERVEROK4:
                    if (DataSupport.where("tps_id = ?", "1").find(SignUpUser.class).size() == 0) {
                        SignUpUser s = new SignUpUser();
                        s.setU_nickname(payload.getNickname());
                        s.setPhonenum(payload.getPhonenum());
                        s.setAct_isleave(CommonUtility.GUIDUI);
                        s.setIsUsedByCurrentDevice(CommonUtility.SELF);
                        s.setTps_id(1);
                        s.setTps_type(CommonUtility.LINGDUI);
                        s.setIsUsedByCurrentDevice(CommonUtility.SELF);
                        s.save();
                    }
                    openActivity(MainActivity.class);
                    finish();
                    break;
                case CommonUtility.ChANNELRSERVERERROR:
                    showToast("登录失败，请重新登录");
                    if (isshowdialog()) {
                        closedialog();
                    }
                    break;
                case CommonUtility.SERVERERRORLOGIN:
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
