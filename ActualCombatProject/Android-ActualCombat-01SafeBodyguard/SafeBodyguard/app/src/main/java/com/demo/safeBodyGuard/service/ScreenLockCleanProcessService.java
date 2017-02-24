package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.demo.safeBodyGuard.engine.ProcessInfoProvider;


/**
 * Created by ImL1s on 2017/2/23.
 * <p>
 * DESC:
 */
public class ScreenLockCleanProcessService extends Service
{
    BroadcastReceiver lockScreenReceiver;

    @Override
    public void onCreate()
    {
        Log.d("debug","***** onCreate *****");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockScreenReceiver = new LockScreenReceiver(), filter);
    }

    @Override
    public void onDestroy()
    {
        if(lockScreenReceiver != null) unregisterReceiver(lockScreenReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    class LockScreenReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ProcessInfoProvider.killAllProcess(getApplicationContext());
        }
    }
}
