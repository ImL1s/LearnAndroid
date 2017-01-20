package com.demo.safeBodyGuard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.service.SafeGuardLocationService;
import com.demo.safeBodyGuard.utils.SPUtil;

/**
 * Created by iml1s-macpro on 2017/1/17.
 */

public class SmsReceiver extends BroadcastReceiver
{

    static MediaPlayer mMediaPlayer = null;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean isOpenSecurity = SPUtil.getBool(context, Config.SP_KEY_BOOL_OPEN_SAFE_GUARD, false);

        if (isOpenSecurity)
        {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for (Object object : objects)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);

                String alarmNum = SPUtil.getString(context, Config.SP_KEY_STRING_ALARM_PHONE_NUMBER, "");
                String address = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody();


                //                if(address.equals(alarmNum))
                {

                    switch (body)
                    {
                        case Config.SMS_CONTROL_KEY_ALARM:
                            alarmPhone(context);
                            abortBroadcast();
                            break;

                        case Config.SMS_CONTROL_KEY_LOCATION:
                            locationPhone(context);
                            abortBroadcast();
                            break;

                        case Config.SMS_CONTROL_KEY_STOP_LOCATION:
                            stopLocationPhone(context);
                            abortBroadcast();
                            break;
                    }
                }
            }
        }
    }

    private void stopLocationPhone(Context context)
    {
        context.stopService(new Intent(context,SafeGuardLocationService.class));
        Log.d("debug","stopLocationPhone");
    }


    private void alarmPhone(Context context)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();

            mMediaPlayer = MediaPlayer.create(context, R.raw.alarm);
            if (!mMediaPlayer.isLooping()) mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            Log.d("debug","alarmPhone");
        }
    }

    private void locationPhone(Context context)
    {
        Intent intent = new Intent(context, SafeGuardLocationService.class);
        context.startService(intent);
        Log.d("debug","locationPhone");
    }
}
