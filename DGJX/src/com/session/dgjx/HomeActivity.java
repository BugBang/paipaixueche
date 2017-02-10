package com.session.dgjx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.BaseActivity;
import com.session.common.BaseFragment;
import com.session.common.BaseFragmentPagerAdapter;
import com.session.common.utils.AppUtil;
import com.session.common.utils.LogUtil;
import com.session.common.utils.UpdateUtil;
import com.session.common.utils.UpdateUtil.OnVersionInfoListener;
import com.session.common.utils.VersionInfo;
import com.session.dgjx.daytraining.DayTrainingListFragment;
import com.session.dgjx.db.MyMessageDao;
import com.session.dgjx.message.MessageActivity;
import com.session.dgjx.order.OrderListFragment;
import com.session.dgjx.personal.PersonalCenterActivity;
import com.session.dgjx.receiver.JpushReceiver;
import com.session.dgjx.training.TrainingListFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    // tab的id,顺序与fragments的顺序一致
    private List<Integer> ids = new ArrayList<Integer>();
    private TextView msgCountTv;
    private MyMessageDao myMessageDao;
    public final static String ACTION_MSG_COUNT_CHANGED = "msg_count_changed_action";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_MSG_COUNT_CHANGED.equals(intent.getAction())) {
                showUnreadMsgCount();
            }
        }
    };

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_home);
//        initTitle("预约");
        findViewById(R.id.ivTitleRight).setOnClickListener(this);
        msgCountTv = (TextView) findViewById(R.id.tvMsgCount);
        fragments.add(new OrderListFragment());
        ids.add(R.id.booking_order);
        fragments.add(new DayTrainingListFragment());
        ids.add(R.id.day_training);
        fragments.add(new TrainingListFragment());
        ids.add(R.id.training);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new BaseFragmentPagerAdapter(getFragmentManager(), fragments));
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (ids.get(index) != id) {
                    radioGroup.check(ids.get(index));
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < ids.size(); i++) {
                    if (checkedId == ids.get(i)) {
                        viewPager.setCurrentItem(i, true);
                        break;
                    }
                }
            }
        });
        radioGroup.check(ids.get(0));
        checkVersion();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_MSG_COUNT_CHANGED);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);
        if (AppInstance.getInstance().getExtraMap().getExtra(JpushReceiver.FROM_NOTIFICATION, false)) {
            startActivity(new Intent(ctx, MessageActivity.class));
        }
    }

    /**
     * 版本检测
     */
    private void checkVersion() {
        UpdateUtil.checkUpdate(new OnVersionInfoListener() {

            @Override
            public void onVersionInfo(VersionInfo info) {
                int code = AppUtil.getVersionCode(getApplicationContext());
                if (null != info) {
                    if (code < info.getCode()) {
                        UpdateUtil.showUpdate(ctx, "版本更新", 0);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleLeft: // 个人中心
                startActivity(new Intent(ctx, PersonalCenterActivity.class));
//                get();
                break;
            case R.id.ivTitleRight: // 我的消息
                startActivity(new Intent(ctx, MessageActivity.class));
                break;
            default:
                super.onClick(v);
                break;
        }
    }

//    private void get() {
//        ProgressDialog progressDialog = buildProcessDialog(null, "正在退出登录...", false);
//        GetPersonalDataRequestData requestData = new GetPersonalDataRequestData();
//        requestData.setAccount(account.getAccount());
//        String data = new Gson().toJson(requestData);
//        new BaseRequestTask() {
//            @Override
//            protected void onResponse(int code, String msg, String response) {
//                $log("get = "+response);
//                try {
//                    switch (code) {
//                        case BaseRequestTask.CODE_SUCCESS:
//
//                            break;
//                        case BaseRequestTask.CODE_SESSION_ABATE:
//                            get();
//                            break;
//                        default:
//                            toastShort(msg);
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    toastShort("网络异常，请稍后重试");
//                } finally {
//                }
//            }
//        }.request(Constants.URL_GET_COACH_INCOME_DATA, data, progressDialog, true);
//    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //toastShort(R.string.toast_press_exit);
            toast(getString(R.string.toast_press_exit, getString(R.string.app_name)), Toast.LENGTH_SHORT, R.dimen.text_medium_xx);
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    public RadioGroup getRadioGroup() {
        return radioGroup;
    }

    private void showUnreadMsgCount() {
        try {
            if (myMessageDao == null) {
                myMessageDao = new MyMessageDao();
            }
            long count = myMessageDao.count(account.getAccount(), false, null);
            if (count > 0) {
                msgCountTv.setVisibility(View.VISIBLE);
                msgCountTv.setText(String.valueOf(count));
            } else {
                msgCountTv.setVisibility(View.INVISIBLE);
            }
        } catch (SQLException e) {
            msgCountTv.setVisibility(View.INVISIBLE);
            LogUtil.e(TAG, e.toString(), e);
        }
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUnreadMsgCount();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (AppInstance.getInstance().getExtraMap().getExtra(JpushReceiver.FROM_NOTIFICATION, false)) {
            startActivity(new Intent(ctx, MessageActivity.class));
        }
    }

}
