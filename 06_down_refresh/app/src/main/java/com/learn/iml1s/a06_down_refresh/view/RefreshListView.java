package com.learn.iml1s.a06_down_refresh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.learn.iml1s.a06_down_refresh.R;

/**
 * Created by aa223 on 2017/5/21.
 */

public class RefreshListView extends ListView {

    private View mHeaderView;
    private View mArrowView;
    private View mPbView;
    private int mHeaderViewHeight;


    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        initHeaderView();
    }

    private void initHeaderView(){
        mHeaderView = View.inflate(getContext(), R.layout.list_view_header_list,null);
        mArrowView = mHeaderView.findViewById(R.id.iv_arrow);
        mPbView = mHeaderView.findViewById(R.id.pb);

        mHeaderView.measure(0,0);

        mHeaderViewHeight = mHeaderView.getMeasuredHeight();

        Log.d("debug","mHeader: "+mHeaderViewHeight);

        addHeaderView(mHeaderView);
    }
}
