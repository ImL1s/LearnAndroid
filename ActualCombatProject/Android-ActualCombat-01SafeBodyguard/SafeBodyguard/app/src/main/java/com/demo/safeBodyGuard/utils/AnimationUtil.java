package com.demo.safeBodyGuard.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by aa223 on 2017/3/31.
 */

public class AnimationUtil {

    public static TranslateAnimation getHorizontalAnimation(boolean moveToLeft, long duration) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, moveToLeft ? 1 : -1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

        animation.setDuration(duration);
        return animation;
    }
}
