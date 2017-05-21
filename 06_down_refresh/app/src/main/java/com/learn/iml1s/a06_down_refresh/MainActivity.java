package com.learn.iml1s.a06_down_refresh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.learn.iml1s.a06_down_refresh.view.RefreshListView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RefreshListView rl_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl_list = (RefreshListView) findViewById(R.id.rl_list);
        rl_list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return "Hello world";
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(MainActivity.this);
                tv.setText("T "+ new Random().nextInt(74));
                return tv;
            }
        });

    }
}
