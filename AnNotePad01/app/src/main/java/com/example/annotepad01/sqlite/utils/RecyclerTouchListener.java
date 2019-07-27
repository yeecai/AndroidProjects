package com.example.annotepad01.sqlite.utils;

/*
*  Created by Ye on 26/07/2019
*
* */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.annotepad01.sqlite.view.MainActivity;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private ClickListener clicklistener;
    private GestureDetector gestureDetector;
    private Object MainActivity;


    /*
    *  Constructor class
    *  Listen LongPress to onLongClick(), then showActionDialog()
    * */


    public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

        this.clicklistener = clicklistener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
         gestureDetector.onTouchEvent(motionEvent);
       /* if (child != null && clicklistener != null && gestureDetector.onTouchEvent(motionEvent)) {
            clicklistener.onClick(child, recyclerView.getChildPosition(child)); // no doing anything here, only longPress work.
        }*/
        return false; //If return true onClick() will be called cause onTouchEvent is called
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
       // Log.d("IF","onIntercepTouchEvent return ture, here will be called"+0);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        // void swipeLeft(View view, int position); // for swipe left to delete
    }
}
