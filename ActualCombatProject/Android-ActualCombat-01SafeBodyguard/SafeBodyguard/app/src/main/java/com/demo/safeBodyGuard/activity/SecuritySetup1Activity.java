package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.handler.IActivityHandler;

import org.xutils.view.annotation.Event;

/**
 * Created by iml1s-macpro on 2017/1/11.
 */
public class SecuritySetup1Activity extends BaseSecuritySetupActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_security_setup1);
        super.onCreate(savedInstanceState);
    }


    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }


    @Event(R.id.security1_btn_next)
    private void onClick(View view)
    {
        if(view.getId() == R.id.security1_btn_next)
        {
            onNextBtnClick();
        }
    }

    @Override
    protected void onNextBtnClick()
    {
        Intent intent = new Intent(this,SecuritySetup2Activity.class);
        startActivity(intent);

        super.overrideNextAnim();
    }
}
