package com.learn.aa223.a1simpletransfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               Intent t = new Intent(MainActivity.this,RecvActivity.class);
                t.putExtra("arg","this is a arg");
                startActivity(t);
            }
        });
    }
}
