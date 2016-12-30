package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;

import com.demo.safeBodyGuard.handler.IActivityHandler;

import org.xutils.x;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */

public abstract class BaseActivity extends Activity
{
    protected IActivityHandler handler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        handler = initHandler();
    }

    public IActivityHandler getHandler()
    {
        return handler;
    }

    public abstract IActivityHandler initHandler();

}
