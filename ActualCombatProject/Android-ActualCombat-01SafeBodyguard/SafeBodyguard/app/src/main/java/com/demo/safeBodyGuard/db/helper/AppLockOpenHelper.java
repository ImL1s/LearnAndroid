package com.demo.safeBodyGuard.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockOpenHelper extends SQLiteOpenHelper
{

    public AppLockOpenHelper(Context context)
    {
        super(context, "appLock", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE appLock(_id INTEGER PRIMARY KEY AUTOINCREMENT, pkg varchar(50));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
