package com.demo.safeBodyGuard.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

public class JsonUtil
{
    /*
     * 將Json轉成指定物件,該物件必須將Field公開
     *
     */
    public static <T> T getObject(String jsonString,T bean)
    {
        JSONObject jsonObject = null;

        try
        {
            jsonObject = new JSONObject(jsonString);

            Field[] fields = bean.getClass().getDeclaredFields();

            for (int i = 0; i< fields.length; i ++)
            {
                String fieldName = fields[i].getName();
                Log.d("debug",fieldName);

                if(Modifier.isStatic(fields[i].getModifiers())) continue;

                Object fieldValue = jsonObject.get(fields[i].getName());
                Field field = bean.getClass().getDeclaredField(fieldName);
                field.set(bean,fieldValue);
            }

        }
        catch (JSONException | NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }


        return bean;
    }
}
