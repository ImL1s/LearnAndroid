package com.learn.aa223.a03news_network.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.aa223.a03news_network.View.NetWorkImageView;
import com.learn.aa223.a03news_network.R;
import com.learn.aa223.a03news_network.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by iml1s-macpro on 2016/10/25.
 */

public class NewsAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<NewsBean> newsArray = new ArrayList<>();

    public NewsAdapter(Context context,ArrayList<NewsBean> newsArray)
    {
        this.context = context;
        this.newsArray = newsArray;
    }

    @Override
    public int getCount()
    {
        return newsArray.size();
    }

    @Override
    public Object getItem(int i)
    {
        return newsArray.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View cacheView, ViewGroup viewGroup)
    {

        NewsBean bean = newsArray.get(i);
        LinearLayout linearLayout = (LinearLayout) View.inflate(context,R.layout.listview_item_news,null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.newsTitleTxt);
        TextView descText = (TextView) linearLayout.findViewById(R.id.newsContentTxt);
        TextView commentText = (TextView) linearLayout.findViewById(R.id.comment);
        TextView typeText = (TextView) linearLayout.findViewById(R.id.type);
        NetWorkImageView imageView = (NetWorkImageView) linearLayout.findViewById(R.id.headImageView);


        textView.setText(bean.getTitle());
        imageView.setImageURI(bean.getIcon_url());
        descText.setText(bean.getDesc());
        descText.setMaxLines(2);
        commentText.setText("評論:" + bean.getCommentStr());

        String typeStr = "";
        switch (bean.getType())
        {
            case 0:
                typeStr = "頭條";
                break;
            case 1:
                typeStr = "娛樂";
                break;
            case 2:
                typeStr = "體育";
                break;
            case 3:
                typeStr = "科技";
                break;
        }

        typeText.setText(typeStr);

        return linearLayout;


    }
}
