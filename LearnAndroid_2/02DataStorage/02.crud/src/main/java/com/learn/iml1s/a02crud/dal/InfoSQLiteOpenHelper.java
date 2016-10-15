package com.learn.iml1s.a02crud.dal;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ImL1s on 2016/10/10.
 */

public class InfoSQLiteOpenHelper extends SQLiteOpenHelper {


    public InfoSQLiteOpenHelper(Context context)
    {
        super(context, "info.db", null, 1);
    }

    // 資料庫第一次創建時會調用此方法（.db文件不存在時）
    // 一般會在此方法中建構表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("create table student(id int increment," +
            "name varchar(20)," +
            "age tinyint)");
    }

    // 資料庫升級時會調用此方法,升級的依據是構造方法傳入的整型數字比舊的資料庫大時,就會升級
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("ALTER TABLE student ADD COLUMN age tinyint;");
    }
}
