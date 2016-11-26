package com.project.lottowebview.a04login_network.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by iml1s-macpro on 2016/11/16.
 */

public class StreamUtil
{
    public static String InputStreamToString(InputStream inputStream)
    {
        String result = "";

        try
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readLength;

            while ((readLength = inputStream.read(buffer)) != -1)
            {
                byteArrayOutputStream.write(buffer,0,readLength);
            }

            result = new String(byteArrayOutputStream.toByteArray(),"utf-8");
        }
        catch (Exception e)
        {
            Log.d("debug",e.toString());
        }

        return result;
    }
}
