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
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class UpdateVersionStrategy implements IMessageHandlerStrategy
{
    Callback.Cancelable cancelable = null;
    @Override
    public void handle(Message msg, Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        builder.setIcon(R.mipmap.ic_launcher).setMessage( context.getResources().getString(R.string.update_version) + VersionBean.getServerVersion().getName()).
                setNegativeButton(R.string.update_version_yes, (dialog, which) -> {

            String path = Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name) + VersionBean.getServerVersion().mName +".apk";

            VersionBean versionBean = VersionBean.getServerVersion();
            RequestParams params = new RequestParams(versionBean.getDownloadURL());

            params.setAutoRename(false);
            params.setAutoResume(true);
            params.setExecutor(new PriorityExecutor(2,true));
            params.setCancelFast(true);
            params.setSaveFilePath(path);

//            cancelable = x.http().get(params, new InstalledApkCallback(path,context));
            cancelable = x.http().request(HttpMethod.GET,params,new InstalledApkCallback(path,context));

        });

        builder.setPositiveButton(R.string.update_version_no,(dialog,which) -> context.startActivity(new Intent(context, MainActivity.class)));

        builder.setOnCancelListener(dialog -> {
            context.startActivity(new Intent(context, MainActivity.class));
        });

        builder.show();
    }

    public class InstalledApkCallback implements Callback.ProgressCallback<File>
    {

        private String path = null;
        private Context context = null;

        public InstalledApkCallback(String path, Context context)
        {
            this.path = path;
            this.context = context;
        }

        @Override
        public void onSuccess(File result)
        {
            Log.d("debug","onSuccess");
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

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                Log.d("debug",path);
                Intent intent = new Intent();
                Uri uri = Uri.parse("android.intent.action.VIEW");
                intent.setData(uri);
                intent.addCategory("android.intent.category.DEFAULT");
                File file = new File(path);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");

                ((Activity) context).startActivityForResult(intent, ActivityResultProtocol.UPDATE_INSTALL_APK);
            }
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
