package com.demo.safeBodyGuard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safeBodyGuard.db.BlackListOpenHelper;

/**
 * Created by ImL1s on 2017/2/3.
 * <p>
 * DESC:
 */

public class BlackListDAO
{
    public static int MODE_MESSAGE = 0x1;
    public static int MODE_PHONE   = 0x2;
    public static int MODE_BOTH    = 0x4;


    private final BlackListOpenHelper mOpenHelper;

    private static BlackListDAO mInstance;

    public BlackListDAO(Context context)
    {
        mOpenHelper = new BlackListOpenHelper(context);
    }

    public static BlackListDAO getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new BlackListDAO(context);
        }
        return mInstance;
    }

    public long insert(String number, String mode)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BlackListOpenHelper.COL_NAME_PHONE_NUMBER, number);
        values.put(BlackListOpenHelper.COL_NAME_MODE, mode);

        long count = db.insert(BlackListOpenHelper.TABLE_NAME_BLACKLIST, null, values);

        db.close();
        return count;
    }

    public void select(int id)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(BlackListOpenHelper.TABLE_NAME_BLACKLIST, new String[]{"*"},
                 BlackListOpenHelper.COL_NAME_ID + " = ?", new String[]{id + ""}, null, null,
                 BlackListOpenHelper.COL_NAME_ID + " desc");

        if(cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {

            }
        }
    }
}
