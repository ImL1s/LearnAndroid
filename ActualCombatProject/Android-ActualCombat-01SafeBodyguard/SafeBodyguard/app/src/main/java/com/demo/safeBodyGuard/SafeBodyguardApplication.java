package com.demo.safeBodyGuard;

import android.app.Application;

import org.xutils.x;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class SafeBodyguardApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
