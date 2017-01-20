package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ToastManager;

/**
 * Created by iml1s-macpro on 2017/1/13.
 */
public class SecuritySetup3Activity extends BaseSecuritySetupActivity implements View.OnClickListener
{
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_security_setup3);
        super.onCreate(savedInstanceState);

        setListener();
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.security3_btn_contact:
                startActivityForResult(new Intent(this, ContactListActivity.class), Config.ACTIVITY_REQUEST_CODE_CONTACT_SELECT);
                break;

            case R.id.security3_btn_next:
                onNextBtnClick();
                break;

            case R.id.security3_btn_pre:
                onPreBtnClick();
                break;
        }
    }

    @Override
    protected void initUI()
    {
        mEditText = (EditText) findViewById(R.id.et_alarm_contact);

        String alarmPhoneNum = SPUtil.getString(getApplicationContext(), Config.SP_KEY_STRING_ALARM_PHONE_NUMBER, null);
        mEditText.setText(alarmPhoneNum == null ? "" : alarmPhoneNum);
    }

    @Override
    protected void onPreBtnClick()
    {
        finish();
        overridePreAnim();
    }

    @Override
    protected void onNextBtnClick()
    {
        if (SPUtil.getString(getApplicationContext(), Config.SP_KEY_STRING_ALARM_PHONE_NUMBER, null) != null)
        {
            startActivity(new Intent(this, SecuritySetup4Activity.class));
        } else
        {
            ToastManager.getInstance().showToast("請先設定報警聯絡人");
        }

        super.overrideNextAnim();
    }

    private void setListener()
    {
        findViewById(R.id.security3_btn_contact).setOnClickListener(this);
        findViewById(R.id.security3_btn_next).setOnClickListener(this);
        findViewById(R.id.security3_btn_pre).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case Config.ACTIVITY_REQUEST_CODE_CONTACT_SELECT:

                if (resultCode == Config.ACTIVITY_RESULT_CODE_CONTACT_SELECTED)
                {
                    String phoneData = data.getStringExtra(Config.INTENT_DATA_KEY_PHONE);
                    phoneData = phoneData.replace('-', (char) 0x00).replace("(", "").replace(")", "").trim();
                    mEditText.setText(phoneData);
                    SPUtil.setString(getApplicationContext(), Config.SP_KEY_STRING_ALARM_PHONE_NUMBER, phoneData);
                    //                    mEditText.setText(phoneData.replace("-","").trim());
                }
                break;
        }

    }
}
