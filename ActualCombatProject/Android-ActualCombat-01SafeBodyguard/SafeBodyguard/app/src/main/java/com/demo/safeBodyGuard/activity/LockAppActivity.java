package com.demo.safeBodyGuard.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.define.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LockAppActivity extends AppCompatActivity {

    @BindView(R.id.tv_app_name)
    TextView tv_app_name;
    @BindView(R.id.ed_pwd)
    EditText ed_pwd;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.iv_lock_icon)
    ImageView iv_lock_icon;
    private String pkgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_app);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        pkgName = intent.getStringExtra(Config.INTENT_KEY_PKG_NAME);

        PackageManager pm = getPackageManager();
        ApplicationInfo info;
        try {
            info = pm.getApplicationInfo(pkgName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            finish();
            return;
        }

        Drawable drawable = info.loadIcon(pm);
        iv_lock_icon.setImageDrawable(drawable);

        tv_app_name.setText(info.loadLabel(pm).toString());
    }

    @OnClick(R.id.btn_ok)
    void onClick(View view) {
        if (ed_pwd.getText().toString().equals("123")) {
            Intent intent = new Intent("com.android.action.LOCK_SKIP_CHECK");
            intent.putExtra("pkgName",pkgName);
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(this,"密碼錯誤",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
