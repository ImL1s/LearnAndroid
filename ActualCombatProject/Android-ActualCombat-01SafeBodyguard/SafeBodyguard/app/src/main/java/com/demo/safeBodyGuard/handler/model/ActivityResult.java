package com.demo.safeBodyGuard.handler.model;

import android.content.Intent;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */

public class ActivityResult
{
    private int requestCode;
    private int resultCode;
    private Intent data;

    public ActivityResult(){}

    public ActivityResult(int requestCode, int resultCode, Intent data)
    {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getRequestCode()
    {
        return requestCode;
    }

    public void setRequestCode(int requestCode)
    {
        this.requestCode = requestCode;
    }

    public int getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(int resultCode)
    {
        this.resultCode = resultCode;
    }

    public Intent getData()
    {
        return data;
    }

    public void setData(Intent data)
    {
        this.data = data;
    }
}
