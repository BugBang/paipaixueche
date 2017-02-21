package com.session.dgjx;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.session.common.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2017-02-20.
 */
public  class BWeb extends BaseActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_bweb);
        ButterKnife.bind(this);

        String title = getIntent().getStringExtra(TITLE);
        String url = getIntent().getStringExtra(URL);

        mTvTitle.setText(title);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

}
