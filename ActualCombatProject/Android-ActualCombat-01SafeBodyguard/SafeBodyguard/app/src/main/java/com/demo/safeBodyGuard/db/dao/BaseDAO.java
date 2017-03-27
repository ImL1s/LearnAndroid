package com.demo.safeBodyGuard.db.dao;

import android.content.Context;

import java.util.List;

/**
 * Created by iml1s on 2017/3/27.
 */

// TODO 未完成,使用反射調用帶參數的構造方法,詳解http://xiaohuafyle.iteye.com/blog/1607258
public abstract class BaseDAO<T>
{
    private static final Object LOCK = new Object();

    private static BaseDAO mInstance;

    protected Context mContext = null;

    public static BaseDAO getInstance(Context context,Class<?> type)
    {
        if(!type.isInstance(BaseDAO.class)) return null;

        synchronized (LOCK)
        {
            if (mInstance == null)
            {
                try
                {
                    mInstance = (BaseDAO) type.newInstance();
                    mInstance.mContext = context;
                }
                catch (InstantiationException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return mInstance;
    }

    public abstract List<T> selectAll();

    public abstract T select(int id);

}
