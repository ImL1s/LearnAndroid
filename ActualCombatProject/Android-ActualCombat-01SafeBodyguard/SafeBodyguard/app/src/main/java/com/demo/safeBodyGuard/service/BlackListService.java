package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.demo.safeBodyGuard.db.dao.BlackListDAO;

import java.lang.reflect.Method;

public class BlackListService extends Service
{
    private final static String CONTENT_URI_CALL_LOG = "content://call_log/calls";


    private BlackListDAO       mBlackListDAO       = null;
    private InnerSMSReceiver   mSmsReceiver        = null;
    private TelephonyManager   mTelephonyManager   = null;
    private PhoneStateListener mPhoneStateListener = null;
    private InnerHandler       mHandler            = new InnerHandler();

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
        mTelephonyManager.listen(mPhoneStateListener = new InnerBlackListPhoneStateListener(),
                                 PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        if(mSmsReceiver != null) unregisterReceiver(mSmsReceiver);
        if(mPhoneStateListener != null && mTelephonyManager != null) mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
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


    class InnerBlackListPhoneStateListener extends PhoneStateListener
    {

        private ContentObserver mObserver;

        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if (state == TelephonyManager.CALL_STATE_RINGING && needBlock(incomingNumber))
            {
                try
                {
                    Class<?> clazz = Class.forName("android.os.ServiceManager");
                    Method method = clazz.getMethod("getService", String.class);
                    IBinder binder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                    ITelephony telephony = ITelephony.Stub.asInterface(binder);
                    telephony.endCall();

                    if (mObserver != null)
                        getContentResolver().unregisterContentObserver(mObserver);

                    mObserver = new InnerCallLogObserver(mHandler, incomingNumber,
                                                         getContentResolver());
                    getContentResolver()
                            .registerContentObserver(Uri.parse(CONTENT_URI_CALL_LOG), false, mObserver);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 判斷是否為黑名單裡的電話號碼
         * @param incomingNumber
         * @return
         */
        private boolean needBlock(String incomingNumber)
        {
            int mode = BlackListDAO.getInstance(getApplicationContext()).getMode(incomingNumber);
            return mode == 0 || mode == 2;
        }
    }


    static class InnerHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {

        }
    }


    static class InnerCallLogObserver extends ContentObserver
    {
        private String          mIncomingNumber = null;
        private ContentResolver mResolver       = null;


        InnerCallLogObserver(Handler handler, String incomingNumber, ContentResolver resolver)
        {
            super(handler);
            this.mIncomingNumber = incomingNumber;
            this.mResolver = resolver;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            super.onChange(selfChange, uri);

            if (uri.toString().equals(CONTENT_URI_CALL_LOG))
            {
                mResolver.delete(Uri.parse(CONTENT_URI_CALL_LOG), "number = ?",
                                 new String[]{mIncomingNumber});
            }
        }
    }
}
