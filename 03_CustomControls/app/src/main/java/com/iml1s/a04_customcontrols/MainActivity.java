package com.iml1s.a04_customcontrols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a05_custom_switch.YSToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YSToggleButton ytb = (YSToggleButton) findViewById(R.id.ys_toggle_btn);
        ytb.setBackground(R.drawable.switch_background);
//        ytb.setSlider(R.drawable.slide_button);
        ytb.setSwitch(true);
    }
}
