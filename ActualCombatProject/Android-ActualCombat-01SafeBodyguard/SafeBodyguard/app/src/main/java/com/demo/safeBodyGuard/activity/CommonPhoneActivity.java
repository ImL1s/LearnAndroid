package com.demo.safeBodyGuard.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.CommonPhoneDAO;
import com.demo.safeBodyGuard.db.dao.model.PhoneClass;

import java.util.List;

public class CommonPhoneActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_phone);

        List<PhoneClass> phoneClassList = CommonPhoneDAO.GetAllPhoneClass();
    }
}
