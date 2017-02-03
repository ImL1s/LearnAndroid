package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.model.Vector2;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.Listener.*;

/**
 * Created by ImL1s on 2017/1/24.
 * <p>
 * DESC:
 */

public class PhoneAddressPosSettingActivity extends Activity
{
    private TextView mFloatView;

    private int originalHeight;
    private int originalWidth;

    private OnPhoneAddressPosSettingTouchListener mOnSettingTouchListener;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_addr_pos_setting);

        init();
        initData();
        loadSavePosition();
        mFloatView.setOnTouchListener(mOnSettingTouchListener);
        mFloatView.setOnClickListener(new DoubleClickListener(v -> {

            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();

            v.layout(screenWidth / 2 - v.getWidth() / 2, screenHeight / 2 - v.getHeight() / 2,
                     (int) (screenWidth * 0.5) + (int) (v.getWidth() * 0.5),
                     (int) (screenHeight * 0.5) + (int) (v.getHeight() * 0.5));
            SPUtil.setInt(getApplicationContext(), Config.SP_KEY_INT_FLOW_VIEW_LOCATION_X,
                          v.getLeft());
            SPUtil.setInt(getApplicationContext(), Config.SP_KEY_INT_FLOW_VIEW_LOCATION_Y,
                          v.getTop());
        }));
    }

    private void init()
    {
        mFloatView = (TextView) findViewById(R.id.activity_phone_addr_pos_setting_tv_float);
        originalHeight = mFloatView.getHeight();
        originalWidth = mFloatView.getWidth();

        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Button mUpBtn = (Button) findViewById(R.id.activity_phone_addr_pos_setting_btn_up);
        Button mDownBtn = (Button) findViewById(R.id.activity_phone_addr_pos_setting_btn_down);

        mOnSettingTouchListener =
                new OnPhoneAddressPosSettingTouchListener(getApplicationContext(), mFloatView,
                                                          mUpBtn, mDownBtn, mWindowManager,
                                                          originalHeight, originalWidth);
    }

    private void initData()
    {
        originalHeight = mFloatView.getHeight();
        originalWidth = mFloatView.getWidth();
    }

    private void loadSavePosition()
    {
        int x = SPUtil.getInt(getApplicationContext(), Config.SP_KEY_INT_FLOW_VIEW_LOCATION_X, 0);
        int y = SPUtil.getInt(getApplicationContext(), Config.SP_KEY_INT_FLOW_VIEW_LOCATION_Y, 0);
//        mFloatView.layout(x, y, x + originalWidth, y + originalHeight);

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;

        mFloatView.setLayoutParams(layoutParams);
        mOnSettingTouchListener.showBtn(new Vector2(x, y));
    }
}
