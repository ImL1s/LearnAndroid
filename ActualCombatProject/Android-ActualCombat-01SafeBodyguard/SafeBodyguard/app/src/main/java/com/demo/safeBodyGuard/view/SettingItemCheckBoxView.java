package com.demo.safeBodyGuard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.model.SettingItemInfo;

/**
 * Created by iml1s-macpro on 2017/1/5.
 */

public class SettingItemCheckBoxView extends AbsSettingItemView
{
    public static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.demo.safeBodyGuard";

    private SettingItemCheckBoxView self;
    private CheckBox                cb_box;


    public SettingItemCheckBoxView(Context context)
    {
        this(context,null);
    }

    public SettingItemCheckBoxView(Context context, AttributeSet attrs)
    {
        this(context,attrs,0);
    }

    public SettingItemCheckBoxView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        self = this;

        View.inflate(context, R.layout.setting_item_view,self);
        tv_title = (TextView) findViewById(R.id.tv_setting_item_title);
        tv_desc = (TextView) findViewById(R.id.tv_setting_item_desc);
        cb_box = (CheckBox) findViewById(R.id.cb_setting_item);

        cb_box.setClickable(false);

        // 使用XML方式初始化
        if(attrs != null)
        {
            itemInfo = new SettingItemInfo
                    (
                    attrs.getAttributeValue(NAME_SPACE,"title"),
                    attrs.getAttributeValue(NAME_SPACE,"descOn"),
                    attrs.getAttributeValue(NAME_SPACE,"descOff")
                    );

            setTitle(itemInfo.getTitle());
            setDesc(itemInfo.getDescOff());
        }
    }


    // 點擊後切換CheckBox和Desc顯示內容
    @Override
    protected void onClick()
    {
        cb_box.setChecked(!cb_box.isChecked());
        tv_desc.setText(cb_box.isChecked()?itemInfo.getDescOn():itemInfo.getDescOff());
    }


    public void setChecked(boolean checked)
    {
        this.cb_box.setChecked(checked);
    }

    public void setCheckedAndText(boolean checked)
    {
        setChecked(checked);
        tv_desc.setText(cb_box.isChecked()?itemInfo.getDescOn():itemInfo.getDescOff());
    }

    public boolean isChecked()
    {
        return cb_box.isChecked();
    }

}
