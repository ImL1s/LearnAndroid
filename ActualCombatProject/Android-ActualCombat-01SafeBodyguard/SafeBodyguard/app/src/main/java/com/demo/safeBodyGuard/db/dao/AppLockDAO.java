package com.demo.safeBodyGuard.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safeBodyGuard.db.helper.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockDAO
{
    private static final Object LOCK = new Object();
    private static AppLockDAO mInstance;

    private final Context           mContext;
    private final AppLockOpenHelper mAppLockOpenHelper;

    public static AppLockDAO getInstance(Context context)
    {
        synchronized (LOCK)
        {
            if(mInstance == null)
            {
                mInstance = new AppLockDAO(context);
            }

            return mInstance;
        }
    }

    public AppLockDAO(Context context)
    {
        mContext = context;
        mAppLockOpenHelper = new AppLockOpenHelper(mContext);
    }

    public List<AppLockInfo> selectAll()
    {
        SQLiteDatabase db = mAppLockOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("appLock", new String[]{"_id", "pkg"}, null, null, null, null, null);

        List<AppLockInfo> appLockInfoList = new ArrayList<>();

        if (cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                appLockInfoList.add(new AppLockInfo(cursor.getInt(0), cursor.getString(1)));
            }
        }
        cursor.close();
        db.close();

        return appLockInfoList;
    }

    public Object select(int id)
    {
        return null;
    }
}
