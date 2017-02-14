package com.demo.safeBodyGuard.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.model.AppInfo;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.engine.AppInfoEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppManagerActivity extends AppCompatActivity
{
    private ListView      mListView;
    private TextView      mTopTitleTextView;
    private List<AppInfo> mAllAppInfoList;
    private List<AppInfo> mSysAppInfoList;
    private List<AppInfo> mNotSysAppInfoList;

    private static Handler mHandler = new Handler()
    {
        private Context mContext;
        private ListView mListView = null;
        private TextView mTopTitleTv;

        @Override
        public void handleMessage(Message msg)
        {
            if (HandlerProtocol.ON_APP_INFO_LOADED == msg.what)
            {
                Object[] obj = (Object[]) msg.obj;
                List<AppInfo> allAppInfoList = (List<AppInfo>) obj[0];
                List<AppInfo> sysAppInfoList = (List<AppInfo>) obj[1];
                List<AppInfo> notSysAppInfoList = (List<AppInfo>) obj[2];
                mListView = (ListView) obj[3];
                mContext = (Context) obj[4];
                mTopTitleTv = (TextView) obj[5];

                Locale zhLocale = new Locale("zh");
                mTopTitleTv.setText(String.format(zhLocale, "一般應用(%d)", notSysAppInfoList.size()));

                AppInfoAdapter adapter =
                        new AppInfoAdapter(allAppInfoList, notSysAppInfoList, sysAppInfoList,
                                           mContext);
                mListView.setAdapter(adapter);
                mListView.setOnScrollListener(
                        new AppInfoListViewOnScrollListener(adapter, notSysAppInfoList,sysAppInfoList,mTopTitleTv));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        mListView = (ListView) findViewById(R.id.activity_app_manager_lv_app_info);
        mTopTitleTextView = (TextView) findViewById(R.id.activity_app_manager_tv_top_title);


        initTitle();
        initData();
    }

    private void initTitle()
    {
        String innerSize = Formatter.formatFileSize(this, getUsableSpace(
                Environment.getDataDirectory().getAbsolutePath()));

        String externalSize = Formatter.formatFileSize(this, getUsableSpace(
                Environment.getExternalStorageDirectory().getAbsolutePath()));

        Toast.makeText(this, innerSize, Toast.LENGTH_LONG).show();

        TextView tv_innerStorage =
                (TextView) findViewById(R.id.activity_app_manager_tv_inner_storage);

        TextView tv_externalStorage =
                (TextView) findViewById(R.id.activity_app_manager_tv_external_storage);

        tv_innerStorage.setText("內部存儲" + innerSize);
        tv_externalStorage.setText("外部存儲" + externalSize);
    }

    private void initData()
    {
        new Thread(() -> {

            mAllAppInfoList = AppInfoEngine.getAppInfos(getApplication());
            mSysAppInfoList = new ArrayList<>();
            mNotSysAppInfoList = new ArrayList<>();

            for (AppInfo appInfo : mAllAppInfoList)
            {
                List<AppInfo> list = appInfo.isSystem ? mSysAppInfoList : mNotSysAppInfoList;
                list.add(appInfo);
            }

            Message msg = Message.obtain();
            msg.what = HandlerProtocol.ON_APP_INFO_LOADED;
            msg.obj = new Object[]{mAllAppInfoList, mSysAppInfoList, mNotSysAppInfoList, mListView,
                                   this, mTopTitleTextView};
            msg.setTarget(mHandler);
            msg.sendToTarget();

        }).run();
    }

    /**
     * 取得指定目錄的剩餘可用空間
     *
     * @return 剩餘可用空間(Byte)
     */
    private long getUsableSpace(String path)
    {
        StatFs statFs = new StatFs(path);
        int blockCount = statFs.getAvailableBlocks();
        int blockSize = statFs.getBlockSize();

        return blockCount * blockSize;
    }

    /**
     * App Info Adapter
     */
    static class AppInfoAdapter extends BaseAdapter
    {
        private static final int ITEM_TYPE_TITLE    = 0;
        private static final int ITEM_TYPE_APP_INFO = 1;

        private Context       mContext;
        private List<AppInfo> mAllAppInfoList;
        private List<AppInfo> mNotSysAppInfoList;
        private List<AppInfo> mSysAppInfoList;

        AppInfoAdapter(List<AppInfo> mAllAppInfoList, List<AppInfo> mNotSysAppInfoList,
                       List<AppInfo> mSysAppInfoList, Context context)
        {
            this.mAllAppInfoList = mAllAppInfoList;
            this.mNotSysAppInfoList = mNotSysAppInfoList;
            this.mSysAppInfoList = mSysAppInfoList;
            this.mContext = context;
        }

        @Override
        public int getViewTypeCount()
        {
            return super.getViewTypeCount() + 1;
        }

        /**
         * @param position
         * @return 0 = 標題欄， 1 = App欄位
         */
        @Override
        public int getItemViewType(int position)
        {
            return (position == 0 || position == mNotSysAppInfoList.size() + 1) ? ITEM_TYPE_TITLE :
                   ITEM_TYPE_APP_INFO;
        }

        @Override
        public int getCount()
        {
            return mAllAppInfoList.size() + 2;
        }

        @Override
        public Object getItem(int position)
        {
            // 如果是標題欄
            if (position == 0 || position == mNotSysAppInfoList.size() + 1)
            {
                return null;
            }

            //檢查是小於第二標題欄還是大於第二個標題欄
            //            return mAllAppInfoList.get(position < mNotSysAppInfoList.size() + 1 ? position - 1 : position + 2);

            int index;

            if (position < mNotSysAppInfoList.size() + 1)
            {
                index = position - 1;
                return mNotSysAppInfoList.get(index);
            }
            else
            {
                index = position - mNotSysAppInfoList.size() - 2;
                return mSysAppInfoList.get(index);
            }
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            int itemType = getItemViewType(position);

            if (itemType == ITEM_TYPE_TITLE)
            {
                // 顯示標題欄
                TextView title;

                if (convertView == null)
                {
                    convertView = View.inflate(mContext, R.layout.listview_app_item_title, null);
                    title = (TextView) convertView
                            .findViewById(R.id.list_view_app_item_title_tv_title);
                    convertView.setTag(new TitleItemHolder(title));
                }
                else
                {
                    title = ((TitleItemHolder) convertView.getTag()).tvTitle;
                }

                title.setText(position == 0 ? "一般應用:(" + mNotSysAppInfoList.size() + ")" :
                              "系統應用:(" + mSysAppInfoList.size() + ")");
            }
            else
            {
                // 顯示AppInfo欄
                TextView tvName;
                TextView tvPath;
                ImageView ivIcon;

                if (convertView == null)
                {
                    convertView = View.inflate(mContext, R.layout.listview_app_item, null);
                    tvName = (TextView) convertView.findViewById(R.id.list_view_app_item_tv_name);
                    tvPath = (TextView) convertView.findViewById(R.id.list_view_app_item_tv_path);
                    ivIcon = (ImageView) convertView.findViewById(R.id.list_view_app_item_iv_icon);
                    convertView.setTag(new ItemHolder(tvName, tvPath, ivIcon));
                }
                else
                {
                    ItemHolder holder = (ItemHolder) convertView.getTag();
                    tvName = holder.tvName;
                    tvPath = holder.tvPath;
                    ivIcon = holder.ivIcon;
                }

                AppInfo appInfo = (AppInfo) getItem(position);
                tvName.setText(appInfo.name != null ? appInfo.name : appInfo.packageName);
                tvPath.setText(appInfo.isSdCard ? "SD Card" : "Inner Storage");
                ivIcon.setBackgroundDrawable(appInfo.icon);
            }

            return convertView;
        }

        /**
         * Title Holder
         */
        class TitleItemHolder
        {
            public TextView tvTitle;

            TitleItemHolder(TextView title)
            {
                tvTitle = title;
            }
        }

        /**
         * Item Holder
         */
        class ItemHolder
        {
            TextView  tvName;
            TextView  tvPath;
            ImageView ivIcon;

            ItemHolder(TextView tvName, TextView tvPath, ImageView ivIcon)
            {
                this.tvName = tvName;
                this.tvPath = tvPath;
                this.ivIcon = ivIcon;
            }
        }
    }


    static class AppInfoListViewOnScrollListener implements AbsListView.OnScrollListener
    {
        private final AppInfoAdapter mAdapter;
        private final List<AppInfo> mNotSysAppInfoList;
        private final List<AppInfo> mSysAppInfoList;
        private final TextView mTopTitleTv;

        public AppInfoListViewOnScrollListener(AppInfoAdapter adapter,
                                               List<AppInfo> notSysAppInfoList,
                                               List<AppInfo> sysAppInfoList, TextView mTopTitleTv)
        {

            this.mAdapter = adapter;
            this.mNotSysAppInfoList = notSysAppInfoList;
            this.mSysAppInfoList = sysAppInfoList;
            this.mTopTitleTv = mTopTitleTv;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount)
        {
            if (firstVisibleItem < mNotSysAppInfoList.size() + 1)
            {
                Locale zhLocale = new Locale("zh");
                mTopTitleTv.setText(String.format(zhLocale, "一般應用(%d)", mNotSysAppInfoList.size()));
            }
            else
            {
                Locale zhLocale = new Locale("zh");
                mTopTitleTv.setText(String.format(zhLocale, "系統應用(%d)", mSysAppInfoList.size()));
            }
            //            Log.d("debug", "firstVisibleItem: " + firstVisibleItem + "/visibleItemCount: " +
            //                           visibleItemCount + "/ totalItemCount: " + totalItemCount);
        }
    }
}
