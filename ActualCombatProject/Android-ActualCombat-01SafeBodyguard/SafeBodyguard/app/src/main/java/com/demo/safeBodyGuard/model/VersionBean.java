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

    private static VersionBean mCurrentVersion = null;
    private static VersionBean mServerVersion = null;

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

    public static VersionBean getServerVersion(){ return mServerVersion; }

    public static void setServerVersion(VersionBean bean){ mServerVersion = bean; }

    public static VersionBean getCurrentVersion()
    {
        if(mCurrentVersion == null)
        {
            mCurrentVersion = new VersionBean(-1,null,null);
        }
        return mCurrentVersion;
    }

    public static void setCurrentVersion(VersionBean bean)
    {
        mCurrentVersion = bean;
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
