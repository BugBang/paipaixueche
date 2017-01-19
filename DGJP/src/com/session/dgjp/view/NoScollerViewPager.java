package com.session.dgjp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScollerViewPager extends ViewPager{

 	private boolean isPagingEnabled = false;
    public NoScollerViewPager(Context context) {
        super(context);
    }
    public NoScollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }
    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
    @Override  
    public void setCurrentItem(int item, boolean smoothScroll) {  
        super.setCurrentItem(item, smoothScroll);  
    }  

    @Override  
    public void setCurrentItem(int item) {  
        super.setCurrentItem(item, false);  
    }  
}
