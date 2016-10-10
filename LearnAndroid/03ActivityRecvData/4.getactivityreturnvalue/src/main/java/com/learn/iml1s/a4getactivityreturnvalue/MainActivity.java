package com.learn.iml1s.a4getactivityreturnvalue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button otherActivityBtn = (Button)findViewById(R.id.button1);

        otherActivityBtn.setOnClickListener(this);

        txtView = (TextView)findViewById(R.id.txtView1);
    }

    @Override
    public void onClick(View v) {

        startActivityForResult(new Intent(this,OtherActivity.class),0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String str = String.format("RequestCode:%d \n ResultCOde:%d \n Data:%s",requestCode,resultCode,data.toString());
        txtView.setText(str);


    }
}
