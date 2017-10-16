package com.lyx.frame.animator;

/**
 * TransFormFactory
 * <p>
 * author:  luoyingxing
 * date: 2017/10/16.
 */

import android.support.v4.view.ViewPager;

public class TransFormFactory {
    public static final String DEPTH_PAGE_TRANSFORMER = "DepthPageTransformer";
    public static final String ZOOM_OUT_PAGE_TRANSFORMER = "ZoomOutPageTransformer";
    public static final String ROTATE_DOWN_TRANSFORMER = "RotateDownPageTransformer";
    public static final String OVER_TURN_PAGE_TRANSFORMER = "OverturnPageTransformer";
    public static final String COMPRESS_PAGE_TRANSFORMER = "CompressPageTransformer";
    public static final String TABLET_PAGE_TRANSFORMER = "TabletPageTransformer";
    public static final String CUBE_TRANSFORMER = "CubeTransformer";

    public static ViewPager.PageTransformer getTransFormer(String type) {
        switch (type) {
            case DEPTH_PAGE_TRANSFORMER:
                return new DepthPageTransformer();
            case ZOOM_OUT_PAGE_TRANSFORMER:
                return new ZoomOutPageTransformer();
            case ROTATE_DOWN_TRANSFORMER:
                return new RotateDownPageTransformer();
            case OVER_TURN_PAGE_TRANSFORMER:
                return new OverturnPageTransformer();
            case COMPRESS_PAGE_TRANSFORMER:
                return new CompressPageTransformer();
            case TABLET_PAGE_TRANSFORMER:
                return new TabletPageTransformer();
            case CUBE_TRANSFORMER:
                return new CubeTransformer();
            default:
                return null;
        }
    }

    public static ViewPager.PageTransformer getTransFormer(int type) {
        switch (type) {
            case 1:
                return new DepthPageTransformer();
            case 2:
                return new ZoomOutPageTransformer();
            case 3:
                return new RotateDownPageTransformer();
            case 4:
                return new OverturnPageTransformer();
            case 5:
                return new CompressPageTransformer();
            case 6:
                return new TabletPageTransformer();
            case 7:
                return new CubeTransformer();
            default:
                return null;
        }
    }
}

