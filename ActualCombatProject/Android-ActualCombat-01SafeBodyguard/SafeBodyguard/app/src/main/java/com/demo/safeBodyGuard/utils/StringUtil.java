package com.demo.safeBodyGuard.utils;

/**
 * Created by iml1s-macpro on 2017/1/22.
 */

public class StringUtil
{
    public static boolean isNullOrEmpty(String str)
    {
        if(str == null || str.trim().isEmpty()) return true;

        return false;
    }
}
