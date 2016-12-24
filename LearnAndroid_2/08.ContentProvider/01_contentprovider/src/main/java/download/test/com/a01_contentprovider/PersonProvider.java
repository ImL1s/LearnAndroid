package download.test.com.a01_contentprovider;

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
    // Uri適配器
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int QUERY_SUCESS = 0;              // 查詢成功

    private static final int INSERT_SUCESS = 1;             // 插入成功

    private PersonOpenHelper personOpenHelper;              // 資料庫幫助類


    // 靜態構造代碼塊
    static
    {
        // 加入match規則
        mUriMatcher.addURI("com.ys.contentprovider","query",QUERY_SUCESS);
        mUriMatcher.addURI("com.ys.contentprovider","insert",INSERT_SUCESS);
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
        Log.d("deubg","Query");

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
        Log.d("debug","insert");
        if(this.mUriMatcher.match(uri) == INSERT_SUCESS)
        {
            SQLiteDatabase db = personOpenHelper.getWritableDatabase();
            long afCount = db.insert(PersonOpenHelper.TABLE_NAME_PERSONINFO,null,contentValues);

            if(afCount > 0)
            {
                return Uri.parse("com.ys.provider.succ");
            }

            return Uri.parse("com.ys.provider.fail");
        }


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
