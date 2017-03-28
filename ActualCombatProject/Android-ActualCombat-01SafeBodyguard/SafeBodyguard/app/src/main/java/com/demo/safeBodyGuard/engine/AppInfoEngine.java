package com.demo.safeBodyGuard.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.demo.safeBodyGuard.db.dao.AppLockInfo;
import com.demo.safeBodyGuard.db.dao.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iml1s on 2017/2/13.
 */

public class AppInfoEngine
{
    private static AppInfoEngine appInfoEngine;

    public static AppInfoEngine GetInstance(Context context)
    {
        if(appInfoEngine == null)
        {
            appInfoEngine = new AppInfoEngine();
        }

        return appInfoEngine;
    }

    public static List<AppInfo> getAppInfos(Context ctx)
    {
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);
        List<AppInfo> appInfoList = new ArrayList<>();

        for (PackageInfo pInfo : packageInfoList)
        {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = pInfo.packageName;
            appInfo.name = pInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.icon = pInfo.applicationInfo.loadIcon(pm);

            appInfo.isSystem = ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM);
            appInfo.isSdCard = ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE);
            appInfoList.add(appInfo);
        }

        return appInfoList;
    }
}
