package com.keyhua.renameyourself.main.Home.giftGiving;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.view.CleareditTextView;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

public class NewGiftGivingActivity extends BaseActivity {
    private CleareditTextView ctv_event = null;
    private CleareditTextView ctv_lijin = null;
    private CleareditTextView ctv_liwu = null;
    private CleareditTextView ctv_time = null;
    private CleareditTextView ctv_tips = null;
    private TextView tv_ok = null;
    private TextView tv_time = null;
    private RelativeLayout rl_time = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gift_giving);
        initHeaderOther(bacStr, "新建送礼单", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        ctv_event = (CleareditTextView) findViewById(R.id.ctv_event);
        ctv_lijin = (CleareditTextView) findViewById(R.id.ctv_lijin);
        ctv_liwu = (CleareditTextView) findViewById(R.id.ctv_liwu);
        ctv_tips = (CleareditTextView) findViewById(R.id.ctv_tips);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_time = (TextView) findViewById(R.id.tv_time);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        tv_ok.setOnClickListener(this);
        rl_time.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                finish();
                break;
            case R.id.rl_time:
                final AlertDialog dialog = new AlertDialog.Builder(NewGiftGivingActivity.this).create();
                dialog.show();
                DatePicker picker = new DatePicker(NewGiftGivingActivity.this);
                picker.setDate(2016, 3);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String date) {
                        Toast.makeText(NewGiftGivingActivity.this, date, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setContentView(picker, params);
                dialog.getWindow().setGravity(Gravity.CENTER);
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }
}
