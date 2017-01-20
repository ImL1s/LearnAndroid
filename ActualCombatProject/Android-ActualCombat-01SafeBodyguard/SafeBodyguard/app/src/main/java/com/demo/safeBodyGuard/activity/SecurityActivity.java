package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.utils.SPUtil;

public class SecurityActivity extends BaseActivity implements View.OnClickListener
{
    TextView mSetupTV;
    TextView mSafePhoneTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setNeedInitUI(false);
        super.onCreate(savedInstanceState);

        boolean isSetup = SPUtil.getBool(this, Config.SP_KEY_BOOL_OPEN_SAFE_GUARD, false);

        if (isSetup)
        {
            setContentView(R.layout.activity_setup_over);
        } else
        {
            startActivity(new Intent(this, SecuritySetup1Activity.class));
            finish();

            return;
        }

        initUI();
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }

    @Override
    protected void initUI()
    {
        mSetupTV = (TextView) findViewById(R.id.activity_setup_over_tv_reset_setup);
        mSetupTV.setClickable(true);
        mSetupTV.setFocusableInTouchMode(true);
        mSetupTV.setOnClickListener(v -> startActivity(new Intent(this, SecuritySetup1Activity.class)));

        mSafePhoneTV = (TextView) findViewById(R.id.activity_setup_over_tv_phone);
        mSafePhoneTV.setText(SPUtil.getString(this, Config.SP_KEY_STRING_ALARM_PHONE_NUMBER, "未設定"));
    }

    @Override
    public void onClick(View v)
    {
        startActivity(new Intent(this, SecuritySetup1Activity.class));
    }
}
