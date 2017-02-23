package com.demo.safeBodyGuard.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.utils.MPermissionUtil;

import org.xutils.x;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */

public abstract class BaseActivity extends Activity
{
    protected IActivityHandler handler    = null;
    protected boolean          needInitUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        handler = initHandler();
        checkPermission();
        if (needInitUI)
            initUI();
    }

    public IActivityHandler getHandler()
    {
        return handler;
    }

    public abstract IActivityHandler initHandler();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        MPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 檢查應用權限
     */
    protected void checkPermission()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            MPermissionUtil.requestPermissionsResult(this, Config.PERMISSION_ALL_REQUEST_CODE,
                                                     new String[]{Manifest.permission.INTERNET,
                                                                  Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                  Manifest.permission.READ_PHONE_STATE,
                                                                  Manifest.permission.READ_CONTACTS,
                                                                  Manifest.permission.READ_SMS,
                                                                  Manifest.permission.SEND_SMS,
                                                                  Manifest.permission.RECEIVE_SMS,
                                                                  Manifest.permission.ACCESS_COARSE_LOCATION,
                                                                  Manifest.permission.ACCESS_FINE_LOCATION,
                                                                  Manifest.permission.READ_PHONE_STATE,
                                                                  Manifest.permission.PROCESS_OUTGOING_CALLS,
                                                                  Manifest.permission.KILL_BACKGROUND_PROCESSES},
                                                     new MPermissionUtil.OnPermissionListener()
                                                     {
                                                         @Override
                                                         public void onPermissionGranted()
                                                         {

                                                         }

                                                         @Override
                                                         public void onPermissionDenied()
                                                         {
                                                             //                    MPermissionUtil.showTipsDialog(BaseActivity.this);
                                                         }
                                                     });
        }
    }

    protected void initUI()
    {
    }

    protected void setNeedInitUI(boolean isNeed)
    {
        needInitUI = isNeed;
    }

    protected void startActivityEasy(Class<?> target)
    {
        startActivity(new Intent(this, target));
    }
}
