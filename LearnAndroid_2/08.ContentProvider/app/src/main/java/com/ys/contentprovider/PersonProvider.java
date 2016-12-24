package com.ys.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/16.
 */

public class PersonProvider extends ContentProvider
{
    // Url匹配器,對他傳入Uri會傳回一個int類型的值,可以比較該int類型是否與設定時的值匹配
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // 查詢成功返回值
    private static final int QUERY_SUCESS = 0;

    // 打開database幫助類
    private PersonOpenHelper personOpenHelper;


    static
    {
        mUriMatcher.addURI("com.ys.contentprovider","query",QUERY_SUCESS);
    }


    @Override
    public boolean onCreate()
    {
        this.personOpenHelper = new PersonOpenHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        if(mUriMatcher.match(uri) == QUERY_SUCESS)
        {
            SQLiteDatabase database = personOpenHelper.getWritableDatabase();
            return database.query(PersonOpenHelper.TABLE_NAME_PERSONINFO,projection,selection,selectionArgs,null,null,sortOrder);
        }

        Log.d("deubg","url不符合...");
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }
}
