package download.test.com.a03_smsbackup;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private final static int PERMISSION_REQUEST_READ_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_READ_SMS:
                Log.d("debug","PERMISSION_REQUEST_READ_SMS grant");
                break;
        }
    }

    // 點擊備份按鈕
    public void onBackUpClick(View view)
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            if(!checkPermission(Manifest.permission.READ_SMS) || !checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) return;
        }

        Uri smsUri = Uri.parse("content://sms");
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(smsUri,new String[]{"address","person","date","read","body"},null,null,null);

        ArrayList<SmsContent> smsList = new ArrayList<>();

        if(cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                smsList.add(
                        new SmsContent(
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("person")),
                        cursor.getInt(cursor.getColumnIndex("date")),
                        cursor.getInt(cursor.getColumnIndex(("read"))) != 0,
                        cursor.getString(cursor.getColumnIndex("body"))
                ));
            }
        }

        for (int i = 0; i < smsList.size(); i++)
        {
            Log.d("debug",smsList.get(i).body);
        }

        SmsContent[] smsContents = new SmsContent[smsList.size()];
        writeXML(smsList.toArray(smsContents));
    }


    // 寫入XML到外部存儲空間
    private void writeXML(SmsContent[] smsContents)
    {
        XmlSerializer serializer = Xml.newSerializer();
        File file = new File(Environment.getExternalStorageDirectory().getPath(),"sms_backup.xml");
        try
        {
            FileOutputStream stream = new FileOutputStream(file);
            serializer.setOutput(stream,"utf-8");
            serializer.startDocument("utf-8",true);
            serializer.startTag(null,"smsArray");


            for (int i = 0; i < smsContents.length; i++)
            {
                SmsContent content = smsContents[i];

                serializer.startTag(null,"sms");

                serializer.startTag(null,"address");
                serializer.text(content.address);
                serializer.endTag(null,"address");

                serializer.startTag(null,"person");
                serializer.text(content.person == null? "":content.person);
                serializer.endTag(null,"person");

                serializer.startTag(null,"date");
                serializer.text(content.date + "");
                serializer.endTag(null,"date");

                serializer.startTag(null,"isRead");
                serializer.text(content.isRead + "");
                serializer.endTag(null,"isRead");

                serializer.startTag(null,"body");
                serializer.text(content.body);
                serializer.endTag(null,"body");

                serializer.endTag(null,"sms");
            }

            serializer.endTag(null,"smsArray");
            serializer.endDocument();

            stream.close();
        }
        catch (Exception e)
        {
            Log.d("debug",e.toString());
            e.printStackTrace();
        }


    }


    // 請求Android權限
    private boolean checkPermission(String permission)
    {
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            // 如果應用的權限被用戶勾選了不再詢問選擇框
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS))
            {
                // 展示解釋畫面
                Log.d("debug","聽我解釋啊!!我只是要讀取你的簡訊,沒什麼大不了的");
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_READ_SMS);
                Log.d("debug","拜託你同意!!");
            }

            return false;
        }

        return true;
    }


    // 簡訊內容
    public class SmsContent
    {
        public String address;
        public String person;
        public int date;
        public boolean isRead;
        public String body;

        public  SmsContent(String address,String person,int date,boolean isRead,String body)
        {
            this.address = address;
            this.person = person;
            this.date = date;
            this.isRead = isRead;
            this.body = body;
        }
    }

}
