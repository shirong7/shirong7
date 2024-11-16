package com.huinan.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.widget.NestedScrollView;


public class DisableScrollNestedScrollView extends NestedScrollView {

    private boolean scrollable = false;

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }



    public DisableScrollNestedScrollView(Context context) {
        super(context);
    }

    public DisableScrollNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableScrollNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollable) {
            return super.onTouchEvent(ev);
        }
        return true;
    }
}