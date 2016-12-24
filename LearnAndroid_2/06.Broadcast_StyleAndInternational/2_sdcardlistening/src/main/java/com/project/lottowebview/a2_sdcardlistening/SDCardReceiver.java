package com.project.lottowebview.a2_sdcardlistening;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by iml1s-macpro on 2016/11/29.
 */

public class SDCardReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if(action.equals("android.intent.action.MEDIA_UNMOUNTED"))
        {
            Toast.makeText(context,"取消掛載SD卡",Toast.LENGTH_SHORT).show();
        }
        else if (action.equals("android.intent.action.MEDIA_MOUNTED"))
        {
            Toast.makeText(context,"掛載SD卡",Toast.LENGTH_SHORT).show();
        }
    }
}
