package com.demo.safeBodyGuard.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by iml1s-macpro on 2017/1/19.
 */

public class SafeGuardDeviceAdminReceiver extends DeviceAdminReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }
}
