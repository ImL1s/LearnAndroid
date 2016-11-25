package com.learn.aa223.a03news_network.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by iml1s-macpro on 2016/10/25.
 */

public class NewsBean
{
    private int id;
    private String title;
    private String desc;
    private Drawable icon;
    private String icon_url;
    private String news_url;
    private String time;

    public String getCommentStr() {
        return commentStr;
    }

    public void setCommentStr(String commentStr) {
        this.commentStr = commentStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String commentStr;
    private int type;

    public NewsBean(){}

    public NewsBean(String title,String desc,Drawable icon,String news_url,String icon_url,String commentStr,int type)
    {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
        this.news_url = news_url;
        this.icon_url = icon_url;
        this.commentStr = commentStr;
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public Drawable getIcon()
    {
        return icon;
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }

    public String getNews_url()
    {
        return news_url;
    }

    public void setNews_url(String news_url)
    {
        this.news_url = news_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
