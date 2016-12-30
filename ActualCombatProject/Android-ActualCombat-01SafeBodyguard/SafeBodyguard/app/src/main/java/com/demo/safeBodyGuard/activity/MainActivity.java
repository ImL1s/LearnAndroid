package com.demo.safeBodyGuard.activity;

import android.os.Bundle;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.handler.IActivityHandler;

public class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }
}
