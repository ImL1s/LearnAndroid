package com.demo.safeBodyGuard.db.dao;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockInfo
{
    public int id;

    public String pkg;

    public AppLockInfo(int id,String pkg)
    {
        this.id = id;
        this.pkg = pkg;
    }
}
