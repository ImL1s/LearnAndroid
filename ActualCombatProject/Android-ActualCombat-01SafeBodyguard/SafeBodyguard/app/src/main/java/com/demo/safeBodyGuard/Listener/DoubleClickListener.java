package com.demo.safeBodyGuard.Listener;

import android.view.View;


/**
 * Created by ImL1s on 2017/2/2.
 *
 * DESC:
 */

public class DoubleClickListener implements View.OnClickListener
{
    private double[] mClickFlow = new double[2];
    private OnDoubleClickListener mListener = null;

    public DoubleClickListener(OnDoubleClickListener listener)
    {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v)
    {
        System.arraycopy(mClickFlow, 1, mClickFlow, 0, mClickFlow.length - 1);

        mClickFlow[mClickFlow.length - 1] = System.currentTimeMillis();

        if (mClickFlow[mClickFlow.length - 1] != 0 &&
            mClickFlow[mClickFlow.length - 1] - mClickFlow[0] < 500)
        {
            if(mListener != null) mListener.onDoubleClick(v);
        }
    }

    public interface OnDoubleClickListener
    {
        void onDoubleClick(View v);
    }
}
