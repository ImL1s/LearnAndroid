package com.learn.iml1s.a08news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.learn.iml1s.a08news.com.learn.iml1s.a08news.bean.NewsBean;
import com.learn.iml1s.a08news.com.learn.iml1s.a08news.com.learn.iml1s.a08news.adapter.MyAdapter;
import com.learn.iml1s.a08news.com.learn.iml1s.a08news.util.NewsUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(new MyAdapter(NewsUtils.getAllNews(this),this));
        listView.setOnItemClickListener(this);
    }


    // ListView內容物按鍵監聽
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ArrayList<NewsBean> beans = NewsUtils.getAllNews(this);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(beans.get(position).getNews_url()));
        startActivity(i);
    }
}
