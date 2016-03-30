package com.keyhua.renameyourself.main;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.Home.GiftGivingActivity;
import com.keyhua.renameyourself.main.Home.SingleGiftsActivity;

public class HomeFragment extends BaseFragment {
    private TextView tv_sl_times = null;//收礼金次数
    private TextView tv_sl_money = null;//收礼金总额
    private ImageView iv_into_should = null;//进入收礼金列表
    private TextView tv_hl_times = null;//送礼金次数
    private TextView tv_hl_money = null;//送礼金总额
    private ImageView iv_into_songld = null;//进入送礼金列表
    private ImageView iv_into_kbsj = null;//进入商家列表
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void onInitData() {
        tv_sl_times = (TextView) getActivity().findViewById(R.id.tv_sl_times);
        tv_sl_money = (TextView) getActivity().findViewById(R.id.tv_sl_money);
        iv_into_should = (ImageView) getActivity().findViewById(R.id.iv_into_should);
        tv_hl_times = (TextView) getActivity().findViewById(R.id.tv_hl_times);
        tv_hl_money = (TextView) getActivity().findViewById(R.id.tv_hl_money);
        iv_into_songld = (ImageView) getActivity().findViewById(R.id.iv_into_songld);
        iv_into_kbsj = (ImageView) getActivity().findViewById(R.id.iv_into_kbsj);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        iv_into_should.setOnClickListener(this);
        iv_into_songld.setOnClickListener(this);
        iv_into_kbsj.setOnClickListener(this);
    }

    @Override
    protected void headerOrFooterViewControl() {

    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_into_should:
                //收礼单
                openActivity(SingleGiftsActivity.class);
                break;
            case R.id.iv_into_songld:
                //送礼单
                openActivity(GiftGivingActivity.class);
                break;
            case R.id.iv_into_kbsj:
                break;
        }
    }
}
