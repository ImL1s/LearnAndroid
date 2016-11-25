package com.learn.aa223.a02viewnetimage;

import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.aa223.a02viewnetimage.utils.StreamUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTxt;
    private ImageView imageView;
    private Button sendBtn;
    private String urlStr = "";

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            Bitmap result = (Bitmap) msg.obj;
            imageView.setImageBitmap(result);
            Log.d("debug","handleMessage: "+Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTxt = (EditText) findViewById(R.id.editTxt);
        imageView = (ImageView) findViewById(R.id.scrollImgView);
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

                try {

                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    int code = connection.getResponseCode();
                    if(code == 200){

                        InputStream inputStream = connection.getInputStream();
                        Bitmap content = StreamUtils.inputStreamToBitmap(inputStream);


                        Message msg = new Message();
                        msg.obj = content;
                        handler.sendMessage(msg);

                        Log.d("debug","handler.sendMessage end");

                    }else {

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
