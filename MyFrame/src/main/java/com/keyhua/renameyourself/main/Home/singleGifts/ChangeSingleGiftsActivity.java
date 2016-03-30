package com.keyhua.renameyourself.main.Home.singleGifts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.view.CleareditTextView;

public class ChangeSingleGiftsActivity extends BaseActivity {

    private CleareditTextView ctv_event = null;
    private CleareditTextView ctv_lijin = null;
    private CleareditTextView ctv_liwu = null;
    private CleareditTextView ctv_time = null;
    private TextView tv_ok = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_single_gifts);
        initHeaderOther(bacStr, "修改收礼单", true, false, false);
        init();

    }

    @Override
    protected void onInitData() {
        ctv_event = (CleareditTextView) findViewById(R.id.ctv_event);
        ctv_lijin = (CleareditTextView) findViewById(R.id.ctv_lijin);
        ctv_liwu = (CleareditTextView) findViewById(R.id.ctv_liwu);
        ctv_time = (CleareditTextView) findViewById(R.id.ctv_time);
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
                finish();
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }
}

