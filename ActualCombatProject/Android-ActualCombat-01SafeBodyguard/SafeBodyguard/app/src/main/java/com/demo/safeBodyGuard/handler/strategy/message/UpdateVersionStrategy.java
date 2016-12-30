package com.demo.safeBodyGuard.handler.strategy.message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.activity.MainActivity;
import com.demo.safeBodyGuard.define.ActivityResultProtocol;
import com.demo.safeBodyGuard.model.VersionBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class UpdateVersionStrategy implements IMessageHandlerStrategy
{
    @Override
    public void handle(Message msg, Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        builder.setIcon(R.mipmap.ic_launcher).setMessage(R.string.update_version).setNegativeButton(R.string.update_version_yes, (dialog, which) -> {

            String path = Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name) + VersionBean.getServerVersion().mName +".apk";
            VersionBean versionBean = VersionBean.getServerVersion();
            RequestParams params = new RequestParams(versionBean.getDownloadURL());
            params.setSaveFilePath(path);
            params.setLoadingUpdateMaxTimeSpan(1);

            x.http().get(params, new InstalledApkCallback(path,context));
        });

        builder.setPositiveButton(R.string.update_version_no,(dialog,which) -> context.startActivity(new Intent(context, MainActivity.class)));

        builder.setOnCancelListener(dialog -> {
            context.startActivity(new Intent(context, MainActivity.class));
        });

        builder.show();
    }

    public class InstalledApkCallback implements Callback.ProgressCallback<String>
    {

        private String path = null;
        private Context context = null;

        public InstalledApkCallback(String path, Context context)
        {
            this.path = path;
            this.context = context;
        }

        @Override
        public void onSuccess(String result)
        {
            Log.d("debug","onSuccess");

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                Intent intent = new Intent();
//                Uri uri = Uri.parse("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = new File(path);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");

                ((Activity) context).startActivityForResult(intent, ActivityResultProtocol.UPDATE_INSTALL_APK);
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback)
        {
            Log.d("debug","onError");
        }

        @Override
        public void onCancelled(CancelledException cex)
        {
            Log.d("debug","onCancelled");
        }

        @Override
        public void onFinished()
        {
            Log.d("debug","onFinished");
        }

        @Override
        public void onWaiting()
        {
            Log.d("debug","onWaiting");
        }

        @Override
        public void onStarted()
        {
            Log.d("debug","onStarted");
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading)
        {
            Log.d("debug",total + "/" + current);
        }
    }
}
