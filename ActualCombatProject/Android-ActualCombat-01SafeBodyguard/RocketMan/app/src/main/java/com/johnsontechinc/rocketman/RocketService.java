package com.johnsontechinc.rocketman;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


/**
 * Created by ImL1s on 2017/2/2.
 * <p>
 * DESC:
 */

public class RocketService extends Service
{

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        View rocketView = View.inflate(getApplicationContext(), R.layout.rocket_view, null);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = layoutParams.WRAP_CONTENT;
        layoutParams.width = layoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.LEFT + Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
        //        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        //                //				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	因为吐司需要根据手势去移动,所以必须要能触摸
        //                       | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        windowManager.addView(rocketView, layoutParams);

        ImageView iv_rocket = (ImageView) rocketView.findViewById(R.id.iv_rocket);
        AnimationDrawable ad = (AnimationDrawable) iv_rocket.getBackground();
        ad.start();
    }

    @Override
    public void onDestroy()
    {

    }
}
