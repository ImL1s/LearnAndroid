package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;


/**
 * Created by ImL1s on 2017/2/23.
 * <p>
 * DESC:
 */

public class ScreenLockCleanProcessService extends Service
{
    @Override
    public void onCreate()
    {

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    class r extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }
}
