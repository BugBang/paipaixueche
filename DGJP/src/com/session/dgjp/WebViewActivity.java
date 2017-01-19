package com.session.dgjp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.common.BaseActivity;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

/**
 * Created by user on 2016-11-23.
 */
public class WebViewActivity extends BaseActivity{

    public static final String LOAD_URL = "url";
    public static final String TITLE = "title";

    private ImageView mIvBack;
    private TextView mTvTitle;
    private XWalkView xWalkWebView;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.web_view);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mIvBack.setOnClickListener(this);
        String url = getIntent().getStringExtra(LOAD_URL);
        String title = getIntent().getStringExtra(TITLE);

        mTvTitle.setText(title);

        xWalkWebView = (XWalkView) findViewById(R.id.wv_content);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
////        webView.setWebChromeClient(new MyWebChromeClient());
//        webView.loadUrl(url);
        XWalkPreferences.setValue("enable-javascript", true);
        xWalkWebView.load(url,null);

        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    /**通过Activity管理XWalkWebView的声明周期*/
    @Override protected void onPause() {
        super.onPause();
        if (xWalkWebView != null) {
            xWalkWebView.pauseTimers();
            xWalkWebView.onHide();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (xWalkWebView != null) {
            xWalkWebView.resumeTimers();
            xWalkWebView.onShow();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (xWalkWebView != null) {
            xWalkWebView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (xWalkWebView != null) {
            xWalkWebView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (xWalkWebView != null) {
            xWalkWebView.onNewIntent(intent);
        }
    }
}
