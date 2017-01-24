package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.utils.LogUtil;

/**
 * Created by ImL1s on 2017/1/24.
 * <p>
 * DESC:
 */

public class PhoneAddressPosSettingActivity extends Activity
{
    private TextView floatView;
    private float    mX;
    private float    mY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_addr_pos_setting);

        floatView = (TextView) findViewById(R.id.activity_phone_addr_pos_setting_tv_float);

        floatView.setOnTouchListener((v, event) -> {
                                         switch (event.getAction())
                                         {
                                             case MotionEvent.ACTION_DOWN:
                                                 mX = event.getRawX();
                                                 mY = event.getRawY();
                                                 //                                                 LogUtil.log("rawX:" + mX + "/ rawY:" + mY);
                                                 //                                                 LogUtil.log("x:" + event.getX() + "/ y:" + event.getY());
                                                 break;

                                             case MotionEvent.ACTION_MOVE:
                                                 float currentX = event.getRawX();
                                                 float currentY = event.getRawY();

                                                 float diffX = currentX - mX;
                                                 float diffY = currentY - mY;

                                                 int left = (int) currentX;
                                                 int top = (int) currentY;

                                                 floatView.layout(left, top , left + floatView.getWidth(),
                                                                  top + floatView.getHeight());

                                                 mX = currentX;
                                                 mY = currentY;
                                                 break;

                                             case MotionEvent.ACTION_UP:

                                                 break;
                                         }

                                         return true;
                                     }

                                    );
    }
}
