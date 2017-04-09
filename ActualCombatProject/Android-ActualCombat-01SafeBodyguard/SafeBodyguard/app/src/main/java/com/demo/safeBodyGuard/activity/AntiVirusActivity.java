package com.demo.safeBodyGuard.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.db.dao.AntiVirusDAO;
import com.demo.safeBodyGuard.db.dao.model.AntiVirus;
import com.demo.safeBodyGuard.db.dao.model.AppInfo;
import com.demo.safeBodyGuard.engine.AppInfoEngine;
import com.demo.safeBodyGuard.utils.CryptionUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AntiVirusActivity extends AppCompatActivity {

    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.pb_bar)
    ProgressBar pb_bar;
    @BindView(R.id.iv_scanning)
    ImageView iv_scanning;
    @BindView(R.id.ll_add)
    LinearLayout ll_add;
    private HashMap<String, AntiVirus> antiVirusMap;
    private List<String> virusMd5List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        ButterKnife.bind(this);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setStartTime(0);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setDuration(1000);

        iv_scanning.startAnimation(rotateAnimation);


        Flowable
                .create((FlowableOnSubscribe<PackageInfo>) e -> {
                    // 將病毒資料導入到集合中(方便比對)
                    antiVirusMap = new HashMap<>();
                    List<AntiVirus> antiVirusList = AntiVirusDAO.GetAll();
                    virusMd5List = new ArrayList<>();

                    for (AntiVirus antiVirus : antiVirusList) {
                        antiVirusMap.put(antiVirus.pkgName, antiVirus);
                        virusMd5List.add(antiVirus.md5);
                    }

                    // 取得所有應用的簽名並發射給觀察者
                    List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(
                            PackageManager.GET_SIGNATURES + PackageManager.GET_UNINSTALLED_PACKAGES);

                    for (PackageInfo info : packageInfoList) {
                        if (e.isCancelled()) {
                            break;
                        }
                        e.onNext(info);
                    }
                    e.onComplete();
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PackageInfo>() {
                    private Subscription s;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.s = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(PackageInfo pkgInfo) {
                        SystemClock.sleep(40);
                        if(pkgInfo == null || pkgInfo.packageName == null) return;;

                        // 取得應用簽名的MD5
                        Signature[] signatures = pkgInfo.signatures;
                        Signature signature = signatures[0];
                        String signatureStr = signature.toCharsString();
                        signatureStr = CryptionUtil.MD5Encoder(signatureStr);
//                        boolean isDangerous = antiVirusMap.containsKey(pkgInfo.packageName) ||
//                                antiVirusMap.get(pkgInfo.packageName).md5.equalsIgnoreCase(signatureStr);
                        boolean isDangerous = virusMd5List.contains(signatureStr) || virusMd5List.contains(signatureStr.toLowerCase());

                        // 打印當前應用的MD5碼(測試用)
                        if(pkgInfo.packageName.startsWith("com.demo")){
                            Log.d("debug","com.demo.safeBodyGuard MD5: " +signatureStr);
                        }

                        runOnUiThread(() -> {
                            TextView tv = new TextView(AntiVirusActivity.this);
                            tv.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            tv.setText(pkgInfo.packageName);
                            tv.setTextColor(isDangerous ? Color.RED : Color.BLACK);
                            ll_add.addView(tv, 0);
                        });
                        s.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        iv_scanning.clearAnimation();
                    }
                });
    }
}
