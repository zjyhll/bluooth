package com.keyhua.renameyourself.main.personCenter.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.importotherlib.R;
import com.keyhua.litepal.User;
import com.keyhua.renameyourself.alertview.MainActivity;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.SPUtils;
import com.keyhua.renameyourself.view.CircleImageView;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends BaseActivity {
    private CircleImageView civ_header = null;//头像
    private CleareditTextView ctv_login = null;//用户名输入框
    private CleareditTextView ctv_pwd = null;//密码输入框
    private TextView tv_login = null;//登陆按钮
    private TextView tv_forgetpwd = null;//找回密码按钮
    private TextView tv_register = null;//注册账号按钮
    private ImageView iv_wx = null;//微信登陆
    private ImageView iv_qq = null;//qq登陆
    private ImageView iv_wb = null;//微博登陆
    private String phoneStr = "";
    private String pwdStr = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initHeaderOther("登录", "", true, false, false);

        init();
    }

    @Override
    protected void onInitData() {
        civ_header = (CircleImageView) findViewById(R.id.civ_header);
        ctv_login = (CleareditTextView) findViewById(R.id.ctv_login);
        ctv_pwd = (CleareditTextView) findViewById(R.id.ctv_pwd);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_forgetpwd = (TextView) findViewById(R.id.tv_forgetpwd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        iv_wx = (ImageView) findViewById(R.id.iv_wx);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
        iv_wb = (ImageView) findViewById(R.id.iv_wb);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        tv_login.setOnClickListener(this);
        tv_forgetpwd.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        iv_wx.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_wb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:

                phoneStr = ctv_login.getText().toString();
                pwdStr = ctv_pwd.getText().toString();
                if (TextUtils.isEmpty(phoneStr)) {
                    showToast("请输入手机号码");
                } else if (TextUtils.isEmpty(pwdStr)) {
                    showToast("请输入密码");
                } else if (!CommonUtility.isPhoneNumber(phoneStr)) {
                    showToast("请输入正确的手机号码");
                } else {
//                    List<User> userList = DataSupport.findAll(User.class);
////                    User userList = DataSupport.find(User.class,1);
//                    for (int i = 0; i < userList.size(); i++) {
//                        if (TextUtils.equals(userList.get(i).getUser_name(), phoneStr) && TextUtils.equals(userList.get(i).getUser_password(), pwdStr)) {
//                            App.getInstance().setUserid(userList.get(i).getUser_uid());
//                            App.getInstance().setUserName(phoneStr);
//                            showToast("登陆成功");
////                            finish();
//                        } else {
//                            showToast("用户名或密码错误");
//                        }
//                    }

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("phone", phoneStr);
                        obj.put("pwd", pwdStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(CommonUtility.URLcheckUser + obj.toString(), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", response.toString());
                                    showToast("登陆成功");
                                    App.getInstance().setPhonenum(phoneStr);
                                    finish();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                            showToast("用户名或密码错误");
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
                break;
            case R.id.tv_forgetpwd:
                openActivity(ForgetPwdActivity.class);
                break;
            case R.id.tv_register:
                openActivity(RegistActivity.class);
                break;
            case R.id.iv_wx:
                break;
            case R.id.iv_qq:
                break;
            case R.id.iv_wb:
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }
}
