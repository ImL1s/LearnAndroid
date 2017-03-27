package com.demo.safeBodyGuard.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by ImL1s on 2017/2/3.
 * <p>
 * DESC:
 */

public class BlackListOpenHelper extends SQLiteOpenHelper
{
    public static final String TABLE_NAME_BLACKLIST  = "blackList";
    public static final String COL_NAME_ID = "id";
    public static final String COL_NAME_PHONE_NUMBER = "phone_number";
    public static final String COL_NAME_MODE         = "mode";

    public BlackListOpenHelper(Context context)
    {
        super(context, "blackListDB.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE blackList (" +
                   "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "phone_number VARCHAR(30) NOT NULL," +
                   "mode INTEGER NOT NULL" +
                   ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
