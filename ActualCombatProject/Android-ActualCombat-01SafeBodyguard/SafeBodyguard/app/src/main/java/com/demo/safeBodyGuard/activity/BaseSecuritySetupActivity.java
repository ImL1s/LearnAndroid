package com.demo.safeBodyGuard.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.handler.IActivityHandler;

/**
 * Created by iml1s-macpro on 2017/1/17.
 */

public class BaseSecuritySetupActivity extends BaseActivity
{
    private GestureDetector mGestureDetector = null;

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGestureDetector = new GestureDetector(this,new NextPreGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    protected void overrideNextAnim()
    {
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    protected void overridePreAnim()
    {
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }


    protected class NextPreGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if(e1.getRawX() - e2.getRawX() > 100)
            {
                showNextPage();
            }
            else if(e1.getRawX() - e2.getRawX() < -100)
            {
                showPrePage();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void showPrePage()
    {
        onPreBtnClick();
    }

    private void showNextPage()
    {
        onNextBtnClick();
    }

    protected void onNextBtnClick(){}


    protected void onPreBtnClick(){}
}
