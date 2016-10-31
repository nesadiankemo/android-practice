package com.wsh.mypracticeapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wsh-03 on 2016/9/29.
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDrawCallback != null) {
            mOnDrawCallback.onDrawCallback(canvas);
        }
    }

    OnDrawCallback mOnDrawCallback;
    public void setOnDrawCallback(OnDrawCallback onDrawCallback) {
        mOnDrawCallback = onDrawCallback;
    }

    public interface OnDrawCallback {
        void onDrawCallback(Canvas canvas);
    }
}
