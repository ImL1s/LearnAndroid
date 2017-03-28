package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.AppLockDAO;
import com.demo.safeBodyGuard.db.dao.AppLockInfo;
import com.demo.safeBodyGuard.db.dao.model.AppInfo;
import com.demo.safeBodyGuard.engine.AppInfoEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockActivity extends Activity
{
    @BindView(R.id.lv_app)
    ListView lv_app;
    private List<AppInfo>     mAppInfoList;
    private List<AppLockInfo> mAppLockList;
    private List<AppInfo> mLockAppInfoList   = new ArrayList<>();
    private List<AppInfo> mUnLockAppInfoList = new ArrayList<>();

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
//            lv_app.setAdapter(unLockAdapter);
            lv_app.setAdapter(lockAdapter);
        }
    };

    private AppListViewAdapter lockAdapter;
    private AppListViewAdapter unLockAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);

        initData();
    }

    private void initData()
    {
        new Thread(() ->
                   {
                       mAppInfoList = AppInfoEngine.getAppInfos(getApplication());
                       mAppLockList = AppLockDAO.getInstance(getApplicationContext()).selectAll();

                       for (AppInfo appInfo : mAppInfoList)
                       {
                           boolean isLock = false;

                           for (AppLockInfo appLockInfo : mAppLockList)
                           {
                               if (appLockInfo.pkg.equals(appInfo.packageName))
                               {
                                   mLockAppInfoList.add(appInfo);
                                   isLock = true;
                               }
                           }
                           if (!isLock)
                           {
                               mUnLockAppInfoList.add(appInfo);
                           }
                       }
                       //                       mAppInfoList.stream().
                       //                               filter(appInfo -> mAppLockList.contains(appInfo)).
                       //                               forEach(appInfo -> mLockAppInfoList.add(appInfo));

                       //                       mAppInfoList.stream().
                       //                               filter(appInfo -> !mAppLockList.contains(appInfo)).
                       //                               forEach(appInfo -> mUnLockAppInfoList.add(appInfo));

                       lockAdapter = new AppListViewAdapter(mLockAppInfoList);
                       unLockAdapter = new AppListViewAdapter(mUnLockAppInfoList);

                       handler.sendEmptyMessage(0);

                   }).start();
    }


    static class AppListViewAdapter extends BaseAdapter
    {
        private List<AppInfo> appInfoList;

        public AppListViewAdapter(List<AppInfo> appInfoList)
        {
            this.appInfoList = appInfoList;
        }


        @Override
        public int getCount()
        {
            return appInfoList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return appInfoList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewGroup view = (ViewGroup) View.inflate(parent.getContext(), R.layout.lv_item_lock_app, null);

            ((TextView) view.findViewById(R.id.tv_name)).setText(((AppInfo) getItem(position)).name);
            return view;
        }
    }
}
