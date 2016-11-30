package com.session.dgjp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by solo on 15/2/9.
 */
public class FractionTranslateLayout extends LinearLayout {

    private static final String TAG = FractionTranslateLayout.class.getName();

    public FractionTranslateLayout(Context context) {
        super(context);
    }

    public FractionTranslateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public float getXFraction() {
        int width = getWidth();
        return (width == 0) ? 0 : getX() / (float) width;
    }

    public void setXFraction(float xFraction) {
        int width = getWidth();
        setX((width > 0) ? (xFraction * width) : 0);
    }
}