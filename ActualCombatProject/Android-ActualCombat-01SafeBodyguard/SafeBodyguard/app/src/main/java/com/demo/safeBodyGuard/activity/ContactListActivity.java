package com.demo.safeBodyGuard.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContactListActivity extends BaseActivity
{
    private List<HashMap<String, String>> mDataList = null;

    @ViewInject(R.id.lv_contacts)
    private ListView mListView;

    private ContactsAdapter adapter;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            adapter = new ContactsAdapter();
            mListView.setAdapter(adapter);
//            mListView.deferNotifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_contact_list);
        super.onCreate(savedInstanceState);

//        super.onCreate(savedInstanceState);

        mListView = (ListView) findViewById(R.id.lv_contacts);
        mListView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent();
            HashMap<String,String> map = adapter.getItem(position);
            intent.putExtra(Config.INTENT_DATA_KEY_PHONE,map.get(Config.INTENT_DATA_KEY_PHONE));
            setResult(Config.ACTIVITY_RESULT_CODE_CONTACT_SELECTED,intent);
            finish();
        });

        selectContactData();

    }

    @Override
    public IActivityHandler initHandler()
    {
        return new IActivityHandler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if (msg.what == HandlerProtocol.CONTACTS_READ_OVER && mListView != null && mListView.getCount() != 0)
                {
                    adapter = new ContactsAdapter();
                    mListView.setAdapter(adapter);
                }
            }

            @Override
            public void handleActivityResult(int requestCode, int resultCode, Intent data)
            {

            }
        };
    }

    private void selectContactData()
    {
        new Thread(() -> {

            ContentResolver resolver = getContentResolver();
            Cursor contactsCursor = resolver.query(Uri.parse(Config.DB_URL_CONTACTS + Config.DB_TABLE_CONTACT_RAW_CONTACT), new String[]{Config.DB_COLUMN_RAW_CONTACT_CONTACT_ID}, null, null, null);

            if (contactsCursor.getCount() > 0)
            {
                mDataList = new ArrayList<>();

                while (contactsCursor.moveToNext())
                {
                    String id = contactsCursor.getString(0);

                    Cursor dataCursor = resolver.query(Uri.parse(Config.DB_URL_CONTACTS + Config.DB_TABLE_CONTACT_DATA), new String[]{

                            Config.DB_COLUMN_DATA_CONTACT_DATA1, Config.DB_COLUMN_DATA_CONTACT_MIME_TYPE}, Config.DB_COLUMN_RAW_CONTACT_CONTACT_ID + "= ?", new String[]{id}, null);

                    HashMap<String,String> map = new HashMap<>();

                    LogUtil.log("***** ID: %s Start *****", id);
                    while (dataCursor.moveToNext())
                    {
                        String data = dataCursor.getString(0);
                        String mimeType = dataCursor.getString(1);

                        switch (mimeType)
                        {
                            case "vnd.android.cursor.item/phone_v2":
                                map.put(Config.INTENT_DATA_KEY_PHONE,data);
                                break;

                            case "vnd.android.cursor.item/name":
                                map.put("name",data);
                                break;
                        }

                        if(map.size() != 0) mDataList.add(map);

                        LogUtil.log(String.format("Data: %s  MimeType: %s", data, mimeType));

                    }
                    LogUtil.log("***** ID: %s End  *****", id);
                    dataCursor.close();
                }

                contactsCursor.close();

                mHandler.sendEmptyMessage(0);
            }

        }).start();
    }


    private class ContactsAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return mDataList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position)
        {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewGroup item = (ViewGroup) View.inflate(ContactListActivity.this, R.layout.listview_contact_item, null);

            TextView tv_name = (TextView) item.findViewById(R.id.contact_item_tv_name);
            TextView tv_phone = (TextView) item.findViewById(R.id.contact_item_tv_phone);

            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));

            return item;
        }
    }
}
