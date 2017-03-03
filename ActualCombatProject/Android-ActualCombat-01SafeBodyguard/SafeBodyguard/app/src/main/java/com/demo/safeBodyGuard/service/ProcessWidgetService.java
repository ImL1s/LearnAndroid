package com.demo.safeBodyGuard.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

    @Override
    public void onCreate()
    {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                AppWidgetManager am = AppWidgetManager.getInstance(getApplicationContext());
                RemoteViews remoteViews =
                        new RemoteViews(getPackageName(), R.layout.widget_process_info);
                remoteViews.setTextViewText(R.id.tv_process_count, ProcessInfoProvider
                                                                           .getProcessCount(
                                                                                   getApplicationContext()) +
                                                                   "");
                ComponentName componentName =
                        new ComponentName(ProcessWidgetService.this, ProcessWidgetProvider.class);

                am.updateAppWidget(componentName, remoteViews);
            }
        }, 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
