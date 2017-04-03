package com.demo.safeBodyGuard.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AntiVirusActivity extends AppCompatActivity {

    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.pb_bar)
    ProgressBar pb_bar;
    @BindView(R.id.iv_scanning)
    ImageView iv_scanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        ButterKnife.bind(this);

//            public RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue,
//        int pivotYType, float pivotYValue)
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setStartTime(0);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setDuration(1000);
//        iv_scanning.setAnimation(rotateAnimation);
        iv_scanning.startAnimation(rotateAnimation);
    }


}
