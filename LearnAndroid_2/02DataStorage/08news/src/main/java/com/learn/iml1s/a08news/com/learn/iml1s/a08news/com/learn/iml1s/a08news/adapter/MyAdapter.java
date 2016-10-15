package com.learn.iml1s.a08news.com.learn.iml1s.a08news.com.learn.iml1s.a08news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.iml1s.a08news.R;
import com.learn.iml1s.a08news.com.learn.iml1s.a08news.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by ImL1s on 2016/10/12.
 * 資料適配器
 */

public class MyAdapter extends BaseAdapter{

    private ArrayList<NewsBean> newsArray;
    private Context context;

    public MyAdapter(ArrayList<NewsBean> array,Context context){

        this.newsArray = array;
        this.context = context;
    }

    // 總共Row數
    @Override
    public int getCount() {
        return newsArray.size();
    }

    // 根據位置取得內容物
    @Override
    public Object getItem(int position) {
        return newsArray.get(position);
    }

    // 根據位置取得ItemID
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 取得內容物會隨著滾動更新
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout linearLayout;
        NewsBean bean = newsArray.get(position);

        if(convertView == null){
            linearLayout = (LinearLayout) View.inflate(context, R.layout.newsitem,null);

        }
        else {
            linearLayout = (LinearLayout) convertView;
        }

        ((TextView)linearLayout.findViewById(R.id.titleTxt)).setText(bean.getTitle());
        ((TextView)linearLayout.findViewById(R.id.contentTxt)).setText(bean.getDesc());
        ((ImageView)linearLayout.findViewById(R.id.imageView)).setImageDrawable(bean.getIcon());

        return linearLayout;
    }
}