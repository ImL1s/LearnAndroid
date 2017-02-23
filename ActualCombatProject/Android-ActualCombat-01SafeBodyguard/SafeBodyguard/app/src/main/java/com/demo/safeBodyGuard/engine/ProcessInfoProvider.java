package com.demo.safeBodyGuard.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.engine.model.ProcessInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iml1s on 2017/2/19.
 */

public class ProcessInfoProvider
{
    /**
     * 取得運行中的進程數
     *
     * @param ctx
     * @return
     */
    public static int getProcessCount(Context ctx)
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }


    /**
     * 取得剩餘可用的記憶體
     *
     * @param ctx
     * @return
     */
    public static long getAvailSpace(Context ctx)
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);

        return memoryInfo.availMem;
    }


    /**
     * 取得裝置總共記憶體大小
     *
     * @param ctx
     * @return
     */
    public static long getTotalSpace(Context ctx)
    {
        if (Build.VERSION.SDK_INT >= 16)
        {
            ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memoryInfo);

            return memoryInfo.totalMem;
        }

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try
        {
            fileReader = new FileReader("proc/meminfo");
            bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            char[] charArray = line.toCharArray();
            StringBuffer sBuffer = new StringBuffer();

            for (char c : charArray)
            {
                if (c >= '0' && c <= '9')
                {
                    sBuffer.append(c);
                }
            }

            String s_totalMemo = sBuffer.toString();
            long totalMemo = Long.parseLong(s_totalMemo);

            return totalMemo;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fileReader != null)
                    fileReader.close();
                if (bufferedReader != null)
                    bufferedReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return -1;
        }
    }


    /**
     * 取得當前正在運行的應用資訊
     *
     * @param ctx
     * @return
     */
    public static List<ProcessInfo> getProcessInfo(Context ctx)
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = ctx.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
                am.getRunningAppProcesses();
        List<ProcessInfo> processInfoList = new ArrayList<>();

        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfoList)
        {
            ProcessInfo processInfo = new ProcessInfo();

            processInfo.packageName = info.processName;

            android.os.Debug.MemoryInfo memoryInfo =
                    am.getProcessMemoryInfo(new int[]{info.pid})[0];

            processInfo.privateDirty = memoryInfo.getTotalPrivateDirty();


            try
            {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processInfo.packageName, 0);

                processInfo.isSystem = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==
                                       ApplicationInfo.FLAG_SYSTEM;

                processInfo.appName = applicationInfo.loadLabel(pm).toString();

                processInfo.icon = applicationInfo.loadIcon(pm);

            }
            catch (PackageManager.NameNotFoundException e)
            {
                processInfo.appName = processInfo.packageName;
                processInfo.icon = ctx.getResources().getDrawable(R.mipmap.ic_launcher);
                e.printStackTrace();
            }
            finally
            {
                processInfoList.add(processInfo);
            }
        }

        return processInfoList;
    }

    /**
     * 殺死進程
     * @param context
     * @param processInfo
     */
    public static void killProcess(Context context, ProcessInfo processInfo)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        am.killBackgroundProcesses(processInfo.packageName);
    }
}
