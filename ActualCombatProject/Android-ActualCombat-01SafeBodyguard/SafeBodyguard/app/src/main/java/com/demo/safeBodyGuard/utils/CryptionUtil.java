package com.demo.safeBodyGuard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by iml1s-macpro on 2017/1/10.
 */

public class CryptionUtil
{
    public static String MD5Encoder(String str)
    {
        try
        {
//            str = str + "abcd";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] strBytes = str.getBytes();
            byte[] md5Bytes = digest.digest(strBytes);

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < md5Bytes.length; i++)
            {
                int tempInt = md5Bytes[i] & 0xff;

                String temp = Integer.toHexString(tempInt);

                if(temp.length() < 2)
                {
                    builder.append("0");
                }

                builder.append(temp);
            }

            return builder.toString();

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
