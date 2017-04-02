package com.demo.safeBodyGuard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.demo.safeBodyGuard.db.dao.model.AppLockInfo;
import com.demo.safeBodyGuard.db.helper.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockDAO {
    private static final Object LOCK = new Object();
    private static AppLockDAO mInstance;

    private final Context mContext;
    private final AppLockOpenHelper mAppLockOpenHelper;
    private final String tb_appLock = "appLock";

    public static AppLockDAO getInstance(Context context) {
        synchronized (LOCK) {
            if (mInstance == null) {
                mInstance = new AppLockDAO(context);
            }

            return mInstance;
        }
    }

    public AppLockDAO(Context context) {
        mContext = context;
        mAppLockOpenHelper = new AppLockOpenHelper(mContext);
    }

    public List<AppLockInfo> selectAll() {
        SQLiteDatabase db = mAppLockOpenHelper.getReadableDatabase();
        Cursor cursor = db
                .query(tb_appLock, new String[]{"_id", "pkg"}, null, null, null, null, null);

        List<AppLockInfo> appLockInfoList = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                appLockInfoList.add(new AppLockInfo(cursor.getInt(0), cursor.getString(1)));
            }
        }
        cursor.close();
        db.close();

        return appLockInfoList;
    }

    public void insert(String pkgName) {
        SQLiteDatabase db = mAppLockOpenHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("pkg", pkgName);

        db.insert(tb_appLock, null, contentValues);
        db.close();
        noticeDataChange();
    }

    public void delete(String pkgName) {
        SQLiteDatabase db = mAppLockOpenHelper.getReadableDatabase();
        db.delete(tb_appLock, "pkg = ?", new String[]{pkgName});
        db.close();
        noticeDataChange();
    }

    public Object select(int id) {
        return null;
    }

    public void noticeDataChange() {
        mContext.getContentResolver().notifyChange(Uri.parse("content://applock/change"), null);
    }
}
