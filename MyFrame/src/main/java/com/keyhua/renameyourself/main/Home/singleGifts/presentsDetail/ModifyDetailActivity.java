package com.keyhua.renameyourself.main.Home.singleGifts.presentsDetail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.litepal.Event;
import com.keyhua.litepal.GiftList;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.litepal.crud.DataSupport;

public class ModifyDetailActivity extends BaseActivity {


    private CleareditTextView ctv_event = null;
    private CleareditTextView ctv_lijin = null;
    private CleareditTextView ctv_liwu = null;
    private CleareditTextView ctv_time = null;
    private TextView tv_ok = null;
    private long id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_detail);
        id = getIntent().getLongExtra("id", 0);
        initHeaderOther("", "修改收礼单详情", true, false, false);
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
        GiftList e = DataSupport.find(GiftList.class, id);
        ctv_event.setText(e.getGift_person());
        ctv_lijin.setText(e.getGift_money());
        ctv_liwu.setText(e.getGift_remark());
    }

    @Override
    protected void setMyViewClick() {
        tv_ok.setOnClickListener(this);
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
                    GiftList g = new GiftList();
                    g.setGift_person(eventStr);
                    g.setGift_money(giftStr);
                    g.setGift_remark(locationStr);
                    g.update(id);
                    showToast("修改成功");
                    finish();
                }
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }
}