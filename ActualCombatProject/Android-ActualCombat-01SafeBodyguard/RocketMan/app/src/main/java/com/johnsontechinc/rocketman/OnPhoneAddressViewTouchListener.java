package com.johnsontechinc.rocketman;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.model.Vector2;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.SPUtil;


/**
 * Created by ImL1s on 2017/1/25.
 *
 * DESC:
 */

public class OnPhoneAddressViewTouchListener implements View.OnTouchListener
{
    /**
     * 頂部TitleBar的長度,不同解析度大小不一樣
     */
    private static int titleBarPixels = 48;

    private       float                      mX;
    private       float                      mY;
    private       View                       mFloatView;
    private       WindowManager              mWindowManager;
    private       Context                    mContext;
    private       int                        originalHeight;
    private       int                        originalWidth;
    private final WindowManager.LayoutParams mLayoutParams;

    public OnPhoneAddressViewTouchListener(Context context, View floatView,
                                           WindowManager windowsManager,
                                           WindowManager.LayoutParams layoutParams)
    {
        this.mContext = context;
        this.mFloatView = floatView;
        this.mWindowManager = windowsManager;
        this.mLayoutParams = layoutParams;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mX = event.getRawX();
                mY = event.getRawY();
                originalHeight = mFloatView.getHeight();
                originalWidth = mFloatView.getWidth();
                break;

            case MotionEvent.ACTION_MOVE:

                Vector2 slideXYScale = getSlideScale(event);
                moveView(slideXYScale, event);

                break;

            case MotionEvent.ACTION_UP:
                savePosition();
                break;
        }

        return true;
    }

    /**
     * 儲存漂浮View的位置
     */
    private void savePosition()
    {
        LogUtil.log("SavePos: X" + mFloatView.getLeft() + " Y:" + mFloatView.getTop());
        SPUtil.setInt(mContext, Config.SP_KEY_INT_FLOW_VIEW_LOCATION_X, mLayoutParams.x);
        SPUtil.setInt(mContext, Config.SP_KEY_INT_FLOW_VIEW_LOCATION_Y, mLayoutParams.y);
    }


    /**
     * 取得當前手指滑動XY量(Layout)
     *
     * @param event
     * @return
     */
    private Vector2 getSlideScale(MotionEvent event)
    {
        float currentX = event.getRawX();
        float currentY = event.getRawY();

        float diffX = currentX - mX;
        float diffY = currentY - mY;

        int left = (int) diffX /*+ mFloatView.getLeft()*/;
        int top = (int) diffY /*+ mFloatView.getTop()*/;

        return new Vector2(left, top);
    }

    /**
     * 檢測當前View是否可以移動,防止View超出螢幕範圍
     *
     * @param viewPos  View的Layout left,top
     * @param screenWH 當前螢幕的長寬
     * @return 是否可以移動View
     */
    private boolean checkCanMove(Vector2 viewPos, Vector2 screenWH)
    {
        return !(viewPos.x < 0 || viewPos.x + mFloatView.getWidth() > screenWH.x ||
                 viewPos.y < 0 ||
                 viewPos.y + mFloatView.getHeight() + titleBarPixels > screenWH.y);
    }

    /**
     * 移動View到指定的位置
     *
     * @param slideXYScale layout中的 left,top
     * @param event        MotionEvent
     */
    private void moveView(Vector2 slideXYScale, MotionEvent event)
    {
        mLayoutParams.x += slideXYScale.x;
        mLayoutParams.y += slideXYScale.y;

        mLayoutParams.x = mLayoutParams.x <= 0 ? 0 : mLayoutParams.x;
        mLayoutParams.y = mLayoutParams.y <= 0 ? 0 : mLayoutParams.y;

        // 防止越界
        mLayoutParams.x = mLayoutParams.x >=
                          mWindowManager.getDefaultDisplay().getWidth() - mFloatView.getWidth() ?
                          mWindowManager.getDefaultDisplay().getWidth() - mFloatView.getWidth() :
                          mLayoutParams.x;
        mLayoutParams.y = mLayoutParams.y >=
                          mWindowManager.getDefaultDisplay().getHeight() - mFloatView.getHeight() -
                          48 ?
                          mWindowManager.getDefaultDisplay().getHeight() - mFloatView.getHeight() -
                          48 : mLayoutParams.y;

        LogUtil.log("X:" + mLayoutParams.x + " Y:" + mLayoutParams.y);
        mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

        mX = event.getRawX();
        mY = event.getRawY();
    }
}
