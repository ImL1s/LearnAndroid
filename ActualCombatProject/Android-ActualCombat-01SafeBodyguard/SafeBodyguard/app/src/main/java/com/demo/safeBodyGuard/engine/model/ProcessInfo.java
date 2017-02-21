package com.demo.safeBodyGuard.engine.model;

import android.graphics.drawable.Drawable;

/**
 * Created by iml1s on 2017/2/19.
 */
public class ProcessInfo
{
    public String packageName;

    public Drawable icon;

    public boolean isSystem;

    /**
     * 佔用記憶體的大小(Bytes)
     */
    public long privateDirty;


    public String appName;

    public boolean isChecked;
}
