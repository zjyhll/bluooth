package com.keyhua.renameyourself.main.personCenter.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.importotherlib.R;
import com.keyhua.litepal.User;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.MainActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistActivity extends BaseActivity {
    private CleareditTextView ctv_phone = null;
    private CleareditTextView ctv_pwd = null;
    private CleareditTextView ctv_pwd_ag = null;
    private TextView tv_ok = null;
    private String phoneStr = "";
    private String pwdStr = "";
    private String pwd_agStr = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initHeaderOther("注册", "", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {

        ctv_phone = (CleareditTextView) findViewById(R.id.ctv_phone);
        ctv_pwd = (CleareditTextView) findViewById(R.id.ctv_pwd);
        ctv_pwd_ag = (CleareditTextView) findViewById(R.id.ctv_pwd_ag);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        tv_ok.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                phoneStr = ctv_phone.getText().toString();
                pwdStr = ctv_pwd.getText().toString();
                pwd_agStr = ctv_pwd_ag.getText().toString();
                if (TextUtils.isEmpty(phoneStr)) {
                    showToast("请输入手机号码");
                } else if (TextUtils.isEmpty(pwdStr)) {
                    showToast("请输入密码");
                } else if (TextUtils.isEmpty(pwd_agStr)) {
                    showToast("请输入确认密码");
                } else if (!TextUtils.equals(pwdStr, pwd_agStr)) {
                    showToast("两次输入密码不相同");
                } else if (pwd_agStr.length() < 6) {
                    showToast("密码不能小于六位");
                } else if (!CommonUtility.isPhoneNumber(phoneStr)) {
                    showToast("请输入正确的手机号码");
                } else {
//                    User user = new User();
//                    user.setUser_name(phoneStr);
//                    user.setUser_password(pwd_agStr);
//                    user.setUser_uid("1");
//                    user.save();
//                    finish();
//                    openActivity(MainActivity.class);
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("userPhone", phoneStr);
                        obj.put("userPwd", pwd_agStr);
//                        obj.put("userUid", "");
//                        obj.put("userSex", "");
//                        obj.put("userPhone", "18202811358");
//                        obj.put("userLocation", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(CommonUtility.URLsaveUser + obj.toString(), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("TAG", response.toString());
                                    showToast("注册成功");
                                    App.getInstance().setPhonenum(phoneStr);
                                    finish();
                                    openActivity(MainActivity.class);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showToast("注册失败，请重试");
                            Log.e("TAG", error.getMessage(), error);
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }

                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }


}
