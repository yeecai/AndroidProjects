package com.example.uipractices.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class MyBlock extends View {

    private int lastX;
    private int lastY;

    public MyBlock(Context context) {
        super(context);
    }

    public MyBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        //mScroller = new Scroller(context);
    }

    public MyBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                lastX = x;
                lastY = y;

                break;

            case MotionEvent.ACTION_MOVE:

                int offsetX = x - lastX;
                int offsetY = y - lastY;


//No.1
                layout(getLeft()+offsetX, getTop()+offsetY,
                        getRight()+offsetX , getBottom()+offsetY);
//No.2
/*                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);*/
// No.3
              /*  LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                setLayoutParams(layoutParams);*/
// No.3

/*                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                setLayoutParams(layoutParams);*/
//No.4
//                ((View)getParent()).scrollBy(-offsetX,-offsetY);
                break;
        }

        return true;
    }
}
