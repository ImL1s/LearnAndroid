package com.learn.aa223.a03news_network;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.learn.aa223.a03news_network.Adapter.NewsAdapter;
import com.learn.aa223.a03news_network.Dao.NewsSQLiteUtils;
import com.learn.aa223.a03news_network.Utils.NewsUtil;
import com.learn.aa223.a03news_network.bean.NewsBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    private ListView listView;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            ArrayList<NewsBean> beanArrayList = (ArrayList<NewsBean>) msg.obj;
            Log.d("debug","HandleMessage");

            if(beanArrayList == null || beanArrayList.size() == 0 ) return;
            listView.setAdapter(new NewsAdapter(MainActivity.this,beanArrayList));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
//        loadCache();
        loadNetwork();
    }

    private void initView()
    {
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
    }

    // 先讀取資料庫緩存
    private void loadCache()
    {
        ArrayList<NewsBean> newsBeanArrayList = NewsUtil.getAllNewsFromDatabase(this);
        NewsAdapter adapter = new NewsAdapter(this,newsBeanArrayList);
        listView.setAdapter(adapter);
    }

    private void loadNetwork()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();

                ArrayList<NewsBean> newsArray = NewsUtil.getAllNewsFromNetwork(getString(R.string.serRemote),MainActivity.this);
                Message msg = Message.obtain();
                msg.obj = newsArray;
                handler.sendMessage(msg);
            }
        }.start();
    }

    // listview的條目點擊時會調用該方法 parent:代表listView  view:點擊的條目上的那個view對像   position:條目的位置  id： 條目的id
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        NewsBean bean = (NewsBean) adapterView.getItemAtPosition(i);

        if(bean.getNews_url() != null && bean.getNews_url() != "" && bean.getNews_url().length() > 5    )
        {
            Intent t = new Intent(Intent.ACTION_VIEW);
            t.setData(Uri.parse(bean.getNews_url()));
            startActivity(t);
        }
        else
        {
            Toast.makeText(this,"新聞數據源遺失..",Toast.LENGTH_SHORT).show();
        }

    }
}
