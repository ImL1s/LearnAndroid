package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.dao.AddressDAO;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ThreadUtil;

/**
 * Created by iml1s-macpro on 2017/1/23.
 */

public class PhoneAddressService extends Service
{
    public final static String TAG = "Debug";

    private TelephonyManager        mTelephonyManager;
    private WindowManager           mWindowManager;
    private QueryPhoneStateListener mListener;
    private ViewGroup               vg_toast_phone_listen_view;
    private String mAddress = "";
    private TextView textView;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == HandlerProtocol.QUERY_PHONE_ADDRESS_COMPLETED)
            {
                textView.setText(mAddress);
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        mTelephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mListener = new QueryPhoneStateListener();
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    class QueryPhoneStateListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            switch (state)
            {
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtil.log("CALL_STATE_IDLE");
                    removeAddressToast();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtil.log("CALL_STATE_OFFHOOK");
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtil.log("CALL_STATE_RINGING");
                    showAddressToast();
                    queryAddress(incomingNumber);
                    break;
            }
        }

        private void removeAddressToast()
        {
            if (mWindowManager != null && vg_toast_phone_listen_view != null)
            {
                mWindowManager.removeView(vg_toast_phone_listen_view);
            }
        }

        private void showAddressToast()
        {
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                           //                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                           | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            //在响铃的时候显示吐司,和电话类型一致
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.setTitle("Toast");

            //指定吐司的所在位置(将吐司指定在右上角)
            params.gravity = Gravity.RIGHT + Gravity.TOP;

            int bgIndex = SPUtil.getInt(getApplicationContext(), Config.SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUNG_INDEX, 0);
            vg_toast_phone_listen_view = (ViewGroup) View.inflate(getApplicationContext(), R.layout.toast_phone_listen_view, null);
            textView = (TextView) vg_toast_phone_listen_view.findViewById(R.id.toast_phone_view_tv_title);
            textView.setText("查詢中...");
            textView.setBackgroundResource(Config.DRAWABLE_RESOURCE_ID_ARRAY_PHONE_QUERY_ADDR_VIEW_BG[bgIndex]);

            mWindowManager.addView(vg_toast_phone_listen_view, params);

        }

        private void queryAddress(String phoneNumber)
        {
            ThreadUtil.start(() -> {
                mAddress = AddressDAO.getAddress(phoneNumber);
                mHandler.sendEmptyMessage(HandlerProtocol.QUERY_PHONE_ADDRESS_COMPLETED);
            });

        }
    }

    @Override
    public void onDestroy()
    {
        if (mTelephonyManager != null && mListener != null)
        {
            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
        }
    }
}
