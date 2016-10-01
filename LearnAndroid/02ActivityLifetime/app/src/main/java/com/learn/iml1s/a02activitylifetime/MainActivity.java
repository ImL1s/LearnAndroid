package com.learn.iml1s.a02activitylifetime;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("OnRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("OnDestroy");
    }
}
;