package com.learn.iml1s.a08news.com.learn.iml1s.a08news.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by ImL1s on 2016/10/12.
 */

public class NewsBean {

    private String title;
    private String desc;
    private Drawable icon;
    private String news_url;

    public NewsBean(){}

    public NewsBean(String title,String desc,Drawable icon,String news_url){

        this.title = title;
        this.desc = desc;
        this.icon = icon;
        this.news_url = news_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }
}
