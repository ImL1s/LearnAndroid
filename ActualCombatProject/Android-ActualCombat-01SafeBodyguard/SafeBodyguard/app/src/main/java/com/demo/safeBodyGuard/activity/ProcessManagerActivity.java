package com.demo.safeBodyGuard.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.engine.ProcessInfoProvider;
import com.demo.safeBodyGuard.engine.model.ProcessInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProcessManagerActivity extends AppCompatActivity
{

    private TextView tv_process_count;
    private TextView tv_process_memory;
    private ListView lv_process;

    private Handler mHandler;

    public ListView getProcessListView()
    {
        return lv_process;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        mHandler = new ProcessHandler(this);

        initUIRef();
        initData();
    }


    private void initUIRef()
    {
        tv_process_count = (TextView) findViewById(R.id.activity_process_tv_process_count);
        tv_process_memory = (TextView) findViewById(R.id.activity_process_tv_process_memory);
        lv_process = (ListView) findViewById(R.id.activity_process_lv_process_list);
    }

    private void initData()
    {
        tv_process_count.setText("進程數:" + ProcessInfoProvider.getProcessCount(this));

        long availMemoryByte = ProcessInfoProvider.getAvailSpace(this);
        long totalMemoryByte = ProcessInfoProvider.getTotalSpace(this);
        double availMemoryGB = ((double) availMemoryByte) / 1024 / 1024 / 1024;
        double totalMemoryGB = ((double) totalMemoryByte) / 1024 / 1024 / 1024;

        tv_process_memory.setText(String.format(Locale.CHINESE, "%.2fG(可用)/%.2fG(不可用)", availMemoryGB, totalMemoryGB));

        new Thread(() -> {

            List<ProcessInfo> mProcessInfoList = ProcessInfoProvider.getProcessInfo(this);
            Message msg = Message.obtain();
            msg.obj = mProcessInfoList;
            msg.what = HandlerProtocol.ON_PROCESS_INFO_LOADED;
            msg.setTarget(mHandler);
            msg.sendToTarget();

        }).start();
    }


    /**
     * Process Activity Handler
     */
    static class ProcessHandler extends Handler
    {
        private WeakReference<Context> mWeakCtxRef;

        private List<ProcessInfo> mCommonProcessInfoList;

        private List<ProcessInfo> mSystemProcessInfoList;

        private List<ProcessInfo> mProcessInfoList;

        public ProcessHandler(Context context)
        {
            mWeakCtxRef = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case HandlerProtocol.ON_PROCESS_INFO_LOADED:
                    handleProcessListLoaded(msg);
                    break;

                case HandlerProtocol.ON_PROCESS_INFO_SPLIT:
                    ProcessManagerActivity processManagerActivity = (ProcessManagerActivity) mWeakCtxRef.get();
                    processManagerActivity.getProcessListView().setAdapter(new ProcessInfoAdapter(processManagerActivity, mProcessInfoList, mCommonProcessInfoList, mSystemProcessInfoList));
                    break;
            }

        }

        private void handleProcessListLoaded(Message msg)
        {
            mProcessInfoList = (List<ProcessInfo>) msg.obj;

            processSplitOnNewThread(msg, mProcessInfoList);
        }

        private void processSplitOnNewThread(Message msg, List<ProcessInfo> processInfoList)
        {
            new Thread(() -> {
                splitProcessInfo(processInfoList);
                Message splitMsg = Message.obtain();
                splitMsg.what = HandlerProtocol.ON_PROCESS_INFO_SPLIT;
                msg.setTarget(this);
                msg.sendToTarget();

            }).start();
        }

        /**
         * 將一般process與系統process分開
         *
         * @param allProcessInfoList
         */
        private void splitProcessInfo(List<ProcessInfo> allProcessInfoList)
        {
            mCommonProcessInfoList = new ArrayList<>();
            mSystemProcessInfoList = new ArrayList<>();

            for (ProcessInfo info : allProcessInfoList)
            {
                List<ProcessInfo> temp = info.isSystem ? mSystemProcessInfoList : mCommonProcessInfoList;
                temp.add(info);
            }
        }
    }


    static class ProcessInfoAdapter extends BaseAdapter
    {
        private static final int VIEW_TYPE_TITLE = 259;

        private static final int VIEW_TYPE_INFO = 411;

        private WeakReference<Context> mWeakCtxRef;

        private List<ProcessInfo> mProcessInfoList;

        private List<ProcessInfo> mCommonProcessInfoList;

        private List<ProcessInfo> mSystemProcessInfoList;

        public ProcessInfoAdapter(Context context, List<ProcessInfo> mProcessInfoList, List<ProcessInfo> commonProcessInfoList, List<ProcessInfo> systemProcessInfoList)
        {
            mWeakCtxRef = new WeakReference<>(context);
            this.mProcessInfoList = systemProcessInfoList;
            this.mCommonProcessInfoList = commonProcessInfoList;
            this.mSystemProcessInfoList = systemProcessInfoList;
        }


        @Override
        public int getItemViewType(int position)
        {
            if (position == 0 || position == mCommonProcessInfoList.size() + 1)
                return VIEW_TYPE_TITLE;

            return VIEW_TYPE_INFO;
        }

        @Override
        public int getViewTypeCount()
        {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getCount()
        {
            return mProcessInfoList.size() + 2;
        }

        @Override
        public Object getItem(int position)
        {
            if (position == 0) return "一般應用";
            if (position == mCommonProcessInfoList.size() + 1) return "系統應用";

            if (position < mCommonProcessInfoList.size() + 1)
                return mCommonProcessInfoList.get(position - 1);

            if (position > mCommonProcessInfoList.size() + 1)
                return mSystemProcessInfoList.get(position - 2);

            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = null;

            if (getItemViewType(position) == VIEW_TYPE_TITLE)
            {
                view = showTitle(position, convertView, parent);
            }
            else
            {
                view = View.inflate(mWeakCtxRef.get(), R.layout.listview_app_item_title, null);
            }

            return view;
        }

        private View showTitle(int position, View convertView, ViewGroup parent)
        {
            TitleViewHolder holder;

            if(convertView != null)
            {
                holder = (TitleViewHolder) convertView.getTag();
            }
            else
            {
                holder = new TitleViewHolder();

                convertView = View.inflate(mWeakCtxRef.get(), R.layout.listview_app_item_title, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.list_view_app_item_title_tv_title);

                convertView.setTag(holder);
            }

            holder.tvTitle.setText(String.format(Locale.CHINESE, "%s(%d)",
                                                 getItem(position), position == 0 ? mCommonProcessInfoList.size() : mSystemProcessInfoList.size()));
            return convertView;
        }

        class TitleViewHolder
        {
            TextView tvTitle;
        }
    }
}
