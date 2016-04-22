package com.keyhua.renameyourself.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.litepal.LitepalUtil;
import com.keyhua.renameyourself.alertview.*;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.CommonUtility;

public class IndexActivity extends BaseActivity {
    private TextView tv_ld = null;
    private TextView tv_dy = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initHeaderOther("", "户外同行", false, false, false);
        if (LitepalUtil.getAllUser().size() > 0) {
            openActivity(MainActivity.class);
            finish();
        } else {
            init();
        }

    }

    @Override
    protected void onInitData() {
        tv_ld = (TextView) findViewById(R.id.tv_ld);
        tv_dy = (TextView) findViewById(R.id.tv_dy);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        tv_ld.setOnClickListener(this);
        tv_dy.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ld:
                App.getInstance().setIs_leader(CommonUtility.LINGDUI);
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.tv_dy:
                App.getInstance().setIs_leader(CommonUtility.DUIYUAN);
                openActivity(MainActivity.class);
                finish();
                break;
        }
    }
}
