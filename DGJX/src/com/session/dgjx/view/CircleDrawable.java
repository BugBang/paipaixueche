package com.session.dgjx.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

/** 圆形图片 */
public class CircleDrawable extends Drawable {
	private Paint mPaint;
	private int mWidth;
	private Bitmap mBitmap;

	public CircleDrawable(Bitmap bm) {
		if (null == bm)
			return;
		mBitmap = bm;
		BitmapShader bitmapShader = new BitmapShader(bm, TileMode.CLAMP, TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
	}

	@Override
	public int getIntrinsicWidth() {
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight() {
		return mWidth;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

}
