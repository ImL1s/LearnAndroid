package com.project.lottowebview.a6_;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sendBtn).setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();
        intent.setAction("com.YS.custom_broadcast");
        intent.putExtra("content","開廣拉！！");
        Toast.makeText(this,"發送一條無序廣播",0).show();
        
        sendBroadcast(intent);
    }
}
