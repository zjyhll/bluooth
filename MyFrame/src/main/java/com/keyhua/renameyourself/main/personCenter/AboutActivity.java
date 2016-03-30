package com.keyhua.renameyourself.main.personCenter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseActivity;

public class AboutActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    private TextView tv_kf = null;
    private AlertView telAlertView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initHeaderOther("", "关于我们", true, false, false);
        init();
    }

    @Override
    protected void onInitData() {
        tv_kf = (TextView) findViewById(R.id.tv_kf);
    }

    @Override
    protected void onResload() {
        telAlertView = new AlertView("温馨提示", "是否拨打客服电话", "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
    }

    @Override
    protected void setMyViewClick() {
        tv_kf.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_kf:
                telAlertView.show();
                break;
            case R.id.toolbar_bac:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == telAlertView) {
            if (position != AlertView.CANCELPOSITION) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "02888888888"));
                startActivity(intent);
            }
        } else {
        }
    }

    @Override
    public void onDismiss(Object o) {

    }

}
