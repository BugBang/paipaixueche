package com.session.dgjx;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by user on 2017-02-07.
 */
public class MainActivity extends TabActivity {
    private TabHost mTabHost;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化TabHost对象，得到TabHost
        mTabHost = getTabHost();

        // 实例化布局对象
        mInflater = LayoutInflater.from(this);

        // 得到Activity的个数
        int count = Constants.ConValue.mTabClassArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(Constants.ConValue.mTextViewArray[i])
                    .setIndicator(getTabItemView(i))
                    .setContent(getTabItemIntent(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mInflater.inflate(R.layout.item_tab_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_icon);
        if (imageView != null) {
            imageView.setImageResource(Constants.ConValue.mImageViewArray[index]);
        }
        TextView textView = (TextView) view.findViewById(R.id.text_name);
        textView.setText(Constants.ConValue.mTextViewArray[index]);
        return view;
    }

    /**
     * 给Tab选项卡设置内容（每个内容是一个Activity）
     */
    private Intent getTabItemIntent(int index) {
        Intent intent = new Intent(this, Constants.ConValue.mTabClassArray[index]);
        return intent;
    }

}
