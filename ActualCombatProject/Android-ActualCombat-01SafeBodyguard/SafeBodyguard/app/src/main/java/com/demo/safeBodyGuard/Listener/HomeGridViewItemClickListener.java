package com.demo.safeBodyGuard.Listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.safeBodyGuard.R;
import com.demo.safeBodyGuard.activity.AToolActivity;
import com.demo.safeBodyGuard.activity.AppManagerActivity;
import com.demo.safeBodyGuard.activity.BlackListActivity;
import com.demo.safeBodyGuard.activity.SecurityActivity;
import com.demo.safeBodyGuard.activity.SettingActivity;
import com.demo.safeBodyGuard.define.Config;
import com.demo.safeBodyGuard.utils.CryptionUtil;
import com.demo.safeBodyGuard.utils.SPUtil;

/**
 * Created by iml1s-macpro on 2017/1/10.
 */

public class HomeGridViewItemClickListener implements AdapterView.OnItemClickListener
{
    private final Context mContext;

    public HomeGridViewItemClickListener(Context context)
    {
        this.mContext = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch (position)
        {
            case 0:
                showPwdDialog();
                break;

            case 1:
                showBlackListActivity();
                break;

            case 2:
                showAppManagerActivity();
                break;

            case 7:
                showAToolActivity();
                break;

            case 8:
                showSettingActivity();
                break;
        }
    }

    private void showAppManagerActivity()
    {
        mContext.startActivity(new Intent(mContext, AppManagerActivity.class));
    }

    private void showBlackListActivity()
    {
        mContext.startActivity(new Intent(mContext, BlackListActivity.class));
    }

    private void showPwdDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogContent;

        InputMethodManager inputMethodManager =
                (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);

        // 還未設定密碼
        if (SPUtil.getString(mContext, Config.SP_KEY_STRING_PWD, null) == null)
        {
            dialogContent = View.inflate(mContext, R.layout.dialog_set_pwd, null);

            EditText et_pwd1 = (EditText) dialogContent.findViewById(R.id.dialog_set_et_pwd1);
            EditText et_pwd2 = (EditText) dialogContent.findViewById(R.id.dialog_set_et_pwd2);
            et_pwd1.requestFocus();

            dialogContent.findViewById(R.id.dialog_btn_passive).setOnClickListener(v -> {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                dialog.dismiss();
            });

            dialogContent.findViewById(R.id.dialog_btn_positive).setOnClickListener(v -> {

                String pwd1 = et_pwd1.getText().toString().trim();
                String pwd2 = et_pwd2.getText().toString().trim();

                if (!pwd1.equals(pwd2))
                {
                    Toast.makeText(mContext, "密碼不一致", Toast.LENGTH_LONG).show();
                    return;
                }

                String md5Pwd = CryptionUtil.MD5Encoder(pwd1);
                SPUtil.setString(mContext, Config.SP_KEY_STRING_PWD, md5Pwd);
                dialog.dismiss();

                mContext.startActivity(new Intent(mContext, SecurityActivity.class));
            });

            dialog.setView(dialogContent);
        }
        // 已經設定過密碼
        else
        {
            dialogContent = View.inflate(mContext, R.layout.dialog_confirm_pwd, null);

            EditText et_pwd = (EditText) dialogContent.findViewById(R.id.dialog_confirm_et_pwd);
            et_pwd.requestFocus();

            dialogContent.findViewById(R.id.dialog_btn_passive).setOnClickListener(v -> {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                dialog.dismiss();
            });

            dialogContent.findViewById(R.id.dialog_btn_positive).setOnClickListener(v -> {

                String pwd = et_pwd.getText().toString().trim();
                String originalMD5Pwd = SPUtil.getString(mContext, Config.SP_KEY_STRING_PWD, null);
                String currentMD5Pwd = CryptionUtil.MD5Encoder(pwd);

                if (originalMD5Pwd.equals(currentMD5Pwd))
                {
                    Toast.makeText(mContext, "密碼一致", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    mContext.startActivity(new Intent(mContext, SecurityActivity.class));
                }
                else
                {
                    Toast.makeText(mContext, "密碼不一致", Toast.LENGTH_LONG).show();
                }
            });

            dialog.setView(dialogContent);
        }

        dialog.setOnShowListener(dialog1 -> inputMethodManager
                .toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY));
        dialog.show();
    }

    private void showAToolActivity()
    {
        mContext.startActivity(new Intent(mContext, AToolActivity.class));
    }

    private void showSettingActivity()
    {
        mContext.startActivity(new Intent(mContext, SettingActivity.class));
    }
}
