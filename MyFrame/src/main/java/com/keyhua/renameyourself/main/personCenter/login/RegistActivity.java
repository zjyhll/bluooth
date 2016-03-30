package com.keyhua.renameyourself.main.personCenter.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.view.CleareditTextView;

public class RegistActivity extends BaseActivity {
    private CleareditTextView ctv_phone = null;
    private CleareditTextView ctv_pwd = null;
    private CleareditTextView ctv_pwd_ag = null;
    private TextView tv_ok = null;

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
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }
}
