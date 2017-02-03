package com.demo.safeBodyGuard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safeBodyGuard.define.Config;

/**
 * Created by iml1s-macpro on 2017/1/20.
 */

public class AddressDAO
{
    public static String DB_CHINA_PATH = "";

    private static SQLiteDatabase mChinaDatabase = null;

    //    private static SQLiteDatabase mTWDatabase = null;


    public static void setDBPath(String path)
    {
        DB_CHINA_PATH = path;
    }

    public static String getAddress(String phone)
    {

        String chinaRegular = "^1[3-8]\\d{9}";
        String taiwanRegular = "^09\\d{8}";

        String result = "Not found";

        if (phone.matches(chinaRegular))
        {
            phone = phone.substring(0, 7);

            initChinaDB();

            Cursor cursor = queryChina(Config.DB_TABLE_ADDRESS_DATA1, "outkey", "id = ?", phone);

            if (cursor.moveToNext())
            {
                String outKey = cursor.getString(0);

                cursor.close();

                cursor = mChinaDatabase.query(Config.DB_TABLE_ADDRESS_DATA2, new String[]{"location"}, "id = ?", new String[]{outKey}, null, null, null);

                if (cursor.moveToNext())
                {
                    String area = cursor.getString(0);

                    result = area;
                }
            }
        }
        else if (phone.matches(taiwanRegular))
        {
            result = "Taiwan Phone";
        }
        else
        {
            switch (phone.length())
            {
                case 3:
                    result = "報警電話";
                    break;

                case 4:
                    result = "模擬器";
                    break;

                case 5:
                    result = "大陸服務電話";
                    break;
            }
        }

        if (mChinaDatabase != null && mChinaDatabase.isOpen()) mChinaDatabase.close();


        return result;
    }


    private static void initChinaDB()
    {
        mChinaDatabase = SQLiteDatabase.openDatabase(DB_CHINA_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }

    private static void initTaiwanDB()
    {
        //        mTWDatabase = SQLiteDatabase.openDatabase(DB_TW_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }

    private static Cursor queryChina(String tableName, String selectColumnName, String selection, String selectionArg)
    {
        return mChinaDatabase.query(tableName, new String[]{selectColumnName}, selection, new String[]{selectionArg}, null, null, null);
    }


}
