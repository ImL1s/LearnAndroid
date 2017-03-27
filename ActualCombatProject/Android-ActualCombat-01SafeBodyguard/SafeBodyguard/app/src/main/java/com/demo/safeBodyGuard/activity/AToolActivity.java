package com.demo.safeBodyGuard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.handler.IActivityHandler;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AToolActivity extends BaseActivity implements View.OnClickListener
{
    private static Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_tool);


    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.activity_a_tool_tv_phone_to_address:
                startActivityEasy(QueryAddressActivity.class);
                break;

            case R.id.activity_a_tool_tv_sms_backup:
                backupSms();
                break;

            case R.id.activity_a_tool_tv_common_phone:
                startActivityEasy(CommonPhoneActivity.class);
                break;

            case R.id.activity_a_tool_tv_app_lock:
                startActivityEasy(AppLockActivity.class);
                break;
        }
    }

    private void backupSms()
    {
        //        OnProgressListener progressListener = new ProgressDialogProgressListener(this);
        //        new Thread(() -> startBackup(this, progressListener)).start();
        new Thread(() -> startBackup(this, new ProgressDialogProgressListener(this))).start();
    }

    private void startBackup(Context context, OnProgressListener progressListener)
    {
        Cursor cursor = getContentResolver()
                .query(Uri.parse("content://sms"), new String[]{"_id", "address", "date", "body"},
                       null, null, null);

        if (cursor == null)
            return;

        progressListener.onGetMaxProgressCount(cursor.getCount());

        OutputStream os = null;
        XmlSerializer serializer = null;

        try
        {
            os = new FileOutputStream(new File(getFilesDir(), "smsBackup.xml"));
            serializer = Xml.newSerializer();
            serializer.setOutput(os, "utf-8");
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "smss");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        int i = 0;

        while (cursor != null && cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            String address = cursor.getString(1);
            String date = cursor.getString(2);
            String body = cursor.getString(3);

            try
            {
                Thread.sleep(500);

                serializer.startTag(null, "sms");

                serializer.startTag(null, "id");
                serializer.text(id + "");
                serializer.endTag(null, "id");

                serializer.startTag(null, "address");
                serializer.text(address);
                serializer.endTag(null, "address");

                serializer.startTag(null, "date");
                serializer.text(date);
                serializer.endTag(null, "date");

                serializer.startTag(null, "body");
                serializer.text(body);
                serializer.endTag(null, "body");

                serializer.endTag(null, "sms");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            progressListener.onProgress(i++);
        }

        try
        {
            if (serializer != null)
            {
                serializer.endTag(null, "smss");
                serializer.endDocument();
                serializer.flush();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        progressListener.onProcessedAll();
    }

    public interface OnProgressListener
    {
        void onGetMaxProgressCount(int max);

        void onProgress(int processed);

        void onProcessedAll();
    }

    class ProgressDialogProgressListener implements OnProgressListener
    {
        private ProgressDialog mProgressDialog;
        private int mMax = Integer.MIN_VALUE;

        public ProgressDialogProgressListener(Context context)
        {
            if (Thread.currentThread() != Looper.getMainLooper().getThread())
            {
                runOnUiThread(() -> initProgressDialog(context));
            }
            else
            {
                initProgressDialog(context);
            }

        }

        @Override
        public void onGetMaxProgressCount(int max)
        {
            mMax = max;
            if (mProgressDialog != null)
                mProgressDialog.setMax(mMax);
        }

        @Override
        public void onProgress(int processed)
        {
            if (mProgressDialog != null && mProgressDialog.getMax() == 0)
                mProgressDialog.setMax(mMax);

            if (mProgressDialog != null)
                mProgressDialog.setProgress(processed);
        }

        @Override
        public void onProcessedAll()
        {
            mProgressDialog.dismiss();
        }

        private void initProgressDialog(Context context)
        {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("備份進度");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(0);
            mProgressDialog.show();
        }
    }

    class ProgressBarProgressListener implements OnProgressListener
    {
        private ProgressBar mProgressBar;

        public ProgressBarProgressListener(Context context)
        {

        }

        @Override
        public void onGetMaxProgressCount(int max)
        {
            mProgressBar.setMax(max);
        }

        @Override
        public void onProgress(int processed)
        {
            mProgressBar.setProgress(processed);
        }

        @Override
        public void onProcessedAll()
        {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


}
