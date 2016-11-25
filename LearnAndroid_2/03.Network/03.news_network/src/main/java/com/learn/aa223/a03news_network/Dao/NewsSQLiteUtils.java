package com.learn.aa223.a03news_network.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.aa223.a03news_network.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by iml1s-macpro on 2016/10/26.
 */

public class NewsSQLiteUtils
{
    private NewsSQLiteOpenHelper newsSQLiteOpenHelper = null;

    private static NewsSQLiteUtils instance;

    private NewsSQLiteUtils(Context context)
    {
        newsSQLiteOpenHelper = new NewsSQLiteOpenHelper(context);
    }

    public static NewsSQLiteUtils getInstance(Context context)
    {
        if(instance == null) instance = new NewsSQLiteUtils(context);

        return instance;
    }

    public ArrayList<NewsBean> getAll()
    {
    SQLiteDatabase db = newsSQLiteOpenHelper.getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM news;",null);
    ArrayList<NewsBean> newsArray = new ArrayList<>();


    if(cursor.getCount() > 0)
    {
        while (cursor.moveToNext())
        {
            NewsBean bean = new NewsBean();
            int id = cursor.getInt(0);
            String desc = cursor.getString(2);
            String title = cursor.getString(1);
            bean.setId(id);
            bean.setTitle(cursor.getString(1));
            bean.setDesc(cursor.getString(2));
            bean.setIcon_url(cursor.getString(3));
            bean.setNews_url(cursor.getString(4));
            bean.setType(cursor.getInt(5));
            bean.setTime(cursor.getString(6));
            bean.setCommentStr(cursor.getInt(7)+"");

            newsArray.add(bean);
        }
    }

    return newsArray;
}

    public void ClearUp()
    {
        SQLiteDatabase db = newsSQLiteOpenHelper.getReadableDatabase();
        db.execSQL("DELETE FROM news");
    }

    public void SaveCache(ArrayList<NewsBean> beanArrayList)
    {
        if(beanArrayList!= null && beanArrayList.size() > 0)
        {
            SQLiteDatabase db = newsSQLiteOpenHelper.getReadableDatabase();

            for (NewsBean bean : beanArrayList)
            {
                ContentValues values = new ContentValues();
                values.put("_id",bean.getId());
                values.put("title",bean.getTitle());
                values.put("desc",bean.getDesc());
                values.put("icon_url",bean.getIcon_url());
                values.put("news_url",bean.getNews_url());
                values.put("type",bean.getType());
                values.put("time",bean.getTime());
                values.put("comment",Integer.valueOf(bean.getCommentStr()));

                db.insert("news",null,values);
            }
        }
    }
}
