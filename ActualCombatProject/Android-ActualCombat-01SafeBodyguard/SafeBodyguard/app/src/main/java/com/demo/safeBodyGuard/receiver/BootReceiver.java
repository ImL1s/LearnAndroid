package com.demo.safeBodyGuard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.utils.SPUtil;

/**
 * Created by iml1s-macpro on 2017/1/17.
 */

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("debug", "********** onBootReceive **********");

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String saveSerialNum = SPUtil.getString(context, Config.SP_KEY_STRING_SIM_SERIAL_NUM, null);
        String currSerialNum = tm.getSimSerialNumber() + "password";

        if (!saveSerialNum.equals(currSerialNum))
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5556", null, "sim change!!", null, null);
        }
    }
}
