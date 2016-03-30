package com.keyhua.renameyourself.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BackHandledInterface;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.base.BaseFragment;

public class MainActivity extends BaseActivity implements
        BackHandledInterface {

    private BaseFragment mBackHandedFragment;
    private Fragment frgContent = null;// 容器
    private String title = "";

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    public void switchConent(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        toolbar_title.setText(title);
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                twoToDefinish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private RadioButton radiobutton_select_one = null;
    private RadioButton radiobutton_select_two = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initHeaderOther("", "礼金理", false, false, false);
        init();
        // 打开页面显示的fragment
        frgContent = new HomeFragment();
        switchConent(frgContent, "礼金理");

    }

    @Override
    protected void onInitData() {
        radiobutton_select_one = (RadioButton) findViewById(R.id.radiobutton_select_one);
        radiobutton_select_two = (RadioButton) findViewById(R.id.radiobutton_select_two);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        radiobutton_select_one.setOnClickListener(this);
        radiobutton_select_two.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radiobutton_select_one:
                frgContent = new HomeFragment();
                title = "礼金理";
                break;
            case R.id.radiobutton_select_two:
                frgContent = new MYFragment();
                title = "我的";
                break;
        }
        if (frgContent != null) {
            switchConent(frgContent, title);
        }
    }

}
