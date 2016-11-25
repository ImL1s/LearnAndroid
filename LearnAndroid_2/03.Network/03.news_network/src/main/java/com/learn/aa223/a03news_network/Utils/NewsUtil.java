package com.learn.aa223.a03news_network.Utils;

import android.content.Context;
import android.util.Log;

import com.learn.aa223.a03news_network.Dao.NewsSQLiteUtils;
import com.learn.aa223.a03news_network.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by iml1s-macpro on 2016/10/25.
 */

public class NewsUtil
{


    // 從網路上取得資料
    public static ArrayList<NewsBean> getAllNewsFromNetwork(String str_serverURL,Context context)
    {
        try
        {
            URL url = new URL(str_serverURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1000 * 10);

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == 200)
            {
                InputStream stream = urlConnection.getInputStream();
                String data = StreamUtil.inputToString(stream);

                Log.d("debug","----------- SUCC ------------- :");
                Log.d("debug",data);

                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("newsArray");
                ArrayList<NewsBean> newsArray = new ArrayList<>();

                Log.d("debug","--------- Parse SUCC ---------");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    NewsBean bean = new NewsBean();
                    bean.setTitle(jo.getString("title"));
                    bean.setDesc(jo.getString("des"));
                    bean.setIcon_url(jo.getString("icon_url"));
                    bean.setNews_url(jo.getString("news_url"));
                    bean.setCommentStr(jo.getString("comment"));
                    bean.setType(Integer.valueOf(jo.getString("type")));

                    newsArray.add(bean);
                }

                // 緩存
                NewsSQLiteUtils.getInstance(context).ClearUp();
                NewsSQLiteUtils.getInstance(context).SaveCache(newsArray);

                return newsArray;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // 從資料庫取得資料
    public static ArrayList<NewsBean> getAllNewsFromDatabase(Context context)
    {
        return NewsSQLiteUtils.getInstance(context).getAll();
    }
}
