package com.demo.safeBodyGuard.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.demo.safeBodyGuard.db.dao.model.AntiVirus;
import com.demo.safeBodyGuard.db.dao.model.PhoneItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa223 on 2017/2/28.
 */

public class AntiVirusDAO {

    private static String mDBPath;

    public static void setDBPath(String mDBPath) {
        AntiVirusDAO.mDBPath = mDBPath;
    }

    public static List<AntiVirus> GetAll() {
        SQLiteDatabase db =
                SQLiteDatabase.openDatabase(mDBPath, null, SQLiteDatabase.OPEN_READONLY);

        List<AntiVirus> antiVirusList = new ArrayList<>();
        Cursor cursor =
                db.query("datable", new String[]{"_id", "md5", "name", "desc"}, null, null, null,
                        null, null);

        while (cursor.moveToNext()) {
            AntiVirus antiVirus = new AntiVirus();
            antiVirus.id = cursor.getInt(0);
            antiVirus.md5 = cursor.getString(1);
            antiVirus.pkgName = cursor.getString(2);
            antiVirus.desc = cursor.getString(3);
            antiVirusList.add(antiVirus);
        }

        cursor.close();
        db.close();

        return antiVirusList;
    }

    public static List<PhoneItem> getPhoneItem(int group) {
        SQLiteDatabase db =
                SQLiteDatabase.openDatabase(mDBPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("SELECT number,name FROM table" + group + ";", null);

        List<PhoneItem> phoneItemList = new ArrayList<>();

        while (cursor.moveToNext()) {
            PhoneItem phoneItem = new PhoneItem();
            phoneItem.number = cursor.getString(0);
            phoneItem.phoneName = cursor.getString(1);

            phoneItemList.add(phoneItem);
        }

        return phoneItemList;
    }
}
