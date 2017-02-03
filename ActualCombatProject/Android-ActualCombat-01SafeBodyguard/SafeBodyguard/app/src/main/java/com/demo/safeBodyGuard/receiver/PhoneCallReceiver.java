package com.demo.safeBodyGuard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ImL1s on 2017/2/3.
 *
 * DESC:
 */

public class PhoneCallReceiver extends BroadcastReceiver
{
    private OnReceiverNewOutGoingCallListener mListener = null;

    public PhoneCallReceiver(OnReceiverNewOutGoingCallListener listener)
    {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String callPhoneNum =  getResultData();
        mListener.receiver(callPhoneNum);

    }

    public interface OnReceiverNewOutGoingCallListener
    {
        public void receiver(String phoneNum);
    }
}
