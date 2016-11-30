package com.session.dgjp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.common.BaseActivity;

/**
 * Created by user on 2016-11-23.
 */
public class WebViewActivity extends BaseActivity{

    public static final String LOAD_URL = "url";
    public static final String TITLE = "title";

    private ImageView mIvBack;
    private TextView mTvTitle;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.web_view);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mIvBack.setOnClickListener(this);
        String url = getIntent().getStringExtra(LOAD_URL);
        String title = getIntent().getStringExtra(TITLE);

        mTvTitle.setText(title);

        WebView webView = (WebView) findViewById(R.id.wv_content);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
//        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
