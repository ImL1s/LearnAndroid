package com.demo.safeBodyGuard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.safeBodyGuard.model.SettingItemInfo;

/**
 * Created by iml1s-macpro on 2017/1/23.
 */

public abstract class AbsSettingItemView extends RelativeLayout
{
    public static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.demo.safeBodyGuard";

    protected SettingItemInfo itemInfo = null;

    protected TextView tv_title;

    protected TextView tv_desc;

    protected int position = -1;


    // 重寫performClick方法,不使用setOnClickListener原因是有可能會被外界設定覆蓋掉
    @Override
    public boolean performClick()
    {
        onClick();
        return super.performClick();
    }

    protected abstract void onClick();


    public AbsSettingItemView(Context context)
    {
        super(context);
    }

    public AbsSettingItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AbsSettingItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        setClickable(true);
    }

    public void setTitle(CharSequence text)
    {
        this.tv_title.setText(text);
    }

    public void setItemInfo(SettingItemInfo info)
    {
        this.itemInfo = info;
    }

    public void setDesc(CharSequence text)
    {
        this.tv_desc.setText(text);
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }
}
