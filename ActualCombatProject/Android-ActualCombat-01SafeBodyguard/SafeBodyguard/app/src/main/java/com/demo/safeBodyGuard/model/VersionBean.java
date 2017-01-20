package com.demo.safeBodyGuard.model;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

public class VersionBean
{
    /*
     * 當前APK的版本號
     */
//    public static int VERSION_CODE_CURRENT = -1;

    private static VersionBean CURRENT_VERSION = null;
    private static VersionBean SERVER_VERSION = null;

    public int mCode;

    public String mName;

    public String mDownloadURL;

    public int getCode()
    {
        return mCode;
    }

    public void setCode(int mCode)
    {
        this.mCode = mCode;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public static VersionBean getServerVersion(){ return SERVER_VERSION; }

    public static void setServerVersion(VersionBean bean){ SERVER_VERSION = bean; }

    public static VersionBean getCurrentVersion()
    {
        if(CURRENT_VERSION == null)
        {
            CURRENT_VERSION = new VersionBean(-1,null,null);
        }
        return CURRENT_VERSION;
    }

    public static void setCurrentVersion(VersionBean bean)
    {
        CURRENT_VERSION = bean;
    }

    public String getDownloadURL()
    {
        return mDownloadURL;
    }

    public VersionBean(){}

    public VersionBean(int code, String name, String downloadURL)
    {
        this.mCode = code;
        this.mName = name;
        this.mDownloadURL = downloadURL;
    }

}
