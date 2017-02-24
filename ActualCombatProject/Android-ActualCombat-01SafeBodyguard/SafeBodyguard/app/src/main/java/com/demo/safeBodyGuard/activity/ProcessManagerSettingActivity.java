package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.service.BlackListService;
import com.demo.safeBodyGuard.service.ScreenLockCleanProcessService;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ServiceUtil;

public class ProcessManagerSettingActivity extends AppCompatActivity
{
    public final static int REQUEST_CODE_SETUP = 113;
    private CheckBox mCb_show_sys_process;
    private CheckBox mCb_lock_kill_process;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager_setting);

        initUIRef();
        syncDataAndUI();
        setListener();
    }

    protected void initUIRef()
    {
        mCb_show_sys_process =
                (CheckBox) findViewById(R.id.activity_process_manager_cb_display_process);

        mCb_lock_kill_process =
                (CheckBox) findViewById(R.id.activity_process_manager_cb_kill_bg_process);
    }

    protected void syncDataAndUI()
    {
        mCb_show_sys_process.setChecked(
                SPUtil.getBool(this, Config.SP_KEY_BOOL_PROCESS_MANAGER_ACT_SYSTEM_SHOW, true));

        String serviceName = ScreenLockCleanProcessService.class.getCanonicalName();

        mCb_lock_kill_process.setChecked(ServiceUtil.isRunning(serviceName, this));
    }

    protected void setListener()
    {

        mCb_show_sys_process.setOnCheckedChangeListener((view, isChecked) -> SPUtil
                .setBool(ProcessManagerSettingActivity.this,
                         Config.SP_KEY_BOOL_PROCESS_MANAGER_ACT_SYSTEM_SHOW, isChecked));

        mCb_lock_kill_process.setOnCheckedChangeListener((view, isChecked) -> {

            boolean isRunning =
                    ServiceUtil.isRunning(ScreenLockCleanProcessService.class.getCanonicalName(),ProcessManagerSettingActivity.this);


            if (isChecked && !isRunning)
            {
                Log.d("debug","start service");
                startService(
                        new Intent(getApplicationContext(), ScreenLockCleanProcessService.class));
            }
            else if (isRunning)
            {
                Log.d("debug","stop service");
                stopService(
                        new Intent(getApplicationContext(), ScreenLockCleanProcessService.class));
            }
        });
    }
}
