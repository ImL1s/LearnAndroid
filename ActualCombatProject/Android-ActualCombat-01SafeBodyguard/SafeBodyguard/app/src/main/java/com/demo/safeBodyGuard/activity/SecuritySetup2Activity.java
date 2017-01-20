package com.demo.safeBodyGuard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ToastManager;
import com.demo.safeBodyGuard.view.SettingItemView;

import org.xutils.view.annotation.ViewInject;

public class SecuritySetup2Activity extends BaseSecuritySetupActivity implements View.OnClickListener
{

    @ViewInject(R.id.siv_sim_card)
    SettingItemView settingItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_security_setup2);
        super.onCreate(savedInstanceState);

        setListener();
    }

    @Override
    protected void initUI()
    {
        String simCardNum = SPUtil.getString(getApplicationContext(), Config.SP_KEY_STRING_SIM_SERIAL_NUM, null);
        settingItemView.setChecked(simCardNum != null);
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }

    private void setListener()
    {
        settingItemView.setOnClickListener(view -> {

            if (settingItemView.isChecked())
            {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String serialNum = telephonyManager.getSimSerialNumber();

                SPUtil.setString(getApplicationContext(), Config.SP_KEY_STRING_SIM_SERIAL_NUM, serialNum);
            } else
            {
                SPUtil.remove(getApplicationContext(), Config.SP_KEY_STRING_SIM_SERIAL_NUM);
            }
        });

        findViewById(R.id.security2_btn_next).setOnClickListener(this);
        findViewById(R.id.security2_btn_pre).setOnClickListener(this);
    }

    public void onClick(View view)
    {
        if (view.getId() == R.id.security2_btn_pre)
        {
            onPreBtnClick();
            return;
        }

        if (!settingItemView.isChecked())
        {
            ToastManager.getInstance().showToast("必須綁定SIM卡");
            return;
        }


        onNextBtnClick();
    }

    @Override
    protected void onNextBtnClick()
    {
        startActivity(new Intent(this, SecuritySetup3Activity.class));
        super.overrideNextAnim();
    }

    @Override
    protected void onPreBtnClick()
    {
        finish();
        overridePreAnim();
    }
}
