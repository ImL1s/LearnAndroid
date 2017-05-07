package com.demo.safeBodyGuard;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.demo.safeBodyGuard.utils.ToastManager;

import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class SafeBodyguardApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        ToastManager.init(this);
//        Toast.makeText(this,"***************",Toast.LENGTH_LONG).show();
        Log.d("debug",Environment.getExternalStorageDirectory().getAbsolutePath());
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"error.log";
                File file = new File(path);

                try {
                    PrintWriter printWriter = new PrintWriter(file);
                    e.printStackTrace(printWriter);
                    printWriter.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                System.exit(0);
            }
        });
    }
}
