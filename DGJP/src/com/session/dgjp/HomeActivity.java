package com.session.dgjp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.session.common.BaseActivity;
import com.session.common.BaseFragment;
import com.session.common.BaseFragmentPagerAdapter;
import com.session.common.BaseRequestTask;
import com.session.common.utils.AppUtil;
import com.session.common.utils.UpdateUtil;
import com.session.common.utils.UpdateUtil.OnVersionInfoListener;
import com.session.common.utils.VersionInfo;
import com.session.dgjp.db.MyMessageDao;
import com.session.dgjp.message.MessageActivity;
import com.session.dgjp.order.OrderListFragment;
import com.session.dgjp.personal.SetPayPasswordActivity;
import com.session.dgjp.request.CommonRequestData;
import com.session.dgjp.school.SchoolListFragment;
import com.session.dgjp.school.SchoolListFragment.OnMapListChangeListener;
import com.session.dgjp.training.TrainingListFragment;
import com.session.dgjp.view.MyViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class HomeActivity extends BaseActivity implements OnMapListChangeListener {

    public final static String FROM_NOTIFICATION = "from_notification";//表示intent时候来源于通知
    public final static String ACTION_ORDER_SUCCESS = "action_order_success";//表示直接创建订单并完成支付
    public final static String PAY_SUCCESS = "pay_success";//表示支付成功
    public final static String ACTION_MSG_COUNT_CHANGED = "msg_count_changed_action";
    private MyViewPager viewPager;
    private RadioGroup radioGroup;
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    // tab的id,顺序与fragments的顺序一致
    private List<Integer> ids = new ArrayList<Integer>();
//    private TextView msgCountTv;
    private MyMessageDao myMessageDao;
    private BaseFragmentPagerAdapter adapter;
    /*private TrainerListFragment trainerListFragment;
    private MapFragment mapFragment;*/
    private String schoolName;
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_MSG_COUNT_CHANGED.equals(intent.getAction())) {
//                showUnreadMsgCount();
            } else if (ACTION_ORDER_SUCCESS.equals(intent.getAction())) {
                ((SchoolListFragment) fragments.get(0)).updateLatestSchool();
                ((OrderListFragment) fragments.get(1)).setNeedLoadFlag(true);
            } else {

            }
            /*if(intent.getBooleanExtra(ORDER_PAY_SUCCESS, false))
			{
				((SchoolListFragment)fragments.get(0)).updateLatestSchool();
				OrderListFragment fragment = (OrderListFragment) fragments.get(1);
				if (fragment.isFirstLoading())
				{
					radioGroup.check(R.id.order);
				} else
				{
					radioGroup.check(R.id.order);
					fragment.reload();
				}
			}else */
        }
    };

    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void init(Bundle savedInstanceState) {
        setContentView(R.layout.act_home);
        //		initTitle(R.string.app_name);
        //		findViewById(R.id.ivTitleRight).setOnClickListener(this);
        //		msgCountTv = (TextView) findViewById(R.id.tvMsgCount);
        SchoolListFragment schoolListFragment = new SchoolListFragment();
        schoolListFragment.setOnMapListChangeListener(this);
        fragments.add(schoolListFragment);
        //fragments.add(new MapFragment());
        ids.add(R.id.trainer);
        fragments.add(new OrderListFragment());
        ids.add(R.id.order);
        fragments.add(new TrainingListFragment());
        ids.add(R.id.training);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        viewPager = (MyViewPager) findViewById(R.id.view_pager);
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(fragments.size());
        adapter = new BaseFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
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
        if (TextUtils.isEmpty(account.getLastLoginTime())) { // 第一次登录检查用户是否设置支付密码
            checkPayPassword();
        }
        checkVersion();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_MSG_COUNT_CHANGED);
        filter.addAction(ACTION_ORDER_SUCCESS);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);
        if (AppInstance.getInstance().getExtraMap().getExtra(HomeActivity.FROM_NOTIFICATION, false)) {
            startActivity(new Intent(ctx, MessageActivity.class));
        }
    }

    /**
     * 检查用户是否设置支付密码
     */
    private void checkPayPassword() {
        CommonRequestData requestData = new CommonRequestData();
        requestData.setAccount(account.getAccount());
        String data = new Gson().toJson(requestData);
        new BaseRequestTask() {

            @Override
            protected void onResponse(int code, String msg, String response) {
                try {
                    switch (code) {
                        case BaseRequestTask.CODE_SUCCESS:
                            JSONObject object = new JSONObject(response);
                            if ("N".equals(object.optString("setFlag"))) { // 设置支付密码
                                startActivity(new Intent(ctx, SetPayPasswordActivity.class));
                            }
                            break;
                        case BaseRequestTask.CODE_SESSION_ABATE:
                            checkPayPassword();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.request(Constants.URL_CHECK_PAY_PASSWORD, data, null, false);
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
            //		case R.id.ivTitleLeft: // 个人中心
            //			startActivity(new Intent(ctx, PersonalCenterActivity.class));
            //			break;
            //		case R.id.ivTitleRight: // 我的消息
            //			startActivity(new Intent(ctx, MessageActivity.class));
            //			break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            // toastShort(R.string.toast_press_exit);
            toast(getString(R.string.toast_press_exit, getString(R.string.app_name)), Toast.LENGTH_SHORT, R.dimen.text_medium_xx);
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


    public RadioGroup getRadioGroup() {
        return radioGroup;
    }

//    private void showUnreadMsgCount() {
//        try {
//            if (myMessageDao == null) {
//                myMessageDao = new MyMessageDao();
//            }
//            long count = myMessageDao.count(account.getAccount(), false, null);
//            if (count > 0) {
//                msgCountTv.setVisibility(View.VISIBLE);
//                msgCountTv.setText(String.valueOf(count));
//            } else {
//                msgCountTv.setVisibility(View.INVISIBLE);
//            }
//        } catch (SQLException e) {
//            msgCountTv.setVisibility(View.INVISIBLE);
//            LogUtil.e(TAG, e.toString(), e);
//        }
//    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        showUnreadMsgCount();
    }

    @Override
    public void onChange(int code) {
        switch (code) {
            case MAP:
                viewPager.setCanScroll(false);
                break;
            case LIST:
                viewPager.setCanScroll(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getBooleanExtra(PAY_SUCCESS, false)) {
            if (radioGroup.getCheckedRadioButtonId() == ids.get(1)) {
                ((OrderListFragment) fragments.get(1)).reload();
            } else {
                radioGroup.check(R.id.order);
            }
        } else if (AppInstance.getInstance().getExtraMap().getExtra(HomeActivity.FROM_NOTIFICATION, false)) {
            startActivity(new Intent(ctx, MessageActivity.class));
        } else {

        }
    }


}
