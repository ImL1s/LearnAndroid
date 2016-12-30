package com.demo.safeBodyGuard.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.demo.safeBodyGuard.define.ActivityResultProtocol;
import com.demo.safeBodyGuard.define.HandlerProtocol;
import com.demo.safeBodyGuard.handler.model.ActivityResult;
import com.demo.safeBodyGuard.handler.strategy.activityResult.IActivityResultHandlerStrategy;
import com.demo.safeBodyGuard.handler.strategy.activityResult.UpdateInstalledAPKStrategy;
import com.demo.safeBodyGuard.handler.strategy.message.EnterHomeStrategy;
import com.demo.safeBodyGuard.handler.strategy.IHandlerStrategy;
import com.demo.safeBodyGuard.handler.strategy.message.URLErrorStrategy;
import com.demo.safeBodyGuard.handler.strategy.message.UpdateVersionStrategy;

import java.util.HashMap;

/**
 * Created by iml1s-macpro on 2016/12/29.
 */

public class SplashHandler extends Handler
{
    private HashMap<Integer,IHandlerStrategy> messageStrategyMap  = null;
    private HashMap<Integer,IActivityResultHandlerStrategy> activityRSStrategyMap  = null;
    private Context context = null;


    public SplashHandler(Context context)
    {
        this.context = context;
        messageStrategyMap = new HashMap<>();
        activityRSStrategyMap = new HashMap<>();

        messageStrategyMap.put(HandlerProtocol.ENTER_HOME,new EnterHomeStrategy());
        messageStrategyMap.put(HandlerProtocol.UPDATE_VERSION,new UpdateVersionStrategy());
        messageStrategyMap.put(HandlerProtocol.URL_ERROR,new URLErrorStrategy());

        activityRSStrategyMap.put(ActivityResultProtocol.UPDATE_INSTALL_APK,new UpdateInstalledAPKStrategy());
    }

    @Override
    public void handleMessage(Message msg)
    {
        Integer protocol = msg.what;

        if(messageStrategyMap.containsKey(protocol))
        {
            messageStrategyMap.get(protocol).handle(msg,context);
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data)
    {
        Integer protocol = requestCode;

        if(activityRSStrategyMap.containsKey(protocol))
        {
            Message msg = new Message();
            msg.what = protocol;
            msg.obj = new ActivityResult(requestCode,resultCode,data);
            messageStrategyMap.get(protocol).handle(msg,context);
        }
    }
}
