package com.session.dgjp.training;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.session.common.BaseActivity;
import com.session.dgjp.R;

/**
 * Created by user on 2017-02-17.
 */
public class TrainingListActivity extends BaseActivity {
    private FrameLayout mFrameLayout ;
    public FragmentManager fragmentManager;
    private TrainingListFragment mFragment;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_training_list);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fragmentManager = getFragmentManager();
        mFrameLayout = (FrameLayout) findViewById(R.id.fl);
        mFragment = new TrainingListFragment();
        addFragment(R.id.fl, mFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = fragmentManager.findFragmentByTag(mFragment.getClass().getSimpleName());
        f.onActivityResult(requestCode, resultCode, data);
    }
}
