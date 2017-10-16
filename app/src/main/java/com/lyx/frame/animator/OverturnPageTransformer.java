package com.lyx.frame.animator;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * OverturnPageTransformer
 * <p>
 * Created by luoyingxing on 2017/5/3.
 */
public class OverturnPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
//        final float MIN_SCALE = 0.8f;
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 1) {
            float mRot;
            float ROT_MAX = 180;
            //下面为设置绕中轴线旋转，不加会导致页面平移
            view.setTranslationX(pageWidth * -position);
            if (position < 0) {
                //下面为设置要出去的页面，滑动到一半时隐藏
                if (position <= -0.5)
                    view.setAlpha(0);
                else
                    view.setAlpha(1);
                mRot = (ROT_MAX * position);
                view.setPivotX(pageWidth * 0.5f);
                view.setRotationY(mRot);

            } else {
                //下面为设置要进入的页面，滑动到一半时显示
                if (0 <= position && position <= 0.5)
                    view.setAlpha(1);
                else
                    view.setAlpha(0);
                mRot = (ROT_MAX * position);
                view.setPivotX(pageWidth * 0.5f);
                view.setRotationY(mRot);

            }
        } else {
            view.setAlpha(0);
        }
    }
}
