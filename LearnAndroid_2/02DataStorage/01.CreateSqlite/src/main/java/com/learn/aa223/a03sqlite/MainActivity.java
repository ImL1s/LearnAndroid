package com.learn.aa223.a03sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InfoSQLiteOpenHelper helper = new InfoSQLiteOpenHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
    }


}
