package download.test.com.a04_smsinsert;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Date;

public class MainActivity extends AppCompatActivity
{

    private final static int PERMISSION_REQUEST_SUCC = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view)
    {
        // Android 4.4以上（包括4.4）,需要將APP設定成默認簡訊程式,才能擁有對簡訊資料庫讀寫的權利
        if(Build.VERSION.SDK_INT >= 23)
            if(!checkPermission(Manifest.permission.READ_SMS,PERMISSION_REQUEST_SUCC) ||
                    !checkPermission(Manifest.permission.SEND_SMS,PERMISSION_REQUEST_SUCC)) return;

        Uri uri = Uri.parse("content://sms");
        ContentResolver resolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put("address","110");
        values.put("date", System.currentTimeMillis());
        values.put("body","你好啊旅行者!");

        resolver.insert(uri,values);
        Log.d("debug","插入成功!!");
    }

    // 請求Android權限
    private boolean checkPermission(String permission, int succRequest)
    {
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            // 如果應用的權限被用戶勾選了不再詢問選擇框
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
            {
                // 展示解釋畫面
                Log.d("debug","聽我解釋啊!!我只是要讀取你的簡訊,沒什麼大不了的");
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{permission},succRequest);
                Log.d("debug","拜託你同意!!");
            }

            return false;
        }

        return true;
    }

}
