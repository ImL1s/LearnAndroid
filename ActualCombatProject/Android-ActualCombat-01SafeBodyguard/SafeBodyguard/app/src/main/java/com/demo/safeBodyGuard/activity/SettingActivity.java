package com.demo.safeBodyGuard.activity;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.adapter.SettingTableAdapter;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.handler.IActivityHandler;
import com.demo.safeBodyGuard.model.SettingItemInfo;
import com.demo.safeBodyGuard.receiver.SafeGuardDeviceAdminReceiver;
import com.demo.safeBodyGuard.service.BlackListService;
import com.demo.safeBodyGuard.service.PhoneAddressService;
import com.demo.safeBodyGuard.utils.SPUtil;
import com.demo.safeBodyGuard.utils.ServiceUtil;
import com.demo.safeBodyGuard.view.AbsSettingItemView;
import com.demo.safeBodyGuard.view.SettingItemArrowsView;
import com.demo.safeBodyGuard.view.SettingItemCheckBoxView;
import com.demo.safeBodyGuard.view.SettingTableView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

/**
 * Created by ImL1s on 2017/1/5.
 * <p>
 * DESC:
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
        SettingItemCheckBoxView si =
                (SettingItemCheckBoxView) findViewById(R.id.setting_item_test1);
        si.setChecked(SPUtil.getBool(getApplicationContext(), Config.SP_KEY_TEST, false));
        si.setOnClickListener(
                v -> SPUtil.setBool(getApplicationContext(), Config.SP_KEY_TEST, si.isChecked()));

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        refreshUI();
    }

    @Override
    public IActivityHandler initHandler()
    {
        return null;
    }


    private void refreshUI()
    {
        boolean isPhoneAddressServiceRunning = ServiceUtil
                .isRunning(PhoneAddressService.class.getCanonicalName(), getApplicationContext());
        int phoneAddrBgIndex = SPUtil.getInt(getApplicationContext(),
                                             Config.SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUND_INDEX,
                                             0);

        getSettingItemCheckBoxView(0).setCheckedAndText(
                SPUtil.getBool(getApplicationContext(), Config.SP_KEY_BOOL_UPDATE, false));
        getSettingItemCheckBoxView(1).setCheckedAndText(devicePolicyManager.isAdminActive(cName));
        getSettingItemCheckBoxView(4).setCheckedAndText(!devicePolicyManager.isAdminActive(cName));
        getSettingItemCheckBoxView(6).setCheckedAndText(isPhoneAddressServiceRunning);
        getSettingItemArrowsView(7)
                .setDesc(Config.DRAWABLE_NAME_ARRAY_PHONE_QUERY_ADDRESS_VIEW_BG[phoneAddrBgIndex]);

        getSettingItemCheckBoxView(9).setCheckedAndText(ServiceUtil.isRunning(
                "com.demo.safeBodyGuard.service.BlackListService", getApplicationContext()));

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
                int phoneAddrBgIndex = SPUtil.getInt(getApplicationContext(),
                                                     Config.SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUND_INDEX,
                                                     0);

                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟", "自動更新已關閉"));
                list.add(new SettingItemInfo("啟動設備管理員權限", "設備管理員權限已開啟", "設備管理員權限已關閉"));
                list.add(new SettingItemInfo("這是鎖頻測試", "", ""));
                list.add(new SettingItemInfo("這是刪除本應用測試", "", ""));
                list.add(new SettingItemInfo("這是移除Admin權限測試", "", ""));
                list.add(new SettingItemInfo("這是鎖頻密碼設定,密碼aaa", "", ""));
                list.add(new SettingItemInfo("懸浮顯示來電歸屬地", getString(
                        R.string.activity_setting_phone_address_service_open), getString(
                        R.string.activity_setting_phone_address_service_off)));
                list.add(new SettingItemInfo("懸浮窗體背景顏色",
                                             Config.DRAWABLE_NAME_ARRAY_PHONE_QUERY_ADDRESS_VIEW_BG[phoneAddrBgIndex]));
                list.add(new SettingItemInfo("設定漂浮視窗位置", ""));
                list.add(new SettingItemInfo("黑名單功能", "已開啟黑名單功能", "已關閉黑名單功能"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));
                //                list.add(new SettingItemInfo("自動更新設定", "自動更新已開啟","自動更新已關閉"));


                return list.toArray(new SettingItemInfo[list.size()]);
            }
        });


        settingTableView.setOnItemClickListener((parent, view, position, id) -> {

            AbsSettingItemView itemView = (AbsSettingItemView) view;

            switch (position)
            {
                case 0:
                    setUpdate((SettingItemCheckBoxView) itemView);
                    break;

                case 1:
                    requestAdmin();
                    break;

                case 2:
                    lockScreenTest();

                    break;

                case 3:
                    uninstallTest();
                    break;

                case 4:
                    removeAdmin();
                    break;

                case 5:
                    resetPwd();
                    break;

                case 6:
                    setFlowPhoneAddrView((SettingItemCheckBoxView) view);

                    break;

                case 7:
                    setFlowPhoneAddrBg();
                    break;

                case 8:
                    setFlowPhoneAddrViewPos();
                    break;

                case 9:
                    setBlackListSwitch((SettingItemCheckBoxView) view);
                    break;
            }

            refreshUI();
        });
    }

    private void setBlackListSwitch(SettingItemCheckBoxView checkBoxView)
    {
        Intent intent = new Intent(getApplicationContext(), BlackListService.class);

        if (checkBoxView.isChecked())
        {
            startService(intent);
            return;
        }

        stopService(intent);
    }

    private void setFlowPhoneAddrViewPos()
    {
        startActivityEasy(PhoneAddressPosSettingActivity.class);
    }

    private void setFlowPhoneAddrBg()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(Config.DRAWABLE_NAME_ARRAY_PHONE_QUERY_ADDRESS_VIEW_BG,
                                     SPUtil.getInt(this,
                                                   Config.SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUND_INDEX,
                                                   0), (dialog, which) -> {
                    SPUtil.setInt(this, Config.SP_KEY_INT_PHONE_ADDRESS_VIEW_BACKGROUND_INDEX,
                                  which);
                    dialog.dismiss();
                });

        builder.setPositiveButton("取消", (dialog, which) -> {
            dialog.dismiss();
        });

        if (Build.VERSION.SDK_INT >= 17)
            builder.setOnDismissListener(dialog -> refreshUI());

        builder.show();
    }

    /**
     * 設定來電查詢的漂浮視窗是否顯示
     *
     * @param view 被點擊的ItemView
     */
    private void setFlowPhoneAddrView(SettingItemCheckBoxView view)
    {
        Intent serviceIntent = new Intent(this, PhoneAddressService.class);

        if (view.isChecked())
        {
            startService(serviceIntent);
        }
        else
        {
            stopService(serviceIntent);
        }
    }

    private boolean resetPwd()
    {
        return devicePolicyManager.resetPassword("aaa", 0);
    }

    private void removeAdmin()
    {
        devicePolicyManager.removeActiveAdmin(cName);
    }

    private void uninstallTest()
    {
        Intent intent2 = new Intent("android.intent.action.DELETE");
        intent2.addCategory("android.intent.category.DEFAULT");
        intent2.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent2);
    }

    private void lockScreenTest()
    {
        if (devicePolicyManager.isAdminActive(cName))
        {
            devicePolicyManager.lockNow();
        }
        else
        {
            Toast.makeText(SettingActivity.this, "需要啟動設備管理員權限", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestAdmin()
    {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "this is QUERY_PHONE_ADDRESS_COMPLETED explanation");
        startActivity(intent);
    }

    private void setUpdate(SettingItemCheckBoxView itemView)
    {
        SettingItemCheckBoxView checkBoxView = itemView;
        SPUtil.setBool(getApplicationContext(), Config.SP_KEY_BOOL_UPDATE,
                       checkBoxView.isChecked());
    }

    private SettingItemCheckBoxView getSettingItemCheckBoxView(int pos)
    {
        return ((SettingItemCheckBoxView) (settingTableView.getItem(pos)));
    }

    private SettingItemArrowsView getSettingItemArrowsView(int pos)
    {
        return ((SettingItemArrowsView) (settingTableView.getItem(pos)));
    }
}
