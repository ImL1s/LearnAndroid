package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.demo.safeBodyGuard.db.dao.BlackListDAO;

public class BlackListService extends Service
{
    private BlackListDAO mBlackListDAO = null;
    private InnerSMSReceiver mSmsReceiver = null;

    public BlackListService()
    {

    }

    @Override
    public void onCreate()
    {
        mBlackListDAO = BlackListDAO.getInstance(getApplicationContext());
        mSmsReceiver = new InnerSMSReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mSmsReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(mSmsReceiver);
    }

    class InnerSMSReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            if (objects == null)
                return;

            for (Object object : objects)
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);

                String originatingAddress = sms.getOriginatingAddress().trim();
//                String messageBody = sms.getMessageBody();

                int mode = mBlackListDAO.getMode(originatingAddress);

                if (mode == 0 || mode == 2)
                {
                    //拦截短信(android 4.4版本失效	短信数据库,删除)
                    abortBroadcast();
                }
            }

        }
    }

    class B extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

        }
    }
}
