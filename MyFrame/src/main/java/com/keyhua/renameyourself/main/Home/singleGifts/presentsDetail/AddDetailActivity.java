package com.keyhua.renameyourself.main.Home.singleGifts.presentsDetail;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.importotherlib.R;
import com.keyhua.litepal.Event;
import com.keyhua.litepal.GiftList;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

public class AddDetailActivity extends BaseActivity {


    private CleareditTextView ctv_event = null;
    private CleareditTextView ctv_lijin = null;
    private CleareditTextView ctv_liwu = null;
    private CleareditTextView ctv_time = null;
    private TextView tv_ok = null;
    private TextView tv_time = null;
    private RelativeLayout rl_time = null;
    private int year = 0;
    private int month = 0;
    private int date = 0;
    private String timeStr = "";
    private long id = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        id = getIntent().getLongExtra("id", 0);
        initHeaderOther("", "添加收礼", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        year = t.year;
        month = t.month + 1;
        date = t.monthDay;
        timeStr = year + "-" + month + "-" + date;
        ctv_event = (CleareditTextView) findViewById(R.id.ctv_event);
        ctv_lijin = (CleareditTextView) findViewById(R.id.ctv_lijin);
        ctv_liwu = (CleareditTextView) findViewById(R.id.ctv_liwu);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_time = (TextView) findViewById(R.id.tv_time);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
    }

    @Override
    protected void onResload() {
        tv_time.setText(timeStr);
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
                //收礼人物
                String eventStr = ctv_event.getText().toString();
                //收礼类型、备注
                String giftStr = ctv_lijin.getText().toString();
                //收礼地点
                String locationStr = ctv_liwu.getText().toString();
                if (TextUtils.isEmpty(eventStr)) {
                    showToast("收礼人物为必填项");
                } else {
                    GiftList g = new GiftList();
                    g.setGift_person(eventStr);
                    g.setGift_money(giftStr);
                    g.setGift_remark(locationStr);
                    g.setEvent_uid(String.valueOf(id));
                    g.setGift_status(CommonUtility.TYPEGIFTGIVING_WHL);
                    g.save();
                    showToast("添加成功");
                    finish();
                }
                break;
            case R.id.rl_time:
                final AlertDialog dialog = new AlertDialog.Builder(AddDetailActivity.this).create();
                dialog.show();
                DatePicker picker = new DatePicker(AddDetailActivity.this);
                picker.setDate(year, month);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String date) {
                        timeStr = date;
                        tv_time.setText(timeStr);
                        Toast.makeText(AddDetailActivity.this, date, Toast.LENGTH_LONG).show();
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