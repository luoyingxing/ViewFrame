package com.lyx.frame.animator;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * TabletPageTransformer
 * <p>
 * author:  luoyingxing
 * date: 2017/10/16.
 */
public class TabletPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if (position < -1) {
            view.setAlpha(1);
        } else if (position <= 1) {
            float mRot;
            if (position < 0) {
                mRot = 30.0f * Math.abs(position);
                view.setPivotX(width / 2);
                view.setPivotY(height / 2);
                view.setRotationY(mRot);
            } else {
                mRot = -30.0f * Math.abs(position);
                view.setPivotX(width / 2);
                view.setPivotY(height / 2);
                view.setRotationY(mRot);
            }
        } else {
            view.setAlpha(1);
        }
    }
}
