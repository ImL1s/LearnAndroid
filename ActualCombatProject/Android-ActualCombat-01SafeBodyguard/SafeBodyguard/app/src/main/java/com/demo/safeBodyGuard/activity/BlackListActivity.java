package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.BlackListDAO;


/**
 * Created by ImL1s on 2017/2/3.
 * <p>
 * DESC:
 */

public class BlackListActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.d("debug","Touch");
            BlackListDAO.getInstance(getApplicationContext()).insert("110", 2);
        }

        return super.onTouchEvent(event);
    }
}
