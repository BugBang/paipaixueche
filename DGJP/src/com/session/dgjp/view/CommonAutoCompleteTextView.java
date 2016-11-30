package com.session.dgjp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

public class CommonAutoCompleteTextView extends AutoCompleteTextView
{
	public CommonAutoCompleteTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CommonAutoCompleteTextView(Context context)
	{
		super(context);
	}
	
	public CommonAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean enoughToFilter()
	{
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(MotionEvent.ACTION_DOWN==event.getAction() && getFilter() != null && !isPopupShowing())
		{
			performFiltering(getText(), 0);
			showDropDown();
		}
		return super.onTouchEvent(event);
	}

}
