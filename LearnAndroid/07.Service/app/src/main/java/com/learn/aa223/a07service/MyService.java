package com.learn.aa223.a07service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by aa223 on 2016/10/20.
 */

public class MyService extends Service
{

    public MyService(){}

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("debug", "OnStartCommand...");

        new Thread()
        {
            @Override
            public void run()
            {
                super.run();

                while (true)
                {
                    Log.d("debug", "Runing...");
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }
}
