package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.demo.safeBodyGuard.R;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
    }
}
