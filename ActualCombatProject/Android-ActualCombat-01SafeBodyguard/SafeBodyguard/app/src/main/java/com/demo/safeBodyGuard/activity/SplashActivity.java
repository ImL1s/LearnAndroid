package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.handler.SplashHandler;
import com.demo.safeBodyGuard.model.VersionBean;
import com.demo.safeBodyGuard.utils.JsonUtil;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.PackageUtil;
import com.demo.safeBodyGuard.utils.ThreadUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity
{
    @ViewInject(R.id.tv_version_name)
    private TextView tv_version_name;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initData();
        checkVersion();

    }

    @Override
    public IActivityHandler initHandler()
    {
        return new SplashHandler(this);
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
            RequestParams params = new RequestParams(getResources().getString(R.string.apk_check_version_url));
            x.http().get(params, new Callback.CommonCallback<String>()
            {
                @Override
                public void onSuccess(String result)
                {
                    LogUtil.log(result);
//                    "{mCode:  2,mName:'1.0.0',mDownloadURL:'" + getResources().getString(R.string.apk_download_url) + "'}"
                    VersionBean bean = JsonUtil.getObject(result, new VersionBean());
                    VersionBean.setServerVersion(bean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback)
                {
                    LogUtil.log("onError");
                }

                @Override
                public void onCancelled(CancelledException cex)
                {
                    LogUtil.log("onError");
                }

                @Override
                public void onFinished()
                {
                    LogUtil.log("onFinished");
                }
            });

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

        }, Config.SPLASH_MIN_INTERVAL_MS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        handler.handleActivityResult(requestCode,resultCode,data);

    }

}
