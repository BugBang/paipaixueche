package com.session.dgjp.view;

import com.squareup.picasso.Transformation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class RoundCornerTransformation implements Transformation
{
	@Override
	public Bitmap transform(Bitmap source)
	{
		int widthLight = source.getWidth();  
        int heightLight = source.getHeight();  
  
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);  
  
        Canvas canvas = new Canvas(bitmap);  
        Paint paintColor = new Paint();  
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);  
  
        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));  
  
        canvas.drawRoundRect(rectF, 10, 10, paintColor);  
  
        Paint paintImage = new Paint();  
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));  
        canvas.drawBitmap(source, 0, 0, paintImage);  
        source.recycle();  
        return bitmap; 
	}

	@Override
	public String key()
	{
		return "roundcorner";
	}
}