package com.session.common.utils;

import com.session.dgjp.R;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Toast提示工具类 */
public final class ToastUtil {
	private static Toast toast;
	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;

	public static void show(Context ctx, CharSequence text, int duration) {
		if (null == toast) {
			toast = Toast.makeText(ctx, text, duration);
		} else {
			toast.setText(text);
			toast.setDuration(duration);
		}
		toast.show();
	}

	public static void show(Context ctx, int resId, int duration) {
		if (null == toast) {
			toast = Toast.makeText(ctx, resId, duration);
		} else {
			toast.setText(resId);
			toast.setDuration(duration);
		}
		toast.show();
	}

	public static void showShort(Context ctx, CharSequence text) {
		if (null == toast) {
			toast = Toast.makeText(ctx, text, LENGTH_SHORT);
		} else {
			toast.setText(text);
			toast.setDuration(LENGTH_SHORT);
		}
		toast.show();
	}

	public static void showShort(Context ctx, int resId) {
		if (null == toast) {
			toast = Toast.makeText(ctx, resId, LENGTH_SHORT);
		} else {
			toast.setText(resId);
			toast.setDuration(LENGTH_SHORT);
		}
		toast.show();
	}

	public static void showLong(Context ctx, CharSequence text) {
		if (null == toast) {
			toast = Toast.makeText(ctx, text, LENGTH_LONG);
		} else {
			toast.setText(text);
			toast.setDuration(LENGTH_LONG);
		}
		toast.show();
	}

	public static void showLong(Context ctx, int resId) {
		if (null == toast) {
			toast = Toast.makeText(ctx, resId, LENGTH_LONG);
		} else {
			toast.setText(resId);
			toast.setDuration(LENGTH_LONG);
		}
		toast.show();
	}
	
	public static void toast(Context context, String content, int defaultContentResId,int duration)
	{
		if(content == null || (content=content.trim()).isEmpty())
		{
			Toast.makeText(context, defaultContentResId, duration).show();
		}else {
			Toast.makeText(context, content, duration).show();
		}
	}
	
	/**
	 * 显示toast
	 * @author YJ Liang
	 * 2016  上午9:47:13
	 * @param context
	 * @param resId
	 * @param duration
	 * @param sizeId,字体大小，R.dimen.textSize
	 */
	public static void toast(Context context, int resId, int duration, int sizeId)
	{
		Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		LinearLayout linearLayout = (LinearLayout)toast.getView();
		TextView textView = (TextView)linearLayout.getChildAt(0);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelOffset(sizeId));
		toast.show();
	}
	
	public static void toast(Context context, int resId, int duration)
	{
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	

}
