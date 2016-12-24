package download.test.com.a05_querycontacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import download.test.com.a05_querycontacts.Bean.Contact;
import download.test.com.a05_querycontacts.Utils.ContactsUtil;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermission(Manifest.permission.READ_CONTACTS,1))
        {
            List<Contact> contactList = ContactsUtil.queryContacts(this);

            for (int i = 0; i < contactList.size(); i++)
            {
                Contact contact = contactList.get(i);
                Log.d("debug",String.format("ID:%s Name:%s Email:%s Phone:%s",contact.getId(),contact.getName(),contact.getEmail(),contact.getPhone()));
            }


//            ContactsUtil.queryDataColumns(this);
//            List<Contact> contactList = ContactsUtil.queryContacts2(this);

//            for (int i = 0; i < contactList.size() ; i++)
//            {
//                Log.d("debug",contactList.get(i).getEmail());
//            }

        }
    }


    public void onClick(View view)
    {
        Log.d("debug","onClick");
        if(checkPermission(Manifest.permission.READ_CONTACTS,1))
        {
            List<Contact> contactList = ContactsUtil.queryContacts(this);

            for (int i = 0; i < contactList.size(); i++)
            {
                Contact contact = contactList.get(i);
                Log.d("debug",String.format("ID:%s Name:%s Email:%s Phone:%s",contact.getId(),contact.getName(),contact.getEmail(),contact.getPhone()));
                Toast.makeText(this,String.format("ID:%s Name:%s Email:%s Phone:%s",contact.getId(),contact.getName(),contact.getEmail(),contact.getPhone()),Toast.LENGTH_LONG).show();
            }
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
