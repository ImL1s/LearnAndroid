package com.learn.aa223.a03news_network.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by iml1s-macpro on 2016/10/25.
 */

public class StreamUtil
{
    public static String inputToString(InputStream in)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int hasBeenLoad = 0;
        String returnStr = null;

        try
        {
            // 從inputStream中讀取最多buffer.length的位元組(最少一個)
            while ((hasBeenLoad = in.read(buffer)) != -1)
            {
                // 將buffer從第0個byte~hasBeenLoad(讀取到的數量)寫到outputStream裡面
                out.write(buffer,0,hasBeenLoad);
                out.flush();
            }

            returnStr = new String(out.toByteArray(),"UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return returnStr;
    }
}
