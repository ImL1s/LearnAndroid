package com.demo.safeBodyGuard.Listener;

import android.content.Context;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.model.Vector2;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.SPUtil;


/**
 * Created by ImL1s on 2017/1/25.
 *
 * DESC:
 */

public class OnPhoneAddressPosSettingTouchListener implements View.OnTouchListener
{
    /**
     * 頂部TitleBar的長度,不同解析度大小不一樣
     */
    private static int titleBarPixels = 48;

    private float         mX;
    private float         mY;
    private TextView      mFloatView;
    private Button        mUpBtn;
    private Button        mDownBtn;
    private WindowManager mWindowManager;
    private Context       mContext;
    private int           originalHeight;
    private int           originalWidth;

    public OnPhoneAddressPosSettingTouchListener(Context context, TextView floatView, Button upBtn,
                                                 Button downBtn, WindowManager windowsManager,
                                                 int originalHeight, int originalWidth)
    {
        this.mContext = context;
        this.mFloatView = floatView;
        this.mUpBtn = upBtn;
        this.mDownBtn = downBtn;
        this.mWindowManager = windowsManager;
        this.originalHeight = originalHeight;
        this.originalWidth = originalWidth;
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

                int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
                int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
                Vector2 screenWH = new Vector2(screenWidth, screenHeight);

                Vector2 nextPos = getNextPos(event);

                if (!checkCanMove(nextPos, screenWH))
                    return true;

                moveView(nextPos, event);
                showBtn(nextPos, screenWH);

                break;

            case MotionEvent.ACTION_UP:
                savePosition();
                break;
        }

        return false;
    }

    /**
     * 儲存漂浮View的位置
     */
    private void savePosition()
    {
        SPUtil.setInt(mContext, Config.SP_KEY_INT_FLOW_VIEW_LOCATION_X, mFloatView.getLeft());
        SPUtil.setInt(mContext, Config.SP_KEY_INT_FLOW_VIEW_LOCATION_Y, mFloatView.getTop());
    }

    /**
     * 根據當前View的位置,顯示上下按鈕
     *
     * @param viewPos
     */
    public void showBtn(Vector2 viewPos)
    {
        Display display = mWindowManager.getDefaultDisplay();
        Vector2 screenWH = new Vector2(display.getWidth(), display.getHeight());

        showBtn(viewPos, screenWH);
    }

    /**
     * 根據當前View的位置,顯示上下按鈕
     *
     * @param viewPos
     * @param screenWH
     */
    private void showBtn(Vector2 viewPos, Vector2 screenWH)
    {
        boolean showUpBtn = viewPos.y > screenWH.y * 0.5;
        mUpBtn.setVisibility(showUpBtn ? View.VISIBLE : View.INVISIBLE);
        mDownBtn.setVisibility(showUpBtn ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * 取得當前手指滑動到的位置(Layout)
     *
     * @param event
     * @return
     */
    private Vector2 getNextPos(MotionEvent event)
    {
        float currentX = event.getRawX();
        float currentY = event.getRawY();

        float diffX = currentX - mX;
        float diffY = currentY - mY;

        int left = (int) diffX + mFloatView.getLeft();
        int top = (int) diffY + mFloatView.getTop();

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
     * @param layoutPos layout中的 left,top
     * @param event     MotionEvent
     */
    private void moveView(Vector2 layoutPos, MotionEvent event)
    {
        int left = layoutPos.x;
        int top = layoutPos.y;

        mFloatView.layout(left, top, left + mFloatView.getWidth(), top + mFloatView.getHeight());

        mX = event.getRawX();
        mY = event.getRawY();
    }

    /**
     * 另外一種MoveView實現
     *
     * @param event
     */
    private void moveView2(MotionEvent event)
    {
        int height = mWindowManager.getDefaultDisplay().getHeight();

        LogUtil.log("CurrentX:" + event.getRawX() + "/ CurrentY:" + event.getRawY());


        int l = (int) event.getRawX() - (int) (originalWidth * 0.5);
        int t = (int) event.getRawY() - titleBarPixels - (int) (originalHeight * 0.5);
        int r = (int) event.getRawX() + originalWidth - (int) (originalWidth * 0.5);
        int b = (int) event.getRawY() + originalHeight - titleBarPixels -
                (int) (originalHeight * 0.5);

        LogUtil.log("l:" + l + "t:" + t + "r:" + r + " b:" + b);


        mFloatView.layout(l, t, r, b);
    }
}
