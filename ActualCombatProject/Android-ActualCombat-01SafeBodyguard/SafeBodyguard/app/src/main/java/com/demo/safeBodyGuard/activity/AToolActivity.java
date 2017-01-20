package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.handler.IActivityHandler;

public class AToolActivity extends BaseActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_tool);


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
            case R.id.activity_a_tool_tv_phone_to_address:
                startActivityEasy(QueryAddressActivity.class);
                break;
        }
    }
}
