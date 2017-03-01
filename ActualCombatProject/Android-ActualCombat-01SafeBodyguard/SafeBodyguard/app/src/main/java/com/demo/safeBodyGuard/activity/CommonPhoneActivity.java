package com.demo.safeBodyGuard.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.CommonPhoneDAO;
import com.demo.safeBodyGuard.db.dao.model.PhoneClass;
import com.demo.safeBodyGuard.db.dao.model.PhoneItem;

import java.util.List;

public class CommonPhoneActivity extends AppCompatActivity
{

    private List<PhoneClass>   mPhoneClassList;
    private ExpandableListView mEp_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_phone);

        initUIRef();
        initData();

    }

    protected void initData()
    {
        mPhoneClassList = CommonPhoneDAO.GetAllPhoneClass();

        mEp_lv.setAdapter(new CommonPhoneAdapter());

        mEp_lv.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            callPhone(mPhoneClassList.get(groupPosition).phoneItems.get(childPosition).number);
            return false;
        });
    }

    protected void initUIRef()
    {
        mEp_lv = (ExpandableListView) findViewById(R.id.activity_common_phone_ep_lv);
    }

    /**
     * 撥打電話
     * @param phoneNum
     */
    protected void callPhone(String phoneNum)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
            PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "請同意撥號權限..", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(intent);
    }


    /**
     * ExpandableListAdapter
     */
    class CommonPhoneAdapter extends BaseExpandableListAdapter
    {

        @Override
        public int getGroupCount()
        {
            return mPhoneClassList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return mPhoneClassList.get(groupPosition).phoneItems.size();
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return mPhoneClassList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            return mPhoneClassList.get(groupPosition).phoneItems.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds()
        {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent)
        {
            PhoneClass phoneClass = (PhoneClass) getGroup(groupPosition);
            TextView textView = new TextView(CommonPhoneActivity.this);
            textView.setText(phoneClass.servicesType);
            textView.setTextColor(Color.BLUE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent)
        {
            View viewItem = View.inflate(CommonPhoneActivity.this,
                                         R.layout.expandable_list_view_common_phone_item, null);

            TextView textView = (TextView) viewItem.findViewById(R.id.expandable_list_view_tv_name);
            PhoneItem phoneItem = (PhoneItem) getChild(groupPosition, childPosition);
            textView.setText(phoneItem.phoneName);

            textView = (TextView) viewItem.findViewById(R.id.expandable_list_view_tv_phone);
            textView.setText(phoneItem.number);

            return viewItem;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }
    }
}
