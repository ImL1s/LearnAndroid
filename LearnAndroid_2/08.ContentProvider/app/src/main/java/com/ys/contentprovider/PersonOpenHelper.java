package com.ys.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by iml1s-macpro on 2016/12/16.
 */

public class PersonOpenHelper extends SQLiteOpenHelper
{
    public final static String TABLE_NAME_PERSONINFO = "personInfo";

    private final String INSERT_STRING = "INSERT INTO personInfo(name,money) VALUES(?,?);";

    public PersonOpenHelper(Context context)
    {
        super(context,"person.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME_PERSONINFO +"(_id INTEGER PRIMARY KEY AUTOINCREMENT , name VARCHAR(20),money VARCHAR(20));");
        sqLiteDatabase.execSQL(INSERT_STRING,new String[]{"蝶祈","6666"});
        sqLiteDatabase.execSQL(INSERT_STRING,new String[]{"中二集","777"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
