package com.session.dgjp.order;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.session.common.BaseActivity;
import com.session.dgjp.R;

/**
 * Created by user on 2017-02-17.
 */
public class OrderListActivity extends BaseActivity {
    private FrameLayout mFrameLayout ;
    public FragmentManager fragmentManager;
    private OrderListFragment mFragment;
    private ImageView mBack;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_order_list);

        mBack = (ImageView) findViewById(R.id.iv_back);
        mBack.setOnClickListener(this);

        fragmentManager = getFragmentManager();
        mFrameLayout = (FrameLayout) findViewById(R.id.fl);
        mFragment = new OrderListFragment();

        addFragment(R.id.fl, mFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = fragmentManager.findFragmentByTag(mFragment.getClass().getSimpleName());
        f.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
