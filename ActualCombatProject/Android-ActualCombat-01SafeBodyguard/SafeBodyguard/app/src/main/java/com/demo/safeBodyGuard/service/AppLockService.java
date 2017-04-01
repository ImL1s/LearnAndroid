package com.demo.safeBodyGuard.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.demo.safeBodyGuard.db.dao.AppLockDAO;
import com.demo.safeBodyGuard.db.dao.model.AppLockInfo;

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

    private List<AppLockInfo> appLockInfoList;
    private List<String> lockPkgNameList;
    private boolean isListening = false;
    private ActivityManager activityManager;

    public AppLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        initObservable();

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

    private void initObservable() {
        Observable
                .create((ObservableOnSubscribe<String>) e -> {
                    isListening = true;
                    appLockInfoList = AppLockDAO.getInstance(getApplicationContext()).selectAll();
                    activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    lockPkgNameList = new ArrayList<>();
                    for (AppLockInfo appLockInfo : appLockInfoList) {
                        e.onNext(appLockInfo.pkg);
                    }
                    e.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .flatMap(pkg -> {
                    lockPkgNameList.add(pkg);
                    return Observable.just(pkg);
                })
                .doOnComplete(() -> {
                    while (isListening) {
                        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
                                .getRunningTasks(1);
                        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                        String pkgName = runningTaskInfo.topActivity.getPackageName();

                        if (lockPkgNameList.contains(pkgName)) {
                            Log.d("debug", "LOCK");
                        }
                    }
                })
                .subscribe();
    }
}
