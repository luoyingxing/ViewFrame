package com.lyx.frame;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * App
 * <p>
 * Created by luoyingxing on 2017/5/2.
 */

public class App extends Application {
    private static App mApp;

    public static Context getAppContext() {
        return mApp;
    }

    public static App getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Fresco.initialize(this);
    }
}