package com.session.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.session.common.utils.LogUtil;
import com.session.common.utils.ToastUtil;
import com.session.dgjp.AppInstance;
import com.session.dgjp.R;
import com.session.dgjp.enity.Account;
import com.session.dgjp.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/** Activity基类 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	/** 账户，登录之后就一直有效的用户实例 */
	// protected Account account;
	/** 日志TAG，混淆之后类名会变化变化 */
	protected final String TAG = "com.session.common." + this.getClass().getSimpleName();
	/** 上下文 */
	protected Context ctx;
	// 等待进度框
	protected ProgressDialog progressDialog;
	// 两次回退键退出
	protected long mExitTime;
	protected Account account;
	private Toast toast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		account = AppInstance.getInstance().getAccount();
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		init(savedInstanceState);
        logI("Activity="+TAG);
	}

	/** 初始化 */
	protected abstract void init(Bundle savedInstanceState);

	/** 初始化标题栏 */
	protected void initTitle(CharSequence title) {
		initTitle(title, true);
	}

	/** 初始化标题栏 */
	protected void initTitle(int resId) {
		initTitle(resId, true);
	}

	/**
	 * 初始化标题栏
	 * @param title 标题
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

	/** 初始化标题栏 */
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
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
		super.onDestroy();
	}


	/**
	 * 创建一个ProgressDialog实例，可以后续自定义修改监听
	 * @param title 标题
	 * @param msg 内容
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

	/** Toast提示 */
	protected void toast(CharSequence text, int duration) {
		if (null == toast) {
			toast = Toast.makeText(this, text, duration);
		} else {
			toast.setText(text);
			toast.setDuration(duration);
		}
		toast.show();
	}

	/** Toast提示 */
	protected void toast(int resId, int duration) {
		if (null == toast) {
			toast = Toast.makeText(this, resId, duration);
		} else {
			toast.setText(resId);
			toast.setDuration(duration);
		}
		toast.show();
	}

	/** Toast提示 */
	protected void toastShort(CharSequence text) {
		toast(text, Toast.LENGTH_SHORT);
	}

	/** Toast提示 */
	protected void toastShort(int resId) {
		toast(resId, Toast.LENGTH_SHORT);
	}

	/** Toast提示 */
	protected void toastLong(CharSequence text) {
		toast(text, Toast.LENGTH_LONG);
	}

	/** Toast提示 */
	protected void toastLong(int resId) {
		toast(resId,Toast.LENGTH_LONG);
	}
	
	/** 打印日志 */
	protected void logD(String text) {
		LogUtil.d(TAG, text);
	}

	/** 打印日志 */
	protected void logI(String text) {
		LogUtil.i(TAG, text);
	}

	/** 打印日志 */
	protected void logW(String text) {
		LogUtil.w(TAG, text);
	}

	/** 打印日志 */
	protected void logE(String text) {
		LogUtil.e(TAG, text);
	}
	
	/** 打印日志 */
	protected void LogE(Throwable throwable)
	{
		LogUtil.e(TAG, throwable.toString(), throwable);
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
	 * @author YJ Liang
	 * 2016  上午10:50:22
	 * @param content 需要显示的内容
	 * @param defaultContentResId 默认内容的资源id
	 * @param duration 显示的时长
	 */
	protected void toast(String content, int defaultContentResId,int duration)
	{
		if(toast == null)
		{
			toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		if(content == null || (content=content.trim()).isEmpty())
		{
			toast.setText(defaultContentResId);
		}else {
			toast.setText(content);
		}
		toast.setDuration(duration);
		toast.show();
	}
	
	protected void toast(int resId, int duration, int sizeId)
	{
		ToastUtil.toast(this, resId, duration, sizeId);
	}
	
	protected void toLogin()
	{
		startActivity(new Intent(this,LoginActivity.class));
		finish();
	}

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(null != this.getCurrentFocus()){
//            /**
//             * 点击空白位置 隐藏软键盘
//             */
//            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
//        }
//        return super .onTouchEvent(event);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void addFragment(int fragmentContentId, BaseFragment fragment) {
        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.fragment_slide_left_in,
                            R.animator.fragment_slide_left_out,
                            R.animator.fragment_slide_right_in,
                            R.animator.fragment_slide_right_out)
                    .replace(fragmentContentId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    protected void addFragment(int fragmentContentId,BaseFragment fragment,Bundle bundle) {
        if (fragment != null) {
            if (bundle!=null){
                fragment.setArguments(bundle);
            }
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.fragment_slide_left_in,
                            R.animator.fragment_slide_left_out,
                            R.animator.fragment_slide_right_in,
                            R.animator.fragment_slide_right_out)
                    .replace(fragmentContentId, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    protected void removeFragment() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
