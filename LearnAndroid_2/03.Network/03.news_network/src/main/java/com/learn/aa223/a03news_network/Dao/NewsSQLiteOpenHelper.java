package com.learn.aa223.a03news_network.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by iml1s-macpro on 2016/10/26.
 */

public class NewsSQLiteOpenHelper extends SQLiteOpenHelper
{
    public NewsSQLiteOpenHelper(Context context)
    {
        this(context,"db1",null,1);
    }

    public NewsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE news(_id integer, title varchar(200),desc varchar(300),icon_url varchar(300),news_url varchar(300),"
        + "type integer, time varchar(100), comment integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
