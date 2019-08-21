package com.example.draganddrawactivity;

import android.graphics.PointF;

public class Box {
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF orgin) {
        this.mCurrent = orgin ;
        this.mOrigin = orgin;
    }

    public PointF getmCurrent() {
        return mCurrent;
    }

    public void setmCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }

    public PointF getmOrigin() {
        return mOrigin;
    }
}
