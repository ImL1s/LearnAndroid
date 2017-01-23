package com.demo.safeBodyGuard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.model.SettingItemInfo;

/**
 * Created by iml1s-macpro on 2017/1/23.
 */
public class SettingItemArrowsView extends AbsSettingItemView
{
    private View v_arrows;

    @Override
    protected void onClick()
    {

    }

    public SettingItemArrowsView(Context context)
    {
        this(context, null);
    }

    public SettingItemArrowsView(Context context, AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    public SettingItemArrowsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.setting_item_view, this);
        tv_title = (TextView) findViewById(R.id.tv_setting_item_title);
        tv_desc = (TextView) findViewById(R.id.tv_setting_item_desc);
        v_arrows = findViewById(R.id.v_setting_item_arrows);


        // 使用XML方式初始化
        if (attrs != null)
        {
            itemInfo = new SettingItemInfo(attrs.getAttributeValue(NAME_SPACE, "title"), attrs.getAttributeValue(NAME_SPACE, "descOn"), attrs.getAttributeValue(NAME_SPACE, "descOff"));

            setTitle(itemInfo.getTitle());
            setDesc(itemInfo.getDefaultDesc());
        }
    }

    public View getArrowsView()
    {
        return v_arrows;
    }

    public void setArrowsView(View arrowsView)
    {
        this.v_arrows = v_arrows;
    }
}
