package com.learn.aa223.a02viewnetimage.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by ImL1s on 2016/10/14.
 */

public class StreamUtils {

    // 取得流中的字串
    public static String inputStreamToString(InputStream stream){

        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;

        try {

            while ((length = stream.read(buffer)) != -1) {

                byteArrayInputStream.write(buffer,0,length);
            }
        }
        catch (Exception e){

        }

        return byteArrayInputStream.toString();
    }

    public static Bitmap inputStreamToBitmap(InputStream stream){

        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }


}
