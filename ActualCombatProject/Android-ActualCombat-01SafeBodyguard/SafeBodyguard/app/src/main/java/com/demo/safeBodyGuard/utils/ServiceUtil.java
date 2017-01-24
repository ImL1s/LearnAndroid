package com.demo.safeBodyGuard.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by iml1s-macpro on 2017/1/24.
 */

public class ServiceUtil
{
    /**
     * 檢測指定服務是否執行中
     * @param serviceName 要查詢服務名稱
     * @param context 上下文
     * @return 返回指定的服務是否執行中
     */
    public static boolean isRunnung(String serviceName, Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(999);

        for (ActivityManager.RunningServiceInfo info : runningServices)
        {
            if(info.service.getClassName().equals(serviceName)) return true;
        }

        return false;
    }
}
