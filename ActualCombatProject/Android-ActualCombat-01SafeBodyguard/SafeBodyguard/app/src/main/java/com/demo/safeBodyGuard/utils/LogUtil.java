package com.demo.safeBodyGuard.utils;

import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */

public class LogUtil
{
    private final static String TAG_DEBUG = "debug";

    public static void log(String str)
    {
        Log.d(TAG_DEBUG,str);
    }

    public static void log(String format,String str)
    {
        Log.d(TAG_DEBUG,String.format(format,str));
    }
}
