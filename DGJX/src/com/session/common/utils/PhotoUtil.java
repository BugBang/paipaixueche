package com.session.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.session.dgjx.view.CircleTransformation;
import com.squareup.picasso.Picasso;

public class PhotoUtil {
    private static CircleTransformation circleTransformation = new CircleTransformation();

    public static void showPhoto(Context context, ImageView imageView, final String url, int defaultDrawableId) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).transform(circleTransformation).placeholder(defaultDrawableId).error(defaultDrawableId).into(imageView);
        } else {
            Picasso.with(context).load(defaultDrawableId).transform(circleTransformation).into(imageView);
        }
    }
}
