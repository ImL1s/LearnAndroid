package com.demo.safeBodyGuard.activity;

import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;

public class AppManagerActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initTitle();
    }

    private void initTitle()
    {
        String innerSize = Formatter.formatFileSize(this, getUsableSpace(
                Environment.getDataDirectory().getAbsolutePath()));
        String externalSize = Formatter.formatFileSize(this, getUsableSpace(
                Environment.getExternalStorageDirectory().getAbsolutePath()));

        Toast.makeText(this, innerSize, Toast.LENGTH_LONG).show();

        TextView tv_innerStorage =
                (TextView) findViewById(R.id.activity_app_manager_tv_inner_storage);

        TextView tv_externalStorage =
                (TextView) findViewById(R.id.activity_app_manager_tv_external_storage);

        tv_innerStorage.setText("內部存儲" + innerSize);
        tv_externalStorage.setText("外部存儲" + externalSize);
    }

    /**
     * 取得指定目錄的剩餘可用空間
     *
     * @return
     */
    private long getUsableSpace(String path)
    {
        StatFs statFs = new StatFs(path);
        int blockCount = statFs.getAvailableBlocks();
        int blockSize = statFs.getBlockSize();

        return blockCount * blockSize;
    }
}
