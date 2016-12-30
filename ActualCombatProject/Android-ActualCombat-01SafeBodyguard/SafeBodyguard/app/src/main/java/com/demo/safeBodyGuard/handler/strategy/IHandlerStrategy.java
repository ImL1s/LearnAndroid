package com.demo.safeBodyGuard.handler.strategy;

import android.content.Context;
import android.os.Message;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public interface IHandlerStrategy
{
    void handle(Message msg, Context context);
}
