package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.engine.ProcessInfoProvider;
import com.demo.safeBodyGuard.engine.model.ProcessInfo;
import com.demo.safeBodyGuard.utils.SPUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProcessManagerActivity extends AppCompatActivity
{
    private TextView tv_process_count;
    private TextView tv_process_memory;
    private ListView lv_process;

    private Handler  mHandler;
    private TextView tv_title;

    public ListView getProcessListView()
    {
        return lv_process;
    }

    public TextView getTvTitle()
    {
        return tv_title;
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
        tv_title = (TextView) findViewById(R.id.activity_process_manager_tv_title);

        findViewById(R.id.activity_process_btn_select_all).setOnClickListener(
                v -> mHandler.sendEmptyMessage(HandlerProtocol.ON_PROCESS_BTN_CLICK_ALL));

        findViewById(R.id.activity_process_btn_select_anti).setOnClickListener(
                v -> mHandler.sendEmptyMessage(HandlerProtocol.ON_PROCESS_BTN_CLICK_ALL_ANTI));

        findViewById(R.id.activity_process_btn_one_key_clear).setOnClickListener(
                v -> mHandler.sendEmptyMessage(HandlerProtocol.ON_PROCESS_BTN_CLICK_CLEAR));

        findViewById(R.id.activity_process_btn_setting).setOnClickListener(
                v -> mHandler.sendEmptyMessage(HandlerProtocol.ON_PROCESS_BTN_CLICK_SETTING));
    }

    private void initData()
    {
        tv_process_count.setText("進程數:" + ProcessInfoProvider.getProcessCount(this));

        long availMemoryByte = ProcessInfoProvider.getAvailSpace(this);
        long totalMemoryByte = ProcessInfoProvider.getTotalSpace(this);
        double availMemoryGB = ((double) availMemoryByte) / 1024 / 1024 / 1024;
        double totalMemoryGB = ((double) totalMemoryByte) / 1024 / 1024 / 1024;

        ProcessHandler processHandler = (ProcessHandler) mHandler;
        processHandler.setAvailMemoryByte(availMemoryByte);
        processHandler.setTotalMemoryByte(totalMemoryByte);

        setMemoryDisplay(availMemoryGB, totalMemoryGB);

        new Thread(() -> {

            List<ProcessInfo> mProcessInfoList = ProcessInfoProvider.getProcessInfo(this);
            sendMsgToCurrentHandler(HandlerProtocol.ON_PROCESS_INFO_LOADED, mProcessInfoList);

        }).start();
    }

    public void setMemoryDisplay(double availMemoryGB, double totalMemoryGB)
    {
        tv_process_memory.setText(
                String.format(Locale.CHINESE, "%.2fG(可用)/%.2fG(總共)", availMemoryGB, totalMemoryGB));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case ProcessManagerSettingActivity.REQUEST_CODE_SETUP:
                sendEmptyMsgToCurrentHandler(HandlerProtocol.ON_SETTING_SETUP);
                break;
        }
    }

    private void sendEmptyMsgToCurrentHandler(int what)
    {
        sendMsgToCurrentHandler(what, null);
    }


    private void sendMsgToCurrentHandler(int what, Object obj)
    {
        Message msg = Message.obtain();
        msg.obj = obj;
        msg.what = what;
        msg.setTarget(mHandler);
        msg.sendToTarget();
    }

    /**
     * Process Activity Handler
     */
    static class ProcessHandler extends Handler
    {
        private long mAvailMemoryByte;

        private long mTotalMemoryByte;

        private boolean iShowSysProcess = true;

        private WeakReference<Context> mWeakCtxRef;

        private List<ProcessInfo> mCommonProcessInfoList;

        private List<ProcessInfo> mSystemProcessInfoList;

        private List<ProcessInfo> mProcessInfoList;

        private ProcessInfoAdapter mProcessInfoAdapter;

        ProcessHandler(Context context)
        {
            mWeakCtxRef = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 運行中進程資料抓取完畢
                case HandlerProtocol.ON_PROCESS_INFO_LOADED:
                    handleProcessListLoaded(msg);
                    break;

                // 一般進程&系統進程區分完畢
                case HandlerProtocol.ON_PROCESS_INFO_SPLIT:
                    handleProcessInfoSplited();
                    break;

                // 點擊全選按鈕
                case HandlerProtocol.ON_PROCESS_BTN_CLICK_ALL:
                    handleProcessBtnClickAll();
                    break;

                // 點擊反選按鈕
                case HandlerProtocol.ON_PROCESS_BTN_CLICK_ALL_ANTI:
                    handleProcessBtnClickAllAnti();
                    break;

                // 點擊清除按鈕
                case HandlerProtocol.ON_PROCESS_BTN_CLICK_CLEAR:
                    handleProcessBtnClickClear();
                    break;

                // 點擊設定按鈕
                case HandlerProtocol.ON_PROCESS_BTN_CLICK_SETTING:
                    handleProcessBtnClickSetting();
                    break;

                // 點擊設定按鈕
                case HandlerProtocol.ON_SETTING_SETUP:
                    handleSettingSetUp();
                    break;
            }

        }

        /**
         * 取得持久化存儲的的isShowSysProcess,並且放到Adapter中.
         */
        private void syncIsShowSysProcess()
        {
            iShowSysProcess = SPUtil.getBool(mWeakCtxRef.get(),
                                             Config.SP_KEY_BOOL_PROCESS_MANAGER_ACT_SYSTEM_SHOW,
                                             true);

            mProcessInfoAdapter.setIShowSysProcess(iShowSysProcess);
        }

        private void handleSettingSetUp()
        {
            syncIsShowSysProcess();
            mProcessInfoAdapter.notifyDataSetChanged();
        }

        private void handleProcessBtnClickSetting()
        {
            ((Activity) mWeakCtxRef.get()).startActivityForResult(
                    new Intent(mWeakCtxRef.get(), ProcessManagerSettingActivity.class),
                    ProcessManagerSettingActivity.REQUEST_CODE_SETUP);
        }

        private void handleProcessBtnClickClear()
        {
            List<ProcessInfo> killingProcessList = new ArrayList<>();

            for (ProcessInfo info : mProcessInfoList)
            {
                if (info.isChecked)
                {
                    killingProcessList.add(info);
                }
            }

            long releaseMemory = 0;
            List<ProcessInfo> temp;

            for (ProcessInfo info : killingProcessList)
            {
                temp = mCommonProcessInfoList.contains(info) ? mCommonProcessInfoList :
                       mSystemProcessInfoList;

                temp.remove(info);
                releaseMemory += info.privateDirty;
                ProcessInfoProvider.killProcess(mWeakCtxRef.get(), info);
            }

            mAvailMemoryByte += releaseMemory;

            ((ProcessManagerActivity) mWeakCtxRef.get())
                    .setMemoryDisplay(((double) mAvailMemoryByte) / 1024 / 1024 / 1024,
                                      ((double) mTotalMemoryByte) / 1024 / 1024 / 1024);

            mProcessInfoAdapter.notifyDataSetChanged();

        }

        private void handleProcessBtnClickAllAnti()
        {
            String currentPackageName = mWeakCtxRef.get().getPackageName();

            for (ProcessInfo info : mProcessInfoList)
            {
                if (info.packageName.equals(currentPackageName))
                    continue;

                info.isChecked = !info.isChecked;
            }
            mProcessInfoAdapter.notifyDataSetChanged();
        }

        private void handleProcessBtnClickAll()
        {
            String currentPackageName = mWeakCtxRef.get().getPackageName();

            for (ProcessInfo info : mProcessInfoList)
            {
                if (info.packageName.equals(currentPackageName))
                    continue;

                if (!info.isChecked)
                    info.isChecked = true;
            }

            mProcessInfoAdapter.notifyDataSetChanged();
        }

        private void handleProcessInfoSplited()
        {
            ProcessManagerActivity processManagerActivity =
                    (ProcessManagerActivity) mWeakCtxRef.get();

            mProcessInfoAdapter = new ProcessInfoAdapter(processManagerActivity, mProcessInfoList,
                                                         mCommonProcessInfoList,
                                                         mSystemProcessInfoList);

            syncIsShowSysProcess();

            processManagerActivity.getProcessListView().setAdapter(mProcessInfoAdapter);

            processManagerActivity.getProcessListView().setOnScrollListener(
                    new ProcessInfoListScrollListener((ProcessManagerActivity) mWeakCtxRef.get(),
                                                      mCommonProcessInfoList,
                                                      mSystemProcessInfoList));

            processManagerActivity.getProcessListView().setOnItemClickListener(
                    new ProcessInfoListOnItemClick(mProcessInfoAdapter, mWeakCtxRef.get()));
        }

        private void handleProcessListLoaded(Message msg)
        {
            mProcessInfoList = (List<ProcessInfo>) msg.obj;

            processSplitOnNewThread(msg, mProcessInfoList);
        }

        /**
         * 在子執行緒中執行分隔系統與一般的ProcessInfo
         *
         * @param msg
         * @param processInfoList
         */
        private void processSplitOnNewThread(Message msg, List<ProcessInfo> processInfoList)
        {
            new Thread(() -> {
                splitProcessInfo(processInfoList);
                Message splitMsg = Message.obtain();
                splitMsg.what = HandlerProtocol.ON_PROCESS_INFO_SPLIT;
                splitMsg.setTarget(ProcessHandler.this);
                splitMsg.sendToTarget();

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
                List<ProcessInfo> temp =
                        info.isSystem ? mSystemProcessInfoList : mCommonProcessInfoList;
                temp.add(info);
            }
        }

        public long getAvailMemoryByte()
        {
            return mAvailMemoryByte;
        }

        public void setAvailMemoryByte(long availMemoryByte)
        {
            this.mAvailMemoryByte = availMemoryByte;
        }

        public long getTotalMemoryByte()
        {
            return mTotalMemoryByte;
        }

        public void setTotalMemoryByte(long totalMemoryByte)
        {
            this.mTotalMemoryByte = totalMemoryByte;
        }
    }


    /**
     * ProcessInfoAdapter
     */
    static class ProcessInfoAdapter extends BaseAdapter
    {
        static final int VIEW_TYPE_TITLE = 0;

        static final int VIEW_TYPE_INFO = 1;

        private boolean mIShowSysProcess;

        private WeakReference<Context> mWeakCtxRef;

        private List<ProcessInfo> mProcessInfoList;

        private List<ProcessInfo> mCommonProcessInfoList;

        private List<ProcessInfo> mSystemProcessInfoList;

        public ProcessInfoAdapter(Context context, List<ProcessInfo> processInfoList,
                                  List<ProcessInfo> commonProcessInfoList,
                                  List<ProcessInfo> systemProcessInfoList)
        {
            mWeakCtxRef = new WeakReference<>(context);
            this.mProcessInfoList = processInfoList;
            this.mCommonProcessInfoList = commonProcessInfoList;
            this.mSystemProcessInfoList = systemProcessInfoList;
        }


        @Override
        public int getItemViewType(int position)
        {
            Log.d("debug", "getItemViewType");
            if (position == 0 || position == mCommonProcessInfoList.size() + 1)
                return VIEW_TYPE_TITLE;

            return VIEW_TYPE_INFO;
        }

        @Override
        public int getViewTypeCount()
        {
            Log.d("debug", "getViewTypeCount");
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getCount()
        {
            Log.d("debug", "getCount");
            return isIShowSysProcess() ? mProcessInfoList.size() + 2 :
                   mCommonProcessInfoList.size() + 1;
        }

        @Override
        public Object getItem(int position)
        {
            Log.d("debug", "getItem");

            if (position == 0)
                return "一般應用(" + mCommonProcessInfoList.size() + ")";
            if (position == mCommonProcessInfoList.size() + 1)
                return "系統應用(" + mSystemProcessInfoList.size() + ")";

            if (mCommonProcessInfoList.size() == 0 || mSystemProcessInfoList.size() == 0)
                return "";

            if (position < mCommonProcessInfoList.size() + 1)
                return mCommonProcessInfoList.get(position - 1);

            if (position > mCommonProcessInfoList.size() + 1)
                return mSystemProcessInfoList.get(position - 2 - mCommonProcessInfoList.size());

            return "";
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view;

            if (getItemViewType(position) == VIEW_TYPE_TITLE)
            {
                view = showTitle(position, convertView, parent);
            }
            else
            {
                view = showInfo(position, convertView, parent);
            }

            return view;
        }

        private View initInfoView()
        {
            return View.inflate(mWeakCtxRef.get(), R.layout.listview_process_item, null);
        }

        private InfoViewHolder initInfoViewHolder(View convertView)
        {
            InfoViewHolder holder = new InfoViewHolder();

            holder.cb_box = (CheckBox) convertView.findViewById(R.id.list_view_process_cb_box);

            holder.tv_app_name =
                    (TextView) convertView.findViewById(R.id.list_view_process_tv_name);

            holder.tv_memory_info =
                    (TextView) convertView.findViewById(R.id.list_view_process_tv_memory_info);

            holder.tv_icon = (ImageView) convertView.findViewById(R.id.list_view_process_iv_icon);

            return holder;
        }

        private View showInfo(int position, View convertView, ViewGroup parent)
        {
            InfoViewHolder holder;

            if (convertView != null)
            {
                holder = (InfoViewHolder) convertView.getTag();

                if (holder == null)
                {
                    holder = initInfoViewHolder(convertView);
                }
            }
            else
            {
                convertView = initInfoView();
                holder = initInfoViewHolder(convertView);
            }

            Object temp = getItem(position);

            if (temp.getClass().equals(String.class))
                return new View(mWeakCtxRef.get());

            ProcessInfo processInfo = (ProcessInfo) getItem(position);

            holder.tv_app_name.setText(processInfo.appName);
            holder.tv_memory_info.setText(("使用的記憶體:" + processInfo.privateDirty));
            holder.tv_icon.setImageDrawable(processInfo.icon);
            holder.cb_box.setChecked(processInfo.isChecked);

            return convertView;
        }

        private View showTitle(int position, View convertView, ViewGroup parent)
        {
            TitleViewHolder holder;

            if (convertView != null)
            {
                holder = (TitleViewHolder) convertView.getTag();
            }
            else
            {
                holder = new TitleViewHolder();

                convertView =
                        View.inflate(mWeakCtxRef.get(), R.layout.listview_app_item_title, null);

                holder.tvTitle =
                        (TextView) convertView.findViewById(R.id.list_view_app_item_title_tv_title);

                convertView.setTag(holder);
            }

            //            holder.tvTitle.setText(String.format(Locale.CHINESE, "%s(%d)",
            //                                                 getItem(position) == null ? "" : getItem(position),
            //                                                 position == 0 ? mCommonProcessInfoList.size() :
            //                                                 mSystemProcessInfoList.size()));
            holder.tvTitle.setText(getItem(position).toString());

            return convertView;
        }

        public boolean isIShowSysProcess()
        {
            return mIShowSysProcess;
        }

        public void setIShowSysProcess(boolean mIShowSysProcess)
        {
            this.mIShowSysProcess = mIShowSysProcess;
        }

        /**
         * TitleViewHolder
         */
        class TitleViewHolder
        {
            TextView tvTitle;
        }


        /**
         * InfoViewHolder
         */
        class InfoViewHolder
        {
            ImageView tv_icon;
            TextView  tv_app_name;
            TextView  tv_memory_info;
            CheckBox  cb_box;
        }
    }

    /**
     * ProcessInfoListScrollListener
     */
    static class ProcessInfoListScrollListener implements AbsListView.OnScrollListener
    {
        private WeakReference<ProcessManagerActivity> mProcessActivityWeakRef;
        private List<ProcessInfo>                     mSystemProcessInfoList;
        private List<ProcessInfo>                     mCommonProcessInfoList;


        ProcessInfoListScrollListener(ProcessManagerActivity processActivityWeakRef,
                                      List<ProcessInfo> commonProcessInfoList,
                                      List<ProcessInfo> systemProcessInfoList)
        {
            this.mProcessActivityWeakRef = new WeakReference<>(processActivityWeakRef);
            this.mCommonProcessInfoList = commonProcessInfoList;
            this.mSystemProcessInfoList = systemProcessInfoList;
        }


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount)
        {
            if (firstVisibleItem < mCommonProcessInfoList.size() + 1)
            {
                mProcessActivityWeakRef.get().getTvTitle()
                        .setText("一般應用(" + mCommonProcessInfoList.size() + ")");
            }
            else
            {
                mProcessActivityWeakRef.get().getTvTitle()
                        .setText("系統應用(" + mSystemProcessInfoList.size() + ")");
            }
        }
    }

    static class ProcessInfoListOnItemClick implements AdapterView.OnItemClickListener
    {
        BaseAdapter mAdapter;

        Context mContext;

        ProcessInfoListOnItemClick(BaseAdapter adapter, Context context)
        {
            this.mAdapter = adapter;
            this.mContext = context;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            if (mAdapter.getItemViewType(position) == ProcessInfoAdapter.VIEW_TYPE_INFO)
            {
                if (mAdapter.getItem(position).getClass().equals(String.class))
                    return;

                ProcessInfo processInfo = (ProcessInfo) mAdapter.getItem(position);

                if (processInfo.packageName.equals(mContext.getPackageName()))
                    return;

                CheckBox cb_box = (CheckBox) view.findViewById(R.id.list_view_process_cb_box);

                cb_box.setChecked(!cb_box.isChecked());

                processInfo.isChecked = cb_box.isChecked();
            }
        }
    }
}
