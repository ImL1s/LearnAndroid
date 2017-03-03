package com.demo.safeBodyGuard.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.demo.safeBodyGuard.service.ProcessWidgetService;


/**
 * Created by ImL1s on 2017/3/3.
 * <p>
 * DESC:
 */

public class ProcessWidgetProvider extends AppWidgetProvider
{
    /**
     * 收到通知
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("debug", "onReceive ..");
        super.onReceive(context, intent);
    }

    /**
     * 第一次啟動，也就是置入第一個Widget
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context)
    {
        Log.d("debug", "onEnabled");
        context.startService(new Intent(context, ProcessWidgetService.class));
        super.onEnabled(context);
    }

    /**
     * 放入一個新的Widget
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.d("debug", "onUpdate");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Widget被調整大小或是加入新的Widget時被調用
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions)
    {
        Log.d("debug", "onAppWidgetOptionsChanged");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 移除一個Widget時被調用
     *
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        Log.d("debug", "onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 全部Widget都被移除時被調用
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context)
    {
        Log.d("debug", "onDisabled");
        context.stopService(new Intent(context, ProcessWidgetService.class));
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds)
    {
        Log.d("debug", "onRestored");
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
