package com.demo.safeBodyGuard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.demo.safeBodyGuard.engine.ProcessInfoProvider;


/**
 * Created by ImL1s on 2017/3/6.
 * <p>
 * DESC:
 */

public class KillProcessReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ProcessInfoProvider.killAllProcess(context);
    }
}
