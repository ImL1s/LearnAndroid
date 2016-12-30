package com.demo.safeBodyGuard.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

public class PackageUtil
{
    /*
    * 取得當前versionCode
    * */
    public static int getVersionCode(PackageManager manager,String packageName)
    {
        PackageInfo info = getPackageInfo(manager, packageName, 0);

        int versionCode = -1;
        if(info != null) versionCode = info.versionCode;

        return versionCode;
    }

    public static String getVersionName(PackageManager manager,String packageName)
    {
        PackageInfo info = getPackageInfo(manager, packageName, 0);

        String versionName = null;
        if(info != null) versionName = info.versionName;

        return versionName;
    }

    public static PackageInfo getPackageInfo(PackageManager manager,String packageName,int flag)
    {
        PackageManager pm = manager;
        PackageInfo info = null;

        try
        {
            info = pm.getPackageInfo(packageName, flag);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e("error",e.toString());
        }
        finally
        {
            return info;
        }

    }

}
