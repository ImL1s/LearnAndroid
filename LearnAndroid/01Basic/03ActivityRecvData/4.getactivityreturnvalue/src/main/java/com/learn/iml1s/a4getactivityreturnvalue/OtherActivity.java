package com.learn.iml1s.a4getactivityreturnvalue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OtherActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        Button btn = (Button)findViewById(R.id.btn2);
        editText = (EditText)findViewById(R.id.editText1);


        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent();
        i.putExtra("data",editText.getText().toString());
        setResult(1,i);
        finish();
    }
}
