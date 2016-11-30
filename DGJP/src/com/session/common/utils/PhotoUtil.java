package com.session.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.session.dgjp.view.CircleDrawable;
import com.session.dgjp.view.CircleTransformation;
import com.session.dgjp.view.RoundCornerTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoUtil
{
	private static CircleTransformation circleTransformation = new CircleTransformation();
	private static RoundCornerTransformation roundCornerTransformation  = new RoundCornerTransformation();
	
	public static void showCirclePhoto(Context context, ImageView imageView , final String url, int defaultDrawableId)
	{
		if (!TextUtils.isEmpty(url))
		{
			Picasso.with(context).load(url).transform(circleTransformation).placeholder(defaultDrawableId).error(defaultDrawableId).into(imageView);
		} else
		{
			Picasso.with(context).load(defaultDrawableId).transform(circleTransformation).into(imageView);
		}
	}
	
	public static void showRoundCornerPhoto(Context context, ImageView imageView, String url, int defaultDrawableId)
	{
		if (!TextUtils.isEmpty(url))
		{
			Picasso.with(context).load(url).transform(roundCornerTransformation).placeholder(defaultDrawableId).error(defaultDrawableId).into(imageView);
		}else {
			Picasso.with(context).load(defaultDrawableId).transform(roundCornerTransformation).into(imageView);
		}
	}
}
