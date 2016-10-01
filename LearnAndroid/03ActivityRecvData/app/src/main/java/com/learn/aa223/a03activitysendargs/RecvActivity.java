package com.learn.aa223.a03activitysendargs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RecvActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv);
        Intent t = getIntent();
        String arg = t.getStringExtra("arg");
        ((TextView)findViewById(R.id.textView1)).setText(arg);
    }
}
