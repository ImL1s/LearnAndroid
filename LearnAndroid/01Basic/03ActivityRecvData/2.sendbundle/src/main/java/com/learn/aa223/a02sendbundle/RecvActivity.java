package com.learn.aa223.a02sendbundle;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RecvActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv);

        Intent t = getIntent();

//        Parcel p1 = Parcel.obtain();
//        Parcel p2 = Parcel.obtain();
//
//        boolean b1 = (p1.equals(p2));
//        boolean b2 = (p1 == p2);
//
//        System.out.println("ABC"+ b1);
//        System.out.println("ABC" + b2);

//        Bundle b1 = t.getBundleExtra("bundle1");
//        Bundle b2 = t.getBundleExtra("bundle2");

//        Bundle b = t.getExtras();
//        Bundle b = t.getBundleExtra("bundle1");

//        ((TextView)findViewById(R.id.txtv1)).setText(
//        String.format("Name:%s,age=%d,undefined:%s",
//        b.getString("name"),
//                /*b.getInt("age")*/ t.getIntExtra("age",0),
//        b.getString("undefined")));

//    ((TextView)findViewById(R.id.txtv1)).setText(
//            String.format("Name:%s,age=%d,undefined:%s",
//                    b1.getString("name") /*t.getStringExtra("name")*/,
//                /*b.getInt("age")*/ t.getIntExtra("age",0),
//                    b2.getString("undefined")));

//        User u = (User) t.getSerializableExtra("user1");

//        ((TextView)findViewById(R.id.txtv1)).setText(
//                String.format("Name:%s,age=%d",
//                        u.getName(),
//                        u.getAge()));

        UserParcelable up = (UserParcelable)t.getParcelableExtra("userP");

        ((TextView)findViewById(R.id.txtv1)).setText(
                String.format("Name:%s,age=%d",
                        up.getName(),
                        up.getAge()));

}
}
