package com.demo.safeBodyGuard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safeBodyGuard.db.dao.model.PhoneClass;
import com.demo.safeBodyGuard.db.dao.model.PhoneItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa223 on 2017/2/28.
 */

public class CommonPhoneDAO
{
    private static String mDBPath;


    public static List<PhoneClass> GetAllPhoneClass()
    {
        SQLiteDatabase db =
                SQLiteDatabase.openDatabase(mDBPath, null, SQLiteDatabase.OPEN_READONLY);

        List<PhoneClass> phoneClassList = new ArrayList<>();
        Cursor cursor =
                db.query("classlist", new String[]{"name", "idx"}, null, null, null, null, null);

        while (cursor.moveToNext())
        {
            PhoneClass phoneClass = new PhoneClass();
            phoneClass.servicesType = cursor.getString(0);
            phoneClass.idx = cursor.getInt(1);
            phoneClass.phoneItems = getPhoneItem(phoneClass.idx);
            phoneClassList.add(phoneClass);
        }

        db.close();

        return phoneClassList;
    }

    public static List<PhoneItem> getPhoneItem(int group)
    {
        SQLiteDatabase db =
                SQLiteDatabase.openDatabase(mDBPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("SELECT number,name FROM table" + group + ";", null);

        List<PhoneItem> phoneItemList = new ArrayList<>();

        while (cursor.moveToNext())
        {
            PhoneItem phoneItem = new PhoneItem();
            phoneItem.number = cursor.getString(0);
            phoneItem.phoneName = cursor.getString(1);

            phoneItemList.add(phoneItem);
        }

        return phoneItemList;
    }


    public static void setDBPath(String mDBPath)
    {
        CommonPhoneDAO.mDBPath = mDBPath;
    }
}
