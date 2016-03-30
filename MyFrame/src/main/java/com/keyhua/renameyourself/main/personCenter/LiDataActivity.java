package com.keyhua.renameyourself.main.personCenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;

public class LiDataActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li_data);
        initHeaderOther("", "送礼数据", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }
}
