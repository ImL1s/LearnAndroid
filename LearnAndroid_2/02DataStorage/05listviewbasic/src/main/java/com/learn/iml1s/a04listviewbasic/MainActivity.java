package com.learn.iml1s.a04listviewbasic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<Integer,View> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = new HashMap<Integer, View>();

        ListView listView = (ListView) findViewById(R.id.myListView);
        listView.setAdapter(new MyAdapter());

    }

     class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            TextView t = null;

            if(convertView == null) {
                t = new TextView(MainActivity.this);
            }
            else {
                t = (TextView) convertView;
            }

            t.setText("Position" + i);
            t.setTextSize(30);
            t.setPadding(10, 10, 10, 10);

            map.put(t.hashCode(),t);
            Log.d("debug",map.size() + "");

            return t;
        }
    }
}



