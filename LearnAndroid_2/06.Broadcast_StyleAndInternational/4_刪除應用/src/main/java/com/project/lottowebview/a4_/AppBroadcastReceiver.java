package com.project.lottowebview.a4_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/1.
 */

public class AppBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if(action.equals("android.intent.action.PACKAGE_INSTALL"))
        {
            Log.d("debug","===== PACKAGE_INSTALL");
        }
        else if(action.equals("android.intent.action.PACKAGE_ADDED"))
        {
            Log.d("debug","===== PACKAGE_ADDED");
        }
        else if (action.equals("android.intent.action.PACKAGE_REMOVED"))
        {
            Log.d("debug","===== PACKAGE_REMOVED");
        }
    }
}
