package com.session.dgjx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 相册所有模式和个人模式显示图片的容器
 * @author Liang YJ
 * 2014年9月4日 下午1:45:09
 */
public class NoScrollGridView extends GridView
{

	/**
	 * @param context
	 */
	public NoScrollGridView(Context context)
	{
		super(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * 重写onMeasure方法，使其不出现滚动条。
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
//		widthMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
//		Context context = getContext();
//		widthMeasureSpec = (int)(3*context.getResources().getDimension(R.dimen.view_80)+2*context.getResources().getDimension(R.dimen.view_10));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	

}
