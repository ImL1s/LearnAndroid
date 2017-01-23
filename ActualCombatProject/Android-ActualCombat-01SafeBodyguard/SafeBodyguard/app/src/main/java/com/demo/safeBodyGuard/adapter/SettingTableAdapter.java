package com.demo.safeBodyGuard.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.demo.safeBodyGuard.model.SettingItemInfo;

/**
 * Created by iml1s-macpro on 2017/1/5.
 */

public abstract class SettingTableAdapter implements Adapter
{
    private SettingItemInfo[] itemInfoArray = null;

    public void setItemInfoArray(SettingItemInfo[] itemInfoArray)
    {
        this.itemInfoArray = itemInfoArray;
    }

    public SettingItemInfo[] getItemInfoArray()
    {
        return null;
    }

    public SettingItemInfo getItemInfo(int position)
    {
        return itemInfoArray[position];
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {

    }

    @Override
    public int getCount()
    {
        if(itemInfoArray == null || itemInfoArray.length == 0) itemInfoArray = getItemInfoArray();
        return itemInfoArray.length;
    }

    @Override
    public Object getItem(int position)
    {
        return itemInfoArray[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return itemInfoArray == null || itemInfoArray.length == 0;
    }
}
