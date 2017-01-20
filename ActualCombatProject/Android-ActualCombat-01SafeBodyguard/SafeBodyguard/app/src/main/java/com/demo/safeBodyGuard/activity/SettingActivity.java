package com.demo.safeBodyGuard.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.adapter.SettingTableAdapter;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.model.SettingItemInfo;
import com.demo.safeBodyGuard.receiver.SafeGuardDeviceAdminReceiver;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.view.SettingItemView;
import com.demo.safeBodyGuard.view.SettingTableView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

/**
 * Created by iml1s-macpro on 2017/1/5.
 */

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity
{

    private SettingTableView settingTableView = null;

    private ComponentName cName = null;

    private DevicePolicyManager devicePolicyManager = null;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        cName = new ComponentName(getApplication(), SafeGuardDeviceAdminReceiver.class);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        initSettingTableView();

        // 單獨在XML文件中定義的SettingItemView設定方式
        SettingItemView si = (SettingItemView) findViewById(R.id.setting_item_test1);
        si.setChecked(SPUtil.getBool(getApplicationContext(), Config.SP_KEY_TEST, false));
        si.setOnClickListener(v -> SPUtil.setBool(getApplicationContext(), Config.SP_KEY_TEST, si.isChecked()));

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        settingTableView.getItem(0).setCheckedAndText(SPUtil.getBool(getApplicationContext(), Config.SP_KEY_BOOL_UPDATE, false));
        settingTableView.getItem(1).setCheckedAndText(devicePolicyManager.isAdminActive(cName));
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }


    private void initSettingTableView()
    {
        settingTableView = (SettingTableView) (findViewById(R.id.tv_setting));

        settingTableView.setAdapter(new SettingTableAdapter()
        {
            @Override
            public SettingItemInfo[] getItemInfoArray()
            {
                ArrayList<SettingItemInfo> list = new ArrayList<>();

                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟", "自動更新已關閉"));
                list.add(new SettingItemInfo("啟動設備管理員權限", "設備管理員權限已開啟", "設備管理員權限已關閉"));
                list.add(new SettingItemInfo("這是鎖頻測試", "", ""));
                list.add(new SettingItemInfo("這是刪除本應用測試", "", ""));
                list.add(new SettingItemInfo("這是移除Admin權限測試","",""));
                list.add(new SettingItemInfo("這是鎖頻密碼設定,密碼aaa","",""));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));


                return list.toArray(new SettingItemInfo[list.size()]);
            }
        });


        settingTableView.setOnItemClickListener((parent, view, position, id) -> {

            SettingItemView stiView = (SettingItemView) view;

            switch (position)
            {
                case 0:
                    SPUtil.setBool(getApplicationContext(), Config.SP_KEY_BOOL_UPDATE, stiView.isChecked());
                    break;

                case 1:
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "this is a explanation");
                    startActivity(intent);
                    break;

                case 2:
                    if (devicePolicyManager.isAdminActive(cName))
                    {
                        devicePolicyManager.lockNow();
                    }
                    else
                    {
                        Toast.makeText(SettingActivity.this, "需要啟動設備管理員權限", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 3:
                    Intent intent2 = new Intent("android.intent.action.DELETE");
                    intent2.addCategory("android.intent.category.DEFAULT");
                    intent2.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent2);
                    break;

                case 4:
                    devicePolicyManager.removeActiveAdmin(cName);
                    break;

                case 5:
                    devicePolicyManager.resetPassword("aaa",0);
                    break;

            }
        });
    }
}
