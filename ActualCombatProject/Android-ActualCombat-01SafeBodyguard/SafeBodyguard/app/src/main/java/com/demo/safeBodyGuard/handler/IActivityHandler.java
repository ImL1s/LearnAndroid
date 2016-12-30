package com.demo.safeBodyGuard.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by iml1s-macpro on 2016/12/30.
 */

public abstract class IActivityHandler extends Handler
{
    public abstract void handleActivityResult(int requestCode, int resultCode, Intent data);
}
