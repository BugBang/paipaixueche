package com.session.common;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.session.common.utils.LogUtil;
import com.session.dgjx.AppInstance;
import com.session.dgjx.enity.Account;
import com.session.dgjx.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;

/** Fragment基类 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
	/** 日志TAG，混淆之后类名会变化变化 */
	protected final String TAG = "com.session.common." + this.getClass().getSimpleName();
	protected Activity act;
	protected View view;
	protected ProgressDialog progressDialog;
	protected LayoutInflater inflater;
	protected Account account;
	private Toast toast;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			this.inflater = inflater;
			account = AppInstance.getInstance().getAccount();
			act = getActivity();
			progressDialog = new ProgressDialog(act);
			progressDialog.setCancelable(false);
			initArgument();
			view = inflater.inflate(getContentRes(), container, false);
			init(savedInstanceState);
		} else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}
		return view;
	}

	/**
	 * 获取fragment初始化参数，子类需要重写该方法{@link #getArguments()}
	 * @author YJ Liang
	 * 2016-5-5  下午17:22:42
	 */
	protected void initArgument()
	{
		
	}
	
	/**
	 * 获取子类的layout文件
	 * @author YJ Liang
	 * 2016  下午5:26:19
	 * @return
	 */
	protected abstract int getContentRes();
	
	/** 初始化 */
	protected abstract void init(Bundle savedInstanceState);

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		MobclickAgent.onPageEnd(this.getClass().getName());
		super.onPause();
	}

	/**
	 * 创建一个ProgressDialog实例，可以后续自定义修改监听
	 * @param title 标题
	 * @param msg 内容
	 * @param cancelable 是否可以取消：ture可以
	 * @return ProgressDialog实例
	 */
	protected ProgressDialog buildProcessDialog(CharSequence title, CharSequence msg, boolean cancelable) {
		ProgressDialog pd = new ProgressDialog(act);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.setCancelable(cancelable);
		return pd;
	}

	/** Toast提示 */
	protected void toast(CharSequence text, int duration) {
		if (null == toast) {
			toast = Toast.makeText(getActivity(), text, duration);
		} else {
			toast.setText(text);
			toast.setDuration(duration);
		}
		toast.show();
	}

	/** Toast提示 */
	protected void toast(int resId, int duration) {
		if (null == toast) {
			toast = Toast.makeText(getActivity(), resId, duration);
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
			toast = Toast.makeText(getActivity(), "", duration);
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
	
	protected void toLogin()
	{
		Activity activity = getActivity();
		startActivity(new Intent(activity,LoginActivity.class));
		activity.finish();
	}

	@Override
	public void onDestroyView()
	{
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
		super.onDestroyView();
	}

}
