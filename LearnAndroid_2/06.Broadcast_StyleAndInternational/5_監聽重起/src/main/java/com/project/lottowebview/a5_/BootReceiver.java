package com.project.lottowebview.a5_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by iml1s-macpro on 2016/12/1.
 */

public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("debug","===== " + intent.getAction());
        Intent intent_ = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "1234"));
//        Intent intent_ = new Intent(context,MainActivity.class);

        // 告訴系統,要開啟一個新的任務棧(因為是在廣播中開啟的)
        intent_.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent_);
    }
}
