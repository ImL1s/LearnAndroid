package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.demo.safeBodyGuard.Listener.HomeGridViewItemClickListener;
import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.adapter.HomeGridViewAdapter;
import com.demo.safeBodyGuard.handler.IActivityHandler;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity
{
    private Context mContext;

    //region View
    @ViewInject(R.id.gv_home)
    private GridView gv_home;
    //endregion

    //region Data
    private String[] mTitleStrings = new String[]{"手機防盜","通信衛士","軟體管理","程式管理","流量統計","手機防毒","緩存清理","高級工具","設定中心"};

    private int[] mDrawableIds = new int[]
            {
                    R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
                    R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                    R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
            };
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
//        initData();
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }

    private void initView()
    {
        gv_home.setAdapter(new HomeGridViewAdapter(this,mDrawableIds,mTitleStrings));
        gv_home.setOnItemClickListener(new HomeGridViewItemClickListener(this));
    }
}
