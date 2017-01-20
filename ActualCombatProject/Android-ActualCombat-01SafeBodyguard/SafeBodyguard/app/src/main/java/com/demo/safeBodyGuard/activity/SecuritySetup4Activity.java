package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ToastManager;

public class SecuritySetup4Activity extends BaseSecuritySetupActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
{
    CheckBox mCheckBox;
    private Button nextBtn;
    private Button preBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_security_setup4);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUI()
    {
        mCheckBox = (CheckBox) findViewById(R.id.cb_box);
        nextBtn = (Button) findViewById(R.id.security4_btn_next);
        preBtn = (Button) findViewById(R.id.security4_btn_pre);

        mCheckBox.setOnCheckedChangeListener(this);
        nextBtn.setOnClickListener(this);
        preBtn.setOnClickListener(this);

        boolean isOpenSafeGuard = SPUtil.getBool(getApplicationContext(),Config.SP_KEY_BOOL_OPEN_SAFE_GUARD,false);
        mCheckBox.setChecked(isOpenSafeGuard);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        SPUtil.setBool(getApplicationContext(), Config.SP_KEY_BOOL_OPEN_SAFE_GUARD, isChecked);
        mCheckBox.setText(isChecked ? "您已經開啟了防盗保護" : "您沒有開啟防盗保護");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.security4_btn_next:
                overrideNextAnim();
                break;

            case R.id.security4_btn_pre:
                onPreBtnClick();
                break;
        }
    }


    @Override
    protected void onPreBtnClick()
    {
        finish();
        overridePreAnim();
    }

    @Override
    protected void overrideNextAnim()
    {
        boolean isOpenGuard = SPUtil.getBool(getApplicationContext(), Config.SP_KEY_BOOL_OPEN_SAFE_GUARD, false);

        if (!isOpenGuard)
        {
            ToastManager.getInstance().showToast("已經關閉手機保護");
        }

        startActivity(new Intent(this, SecurityActivity.class));
        super.overrideNextAnim();
    }
}
