package download.test.com.a01_contentprovider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printDatabaseInfo();
        openDatabaseByChangeMod();
    }

    // 將資料庫中的資料印出
    private void printDatabaseInfo()
    {
        // 打開資料庫幫助類
        PersonOpenHelper personOpenHelper = new PersonOpenHelper(getApplicationContext());
        SQLiteDatabase database = personOpenHelper.getWritableDatabase();

        Cursor cursor = database.query(PersonOpenHelper.TABLE_NAME_PERSONINFO,null,null,null,null,null,null);

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

        for (int i = 0; i < personList.size(); i++)
        {
            Log.d("debug","One APP:" + personList.get(i).Name);
        }
    }

    // 直接用路徑打開,如果不是本應用打開,必須要用chmod 改變讀寫權限
    private void openDatabaseByChangeMod()
    {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/download.test.com.a01_contentprovider/databases/person.db",null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.query(PersonOpenHelper.TABLE_NAME_PERSONINFO,null,null,null,null,null,null);

        ArrayList<Person> personArrayList = getPersonArray(cursor);

        for (int i = 0; i < personArrayList.size(); i++)
        {
            Log.d("debug","One APP:" + personArrayList.get(i).Name);
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

    public class Person
    {
        public String Name;

        public String Money;
    }
}
