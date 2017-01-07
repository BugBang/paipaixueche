package com.session.dgjp.sign;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.session.common.BaseFragment;
import com.session.dgjp.R;

/**
 * Created by user on 2016-11-16.
 */
public class SignClassDetailFragment extends BaseFragment {

    private ImageView mIvBack,mIvDealCheck;
    private TextView mTvTitle,mTvDeal;
    private WebView mWvContent;
    private Button mBtSignNow;
    private boolean isCheck = false;
    private ProgressBar mWebviewPgbProgress;


    public static SignClassDetailFragment newInstance() {
        return new SignClassDetailFragment();
    }

    @Override
    protected int getContentRes() {
        return R.layout.sign_class_detail_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvTitle = (TextView) view.findViewById(R.id.tvTitle);
        mWvContent = (WebView) view.findViewById(R.id.wv_content);
        mIvDealCheck = (ImageView) view.findViewById(R.id.iv_deal_check);
        mTvDeal = (TextView) view.findViewById(R.id.tv_deal);
        mBtSignNow = (Button) view.findViewById(R.id.bt_sign_now);
        mWebviewPgbProgress = (ProgressBar) view.findViewById(R.id.webview_pgb_progress);

        mIvBack.setOnClickListener(this);
        mIvDealCheck.setOnClickListener(this);
        mTvDeal.setOnClickListener(this);
        mBtSignNow.setOnClickListener(this);

        WebSettings settings = mWvContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        mWvContent.setWebChromeClient(new MyWebChromeClient());
        mWvContent.loadUrl("http://www.papaxueche.com/cheet/cheet.html");
//        mWvContent.loadUrl("http://10.0.0.43/222/index.html");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                removeFragment();
                break;
            case R.id.iv_deal_check:
                if (isCheck){
                    mIvDealCheck.setImageResource(R.drawable.deal_uncheck);
                    isCheck = !isCheck;
                }else {
                    mIvDealCheck.setImageResource(R.drawable.deal_checked);
                    isCheck = !isCheck;
                }
                break;
            case R.id.tv_deal:
                toastLong("tv_deal");
                break;
            case R.id.bt_sign_now:
                if (isCheck){
                    addFragment(R.id.content,EnterPersonInformationFragment1.newInstance(),null);
                }else {
                    toastLong("请阅读并同意《报名协议》才可以报名");
                }
                break;
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mWebviewPgbProgress.setVisibility(View.GONE);
            } else {
                if (mWebviewPgbProgress.getVisibility() == View.GONE) {
                    mWebviewPgbProgress.setVisibility(View.VISIBLE);
                }
                mWebviewPgbProgress.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
