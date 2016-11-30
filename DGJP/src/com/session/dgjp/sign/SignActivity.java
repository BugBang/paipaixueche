package com.session.dgjp.sign;

import android.os.Bundle;

import com.session.common.BaseActivity;
import com.session.dgjp.R;

public class SignActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_sign);
        addFragment(R.id.content,SignSchoolFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        removeFragment();
    }
}
