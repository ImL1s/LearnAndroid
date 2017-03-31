package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.AppLockDAO;
import com.demo.safeBodyGuard.db.dao.model.AppLockInfo;
import com.demo.safeBodyGuard.db.dao.model.AppInfo;
import com.demo.safeBodyGuard.engine.AppInfoEngine;
import com.demo.safeBodyGuard.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by iml1s on 2017/3/27.
 */

public class AppLockActivity extends Activity {
    @BindView(R.id.lv_app)
    ListView lv_app;
    @BindView(R.id.tv_app_count)
    TextView tv_app_count;
    @BindView(R.id.btn_unlock_app)
    Button btn_unlock;
    @BindView(R.id.btn_lock_app)
    Button btn_lock;

    private List<AppInfo> mAppInfoList;
    private List<AppLockInfo> mAppLockList;
    private List<AppInfo> mLockAppInfoList = new ArrayList<>();
    private List<AppInfo> mUnLockAppInfoList = new ArrayList<>();


    private AppListViewAdapter lockAdapter;
    private AppListViewAdapter unLockAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Observable.create((ObservableOnSubscribe<AppInfo>) e ->
        {
            Log.d("debug", "onSub");
            mAppInfoList = AppInfoEngine.getAppInfos(getApplication());
            mAppLockList = AppLockDAO.getInstance(getApplicationContext()).selectAll();
            for (AppInfo appInfo : mAppInfoList) {
                e.onNext(appInfo);
            }
            e.onComplete();

        }).doOnNext(appInfo -> {
            Log.d("debug", "doOnNext " + Thread.currentThread().toString());
            boolean isLock = false;
            for (AppLockInfo appLockInfo : mAppLockList) {
                if (appLockInfo.pkg.equals(appInfo.packageName)) {
                    isLock = true;
                    mLockAppInfoList.add(appInfo);
                }
            }
            if (!isLock) {
                mUnLockAppInfoList.add(appInfo);
            }
        }).doOnComplete(() -> {
            Log.d("debug", "onComplete " + Thread.currentThread().toString());
            lockAdapter = new AppListViewAdapter(mLockAppInfoList, true, this);
            unLockAdapter = new AppListViewAdapter(mUnLockAppInfoList, false, this);
            lv_app.setAdapter(lockAdapter);
            refreshTvCountDisplay(true);

        }).subscribe();
    }

    private void refreshTvCountDisplay(boolean isLock) {
        String str_appCount;
        AppListViewAdapter listViewAdapter;

        if (isLock) {
            str_appCount = getString(R.string.lock_app_count);
            listViewAdapter = lockAdapter;
        } else {
            str_appCount = getString(R.string.unlock_app_count);
            listViewAdapter = unLockAdapter;
        }
        tv_app_count.setText(str_appCount + ":" + listViewAdapter.getCount());
    }

    @OnClick({R.id.btn_lock_app, R.id.btn_unlock_app})
    void onClick(View view) {
        AppListViewAdapter listViewAdapter = null;

        switch (view.getId()) {
            case R.id.btn_lock_app:
                listViewAdapter = lockAdapter;
                btn_lock.setBackgroundResource(R.drawable.tab_left_pressed);
                btn_unlock.setBackgroundResource(R.drawable.tab_right_default);
                refreshTvCountDisplay(true);
                break;

            case R.id.btn_unlock_app:
                listViewAdapter = unLockAdapter;
                btn_lock.setBackgroundResource(R.drawable.tab_left_default);
                btn_unlock.setBackgroundResource(R.drawable.tab_right_pressed);
                refreshTvCountDisplay(false);
                break;
        }

        lv_app.setAdapter(listViewAdapter);
    }


    static class AppListViewAdapter extends BaseAdapter {
        private AppLockActivity context;
        private final boolean isLock;
        private List<AppInfo> appInfoList;

        AppListViewAdapter(List<AppInfo> appInfoList, boolean isLock, AppLockActivity context) {
            this.appInfoList = appInfoList;
            this.isLock = isLock;
            this.context = context;
        }


        @Override
        public int getCount() {
            return appInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.lv_item_lock_app, null);
            }
            ViewHolder viewHolder;

            if (convertView.getTag() == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.setAppInfo(((AppInfo) getItem(position)));
            return convertView;
        }

        class ViewHolder {

            AppInfo appInfo;
            @BindView(R.id.tv_name)
            TextView tv_app_name;

            @BindView(R.id.iv_lock)
            ImageView iv_lock;

            @BindView(R.id.tv_icon)
            ImageView tv_icon;

            private View view;

            ViewHolder(View view) {
                this.view = view;
                ButterKnife.bind(this, view);
            }

            public void setAppInfo(AppInfo appInfo) {
                this.appInfo = appInfo;

                tv_icon.setImageDrawable(appInfo.icon);
                tv_app_name.setText(appInfo.name);
                iv_lock.setImageDrawable(context.getResources().getDrawable(isLock ? R.drawable.lock : R.drawable.unlock));
            }

            @OnClick(R.id.iv_lock)
            void onClick(View iv_view) {

                TranslateAnimation translateAnimation = AnimationUtil.getHorizontalAnimation(true, 500);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (isLock) {
                            context.mLockAppInfoList.remove(appInfo);
                            context.mUnLockAppInfoList.add(appInfo);
                            AppLockDAO.getInstance(context.getApplicationContext()).delete(appInfo.packageName);

                        } else {
                            context.mLockAppInfoList.add(appInfo);
                            context.mUnLockAppInfoList.remove(appInfo);
                            AppLockDAO.getInstance(context.getApplicationContext()).insert(appInfo.packageName);
                        }
                        context.refreshTvCountDisplay(isLock);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                view.startAnimation(translateAnimation);
            }
        }
    }
}
