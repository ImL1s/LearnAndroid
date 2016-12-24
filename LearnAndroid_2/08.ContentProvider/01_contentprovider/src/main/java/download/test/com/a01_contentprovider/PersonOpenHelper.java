package download.test.com.a01_contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/16.
 */

public class PersonOpenHelper extends SQLiteOpenHelper
{
    // PersonInfo表名
    public final static String TABLE_NAME_PERSONINFO = "personInfo";

    // 插入資料模板statement
    private final String INSERT_STATEMENT = "INSERT INTO "+ TABLE_NAME_PERSONINFO +"(name,money) VALUES(?,?);";

    public PersonOpenHelper(Context context)
    {
        super(context,"person.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        Log.d("debug","onCreate");

        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME_PERSONINFO +"(_id INTEGER PRIMARY KEY AUTOINCREMENT , name VARCHAR(20),money VARCHAR(20));");
        sqLiteDatabase.execSQL(INSERT_STATEMENT,new String[]{"蝶祈","6666"});
        sqLiteDatabase.execSQL(INSERT_STATEMENT,new String[]{"中二集","777"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        Log.d("debug","onUpgrade");
    }
}
