package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.handler.SplashHandler;
import com.demo.safeBodyGuard.model.VersionBean;
import com.demo.safeBodyGuard.utils.JsonUtil;
import com.demo.safeBodyGuard.utils.PackageUtil;
import com.demo.safeBodyGuard.manager.TimerManager;
import com.demo.safeBodyGuard.utils.ThreadUtil;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

public class SplashActivity extends Activity
{
    private TextView tv_version_name;

    private SplashHandler handler = new SplashHandler(this);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initUI();
        initData();
        checkVersion();

    }


    private void initUI()
    {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }

    private void initData()
    {
        VersionBean.getCurrentVersion().setCode(PackageUtil.getVersionCode(getPackageManager(),getPackageName()));
        VersionBean.getCurrentVersion().setName(PackageUtil.getVersionName(getPackageManager(),getPackageName()));

        tv_version_name.setText(String.format("%s%s", getResources().getString(R.string.tv_update_version), VersionBean.getCurrentVersion().getName()));
    }

    private void checkVersion()
    {
        ThreadUtil.scheduleTaskInMinTime(() ->
        {
            // TODO 網路取得版本JSON資料

            VersionBean bean = JsonUtil.getObject("{mCode:2,mName:'1.0.0',mDownloadURL:'" + getResources().getString(R.string.apk_download_url) + "'}", new VersionBean());
            VersionBean.setServerVersion(bean);

        }, () ->
        {
            Message msg = Message.obtain();
            msg.setTarget(handler);

            VersionBean serverVersion = VersionBean.getServerVersion();
            VersionBean currentVersion = VersionBean.getCurrentVersion();

            if (serverVersion.getCode() > currentVersion.getCode())
            {
                msg.what = HandlerProtocol.UPDATE_VERSION;
                msg.obj = serverVersion;
                msg.sendToTarget();
            } else
            {
                msg.what = HandlerProtocol.ENTER_HOME;
                msg.sendToTarget();
            }

        }, 2000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        handler.handleActivityResult(requestCode,resultCode,data);

    }

    public SplashHandler getHandler()
    {
        return handler;
    }
}
