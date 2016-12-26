package com.learn.aa223.a07service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this,MyService.class);

        findViewById(R.id.startServiceBtn).setOnClickListener(this);
        findViewById(R.id.stopServiceBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.startServiceBtn:
                // 啟動一個Service
                startService(intent);
                Toast.makeText(this,"StartService",Toast.LENGTH_LONG).show();
                Log.d("debug","StartService...");
                break;

            case R.id.stopServiceBtn:
                // 關閉一個Service
                stopService(intent);
                Toast.makeText(this,"StopService",Toast.LENGTH_LONG).show();
                Log.d("debug","StopService...");
                break;
        }
    }
}
