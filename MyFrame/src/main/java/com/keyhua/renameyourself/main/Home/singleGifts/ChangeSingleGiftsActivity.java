package com.keyhua.renameyourself.main.Home.singleGifts;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.importotherlib.R;
import com.keyhua.litepal.Event;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.litepal.crud.DataSupport;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

public class ChangeSingleGiftsActivity extends BaseActivity {

    private CleareditTextView ctv_event = null;
    private CleareditTextView ctv_lijin = null;
    private CleareditTextView ctv_liwu = null;
    private TextView ctv_time = null;
    private TextView tv_ok = null;
    private int year = 0;
    private int month = 0;
    private int date = 0;
    private String timeStr = "";
    private long id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_single_gifts);
        id = getIntent().getLongExtra("id", 0);
        initHeaderOther(bacStr, "修改收礼单", true, false, false);
        init();

    }

    @Override
    protected void onInitData() {

        //时间
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        year = t.year;
        month = t.month + 1;
        date = t.monthDay;
        timeStr = year + "-" + month + "-" + date;
        //控件
        ctv_event = (CleareditTextView) findViewById(R.id.ctv_event);
        ctv_lijin = (CleareditTextView) findViewById(R.id.ctv_lijin);
        ctv_liwu = (CleareditTextView) findViewById(R.id.ctv_liwu);
        ctv_time = (TextView) findViewById(R.id.ctv_time);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
    }

    @Override
    protected void onResload() {
        //由id查询当前事件
        Event e = DataSupport.find(Event.class, id);
        ctv_event.setText(e.getEvent_name());
        ctv_time.setText(e.getEvent_time());
        ctv_lijin.setText(e.getEvent_remarks());
        ctv_liwu.setText(e.getEvent_location());
    }

    @Override
    protected void setMyViewClick() {
        tv_ok.setOnClickListener(this);
        ctv_time.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                //收礼人物
                String eventStr = ctv_event.getText().toString();
                //收礼类型
                String giftStr = ctv_lijin.getText().toString();
                //收礼地点
                String locationStr = ctv_liwu.getText().toString();
                if (TextUtils.isEmpty(eventStr)) {
                    showToast("收礼人物为必填项");
                } else {
                    Event event = new Event();
                    event.setEvent_location(locationStr);
                    event.setEvent_remarks(giftStr);
                    event.setEvent_name(eventStr);
                    event.setEvent_time(timeStr);
                    event.update(id);
                    showToast("修改成功");
                    finish();
                }

                break;
            case R.id.ctv_time:
                final AlertDialog dialog = new AlertDialog.Builder(ChangeSingleGiftsActivity.this).create();
                dialog.show();
                DatePicker picker = new DatePicker(ChangeSingleGiftsActivity.this);
                picker.setDate(year, month);
                picker.setMode(DPMode.SINGLE);
                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String date) {
                        timeStr = date;
                        ctv_time.setText(timeStr);
                        Toast.makeText(ChangeSingleGiftsActivity.this, date, Toast.LENGTH_LONG).show();
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

