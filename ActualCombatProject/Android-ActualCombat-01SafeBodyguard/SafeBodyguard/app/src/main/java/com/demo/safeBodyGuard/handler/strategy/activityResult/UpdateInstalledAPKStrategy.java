package com.demo.safeBodyGuard.handler.strategy.activityResult;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.demo.safeBodyGuard.activity.MainActivity;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */
public class UpdateInstalledAPKStrategy implements IActivityResultHandlerStrategy
{
    @Override
    public void handle(Message msg, Context context)
    {
        context.startActivity(new Intent(context,MainActivity.class));
    }
}
