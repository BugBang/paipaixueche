package com.session.common.utils;

import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by user on 2016-09-21.
 */
public class AnimUtil {

    public static void ValueAnimator(float startValue, float endValue, final View view, int duration) {
        android.animation.ValueAnimator _valueAnimator = android.animation.ValueAnimator.ofFloat(startValue, endValue).setDuration(duration);
        _valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                float animatedFraction = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (int)animatedFraction;
                view.setLayoutParams(layoutParams);
                view.invalidate();
            }
        });
        _valueAnimator.start();
    }


    public static void RotationAnimator(float startValueX, float endValueX, float startValueY, float endValueY, float startValueZ, float endValueZ, final View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", startValueX, endValueX),
                ObjectAnimator.ofFloat(view, "rotationY", startValueY, endValueY),
                ObjectAnimator.ofFloat(view, "rotation", startValueZ, endValueZ)
        );
        set.setDuration(duration).start();
    }

    public static void TranslationAnimator(float startValueX, float endValueX, float startValueY, float endValueY, final View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", startValueX, endValueX),
                ObjectAnimator.ofFloat(view, "translationY", startValueY, endValueY)
        );
        set.setDuration(duration).start();
    }

    public static void AlphaAnimator(float startValueX, float endValueX, final View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "alpha", startValueX, endValueX)
        );
        set.setDuration(duration).start();
    }


    public static void ScaleAnimator(float startValueX, float endValueX, float startValueY, float endValueY, final View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", startValueX, endValueX),
                ObjectAnimator.ofFloat(view, "scaleY", startValueY, endValueY)
        );
        set.setDuration(duration).start();
    }

    public static void TogetherAnimator(float startRotationValueX, float endRotationValueX,
                                        float startRotationValueY, float endRotationValueY,
                                        float startRotation, float endRotation,
                                        float startTranslationX, float endTranslationX,
                                        float startTranslationY, float endTranslationY,
                                        float startScaleX, float endScaleX,
                                        float startScaleY, float endScaleY,
                                        float startAlpha, float endAlpha,
                                        final View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", startRotationValueX, endRotationValueX),
                ObjectAnimator.ofFloat(view, "rotationY", startRotationValueY, endRotationValueY),
                ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation),
                ObjectAnimator.ofFloat(view, "translationX", startTranslationX, endTranslationX),
                ObjectAnimator.ofFloat(view, "translationY", startTranslationY, endTranslationY),
                ObjectAnimator.ofFloat(view, "scaleX", startScaleX, endScaleX),
                ObjectAnimator.ofFloat(view, "scaleY", startScaleY, endScaleY),
                ObjectAnimator.ofFloat(view, "alpha",startAlpha,endAlpha)
        );
        set.setDuration(duration).start();
    }
}
