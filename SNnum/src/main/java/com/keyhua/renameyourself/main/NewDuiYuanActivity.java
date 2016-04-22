package com.keyhua.renameyourself.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.litepal.SignUpUser;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.view.CleareditTextView;

import org.litepal.crud.DataSupport;

public class NewDuiYuanActivity extends BaseActivity {
    private CleareditTextView ctv_event = null;//队员名
    private CleareditTextView ctv_lijin = null;//队员电话
    private TextView tv_snnum = null;//sn
    private TextView tv_snbtn = null;//sn
    private TextView tv_ok = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_duiyuan);
        initHeaderOther(bacStr, "添加队员", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        ctv_event = (CleareditTextView) findViewById(R.id.ctv_event);
        ctv_lijin = (CleareditTextView) findViewById(R.id.ctv_lijin);
        tv_snnum = (TextView) findViewById(R.id.tv_snnum);
        tv_snbtn = (TextView) findViewById(R.id.tv_snbtn);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        tv_ok.setOnClickListener(this);
        tv_snbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                //姓名
                String eventStr = ctv_event.getText().toString();
                //电话
                String giftStr = ctv_lijin.getText().toString();
                //sn
                String locationStr = tv_snnum.getText().toString();
                if (TextUtils.isEmpty(eventStr)) {
                    showToast("队员姓名为必填项");
                } else {
                    int numInt = 0;
                    try {
                        numInt = DataSupport.findLast(SignUpUser.class).getTps_id() + 1;//拿到最后一个队员的组内id
                    } catch (Exception e) {
                        numInt = 2;
                    }

                    SignUpUser event = new SignUpUser();
                    event.setU_nickname(eventStr);
                    event.setPhonenum(giftStr);
                    event.setStrDeviceSN(locationStr);
                    event.setAct_isleave(CommonUtility.GUIDUI);//默认为归队
                    event.setTps_id(numInt);
                    event.setTps_type(CommonUtility.DUIYUAN);
                    if (numInt == 2) {
                        event.setUser_longitude("104.06");
                        event.setUser_latitude("30.67");
                    }  if (numInt == 3) {
                        event.setUser_longitude("104.067329");
                        event.setUser_latitude("30.482487");
                    }
                    event.save();
                    showToast("添加成功");
                    finish();
                }

                break;
            case R.id.toolbar_bac:
                finish();
                break;
            case R.id.tv_snbtn:
                showToast("跳入扫描界面");
                break;
        }
    }
}

