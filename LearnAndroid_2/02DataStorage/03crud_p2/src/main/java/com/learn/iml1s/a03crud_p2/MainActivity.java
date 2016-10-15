package com.learn.iml1s.a03crud_p2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.learn.iml1s.a03crud_p2.dal.StudentDAL;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    StudentDAL dal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dal = new StudentDAL(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNewStudent("Marry",17);
            }
        });

        findViewById(R.id.queryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                queryStudent("Marry");
            }
        });

        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateStudent("Marry",new StudentBean(-1,"MarryChange",12));
            }
        });

        findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteStudent("Marry");
            }
        });


//
//        StudentBean re = dal.query("sam").get(0);
//        Log.d("debug","Name: " + re.getName() + "Age: "+re.getAge() + "ID: " + re.getId());
    }

    private void addNewStudent(String name,int age){

        StudentBean s = new StudentBean();
        s.setName(name);
        s.setAge(age);

        long affectedCount = dal.add(s);

        if(affectedCount > 0) Toast.makeText(this,"加入成功,受影響Row數: "+ affectedCount,Toast.LENGTH_SHORT).show();
        else Toast.makeText(this,"加入成功,受影響Row數: "+ affectedCount,Toast.LENGTH_SHORT).show();
    }

    private void queryStudent(String name){

        ArrayList<StudentBean> beanArrayList = dal.query(name);

        Toast.makeText(this,"查詢成功,資料數: "+ (beanArrayList == null? 0:beanArrayList.size()),Toast.LENGTH_SHORT).show();

    }

    private void updateStudent(String primaryKeyName,StudentBean changedBean){

        ArrayList<StudentBean> beanArrayList = dal.query(primaryKeyName);
        StudentBean bean = beanArrayList.get(0);
        bean.setName(changedBean.getName());
        bean.setAge(changedBean.getAge());

        long affectedCount = dal.update(bean);

        Toast.makeText(this,"更新成功,資料數: "+ affectedCount,Toast.LENGTH_SHORT).show();
    }

    private void deleteStudent(String name){

        ArrayList<StudentBean> beanArrayList = dal.query(name);
        StudentBean bean = beanArrayList.get(0);
        long affectedCount = dal.delete(bean);

        Toast.makeText(this,"刪除成功,資料數: "+ affectedCount,Toast.LENGTH_SHORT).show();
    }
}
