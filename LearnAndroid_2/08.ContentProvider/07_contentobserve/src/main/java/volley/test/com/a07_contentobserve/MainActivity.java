package volley.test.com.a07_contentobserve;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // 只有當sms Uri相關的表有變動時,onChange方法才會被調用
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new ContentObserver(new Handler())
        {
            @Override
            public void onChange(boolean selfChange)
            {
                Log.d("debug","OnSMS Change");
            }
        });

        // 只有當contacts Uri相關的表有變動時,onChange方法才會被調用
        getContentResolver().registerContentObserver(Uri.parse("content://com.android.contacts/"), false, new ContentObserver(new Handler())
        {
            @Override
            public void onChange(boolean selfChange)
            {
                Log.d("debug","OnContacts Change");
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == 1)
        {
            int i = 5;
            int a = i+5;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void onClick(View view)
    {
        boolean hasPermission = true;

        if(Build.VERSION.SDK_INT >= 23)
        {
            int permissionCount = 0;

            if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                checkPermission(Manifest.permission.READ_CONTACTS, 1);
            }
            else
            {
                permissionCount++;
            }

            hasPermission = (permissionCount == 1);
        }

        if(hasPermission)
        {
            Cursor cursor = getContentResolver().query(Uri.parse("content://com.android.contacts/raw_contacts"),null,null,null,null);
        }
    }

    // 請求Android權限
    private boolean checkPermission(String permission, int succRequest)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            {
                // 如果應用的權限被用戶勾選了不再詢問選擇框
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                {
                    // 展示解釋畫面
                    Log.d("debug", "聽我解釋啊!!我只是要讀取你的簡訊,沒什麼大不了的");
                } else
                {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, succRequest);
                    Log.d("debug", "拜託你同意!!");
                }

                return false;
            }
        }

        return true;
    }
}
