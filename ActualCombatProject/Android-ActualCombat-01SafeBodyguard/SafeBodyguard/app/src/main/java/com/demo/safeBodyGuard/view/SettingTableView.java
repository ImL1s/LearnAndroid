package com.demo.safeBodyGuard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.demo.safeBodyGuard.adapter.SettingTableAdapter;
import com.demo.safeBodyGuard.model.SettingItemInfo;

import java.util.ArrayList;

/**
 * Created by iml1s-macpro on 2017/1/5.
 */

public class SettingTableView extends ScrollView implements View.OnClickListener
{
    //    xmlns:mobilesafe="http://schemas.android.com/apk/res/com.itheima.mobilesafe74"
    public static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.demo.SafeBodyguard";

    private SettingTableAdapter mAdapter = null;

    private Context mContext = null;

    private ArrayList<AbsSettingItemView> settingItemViews = null;

    private AdapterView.OnItemClickListener onItemClickListener = null;

    public SettingTableView(Context context)
    {
        this(context, null);
    }

    public SettingTableView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SettingTableView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) return;
        mContext = context;
    }

    public void setAdapter(SettingTableAdapter adapter)
    {
        mAdapter = adapter;
        render();
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener listener)
    {
        onItemClickListener = listener;
    }

    public AbsSettingItemView getItem(int position)
    {
        if (this.settingItemViews != null) return settingItemViews.get(position);

        return null;
    }

    public <T> T getItem(T t, int position)
    {
        if (this.settingItemViews != null && t.getClass().isInstance(AbsSettingItemView.class))
        {
            return (T) settingItemViews.get(position);
        }

        return null;
    }

    private void render()
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        settingItemViews = new ArrayList<>();

        for (int i = 0; i < mAdapter.getCount(); i++)
        {
            SettingItemInfo itemInfo = mAdapter.getItemInfo(i);

            AbsSettingItemView stiView = null;

            if (itemInfo.getItemType() == SettingItemInfo.ITEM_TYPE_CHECK_BOX)
            {
                stiView = new SettingItemCheckBoxView(mContext);
                stiView.setDesc(itemInfo.getDescOff());
            }
            else if (itemInfo.getItemType() == SettingItemInfo.ITEM_TYPE_CHECK_ARROWS)
            {
                stiView = new SettingItemArrowsView(mContext);
                stiView.setDesc(itemInfo.getDefaultDesc());
            }

            stiView.setItemInfo(itemInfo);
            stiView.setTitle(itemInfo.getTitle());
            stiView.setPosition(i);

            linearLayout.addView(stiView);
            settingItemViews.add(stiView);

            // 是否有單獨為某一個Item設定點擊事件
            if (itemInfo.getOnClickListener() != null)
                stiView.setOnClickListener(itemInfo.getOnClickListener());
            else stiView.setOnClickListener(this);
        }

        this.addView(linearLayout);
    }

    @Override
    public void onClick(View v)
    {
        SettingItemCheckBoxView stiView = (SettingItemCheckBoxView) v;

        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, stiView, stiView.getPosition(), -1);
    }
}
