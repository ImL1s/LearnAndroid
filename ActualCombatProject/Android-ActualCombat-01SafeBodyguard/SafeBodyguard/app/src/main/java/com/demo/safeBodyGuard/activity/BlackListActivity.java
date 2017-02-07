package com.demo.safeBodyGuard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.BlackListDAO;
import com.demo.safeBodyGuard.db.dao.model.BlackRoll;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.utils.LogUtil;
import com.demo.safeBodyGuard.utils.StringUtil;
import com.demo.safeBodyGuard.utils.ThreadUtil;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by ImL1s on 2017/2/3.
 * <p>
 * DESC:
 */

public class BlackListActivity extends Activity
{
    private ListView mBlackListView;
    private Button   mAddBtn;

    private static Handler mHandler = null;

    public ListView getBlackListView()
    {
        return mBlackListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        mHandler = new BlackListHandler(this);

        initUI();
        setListener();
        initData();
    }

    /**
     * 設定各種監聽
     */
    private void setListener()
    {
        mAddBtn.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alertDialog = builder.create();

            ViewGroup viewGroup =
                    (ViewGroup) View.inflate(this, R.layout.dialog_black_list_add, null);

            alertDialog.setView(viewGroup);
            alertDialog.setCancelable(true);

            // 被closure(閉包)參考的變數mode
            final int[] mode = new int[1];

            RadioGroup radioGroup =
                    ((RadioGroup) viewGroup.findViewById(R.id.dialog_black_list_add_rg_mode));
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

                switch (checkedId)
                {
                    case R.id.dialog_black_list_add_rb_msg:
                        mode[0] = 0;
                        break;

                    case R.id.dialog_black_list_add_rb_phone:
                        mode[0] = 1;
                        break;

                    case R.id.dialog_black_list_add_rb_all:
                        mode[0] = 2;
                        break;
                }

            });

            EditText editText =
                    (EditText) viewGroup.findViewById(R.id.dialog_black_list_add_et_phone_num);

            viewGroup.findViewById(R.id.dialog_black_list_add_btn_confirm)
                    .setOnClickListener((confirmBtn) -> {

                        LogUtil.log(editText.getText().toString() + " / " + mode[0]);

                        String phoneNum = editText.getText().toString();

                        if (StringUtil.isNullOrEmpty(phoneNum))
                        {
                            Toast.makeText(BlackListActivity.this, "請輸入號碼", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else
                        {
                            ThreadUtil.start(() -> {
                                BlackListDAO.getInstance(getApplicationContext())
                                        .insert(phoneNum, mode[0]);

                                Message msg = Message.obtain();
                                msg.what = HandlerProtocol.ON_BLACK_LIST_ADD;
                                msg.obj = new BlackRoll(Integer.MAX_VALUE, phoneNum, mode[0]);
                                msg.setTarget(mHandler);
                                msg.sendToTarget();
                            });

                            alertDialog.dismiss();
                        }
                    });

            viewGroup.findViewById(R.id.dialog_black_list_add_btn_cancel)
                    .setOnClickListener((cancelBtn) -> {
                        alertDialog.dismiss();
                    });

            alertDialog.show();
        });
    }


    /**
     * 初始化UI參考
     */
    private void initUI()
    {
        mBlackListView = (ListView) findViewById(R.id.activity_black_list_lv_black_list);
        mAddBtn = (Button) findViewById(R.id.activity_black_list_btn_add);
    }

    /**
     * 初始化資料
     */
    private void initData()
    {
        new Thread(() -> {
            List<BlackRoll> blackRolls =
                    BlackListDAO.getInstance(getApplicationContext()).selectRange(0);

            Message msg = Message.obtain();
            msg.what = HandlerProtocol.ON_BLACK_LIST_INIT;
            msg.obj = blackRolls;
            msg.setTarget(mHandler);
            msg.sendToTarget();

        }).start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Log.d("debug", "Touch");
            BlackListDAO.getInstance(getApplicationContext()).insert("110", 2);
        }

        return super.onTouchEvent(event);
    }


    /**
     * BlackListHandler
     */
    static class BlackListHandler extends Handler
    {
        private WeakReference<BlackListActivity> mWeak_activity;

        private List<BlackRoll> mBlackRolls = null;

        private BaseAdapter mAdapter;

        // 是否滑動到BlackListView底部，並且正在讀取資料
        private boolean mIsLoading = false;

        public BlackListHandler(BlackListActivity blackListActivity)
        {
            mWeak_activity = new WeakReference<>(blackListActivity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case HandlerProtocol.ON_BLACK_LIST_INIT:
                    handleInit(msg);
                    handleSelectAll(msg);
                    break;

                case HandlerProtocol.ON_BLACK_LIST_SELECTED_ALL:
                    handleSelectAll(msg);
                    break;

                case HandlerProtocol.ON_BLACK_LIST_ADD:
                    handleAdd(msg);
                    break;

                case HandlerProtocol.ON_BLACK_LIST_LOADED:
                    handleLoaded(msg);
                    break;
            }
        }

        private void handleLoaded(Message msg)
        {
            List<BlackRoll> blackRolls = (List<BlackRoll>) msg.obj;
            mBlackRolls.addAll(blackRolls);
            mAdapter.notifyDataSetChanged();
            mIsLoading = false;
        }

        private void handleInit(Message msg)
        {
            mAdapter = new BlackListAdapter();

            ListView listView = mWeak_activity.get().getBlackListView();

            listView.setOnScrollListener(new BlackListViewOnScrollListener());
        }

        private void handleSelectAll(Message msg)
        {
            mBlackRolls = (List<BlackRoll>) msg.obj;

            mWeak_activity.get().getBlackListView().setAdapter(mAdapter);
        }

        private void handleAdd(Message msg)
        {
            BlackRoll blackRoll = (BlackRoll) msg.obj;
            mBlackRolls.add(0, blackRoll);
            mAdapter.notifyDataSetChanged();
        }

        private Context getContext()
        {
            return mWeak_activity.get().getApplicationContext();
        }

        /**
         * BlackListViewHolder
         */
        class BlackListViewHolder
        {
            TextView    tv_phone;
            TextView    tv_mod;
            ImageButton ib_delete;
        }

        /**
         * BlackListAdapter
         */
        class BlackListAdapter extends BaseAdapter
        {
            @Override
            public int getCount()
            {
                return mBlackRolls.size();
            }

            @Override
            public Object getItem(int position)
            {
                return mBlackRolls.get(position);
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                BlackListViewHolder viewHolder;

                // 複用convertView
                if (convertView == null)
                {
                    convertView = View.inflate(mWeak_activity.get().getApplicationContext(),
                                               R.layout.listview_black_list_item, null);

                    viewHolder = new BlackListViewHolder();

                    viewHolder.tv_phone = (TextView) convertView
                            .findViewById(R.id.list_view_black_list_item_tv_phone);

                    viewHolder.tv_mod = (TextView) convertView
                            .findViewById(R.id.list_view_black_list_item_tv_mode);

                    viewHolder.ib_delete = (ImageButton) convertView
                            .findViewById(R.id.list_view_black_list_item_btn_delete);

                    convertView.setTag(viewHolder);
                }
                else
                {
                    viewHolder = (BlackListViewHolder) convertView.getTag();
                }
 
                BlackRoll blackRoll = mBlackRolls.get(position);

                viewHolder.tv_phone.setText(blackRoll.name);

                viewHolder.tv_mod.setText((getModeText(blackRoll.mode)));

                viewHolder.ib_delete.setOnClickListener((btn) -> {
                    BlackListDAO.getInstance(getContext())
                            .delete(viewHolder.tv_phone.getText().toString());
                    mBlackRolls.remove(blackRoll);
                    mAdapter.notifyDataSetChanged();
                });

                return convertView;
            }

            private String getModeText(int mode)
            {
                switch (mode)
                {
                    case 0:
                        return "攔截電話";

                    case 1:
                        return "攔截簡訊";

                    case 2:
                        return "攔截電話、簡訊";
                }

                return "未知類型";
            }
        }

        class BlackListViewOnScrollListener implements AbsListView.OnScrollListener
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                // 滑動到ListView底部
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !mIsLoading &&
                    view.getLastVisiblePosition() >= mBlackRolls.size() - 1)
                {
                    mIsLoading = true;

                    ThreadUtil.start(() -> {
                        List<BlackRoll> blackRolls = BlackListDAO.getInstance(getContext())
                                .selectRange(mBlackRolls.size(), 20);
                        Message msg = Message.obtain();
                        msg.obj = blackRolls;
                        msg.what = HandlerProtocol.ON_BLACK_LIST_LOADED;
                        msg.setTarget(mHandler);
                        msg.sendToTarget();
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount)
            {

            }
        }
    }


}
