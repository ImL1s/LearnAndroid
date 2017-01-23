package com.demo.safeBodyGuard.utils;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class ThreadUtil
{
    public static void start(Runnable runnable)
    {
        new Thread(runnable).start();
    }

    /*
     * 安排兩個任務,這兩個任務中間間隔必須超過minTime毫秒
     */
    public static void scheduleTaskInMinTime(Runnable taskOne, Runnable taskTwo, long minTime)
    {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();

            taskOne.run();

            long stopTime = System.currentTimeMillis();
            long interval = stopTime - startTime;
            if(interval < minTime)
            {
                try
                {
                    Thread.sleep(minTime - interval);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            taskTwo.run();
        }).start();
    }
}
