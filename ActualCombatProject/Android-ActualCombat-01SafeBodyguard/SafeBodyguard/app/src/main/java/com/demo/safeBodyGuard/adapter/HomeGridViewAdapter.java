package com.demo.safeBodyGuard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;

/**
 * Created by iml1s-macpro on 2017/1/10.
 */

public class HomeGridViewAdapter extends BaseAdapter
{
    private Context context;
    private int[] mDrawableIds;
    private String[] mTitleStrings;

    public HomeGridViewAdapter(Context context,int[] drawableIds, String[] titleStrings)
    {
        super();
        this.context = context;
        this.mDrawableIds = drawableIds;
        this.mTitleStrings = titleStrings;
    }

    @Override
    public int getCount()
    {
        return 9;
    }

    @Override
    public Object getItem(int position)
    {
        return mTitleStrings[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View item = View.inflate(context,R.layout.grid_view_home_item,null);
        ImageView imageView = (ImageView) item.findViewById(R.id.gv_home_time_iv_icon);
        TextView textView = (TextView) item.findViewById(R.id.gv_home_time_tv_title);

        imageView.setBackgroundResource(mDrawableIds[position]);
        textView.setText(mTitleStrings[position]);
        textView.setTextColor(0xFF333333);

        return item;
    }
}
