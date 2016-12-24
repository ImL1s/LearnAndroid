package com.project.lottowebview.a3_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by iml1s-macpro on 2016/11/30.
 */

public class EavesdropReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("debug","OnReceive");
        Toast.makeText(context,"OnReceive",Toast.LENGTH_LONG).show();
        byte[][] pdus = (byte[][])intent.getExtras().get("pdus");

        for (byte[] pdu: pdus)
        {
            SmsMessage smsMessage = SmsMessage.createFromPdu(pdu);
            String smsBody = smsMessage.getMessageBody();
            String address = smsMessage.getOriginatingAddress();

            Log.d("debug",String.format("SMSBody:%s SMSAddress:%s",smsBody ,address));
        }
    }
}
