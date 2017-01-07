package com.session.dgjp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.session.common.BaseFragment;
import com.session.dgjp.MainActivity;
import com.session.dgjp.R;

/**
 * Created by user on 2016-12-15.
 */
public class SignPaySuccessFragment extends BaseFragment {

    private Button mBtGoHome;

    public static SignPaySuccessFragment newInstance() {
        return new SignPaySuccessFragment();
    }
    @Override
    protected int getContentRes() {
        return R.layout.sign_pay_success_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBtGoHome = (Button) view.findViewById(R.id.bt_go_home);
        mBtGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(act, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}
