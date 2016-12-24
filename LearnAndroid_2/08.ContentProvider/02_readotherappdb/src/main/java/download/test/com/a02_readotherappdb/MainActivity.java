package download.test.com.a02_readotherappdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        selectDatabaseByChangeMod();
//        insertOtherAppDatabase();
//        mUriMatcher.addURI("com.ys.contentprovider","query",QUERY_SUCESS);
    }

    // 此方式為Google官方的方式
    private void selectOtherAppDatabase()
    {
        Uri uri = Uri.parse("content://com.ys.contentprovider/query");
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);

        if(cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                Log.d("debug",cursor.getString(0) + ": " + cursor.getString(1));
            }
        }
    }

    private void insertOtherAppDatabase()
    {
        Uri uri = Uri.parse("content://com.ys.contentprovider/insert");
        ContentValues values = new ContentValues();
        values.put("name","無名");
        values.put("money","51");
        Uri resultUri = getContentResolver().insert(uri,values);

        if(resultUri.toString() .equals("com.ys.provider.succ"))
        {
            Log.d("debug","插入成功");
        }
        else
        {
            Log.d("debug","插入失敗");
        }
    }

    // 邪門歪道,使用此方法前必須先使用chmod指令將db文件的權限改成可讀寫,並且將journal(回滾用臨時文件)文件刪除
    private void selectDatabaseByChangeMod()
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/download.test.com.a01_contentprovider/databases/person.db",null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.query("personInfo",null,null,null,null,null,null);

        ArrayList<Person> personArrayList = getPersonArray(cursor);

        for (int i = 0; i < personArrayList.size(); i++)
        {
            Log.d("debug","Two APP:" + personArrayList.get(i).Name);
        }

        Toast.makeText(this,personArrayList.get(0).Name,Toast.LENGTH_LONG).show();
    }


    private ArrayList<Person> getPersonArray(Cursor cursor)
    {
        ArrayList<Person> personList = new ArrayList<Person>();

        if(cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                Person p = new Person();
                p.Name = cursor.getString(1);
                p.Money = cursor.getString(2);
                personList.add(p);
            }
        }

        return personList;
    }

    public void onClickA(View v)
    {
        Toast.makeText(this,"oonClick",Toast.LENGTH_LONG).show();
        this.selectOtherAppDatabase();
    }

    public void onClickSelect(View view)
    {
        Toast.makeText(this,"onClickSelect",Toast.LENGTH_LONG).show();
        this.selectOtherAppDatabase();
    }

    public void onClickInsert(View view)
    {
        this.insertOtherAppDatabase();
    }

    public class Person
    {
        public String Name;

        public String Money;
    }
}
