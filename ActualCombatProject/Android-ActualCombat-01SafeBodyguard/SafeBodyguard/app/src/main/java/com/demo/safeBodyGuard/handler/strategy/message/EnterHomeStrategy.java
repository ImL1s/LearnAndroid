package com.demo.safeBodyGuard.handler.strategy.message;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.demo.safeBodyGuard.activity.HomeActivity;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class EnterHomeStrategy implements IMessageHandlerStrategy
{
    @Override
    public void handle(Message msg, Context context)
    {
        Intent intent = new Intent(context, HomeActivity.class);

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

//        ((SplashActivity)(context)).finish();

        context.startActivity(intent);
    }
}
