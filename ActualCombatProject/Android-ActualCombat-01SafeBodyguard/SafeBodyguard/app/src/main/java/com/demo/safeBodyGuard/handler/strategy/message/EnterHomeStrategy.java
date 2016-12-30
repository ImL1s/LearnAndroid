package com.demo.safeBodyGuard.handler.strategy.message;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.demo.safeBodyGuard.activity.MainActivity;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class EnterHomeStrategy implements IMessageHandlerStrategy
{
    @Override
    public void handle(Message msg, Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
