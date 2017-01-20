package com.demo.safeBodyGuard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by iml1s-macpro on 2017/1/5.
 */

public class SPUtil
{
    public static final String SP_FILE_NAME = "config";

    private static SharedPreferences mSP = null;

    /*
     * 寫入Boolean類型的資料
     * @param context 上下文
     * @param key     儲存資料的節點名稱
     * @param value   儲存的資料
     */
    public static void setBool(Context context,String key, boolean value)
    {
        getSP(context).edit().putBoolean(key,value).apply();
    }


    public static void setString(Context context,String key, String value)
    {
        getSP(context).edit().putString(key,value).apply();
    }


    public static void setInt(Context context,String key, int value)
    {
        getSP(context).edit().putInt(key,value).apply();
    }


    public static boolean getBool(Context context,String key,boolean defValue)
    {
        return getSP(context).getBoolean(key,defValue);
    }


    public static String getString(Context context,String key,String defValue)
    {
        return getSP(context).getString(key,defValue);
    }


    public static int getInt(Context context,String key,int defValue)
    {
        return getSP(context).getInt(key,defValue);
    }


    public static void remove(Context context, String key)
    {
        getSP(context).edit().remove(key).apply();
    }

    public static SharedPreferences getSP(Context context)
    {
        if(mSP == null)
            mSP = context.getSharedPreferences(SP_FILE_NAME,Context.MODE_PRIVATE);

        return mSP;
    }

}
