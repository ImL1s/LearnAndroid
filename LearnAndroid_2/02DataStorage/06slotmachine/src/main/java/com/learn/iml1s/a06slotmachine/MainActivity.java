package com.learn.iml1s.a06slotmachine;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListView(R.id.listView1);
        setListView(R.id.listView2);
        setListView(R.id.listView3);
    }

    protected void setListView(int id){

        ((ListView)findViewById(id)).setAdapter(new MyAdapter());
    }

    public class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return 100;
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
        public View getView(int i, View view, ViewGroup viewGroup) {

            TextView t;
            if(view == null) {
                t = new TextView(MainActivity.this);
            }
            else {
                t = (TextView) view;
            }
            t.setTextSize(50);
            t.setGravity(Gravity.CENTER_HORIZONTAL);

            Random r = new Random();
            int randomInt = r.nextInt(100);

            if(randomInt < 20){
                t.setText("桃");
                t.setTextColor(Color.parseColor("#ff00ff"));
            }
            else if(randomInt < 40){
                t.setText("李");
                t.setTextColor(Color.parseColor("#777777"));
            }
            else if(randomInt < 60){
                t.setText("蘋");
                t.setTextColor(Color.parseColor("#ff00ff"));
            }
            else if(randomInt < 80){
                t.setText("蕉");
                t.setTextColor(Color.parseColor("#FFFF00"));
            }
            else {
                t.setText("水");
                t.setTextColor(Color.parseColor("#0011FF"));
            }

            return t;
        }
    }
}
