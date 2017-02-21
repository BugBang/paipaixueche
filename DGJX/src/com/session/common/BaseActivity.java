package com.session.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.session.common.utils.LogUtil;
import com.session.common.utils.TextUtil;
import com.session.common.utils.ToastUtil;
import com.session.dgjx.AppInstance;
import com.session.dgjx.R;
import com.session.dgjx.enity.Account;
import com.session.dgjx.event.EventMsg;
import com.session.dgjx.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * Activity基类
 */
public abstract class BaseActivity extends Activity implements OnClickListener {
    /** 账户，登录之后就一直有效的用户实例 */
    // protected Account account;
    /**
     * 日志TAG，混淆之后类名会变化变化
     */
    protected final String TAG = "com.session.common." + this.getClass().getSimpleName();
    /**
     * 上下文
     */
    protected Context ctx;
    // 等待进度框
    protected ProgressDialog progressDialog;
    // 两次回退键退出
    protected long mExitTime;
    protected Account account;
    private Toast toast;
    private Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        account = AppInstance.getInstance().getAccount();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        init(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 初始化标题栏
     */
    protected void initTitle(CharSequence title) {
        initTitle(title, true);
    }

    /**
     * 初始化标题栏
     */
    protected void initTitle(int resId) {
        initTitle(resId, true);
    }

    /**
     * 初始化标题栏
     *
     * @param title    标题
     * @param showBack 是否显示左上角的返回按钮
     */
    protected void initTitle(CharSequence title, boolean showBack) {
        View layoutTitle = findViewById(R.id.layoutTitle);
        if (null != layoutTitle) {
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvTitle.setSelected(true);// 部分标题较长，开启走马灯效果
            tvTitle.setText(title);
            if (showBack) {
                findViewById(R.id.ivTitleLeft).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.ivTitleLeft).setVisibility(View.INVISIBLE);
            }
            findViewById(R.id.ivTitleLeft).setOnClickListener(this);
        }
    }

    /**
     * 初始化标题栏
     */
    protected void initTitle(int resId, boolean showBack) {
        if (0 != resId) {
            initTitle(getString(resId), showBack);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }


    /**
     * 创建一个ProgressDialog实例，可以后续自定义修改监听
     *
     * @param title      标题
     * @param msg        内容
     * @param cancelable 是否可以取消：ture可以
     * @return ProgressDialog实例
     */
    protected ProgressDialog buildProcessDialog(CharSequence title, CharSequence msg, boolean cancelable) {
        ProgressDialog pd = new ProgressDialog(ctx);
        pd.setTitle(title);
        pd.setMessage(msg);
        pd.setCancelable(cancelable);
        return pd;
    }

    /**
     * Toast提示
     */
    protected void toast(CharSequence text, int duration) {
        if (null == toast) {
            toast = Toast.makeText(this, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }

    /**
     * Toast提示
     */
    protected void toast(int resId, int duration) {
        if (null == toast) {
            toast = Toast.makeText(this, resId, duration);
        } else {
            toast.setText(resId);
            toast.setDuration(duration);
        }
        toast.show();
    }

    /**
     * Toast提示
     */
    protected void toastShort(CharSequence text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * Toast提示
     */
    protected void toastShort(int resId) {
        toast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * Toast提示
     */
    protected void toastLong(CharSequence text) {
        toast(text, Toast.LENGTH_LONG);
    }

    /**
     * Toast提示
     */
    protected void toastLong(int resId) {
        toast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 打印日志
     */
    protected void logD(String text) {
        LogUtil.d(TAG, text);
    }

    /**
     * 打印日志
     */
    protected void logI(String text) {
        LogUtil.i(TAG, text);
    }

    /**
     * 打印日志
     */
    protected void logW(String text) {
        LogUtil.w(TAG, text);
    }

    /**
     * 打印日志
     */
    protected void logE(String text) {
        LogUtil.e(TAG, text);
    }

    /**
     * 打印日志
     */
    protected void LogE(Throwable throwable) {
        LogUtil.e(TAG, throwable.toString(), throwable);
    }
    /**
     * 打印日志
     */
    protected void $log(String msg) {
        if (!TextUtil.isEmpty(msg)){
            Logger.i(msg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleLeft:
                onBackPressed();
                break;
            default:
                break;
        }
    }


    /**
     * 显示信息，如果content是null或空字符串，则显示默认内容，否则显示content
     *
     * @param content             需要显示的内容
     * @param defaultContentResId 默认内容的资源id
     * @param duration            显示的时长
     * @author YJ Liang
     * 2016  上午10:50:22
     */
    protected void toast(String content, int defaultContentResId, int duration) {
        if (toast == null) {
            toast = Toast.makeText(this, "", duration);
        }
        if (content == null || (content = content.trim()).isEmpty()) {
            toast.setText(defaultContentResId);
        } else {
            toast.setText(content);
        }
        toast.setDuration(duration);
        toast.show();
    }

    protected void toast(int resId, int duration, int sizeId) {
        ToastUtil.toast(this, resId, duration, sizeId);
    }

    protected void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPostThread(EventMsg messageEvent) {
        Log.e("PostThread", Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(EventMsg messageEvent) {
        Log.e("MainThread", Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEventBackgroundThread(EventMsg messageEvent) {
        Log.e("BackgroundThread", Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(EventMsg messageEvent) {
        Log.e("Async", Thread.currentThread().getName());
    }

}
