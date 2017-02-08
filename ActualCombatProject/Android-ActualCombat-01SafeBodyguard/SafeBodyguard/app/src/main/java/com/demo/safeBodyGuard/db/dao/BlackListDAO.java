package com.demo.safeBodyGuard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safeBodyGuard.db.BlackListOpenHelper;
import com.demo.safeBodyGuard.db.dao.model.BlackRoll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public final static Object LOCK = new Object();


    private final BlackListOpenHelper mOpenHelper;

    private static BlackListDAO mInstance;

    public BlackListDAO(Context context)
    {
        mOpenHelper = new BlackListOpenHelper(context);
    }

    public static BlackListDAO getInstance(Context context)
    {
        synchronized (LOCK)
        {
            if (mInstance == null)
            {
                mInstance = new BlackListDAO(context);
                //                mInstance.addDummyData();
            }
        }
        return mInstance;
    }

    /**
     * 插入
     *
     * @param number
     * @param mode
     * @return
     */
    public long insert(String number, int mode)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BlackListOpenHelper.COL_NAME_PHONE_NUMBER, number);
        values.put(BlackListOpenHelper.COL_NAME_MODE, mode);

        long count = db.insert(BlackListOpenHelper.TABLE_NAME_BLACKLIST, null, values);

        db.close();
        return count;
    }

    /**
     * 查詢全部
     *
     * @return
     */
    public List<BlackRoll> selectAll()
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + BlackListOpenHelper.COL_NAME_PHONE_NUMBER + "," +
                                    BlackListOpenHelper.COL_NAME_MODE + " FROM " +
                                    BlackListOpenHelper.TABLE_NAME_BLACKLIST + " ORDER BY " +
                                    BlackListOpenHelper.COL_NAME_ID + " DESC", new String[]{});

        List<BlackRoll> blackRollList = new ArrayList<>();

        if (cursor.getCount() > 0)
        {
            BlackRoll blackRoll;

            while (cursor.moveToNext())
            {
                blackRoll = new BlackRoll();
                blackRoll.id = Integer.MAX_VALUE;
                blackRoll.name = cursor.getString(0);
                blackRoll.mode = cursor.getInt(1);

                blackRollList.add(blackRoll);
            }
        }

        cursor.close();
        db.close();

        return blackRollList;
    }

    /**
     * 分頁查詢,默認limit20條,(DESC)
     *
     * @param start 查詢開始的位置
     * @return 查詢到的黑名單List
     */
    public List<BlackRoll> selectRange(int start)
    {
        return selectRange(start, 20, true);
    }

    /**
     * 分頁查詢(DESC)
     *
     * @param start 查詢開始的位置
     * @param limit 總共查詢多少條資料
     * @return 查詢到的黑名單List
     */
    public List<BlackRoll> selectRange(int start, int limit)
    {
        return selectRange(start, limit, true);
    }

    /**
     * 分頁查詢
     *
     * @param start  查詢開始的位置
     * @param limit  總共查詢多少條資料
     * @param isDesc 是否倒序排列
     * @return 查詢到的黑名單List
     */
    public List<BlackRoll> selectRange(int start, int limit, boolean isDesc)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                String.format("SELECT %s,%s FROM %s ORDER BY %s %s LIMIT  %s, %s",
                              BlackListOpenHelper.COL_NAME_PHONE_NUMBER,
                              BlackListOpenHelper.COL_NAME_MODE,
                              BlackListOpenHelper.TABLE_NAME_BLACKLIST,
                              BlackListOpenHelper.COL_NAME_ID, isDesc ? "DESC" : "ASC", start + "",
                              limit + ""), null);

        List<BlackRoll> blackRollList = new ArrayList<>();

        if (cursor.getCount() > 0)
        {
            BlackRoll blackRoll;

            while (cursor.moveToNext())
            {
                blackRoll = new BlackRoll();
                blackRoll.id = Integer.MAX_VALUE;
                blackRoll.name = cursor.getString(0);
                blackRoll.mode = cursor.getInt(1);

                blackRollList.add(blackRoll);
            }
        }

        cursor.close();
        db.close();
        return blackRollList;
    }


    /**
     * 單一查詢
     *
     * @param id
     * @return
     */
    public List<BlackRoll> select(int id)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(BlackListOpenHelper.TABLE_NAME_BLACKLIST, new String[]{"*"},
                                 BlackListOpenHelper.COL_NAME_ID + " = ?", new String[]{id + ""},
                                 null, null, BlackListOpenHelper.COL_NAME_ID + " desc");

        List<BlackRoll> blackRollList = new ArrayList<>();

        if (cursor.getCount() > 0)
        {
            BlackRoll blackRoll;

            while (cursor.moveToNext())
            {
                blackRoll = new BlackRoll();
                blackRoll.id = cursor.getInt(0);
                blackRoll.name = cursor.getString(1);
                blackRoll.mode = cursor.getInt(2);

                blackRollList.add(blackRoll);
            }
        }

        cursor.close();
        db.close();
        return blackRollList;
    }

    /**
     * 更新
     *
     * @param id
     * @param phoneNum
     * @param mode
     * @return
     */
    public int update(int id, String phoneNum, int mode)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BlackListOpenHelper.COL_NAME_PHONE_NUMBER, phoneNum);
        values.put(BlackListOpenHelper.COL_NAME_MODE, mode);

        int row = db.update(BlackListOpenHelper.TABLE_NAME_BLACKLIST, values,
                            BlackListOpenHelper.COL_NAME_ID + "= ?", new String[]{id + ""});
        db.close();
        return row;
    }

    /**
     * 更新
     *
     * @param phoneNum
     * @param mode
     * @return
     */
    public int update(String phoneNum, int mode)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BlackListOpenHelper.COL_NAME_PHONE_NUMBER, phoneNum);
        values.put(BlackListOpenHelper.COL_NAME_MODE, mode);

        int row = db.update(BlackListOpenHelper.TABLE_NAME_BLACKLIST, values,
                            BlackListOpenHelper.COL_NAME_PHONE_NUMBER + "= ?",
                            new String[]{phoneNum});
        db.close();
        return row;
    }

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    public int delete(int id)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int row = db.delete(BlackListOpenHelper.TABLE_NAME_BLACKLIST,
                            BlackListOpenHelper.COL_NAME_ID + "= ?", new String[]{id + ""});
        db.close();
        return row;
    }

    /**
     * 刪除
     *
     * @param phoneNum
     * @return
     */
    public int delete(String phoneNum)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int row = db.delete(BlackListOpenHelper.TABLE_NAME_BLACKLIST,
                            BlackListOpenHelper.COL_NAME_PHONE_NUMBER + "= ?",
                            new String[]{phoneNum});
        db.close();
        return row;
    }

    public void addDummyData()
    {
        for (int i = 0; i < 100; i++)
        {
            insert("09281043" + (i < 10 ? i + 10 : i), new Random().nextInt(3));
        }
    }

    public int getMode(String phoneNumber)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(BlackListOpenHelper.TABLE_NAME_BLACKLIST,
                 new String[]{BlackListOpenHelper.COL_NAME_MODE},
                 BlackListOpenHelper.COL_NAME_PHONE_NUMBER + "=?", new String[]{phoneNumber}, null,
                 null, null);

        int mode;
        if (cursor.moveToNext())
        {
            mode = cursor.getInt(0);
        }
        else
        {
            mode = -1;
        }

        cursor.close();
        db.close();

        return mode;
    }
}
