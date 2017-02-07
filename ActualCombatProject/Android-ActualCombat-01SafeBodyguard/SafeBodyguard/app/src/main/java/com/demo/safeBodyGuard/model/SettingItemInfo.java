package com.demo.safeBodyGuard.model;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by iml1s-macpro on 2017/1/5.
 */

public class SettingItemInfo
{
    /**
     * 擁有CheckBox的Item
     */
    public final static int ITEM_TYPE_CHECK_BOX    = 0x0;

    /**
     * 擁有箭頭圖示的Item
     */
    public final static int ITEM_TYPE_CHECK_ARROWS = 0x1;

    private String title;
    private String descOn;
    private String descOff;
    private final String defaultDesc;
    private int itemType = ITEM_TYPE_CHECK_BOX;

    private View.OnClickListener onClickListener;

    /**
     * 本方法將構建一個擁有箭頭的Item
     * @param title  顯示的文字標題
     * @param descOn 打勾時顯示的文字
     * @param descOn 沒打勾時顯示的文字
     */
    public SettingItemInfo(String title, String descOn, String descOff)
    {
        this(title, descOn, descOff, null, null, ITEM_TYPE_CHECK_BOX);
    }

    /**
     * 本方法將構建一個擁有CheckBox的Item
     * @param title  顯示的文字標題
     * @param defaultDesc 默認顯示的文字
     */
    public SettingItemInfo(String title, String defaultDesc)
    {
        this(title, null, null, defaultDesc, null,ITEM_TYPE_CHECK_ARROWS);
    }

    public SettingItemInfo(String title, String descOn, String descOff, String defaultDesc, View.OnClickListener listener, int itemType)
    {
        this.title = title;
        this.descOn = descOn;
        this.descOff = descOff;
        this.onClickListener = listener;
        this.itemType = itemType;
        this.defaultDesc = defaultDesc;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescOff()
    {
        return descOff;
    }

    public void setDescOff(String descOff)
    {
        this.descOff = descOff;
    }

    public String getDescOn()
    {
        return descOn;
    }

    public void setDescOn(String descOn)
    {
        this.descOn = descOn;
    }

    public View.OnClickListener getOnClickListener()
    {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public int getItemType()
    {
        return itemType;
    }

    public void setItemType(int itemType)
    {
        this.itemType = itemType;
    }

    public String getDefaultDesc()
    {
        return defaultDesc;
    }
}
