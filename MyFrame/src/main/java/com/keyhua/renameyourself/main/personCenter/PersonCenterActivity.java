package com.keyhua.renameyourself.main.personCenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.importotherlib.R;
import com.keyhua.renameyourself.app.App;
import com.keyhua.renameyourself.base.BaseActivity;
import com.keyhua.renameyourself.main.personCenter.upUtil.HttpMultipartPost;
import com.keyhua.renameyourself.util.CommonUtility;
import com.keyhua.renameyourself.util.FastBlur;
import com.keyhua.renameyourself.util.ImageControl;
import com.keyhua.renameyourself.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.nereo.multi_image_selector.MultiImageSelectorActivity_Header;


public class PersonCenterActivity extends BaseActivity {
    //整个大的背景图
    private ImageView iv_header_bac = null;
    //小圆头像
    private CircleImageView civ_header = null;
    //名字
    private TextView tv_name = null;
    //
    private RelativeLayout rl_fd = null;
    private RelativeLayout rl_pwd = null;
    private RelativeLayout rl_out = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        initHeaderOther("", "", true, false, false);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (!TextUtils.isEmpty(App.getInstance().getSelectPath())
//                && !TextUtils.isEmpty(App.getInstance().getSelectPathTemp())) {
//            mImageLoader.displayImage("file://" + App.getInstance().getSelectPathTemp(), civ_header);
//            if (NetUtil.isNetworkAvailable(PersonCenterActivity.this)) {
//                if (!TextUtils.equals(App.getInstance().getSelectPath(), App.getInstance().getSelectPathTemp())) {
//                    handlerlist.sendEmptyMessage(CommonUtility.UPLOADING);
//                }
//            } else {
//                showToastNet();
//            }
//        }
        if (TextUtils.isEmpty(App.getInstance().getAut())) {
//            openActivity(LoginActivity.class);
//            finish();
        }
    }

    @Override
    protected void onInitData() {
        iv_header_bac = (ImageView) findViewById(R.id.iv_header_bac);
        civ_header = (CircleImageView) findViewById(R.id.civ_header);
        tv_name = (TextView) findViewById(R.id.tv_name);
        rl_fd = (RelativeLayout) findViewById(R.id.rl_fd);
        rl_pwd = (RelativeLayout) findViewById(R.id.rl_pwd);
        rl_out = (RelativeLayout) findViewById(R.id.rl_out);
    }

    @Override
    protected void onResload() {
//        top_tv_right.setVisibility(View.GONE);
//        top_tv_title.setText("个人中心");
        String name = App.getInstance().getUserName();
        String heard = App.getInstance().getHeadurl();
        if (!TextUtils.isEmpty(name)) {
            tv_name.setText(name);
        }
        if (!TextUtils.isEmpty(heard)) {
            Glide.with(this)
                    .load(heard)
                    .error(R.mipmap.app_logo)
                    .into(civ_header);
        }

    }

    @Override
    protected void setMyViewClick() {
        rl_fd.setOnClickListener(this);
        rl_pwd.setOnClickListener(this);
        rl_out.setOnClickListener(this);
        civ_header.setOnClickListener(this);
//        top_itv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fd:
//                openActivity(ProfilesActivity.class);
                break;
            case R.id.rl_pwd:
//                openActivity(ChangePwdActivity.class);
                break;
            case R.id.rl_out:
                sendAsyn();
                break;
            case R.id.civ_header:
                Intent intent = new Intent(PersonCenterActivity.this, MultiImageSelectorActivity_Header.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity_Header.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity_Header.EXTRA_SELECT_COUNT, 1);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity_Header.EXTRA_SELECT_MODE,
                        MultiImageSelectorActivity_Header.MODE_SINGLE);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.top_itv_back:
                finish();
                break;
        }
    }

    /**
     * 得到选择的照片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> mSelectPath =
                        data.getStringArrayListExtra(MultiImageSelectorActivity_Header.EXTRA_RESULT);
//                mImageLoader.displayImage("file://" + mSelectPath.get(0), civ_header);
//                mImageLoader.displayImage("file://" + mSelectPath.get(0), iv_header_bac);
                Glide.with(this)
                        .load("file://" + mSelectPath.get(0))
                        .into(civ_header);
                try {
                    iv_header_bac.setImageBitmap(ImageControl.getBitmaptz(mSelectPath.get(0)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                applyBlur();

            }
        }
    }


    private void applyBlur() {
        iv_header_bac.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                iv_header_bac.getViewTreeObserver().removeOnPreDrawListener(this);
                iv_header_bac.buildDrawingCache();
                Bitmap bmp = iv_header_bac.getDrawingCache();
                blur(bmp, iv_header_bac);
                return true;
            }
        });
    }

    private void blur(Bitmap bkg, ImageView view) {
        Bitmap overlay = null;
        try {
            float scaleFactor = 8;
            float radius = 2;
            overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                    (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);
            overlay = FastBlur.doBlur(overlay, (int) radius, true);
            view.setImageBitmap(overlay);
        } catch (Exception e) {
        }
    }

    private Thread thread = null;

    public void sendAsyn() {
        thread = new Thread() {
            public void run() {
                Action();
            }
        };
        thread.start();
    }

    private String oldPass = "";
    private String newPass = "";
    // 服务器返回提示信息
    private Integer state = Integer.valueOf(0);
    private String msgStr = "";

    public void Action() {
//        LoginOutRequest request = new LoginOutRequest();
//        LoginOutRequestParameter parameter = new LoginOutRequestParameter();
//        request.setAuthenticationToken(App.getInstance().getAut());
//        request.setParameter(parameter);
//        String requestString = null;
//        try {
//            requestString = request.toJSONString();
//        } catch (ProtocolMissingFieldException e) {
//            e.printStackTrace();
//        }
//        String requestUrl = CommonUtility.URL;
//        JSONRequestSender sender = new JSONRequestSender(requestUrl);
//        com.keyhua.protocol.json.JSONObject responseObject = null;
//        try {
//            responseObject = sender.send(new com.keyhua.protocol.json.JSONObject(requestString));
//        } catch (com.keyhua.protocol.json.JSONException e) {
//            e.printStackTrace();
//        }
//        if (responseObject != null) {
//            try {
//                int ret = responseObject.getInt("ret");
//                if (ret == 0) {
//                    LoginOutResponse response = new LoginOutResponse();
//                    try {
//                        response.fromJSONString(responseObject.toString());
//                    } catch (ProtocolInvalidMessageException e) {
//                        e.printStackTrace();
//                    } catch (ProtocolMissingFieldException e) {
//                        e.printStackTrace();
//                    }
//                    // 处理返回的参数，需要强制类型转换
//                    LoginOutResponsePayload payload = (LoginOutResponsePayload) response.getPayload();
//                    state = payload.getState();
//                    msgStr = payload.getMsg();
//                    handlerlist.sendEmptyMessage(CommonUtility.SERVEROK1);
//                } else if (ret == 500) {
//                    handlerlist.sendEmptyMessage(CommonUtility.KONG);
//                } else if (ret == 5011) {
//                    handlerlist.sendEmptyMessage(CommonUtility.SERVERERRORLOGIN);
//                } else {
//                    handlerlist.sendEmptyMessage(CommonUtility.SERVERERROR);
//                }
//            } catch (com.keyhua.protocol.json.JSONException e1) {
//                e1.printStackTrace();
//            }
//        } else {
//            handlerlist.sendEmptyMessage(CommonUtility.SERVERERROR);
//        }
    }

    // 上传
    private HttpMultipartPost post;
    private String result = null;
    private String head = null;// 头像url
    @SuppressLint("HandlerLeak")
    Handler handlerlist = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommonUtility.SERVEROK1://退出登录
                    // "state":0   // 和msg对应 1：成功，0：失败
                    showToast(msgStr);
                    switch (state) {
                        case 0:

                            break;
                        case 1:
                            App.getInstance().setAut("");
                            finish();
                            break;
                    }
                    break;
                case CommonUtility.SERVERERRORLOGIN:
                    showToastLogin();
                    App.getInstance().setAut("");
//                    openActivity(LoginActivity.class);
                    break;
                case CommonUtility.SERVERERROR:
                    break;
                case CommonUtility.KONG:
                    break;
                case CommonUtility.UPLOADING:
                    if (!TextUtils.isEmpty(App.getInstance().getSelectPathTemp())) {
                        File file = new File(App.getInstance().getSelectPathTemp());
                        if (file.exists()) {
                            post = new HttpMultipartPost(PersonCenterActivity.this, App.getInstance().getSelectPathTemp());
                            try {
                                result = post.execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        head = jsonObject.getString("fullurl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case CommonUtility.UPLOADINGCANCLE:
                    if (post != null) {
                        if (!post.isCancelled()) {
                            post.cancel(true);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
