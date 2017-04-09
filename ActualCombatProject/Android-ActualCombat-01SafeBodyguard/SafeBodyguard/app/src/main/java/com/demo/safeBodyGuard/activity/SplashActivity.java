package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.AddressDAO;
import com.demo.safeBodyGuard.db.dao.AntiVirusDAO;
import com.demo.safeBodyGuard.db.dao.CommonPhoneDAO;
import com.demo.safeBodyGuard.db.dao.model.AntiVirus;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.handler.SplashHandler;
import com.demo.safeBodyGuard.model.VersionBean;
import com.demo.safeBodyGuard.utils.DbUtil;
import com.demo.safeBodyGuard.utils.JsonUtil;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.PackageUtil;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ToastManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by iml1s-macpro on 2016/12/28.
 */

@ContentView(R.layout.activity_splash)
public class SplashActivity extends BaseActivity
{
    @ViewInject(R.id.tv_version_name) private TextView tv_version_name;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initData();
        createIcon();
        initDatabase();
        checkUpdate();
    }

    @Override
    public IActivityHandler initHandler()
    {
        return new SplashHandler(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        handler.handleActivityResult(requestCode, resultCode, data);

    }

    private void initData()
    {
        VersionBean.getCurrentVersion()
                .setCode(PackageUtil.getVersionCode(getPackageManager(), getPackageName()));
        VersionBean.getCurrentVersion()
                .setName(PackageUtil.getVersionName(getPackageManager(), getPackageName()));

        tv_version_name.setText(
                String.format("%s%s", getResources().getString(R.string.tv_update_version),
                              VersionBean.getCurrentVersion().getName()));
    }

    private void initDatabase()
    {
        File dbDir = getFilesDir();
        File dbFile = new File(dbDir, Config.DB_FILE_NAME_ADDRESS);
        AddressDAO.setDBPath(dbFile.getAbsolutePath());

        DbUtil.copyDbToDbFolder(this, dbFile, Config.DB_FILE_NAME_ADDRESS);

        dbFile = new File(dbDir,Config.DB_FILE_NAME_COMMON_PHONE);
        CommonPhoneDAO.setDBPath(dbFile.getAbsolutePath());

        DbUtil.copyDbToDbFolder(this, dbFile, Config.DB_FILE_NAME_COMMON_PHONE);

        dbFile = new File(dbDir,Config.DB_FILE_NAME_ANTI_VIRUS);
        AntiVirusDAO.setDBPath(dbFile.getAbsolutePath());
        DbUtil.copyDbToDbFolder(getApplicationContext(),dbFile,Config.DB_FILE_NAME_ANTI_VIRUS);
        //        File dbDir = getFilesDir();
        //        File dbFile = new File(dbDir, Config.DB_FILE_NAME_ADDRESS);
        //        AddressDAO.setDBPath(dbFile.getAbsolutePath());
        //
        //        if (dbFile.exists())
        //            return;
        //
        //        InputStream is = null;
        //        OutputStream os = null;
        //
        //        try
        //        {
        //            is = getAssets().open(Config.DB_FILE_NAME_ADDRESS);
        //            os = new FileOutputStream(dbFile);
        //
        //            byte[] buffer = new byte[Config.IO_BUFFER_SIZE];
        //            int haveRead;
        //
        //            while ((haveRead = is.read(buffer)) != -1)
        //            {
        //                os.write(buffer, 0, haveRead);
        //            }
        //        }
        //        catch (IOException e)
        //        {
        //            e.printStackTrace();
        //        }
        //        finally
        //        {
        //            try
        //            {
        //                if (is != null)
        //                    is.close();
        //                if (os != null)
        //                    os.close();
        //            }
        //            catch (IOException e)
        //            {
        //                e.printStackTrace();
        //            }
        //        }
    }

    private void checkUpdate()
    {
        boolean needUpdateVersion =
                SPUtil.getBool(getApplicationContext(), Config.SP_KEY_BOOL_UPDATE, true);

        if (needUpdateVersion)
        {
            checkVersion();
        }
        else
        {
            skipUpdate();
        }
    }

    private void checkVersion()
    {
        new Thread(() -> {
            RequestParams params =
                    new RequestParams(getResources().getString(R.string.apk_check_version_url));
            x.http().get(params, new Callback.CommonCallback<String>()
            {
                @Override
                public void onSuccess(String result)
                {
                    LogUtil.log(result);
                    //                    "{mCode:  2,mName:'1.0.0',mDownloadURL:'" + getResources().getString(R.string.apk_download_url) + "'}"
                    VersionBean bean = JsonUtil.getObject(result, new VersionBean());
                    VersionBean.setServerVersion(bean);

                    Message msg = Message.obtain();
                    msg.setTarget(handler);

                    VersionBean serverVersion = VersionBean.getServerVersion();
                    VersionBean currentVersion = VersionBean.getCurrentVersion();

                    if (serverVersion.getCode() > currentVersion.getCode())
                    {
                        msg.what = HandlerProtocol.UPDATE_VERSION;
                        msg.obj = serverVersion;
                        msg.sendToTarget();
                    }
                    else
                    {
                        msg.what = HandlerProtocol.ENTER_HOME;
                        msg.sendToTarget();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback)
                {
                    LogUtil.log("onError " + ex.toString());

                    ToastManager.getInstance().showToast("網路不穩,跳過更新");
                    startActivityHome();
                }

                @Override
                public void onCancelled(CancelledException cex)
                {
                    LogUtil.log("onCancelled " + cex.toString());
                    ToastManager.getInstance().showToast("網路不穩,跳過更新");
                    startActivityHome();
                }

                @Override
                public void onFinished()
                {
                    LogUtil.log("onFinished");
                }

                protected void startActivityHome()
                {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    msg.what = HandlerProtocol.ENTER_HOME;
                    msg.sendToTarget();
                }
            });
        }).start();

        //        ThreadUtil.scheduleTaskInMinTime(() -> {
        //            RequestParams params =
        //                    new RequestParams(getResources().getString(R.string.apk_check_version_url));
        //            x.http().get(params, new Callback.CommonCallback<String>()
        //            {
        //                @Override
        //                public void onSuccess(String result)
        //                {
        //                    LogUtil.log(result);
        //                    //                    "{mCode:  2,mName:'1.0.0',mDownloadURL:'" + getResources().getString(R.string.apk_download_url) + "'}"
        //                    VersionBean bean = JsonUtil.getObject(result, new VersionBean());
        //                    VersionBean.setServerVersion(bean);
        //                }
        //
        //                @Override
        //                public void onError(Throwable ex, boolean isOnCallback)
        //                {
        //                    LogUtil.log("onError " + ex.toString());
        //                }
        //
        //                @Override
        //                public void onCancelled(CancelledException cex)
        //                {
        //                    LogUtil.log("onCancelled " + cex.toString());
        //                }
        //
        //                @Override
        //                public void onFinished()
        //                {
        //                    LogUtil.log("onFinished");
        //                }
        //            });
        //
        //        }, () -> {
        //            Message msg = Message.obtain();
        //            msg.setTarget(handler);
        //
        //            VersionBean serverVersion = VersionBean.getServerVersion();
        //            VersionBean currentVersion = VersionBean.getCurrentVersion();
        //
        //            if (serverVersion.getCode() > currentVersion.getCode())
        //            {
        //                msg.what = HandlerProtocol.UPDATE_VERSION;
        //                msg.obj = serverVersion;
        //                msg.sendToTarget();
        //            }
        //            else
        //            {
        //                msg.what = HandlerProtocol.ENTER_HOME;
        //                msg.sendToTarget();
        //            }
        //
        //        }, Config.SPLASH_MIN_INTERVAL_MS);

    }

    private void skipUpdate()
    {
        Message msg = Message.obtain();
        msg.what = HandlerProtocol.ENTER_HOME;
        handler.sendMessageDelayed(msg, Config.SPLASH_MIN_INTERVAL_MS);
    }

    private void createIcon()
    {
        boolean isCreated =
                SPUtil.getBool(getApplicationContext(), Config.SP_KEY_BOOL_CREATED_ICON, false);

        if (!isCreated)
        {
            Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "安全衛士");

            Intent iconIntent = new Intent();
            iconIntent.addCategory("android.intent.category.DEFAULT");
            iconIntent.setAction("android.intent.action.HOME");

            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, iconIntent);

            sendBroadcast(intent);

            SPUtil.setBool(getApplicationContext(), Config.SP_KEY_BOOL_CREATED_ICON, true);
        }
    }
}
