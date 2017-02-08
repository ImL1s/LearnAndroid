package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.demo.safeBodyGuard.db.BlackListOpenHelper;
import com.demo.safeBodyGuard.db.dao.BlackListDAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlackListService extends Service
{
    private BlackListDAO       mBlackListDAO       = null;
    private InnerSMSReceiver   mSmsReceiver        = null;
    private TelephonyManager   mTelephonyManager   = null;
    private PhoneStateListener mPhoneStateListener = null;

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

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener = new BlackListPhoneStateListener(),
                                 PhoneStateListener.LISTEN_CALL_STATE);
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
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
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

                if (mode == 1 || mode == 2)
                {
                    //拦截短信(android 4.4版本失效	短信数据库,删除)
                    abortBroadcast();
                }
            }
        }
    }


    class BlackListPhoneStateListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if (state == TelephonyManager.CALL_STATE_RINGING)
            {
                try
                {
                    Class<?> clazz = Class.forName("android.os.ServiceManager");
                    Method method = clazz.getMethod("getService", String.class);
                    IBinder binder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                    ITelephony telephony = ITelephony.Stub.asInterface(binder);
                    telephony.endCall();

                    getContentResolver().delete(Uri.parse("content://call_log/calls"),
                                                 "number = ?",
                                                new String[]{incomingNumber});
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
