package com.demo.safeBodyGuard.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.engine.ProcessInfoProvider;
import com.demo.safeBodyGuard.receiver.ProcessWidgetProvider;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ImL1s on 2017/3/3.
 * <p>
 * DESC:
 */

public class ProcessWidgetService extends Service
{
    private Timer mTimer = null;
    private ScreenStateReceiver mScreenStateReceiver;


    @Override
    public void onCreate()
    {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        startTimer();

        mScreenStateReceiver = new ScreenStateReceiver();
        registerReceiver(mScreenStateReceiver, intentFilter);
    }

    /**
     * 開啟RemoteView更新Timer
     */
    private void startTimer()
    {
        if(mTimer != null)
        {
            return;
        }

        mTimer = new Timer();
        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                updateWidget();
            }
        }, 0, 1000);
    }

    private void cancelTimer()
    {
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void updateWidget()
    {
        AppWidgetManager am = AppWidgetManager.getInstance(getApplicationContext());
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_process_info);

        int processCount = ProcessInfoProvider.getProcessCount(getApplicationContext());

        remoteViews.setTextViewText(R.id.tv_process_count, "進程總數: " + processCount);
        float availMemory =
                (float) ProcessInfoProvider.getAvailSpace(getApplicationContext()) / 1024 / 1024 /
                1024;

        remoteViews.setTextViewText(R.id.tv_process_memory, "可用記憶體: " + availMemory + "G");

        Intent actIntent = new Intent("android.intent.action.HOME");
        Intent clearIntent = new Intent("android.intent.action.KILL_BACKGROUND_PROCESS");

        actIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent openPIntent =
                PendingIntent.getActivity(this, 0, actIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        PendingIntent clearPIntent =
                PendingIntent.getBroadcast(this, 0, clearIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.widget_process_info_root, openPIntent);

        remoteViews.setOnClickPendingIntent(R.id.btn_btn_clear, clearPIntent);

        ComponentName componentName =
                new ComponentName(ProcessWidgetService.this, ProcessWidgetProvider.class);

        am.updateAppWidget(componentName, remoteViews);
    }

    @Override
    public void onDestroy()
    {
        if(mScreenStateReceiver != null)
        {
            unregisterReceiver(mScreenStateReceiver);
            mScreenStateReceiver = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    class ScreenStateReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
            {
                startTimer();
            }
            else
            {
                cancelTimer();
            }
        }
    }
}
