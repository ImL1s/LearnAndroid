package com.learn.iml1s.a08news.com.learn.iml1s.a08news.util;

import android.content.Context;

import com.learn.iml1s.a08news.R;
import com.learn.iml1s.a08news.com.learn.iml1s.a08news.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by ImL1s on 2016/10/12.
 *
 * 取得News數據工具
 */

public class NewsUtils {

    private static ArrayList<NewsBean> newsBeanArray = null;

    // 取得News數據
    public static ArrayList<NewsBean> getAllNews(Context context){

        if(newsBeanArray == null) {

            newsBeanArray = new ArrayList<>();

            for (int i = 0; i < 200; i++) {

                NewsBean bean1 = new NewsBean("林書豪不在就崩盤 人力捉襟見肘讓籃網頭痛",
                        "籃網隊出戰熱火隊一戰，證明了一件事情：只要林書豪（Jeremy Lin）不在場上控球，進攻系統將完全瓦解。可以想見正式球季開始後，林書豪的上場時間如何拿捏將會成為籃網教練艾特金森（Kenny Atkinson）的一大難題。"
                        , context.getResources().getDrawable(R.mipmap.ic_launcher), "http://www.google.com");

                NewsBean bean2 = new NewsBean("鼓勵施打流感疫苗 特製限量乖乖防疫包",
                        "鼓勵民眾打流感疫苗，衛福部疾管署除設計多款長輩圖宣傳，也和餅乾公司合作推出「乖乖打疫苗」包，提醒民眾打疫苗，讓病毒乖乖不發作。"
                        , context.getResources().getDrawable(R.mipmap.ic_gg), "http://www.cna.com.tw/news/firstnews/201610110238-1.aspx");

                newsBeanArray.add(bean1);
                newsBeanArray.add(bean2);
            }
        }

        return newsBeanArray;
    }
}
