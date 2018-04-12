package com.dinodevs.pacecalendarwidget;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

public class APtouch {

    private int LONG_CLICK_DURATION = 600;   // long click time
    private int LONG_CLICK_MOVEMENT_THRESHOLD = 10;   // long click time
    private float MIN_SWIPE_LENGTH = 80;     // minimum length of a swipe
    private long MAX_SWIPE_TIME = 250;       // maximum time of a swipe
    private long MAX_DOUBLE_CLICK_TIME = 300;// maximum time of a swipe

    private View.OnTouchListener listener;

    private Handler handler;
    private boolean handler_active;
    private Runnable handler_runnable;

    private long last_click_time;
    private long start_time;
    private float start_position_x;
    private float start_position_y;
    private boolean moved;

    APtouch () {

        // Double click timer
        this.last_click_time = 0;

        // Long Click handler
        this.handler = new Handler();
        this.handler_runnable = new Runnable() {
            @Override
            public void run(){
                onLongClick();
            }
        };

        // Create listener
        this.listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Detect action
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return action_down(motionEvent);

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        return action_up(motionEvent);

                    case MotionEvent.ACTION_MOVE:
                        return action_move(motionEvent);
                }
                return true;
            }
        };
    }

    private String TAG = "PaceCalendarWidget";

    // Handle actions
    private boolean action_down(MotionEvent motionEvent){
        // Save action start info
        this.start_time = Calendar.getInstance().getTimeInMillis();
        this.start_position_x = motionEvent.getX();
        this.start_position_y = motionEvent.getY();
        this.moved = false;

        // Log.d(this.TAG, "[Action Down] {x:" + this.start_position_x + ", y:" + this.start_position_y + "}");

        // Trigger long click in x ms
        this.handler_active = true;
        this.handler.postDelayed(this.handler_runnable, this.LONG_CLICK_DURATION);

        return true;
    }
    private boolean action_move(MotionEvent motionEvent){
        // Find dx and dy
        float dx = Math.abs(this.start_position_x - motionEvent.getX());
        float dy = Math.abs(this.start_position_y - motionEvent.getY());
        this.moved = true;

        // Log.d(this.TAG, "[Action Move] {dx:" + dx + ", dy:" + dy + "}");

        // Disable long click
        if (this.handler_active && (dx > this.LONG_CLICK_MOVEMENT_THRESHOLD || dy > this.LONG_CLICK_MOVEMENT_THRESHOLD)) {
            this.handler_active = false;
            this.handler.removeCallbacks(this.handler_runnable);
        }

        return true;
    }
    private boolean action_up(MotionEvent motionEvent){
        // Disable long click
        if (this.handler_active) {
            this.handler_active = false;
            this.handler.removeCallbacks(this.handler_runnable);
        }

        long now = Calendar.getInstance().getTimeInMillis();

        // If it was a click
        if (!this.moved) {
            if (this.last_click_time > 0 && now - this.last_click_time < this.MAX_DOUBLE_CLICK_TIME) {
                this.last_click_time = 0;
                return onDoubleClick();
            }
            else {
                this.last_click_time = now;
                return onClick();
            }
        }

        this.last_click_time = 0;

        // Check if haven't met time limit for a swipe
        if (now - this.start_time > this.MAX_SWIPE_TIME) {
            // Log.d(this.TAG, "[Action Up] Not time long enough to be a swipe.");
            return true;
        }

        // Find dx and dy
        float end_position_x = motionEvent.getX();
        float end_position_y = motionEvent.getY();
        float dx = Math.abs(this.start_position_x - end_position_x);
        float dy = Math.abs(this.start_position_y - end_position_y);

        // Check if haven't met distance limit
        if (dx < this.MIN_SWIPE_LENGTH && dy < this.MIN_SWIPE_LENGTH) {
            // Log.d(this.TAG, "[Action Up] Not distance long enough to be a swipe.");
            return true;
        }

        // Detect direction
        if (dx > dy) {
            if (this.start_position_x < end_position_x) {
                // Swipe right
                // Log.d(this.TAG, "[Action Up] Swipe Right.");
                return onSwipeRight();
            }
            else {
                // Swipe left
                // Log.d(this.TAG, "[Action Up] Swipe Left.");
                return onSwipeLeft();
            }
        }
        else {
            if (this.start_position_y < end_position_y){
                // Swipe down
                // Log.d(this.TAG, "[Action Up] Swipe Down.");
                return onSwipeDown();
            }
            else {
                // Swipe up
                // Log.d(this.TAG, "[Action Up] Swipe Up.");
                return onSwipeUp();
            }
        }
    }


    // Function to attach listener
    public View.OnTouchListener listen() {
        return this.listener;
    }

    // Events
    public boolean onSwipeUp() {return true;}
    public boolean onSwipeDown() {return true;}
    public boolean onSwipeLeft() {return true;}
    public boolean onSwipeRight() {return true;}
    public boolean onClick() {return true;}
    public boolean onDoubleClick() {return this.onClick();}
    public boolean onLongClick() {return true;}
}
