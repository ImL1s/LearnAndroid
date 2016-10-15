package com.learn.iml1s.a02crud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.learn.iml1s.a02crud.dal.StudentDAL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StudentBean s = new StudentBean();
//        s.setName("sam");
//        s.setAge(23);

        StudentDAL dal = new StudentDAL(this);
//        dal.add(s);

        StudentBean re = dal.query("sam").get(0);
        Log.d("debug","Name: " + re.getName() + "Age: "+re.getAge() + "ID: " + re.getId());
    }
}
