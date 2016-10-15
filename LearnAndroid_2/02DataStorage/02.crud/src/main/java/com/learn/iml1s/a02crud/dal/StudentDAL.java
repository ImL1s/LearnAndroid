package com.learn.iml1s.a02crud.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.learn.iml1s.a02crud.StudentBean;

import java.util.ArrayList;

/**
 * Created by ImL1s on 2016/10/11.
 */

public class StudentDAL
{
    InfoSQLiteOpenHelper helper = null;

    public  StudentDAL(Context content)
    {
        helper = new InfoSQLiteOpenHelper(content);
    }

    // 加入
    public void add(StudentBean bean){

        SQLiteDatabase database = helper.getReadableDatabase();
        database.execSQL("INSERT INTO student(name,age) values(?,?)",new Object[]{bean.getName(),bean.getAge()});
        database.close();
    }


    // 刪除
    public void delete(StudentBean bean){

        SQLiteDatabase database = helper.getReadableDatabase();
        database.execSQL("DELETE student where name=? or id=?;",new Object[]{bean.getName(),bean.getId()});
        database.close();
    }

    // 查詢
    public ArrayList<StudentBean> query(String name){

        return query(new StudentBean(-1,name,-1));
    }

    // 查詢
    public ArrayList<StudentBean> query(StudentBean bean){

        ArrayList<StudentBean> returnBeanList = null;
        StudentBean returnBean;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM student WHERE name=?;",new String[]{bean.getName()});

        if(cursor != null && cursor.getCount() > 0){

            returnBeanList = new ArrayList<StudentBean>();

            while (cursor.moveToNext()){

                returnBean = new StudentBean();
                returnBean.setId(cursor.getInt(cursor.getColumnIndex("id")));
                returnBean.setName(cursor.getString(cursor.getColumnIndex("name")));
                returnBean.setAge(cursor.getInt(cursor.getColumnIndex("age")));

                returnBeanList.add(returnBean);

            }
        }

        cursor.close();
        database.close();
        return returnBeanList;
    }

    // 更新
    public void update(StudentBean bean){

        SQLiteDatabase database = helper.getReadableDatabase();
        database.execSQL("UPDATE student set  ",new Object[]{bean.getAge()+""});
        database.close();
    }


}
