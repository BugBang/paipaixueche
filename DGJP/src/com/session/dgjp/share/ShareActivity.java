package com.session.dgjp.share;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseRequestTask;
import com.session.common.utils.TextUtil;
import com.session.dgjp.Constants;
import com.session.dgjp.R;
import com.session.dgjp.enity.ShareData;
import com.session.dgjp.request.ShareDataRequestData;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by user on 2016-12-20.
 */
public class ShareActivity extends BaseActivity implements PlatformActionListener {

    private View inflate;
    private Dialog dialog;
    private Gson mGson;
    private String mTitle;
    private String mMessage;
    private String mImage;
    private String mUrl;
    private boolean mShareByWechat;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_share);
        ShareSDK.initSDK(this);
        TextView tvTuiJian1 = (TextView) findViewById(R.id.tv_tuijian1);
        TextView tvTuiJian2 = (TextView) findViewById(R.id.tv_tuijian2);
        String tuiJian1 = getResources().getString(R.string.tuijian1);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(tuiJian1);
        ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.RED);
        builder1.setSpan(redSpan1, tuiJian1.indexOf("1"), tuiJian1.indexOf("1") + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTuiJian1.setText(builder1);
        String tuiJian2 = getResources().getString(R.string.tuijian2);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(tuiJian2);
        ForegroundColorSpan redSpan2 = new ForegroundColorSpan(Color.RED);
        builder2.setSpan(redSpan2, tuiJian2.indexOf("2"), tuiJian2.indexOf("2") + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTuiJian2.setText(builder2);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button share = (Button) findViewById(R.id.bt_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareWindos();
            }
        });
        getShareData();
    }

    private void getShareData() {
        ProgressDialog progressDialog = buildProcessDialog(null, "请稍等", false);
        ShareDataRequestData requestData = new ShareDataRequestData();
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {

            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            paresData(response);
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            getShareData();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.request(Constants.URL_GET_SHARE_DATA, data, progressDialog, true);
    }

    private void paresData(String response) {
        if (mGson == null) {
            mGson = new Gson();
        }
        ShareData shareData = mGson.fromJson(response, ShareData.class);
        mTitle = shareData.getShare().get(0).getTitle();
        mMessage = shareData.getShare().get(0).getMessage();
        mImage = shareData.getShare().get(0).getImage();
        mUrl = shareData.getShare().get(0).getUrl();
    }


    private void showShareWindos() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.dig_show_share, null);
        //初始化控件
        LinearLayout wechat = (LinearLayout) inflate.findViewById(R.id.wechat);
        LinearLayout wechatmoments = (LinearLayout) inflate.findViewById(R.id.wechatmoments);
        LinearLayout qq = (LinearLayout) inflate.findViewById(R.id.qq);
        LinearLayout weibo = (LinearLayout) inflate.findViewById(R.id.weibo);

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkShareData()) {
                    mShareByWechat = true;
                    Wechat.ShareParams wechat = new Wechat.ShareParams();
                    wechat.setTitle(mTitle);
                    wechat.setText(mMessage);
//                    wechat.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    wechat.setImageUrl(mImage);
                    wechat.setUrl(mUrl);
                    wechat.setShareType(Platform.SHARE_WEBPAGE);
                    Platform weixin = ShareSDK.getPlatform(ShareActivity.this, Wechat.NAME);
                    weixin.setPlatformActionListener(ShareActivity.this);
                    weixin.share(wechat);
                }
            }
        });
        wechatmoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkShareData()) {
                    WechatMoments.ShareParams wechatMoments = new WechatMoments.ShareParams();
                    wechatMoments.setTitle(mTitle);
                    wechatMoments.setText(mMessage);
//                    wechatMoments.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    wechatMoments.setImageUrl(mImage);
                    wechatMoments.setUrl(mUrl);
                    wechatMoments.setShareType(Platform.SHARE_WEBPAGE);
                    Platform weixin = ShareSDK.getPlatform(ShareActivity.this, WechatMoments.NAME);
                    weixin.setPlatformActionListener(ShareActivity.this);
                    weixin.share(wechatMoments);
                }
            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkShareData()) {
                    QQ.ShareParams qq = new QQ.ShareParams();
                    qq.setTitle(mTitle);
                    qq.setText(mMessage);
                    qq.setUrl(mUrl);
//                    qq.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    qq.setImageUrl(mImage);
                    qq.setShareType(Platform.SHARE_WEBPAGE);
                    Platform qq_p = ShareSDK.getPlatform(ShareActivity.this, QQ.NAME);
                    qq_p.setPlatformActionListener(ShareActivity.this);
                    qq_p.share(qq);
                }
            }
        });

        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkShareData()) {
                    SinaWeibo.ShareParams weibo = new SinaWeibo.ShareParams();
                    weibo.setTitle(mTitle);
                    weibo.setText(mMessage);
                    weibo.setUrl(mUrl);
//                    weibo.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                    weibo.setImageUrl(mImage);
                    weibo.setShareType(Platform.SHARE_WEBPAGE);
                    Platform weio = ShareSDK.getPlatform(ShareActivity.this, SinaWeibo.NAME);
                    weio.setPlatformActionListener(ShareActivity.this);
                    weio.share(weibo);
                }
            }
        });

        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.show();//显示对话框

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private boolean checkShareData() {
        if (TextUtil.isEmpty(mTitle) || TextUtil.isEmpty(mMessage) ||
                TextUtil.isEmpty(mImage) || TextUtil.isEmpty(mUrl)) {
            toastLong("获取分享内容失败,请稍候重试");
            return false;
        }
        return true;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        logI("onComplete i = " + i + "-----platform = " + platform.toString());
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        logI("onError i = " + i + "-----platform = " + platform.toString());

    }

    @Override
    public void onCancel(Platform platform, int i) {
        logI("onCancel i = " + i + "-----platform = " + platform.toString());
    }

    @Override
    protected void onResume() {
        if (mShareByWechat) {
            logI("分享微信成功");
        }
        super.onResume();
    }
}
