package com.learn.iml1s.a03network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.iml1s.a03network.utils.StreamUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTxt;
    private TextView textView;
    private Button sendBtn;
    private String urlStr = "";

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            String result = (String) msg.obj;
            textView.setText(result);
            Log.d("debug","handleMessage: "+Thread.currentThread().getName());
            Toast.makeText(MainActivity.this,"下載成功",0).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTxt = (EditText) findViewById(R.id.editTxt);
        textView = (TextView) findViewById(R.id.scrollTxtView);
        sendBtn = (Button) findViewById(R.id.btn);

        Log.d("debug","onCreate: "+Thread.currentThread().getName());

        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d("debug","onClick: "+Thread.currentThread().getName());

        if(TextUtils.isEmpty(editTxt.getText()) || editTxt.getText() == null){

            Toast.makeText(this,"URL不能為空!",Toast.LENGTH_SHORT).show();
            return;
        }

        urlStr = editTxt.getText().toString();

        new Thread(new Runnable() {

            @Override
            public void run() {

                Log.d("debug","Run: "+Thread.currentThread().getName());
//                Looper.prepare();

                try {

                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    int code = connection.getResponseCode();
                    if(code == 200){

                        InputStream inputStream = connection.getInputStream();
                        String content = StreamUtils.inputStreamToString(inputStream);
//                        textView.setText(content);


                        Message msg = new Message();
                        msg.obj = content;
                        handler.sendMessage(msg);
                        Log.d("debug","handler.sendMessage end");

                    }else {

                        Log.d("debug","請求錯誤");
//                        Toast.makeText(MainActivity.this,"請求錯誤",0).show();
                    }
                }
                catch (Exception e){

                    e.printStackTrace();
                }
            }
        }).start();




    }
}
