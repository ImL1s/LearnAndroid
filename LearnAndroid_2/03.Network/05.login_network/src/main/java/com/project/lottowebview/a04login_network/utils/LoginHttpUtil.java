package com.project.lottowebview.a04login_network.utils;

import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.project.lottowebview.a04login_network.Define;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by iml1s-macpro on 2016/11/16.
 */

public class LoginHttpUtil
{
    // Get方式登入
    public static void loginForNetworkByGet(final Handler handler, final String account, final String password, final String loginServerUrl)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    String urlStr = loginServerUrl + "?username=" + URLEncoder.encode(account.trim(), "utf-8") +
                            "&password=" + URLEncoder.encode(password.trim(), "utf-8");
                    Log.d("debug",urlStr);
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10*1000);
                    int rCode = conn.getResponseCode();

                    if(rCode == 200)
                    {
                        InputStream stream = conn.getInputStream();
                        String result = StreamUtil.InputStreamToString(stream);

                        Message msg = Message.obtain();
                        msg.obj = result;
//                        msg.what = (Define.LoginBackResult.Get_LoginBack).ordinal();
                        msg.what = 1;
                        msg.setTarget(handler);
                        msg.sendToTarget();
                    }
                    else
                    {
                        Message msg = Message.obtain();
                        msg.obj = "error";
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                }
                catch (Exception e)
                {
                    Log.d("debug",e.toString());
                }
            }
        }).start();
    }

    // post 方式登入
    public static void loginForNetworkByPost(final Handler handler, final String account, final String password, final String loginServerUrl)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    String urlStr = loginServerUrl;

                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(10*1000);
                    conn.setDoInput(true);

                    // 默認有設定header Content-Type: application/x-www-form-urlencoded
//                    conn.setRequestProperty("","");

                    String body = "username=" + URLEncoder.encode(account,"UTF-8") + "&password=" + URLEncoder.encode(password,"UTF-8");
                    conn.getOutputStream().write(body.getBytes());
                    Log.d("debug",body);

                    int rCode = conn.getResponseCode();

                    if(rCode == 200)
                    {
                        InputStream stream = conn.getInputStream();
                        String result = StreamUtil.InputStreamToString(stream);

                        Message msg = Message.obtain();
                        msg.obj = result;
//                        msg.what = (Define.LoginBackResult.Get_LoginBack).ordinal();
                        msg.what = 2;
                        msg.setTarget(handler);
                        msg.sendToTarget();
                    }
                    else
                    {
                        Message msg = Message.obtain();
                        msg.obj = "error";
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }

                }
                catch (Exception e)
                {
                    Log.d("debug",e.toString());
                }
            }
        }).start();
    }
}
