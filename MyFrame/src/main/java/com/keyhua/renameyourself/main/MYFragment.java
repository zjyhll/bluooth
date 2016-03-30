package com.keyhua.renameyourself.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.importotherlib.R;
import com.keyhua.renameyourself.base.BaseFragment;
import com.keyhua.renameyourself.main.personCenter.AboutActivity;
import com.keyhua.renameyourself.main.personCenter.FeedbackActivity;
import com.keyhua.renameyourself.main.personCenter.LiDataActivity;
import com.keyhua.renameyourself.main.personCenter.MyCollectionActivity;
import com.keyhua.renameyourself.main.personCenter.MyYunActivity;
import com.keyhua.renameyourself.main.personCenter.login.LoginActivity;

public class MYFragment extends BaseFragment {
    private RelativeLayout rl_personcenter = null;
    private RelativeLayout rl_ybf = null;
    private RelativeLayout rl_slzs = null;
    private RelativeLayout rl_wdsc = null;
    private RelativeLayout rl_yjfk = null;
    private RelativeLayout rl_gywm = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    protected void onInitData() {
        rl_personcenter = (RelativeLayout) getActivity().findViewById(R.id.rl_personcenter);
        rl_ybf = (RelativeLayout) getActivity().findViewById(R.id.rl_ybf);
        rl_slzs = (RelativeLayout) getActivity().findViewById(R.id.rl_slzs);
        rl_wdsc = (RelativeLayout) getActivity().findViewById(R.id.rl_wdsc);
        rl_yjfk = (RelativeLayout) getActivity().findViewById(R.id.rl_yjfk);
        rl_gywm = (RelativeLayout) getActivity().findViewById(R.id.rl_gywm);
    }

    @Override
    protected void onResload() {

    }

    @Override
    protected void setMyViewClick() {
        rl_personcenter.setOnClickListener(this);
        rl_ybf.setOnClickListener(this);
        rl_slzs.setOnClickListener(this);
        rl_wdsc.setOnClickListener(this);
        rl_yjfk.setOnClickListener(this);
        rl_gywm.setOnClickListener(this);
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
            case R.id.rl_personcenter:
                //已登录
//                openActivity(PersonCenterActivity.class);
                //未登录
                openActivity(LoginActivity.class);
                break;
            case R.id.rl_ybf:
                openActivity(MyYunActivity.class);
                break;
            case R.id.rl_slzs:
                openActivity(LiDataActivity.class);
                break;
            case R.id.rl_wdsc:
                openActivity(MyCollectionActivity.class);
                break;
            case R.id.rl_yjfk:
                openActivity(FeedbackActivity.class);
                break;
            case R.id.rl_gywm:
                openActivity(AboutActivity.class);
                break;
        }
    }
}
