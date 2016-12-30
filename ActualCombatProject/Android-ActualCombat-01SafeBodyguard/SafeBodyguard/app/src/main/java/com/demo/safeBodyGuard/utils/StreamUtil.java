package com.demo.safeBodyGuard.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

public class StreamUtil
{
    public static String getStringFromStream(InputStream stream)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int hasBeanLoad = 0;
        String returnStr = null;

        try
        {
            while ((hasBeanLoad = stream.read(buffer)) != -1)
            {
                out.write(buffer,0,hasBeanLoad);
            }

            returnStr = new String(out.toByteArray(),"UTF-8");

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnStr;
    }
}
