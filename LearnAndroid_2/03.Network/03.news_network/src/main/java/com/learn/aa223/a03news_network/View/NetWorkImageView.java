package com.learn.aa223.a03news_network.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by iml1s-macpro on 2016/10/26.
 */

public class NetWorkImageView extends ImageView
{
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bitmap bitmap = (Bitmap) msg.obj;
            if(bitmap != null)
            {
                setImageBitmap(bitmap);
            }
        }
    };

    public NetWorkImageView(Context context) {
        super(context);
    }

    public NetWorkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetWorkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NetWorkImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImageURI(final String uriStr)
    {
        new Thread(){

            @Override
            public void run()
            {
                try
                {
                    URL url = new URL(uriStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(1000*10);
                    int code = conn.getResponseCode();

                    if(code == 200)
                    {
                        InputStream in = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);

                        Message msg = Message.obtain();
                        msg.obj = bitmap;

                        handler.sendMessage(msg);

                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
