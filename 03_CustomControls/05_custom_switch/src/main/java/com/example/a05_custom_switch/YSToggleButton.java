package com.example.a05_custom_switch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aa223 on 2017/5/7 .
 */

public class YSToggleButton extends View {

    private Paint paint;
    private Bitmap bgBitmap;
    private Bitmap sliderBitmap;
    private boolean isOn = false;
    private float slider_x = 0;
    private State state = State.IDLE;

    private enum State {
        IDLE(0), TOUCHING(1);

        State(int i) {

        }
    }

    public YSToggleButton(Context context) {
        super(context);
    }

    public YSToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);
    }

    public YSToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(attrs);
    }

    public void setAttributes(AttributeSet attributes) {
        String namespace = "http://schemas.android.com/apk/com.example.a05_custom_switch";
        int slider_src_res_id = attributes.getAttributeResourceValue(namespace, "slide_src", -1);
        sliderBitmap = BitmapFactory.decodeResource(getResources(),slider_src_res_id);
    }

    public void setBackground(int backgroundResID) {
        this.bgBitmap = BitmapFactory.decodeResource(getResources(), backgroundResID);
    }

    public void setSlider(int sliderResID) {
        this.sliderBitmap = BitmapFactory.decodeResource(getResources(), sliderResID);
    }

    // region override method
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bgBitmap.getWidth(), bgBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (state == State.IDLE) {
            canvas.drawBitmap(bgBitmap, 0, 0, paint);
            canvas.drawBitmap(sliderBitmap, isOn ? (getWidth() - sliderBitmap.getWidth()) : 0, 0, paint);

        } else if (state == State.TOUCHING) {
            canvas.drawBitmap(bgBitmap, 0, 0, paint);
            float drawX = (slider_x - sliderBitmap.getWidth() * 0.5f);
            float maxX = getWidth() - sliderBitmap.getWidth();
            canvas.drawBitmap(sliderBitmap,
                    (drawX < 0) ? 0 : (drawX + sliderBitmap.getWidth() > getWidth() ? maxX : drawX),
                    0,
                    paint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return true;

            case MotionEvent.ACTION_UP:
                boolean isSwitchOn = !(slider_x < getWidth() * 0.5f);
                setSwitch(isSwitchOn);
                state = State.IDLE;
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                slider_x = event.getX();
                state = State.TOUCHING;
                invalidate();
                return true;
        }

        return false;
    }

    // endregion

    // region method
    private void init() {
        paint = new Paint();
    }

    public void setSwitch(boolean state) {
        if (!isOn == state) {
            isOn = state;
            invalidate();
        }
    }
    // endregion


}
