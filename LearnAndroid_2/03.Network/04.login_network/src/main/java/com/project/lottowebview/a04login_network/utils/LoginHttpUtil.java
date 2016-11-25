package com.project.lottowebview.a04login_network.utils;

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
    public static void loginForNetworkByGet(final Handler handler, final String account, final String password, final String loginServerUrl)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    String urlStr = loginServerUrl + "/username=" + URLEncoder.encode(account.trim(), "utf-8") +
                            "&password=" + URLEncoder.encode(password.trim(), "utf-8");
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("get");
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

                }
                catch (Exception e)
                {
                    Log.d("debug",e.toString());
                }
            }
        }).start();
    }
}
