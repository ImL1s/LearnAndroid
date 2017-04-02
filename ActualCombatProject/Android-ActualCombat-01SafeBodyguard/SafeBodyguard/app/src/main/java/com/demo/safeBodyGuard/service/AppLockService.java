package com.demo.safeBodyGuard.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.demo.safeBodyGuard.activity.LockAppActivity;
import com.demo.safeBodyGuard.db.dao.AppLockDAO;
import com.demo.safeBodyGuard.db.dao.model.AppLockInfo;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.receiver.BootReceiver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AppLockService extends Service {

    private List<AppLockInfo> appLockInfoList = new ArrayList<>();
    private List<String> lockPkgNameList = new ArrayList<>();
    private boolean isListening = false;
    private ActivityManager activityManager;
    private String skipPkgName = "";
    private BroadcastReceiver innerReceiver;

    public AppLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        initObservable();

        IntentFilter intentFilter = new IntentFilter("com.android.action.LOCK_SKIP_CHECK");
        innerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AppLockService.this.skipPkgName = intent.getStringExtra("pkgName");
            }
        };
        registerReceiver(innerReceiver, intentFilter);

        getContentResolver().registerContentObserver(Uri.parse("content://applock/change"), true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        loadPkgData();
                        loadAppLockInfoToLockPkgName();
                    }
                });

//        new Thread(() -> {
//            isListening = true;
//            appLockInfoList = AppLockDAO.getInstance(getApplicationContext()).selectAll();
//            activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//            lockPkgNameList = new ArrayList<>();
//            for (AppLockInfo appLockInfo : appLockInfoList) {
//                lockPkgNameList.add(appLockInfo.pkg);
//            }
//
//            while (isListening) {
//                List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
//                        .getRunningTasks(1);
//                ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
//                String pkgName = runningTaskInfo.topActivity.getPackageName();
//
//                if (lockPkgNameList.contains(pkgName)) {
//                    Log.d("debug", "LOCK");
//                }
//            }
//        }).start();
    }

    @Override
    public void onDestroy() {
        if (innerReceiver != null) {
            unregisterReceiver(innerReceiver);
        }
    }

    private void loadPkgData() {
        synchronized (this) {
            this.appLockInfoList = AppLockDAO.getInstance(getApplicationContext()).selectAll();
            if(lockPkgNameList.size() > 0) {
                lockPkgNameList.clear();
            }
        }
    }

    private void loadAppLockInfoToLockPkgName(){
        for (AppLockInfo info : appLockInfoList) {
            lockPkgNameList.add(info.pkg);
        }
    }

    private void initObservable() {
        Observable
                .create((ObservableOnSubscribe<String>) e -> {
                    isListening = true;
                    loadPkgData();
                    activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    for (AppLockInfo appLockInfo : appLockInfoList) {
                        e.onNext(appLockInfo.pkg);
                    }
                    e.onComplete();
                })
                .doOnComplete(() -> {
                    while (isListening) {
                        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
                                .getRunningTasks(1);
                        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                        String pkgName = runningTaskInfo.topActivity.getPackageName();

                        if (!pkgName.equals(this.skipPkgName) && lockPkgNameList
                                .contains(pkgName)) {
                            Log.d("debug", "LOCK " + Thread.currentThread());
                            Intent intent = new Intent(getApplicationContext(),
                                    LockAppActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Config.INTENT_KEY_PKG_NAME, pkgName);
                            startActivity(intent);
                        }

                        Thread.sleep(500);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(pkg -> lockPkgNameList.add(pkg));
    }
}
