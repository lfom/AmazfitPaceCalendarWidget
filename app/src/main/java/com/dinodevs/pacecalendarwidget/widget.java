package com.dinodevs.pacecalendarwidget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import clc.sliteplugin.flowboard.AbstractPlugin;
import clc.sliteplugin.flowboard.ISpringBoardHostStub;

public class widget extends AbstractPlugin {

    //Tag for logging purposes. Change this to something suitable
    private static final String TAG = "PaceCalendarWidget";
    // Version
    public String version = "n/a";
    // Errors
    public String errors = "";

    //As AbstractPlugin is not an Activity or Service, we can't just use "this" as a context or getApplicationContext, so Context is global to allow easier access
    private Context mContext;

    //These get set up later
    private View mView;
    private boolean mHasActive = false;
    private ISpringBoardHostStub mHost = null;

    // Swipe / Long press handling
    private static int LONG_CLICK_DURATION = 600; // long click time
    private static float min_swipe_length = 100; // minimum length of a swipe
    private static long max_swipe_time = 250; // maximum time of a swipe
    private boolean pressed_state = false; // true while touching the screen
    private Handler handler;
    private Runnable runnable;
    private float mFirstY; // x position of first screen touch
    private float mFirstX; // y position of first screen touch

    // Calendar vars
    private Vibrator vibe;
    private Calendar shown_date;
    private int current_color;
    private TextView current_color_element;
    private boolean shown_year;
    private APcalendar apcalendar;


    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @Override
    public View getView(Context paramContext) {
        //Log.d(TAG, "getView()" + paramContext.getPackageName());
        // Get widget version number
        try {
            PackageInfo pInfo = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
            this.version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Keep context
        this.mContext = paramContext;
        //Inflate layout as required. The layout here being inflated is "widget_blank"
        this.mView = LayoutInflater.from(paramContext).inflate(R.layout.widget_blank, null);
        // Set vibrator
        this.vibe = (Vibrator) paramContext.getSystemService(Context.VIBRATOR_SERVICE);

        // Set date to current
        this.shown_date = Calendar.getInstance();

        // Run Calendar class
        try {
            this.apcalendar = new APcalendar(this.mView, this.mContext, this.shown_date, this.current_color);
        } catch (Exception e) {
            this.errors = e.getMessage();
        }

        // Set default settings
        this.current_color = Color.parseColor("#efb171");
        this.current_color_element = (TextView) this.mView.findViewById(R.id.color5);
        this.current_color_element.setText("✔");
        this.shown_year = true;

        // About / Errors button event
        TextView settings = (TextView) this.mView.findViewById(R.id.about);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Pace Calendar Widget v"+widget.this.version+" by GreatApo & DarkThanos" + (errors.length() > 0 ? ", Errors: " + errors : ""), Toast.LENGTH_SHORT).show();
                widget.this.vibe.vibrate(10);
            }
        });


        //========= LONG PRESS AND SWIPE MANAGER =================
        // long press to run
        this.handler = new Handler();
        this.runnable = new Runnable() {
            @Override
            public void run(){
                if(pressed_state) {
                    //Open settings
                    Log.d("APcalendar", "Pop settings");
                    widget.this.vibe.vibrate(50);
                    widget.this.mView.findViewById(R.id.settings_box).setVisibility(View.VISIBLE);
                }
            }
        };
        // manager
        ConstraintLayout whole_view_calendar = (ConstraintLayout) this.mView.findViewById(R.id.container);
        whole_view_calendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String action;
                long down_duaration = motionEvent.getEventTime() - motionEvent.getDownTime();
                float dy = Math.abs(mFirstY-motionEvent.getY());
                float dx = Math.abs(mFirstX-motionEvent.getX());
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        action = "ACTION_DOWN";
                        mFirstY = motionEvent.getY();
                        mFirstX = motionEvent.getX();
                        //Trigger long press
                        handler.postDelayed(runnable, LONG_CLICK_DURATION);
                        pressed_state = true;
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        action = "ACTION_UP/CANCEL";
                        if(dx>dy){
                            if( dx>min_swipe_length & down_duaration<max_swipe_time){
                                if(mFirstX<motionEvent.getX()){
                                    //Swipe right
                                }else{
                                    //Swipe left
                                }
                            }
                        }else{
                            if( dy>min_swipe_length & down_duaration<max_swipe_time){
                                if(mFirstY<motionEvent.getY()){
                                    //Swipe up
                                    widget.this.shown_date.add(Calendar.MONTH, -1);
                                    widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
                                    widget.this.vibe.vibrate(10);
                                }else{
                                    //Swipe down
                                    widget.this.shown_date.add(Calendar.MONTH, 1);
                                    widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
                                    widget.this.vibe.vibrate(10);
                                }
                            }
                        }
                        //remove long press
                        if(pressed_state & (dx>10 || dy>10)) {
                            pressed_state = false;
                            handler.removeCallbacks(runnable);
                        }
                    case MotionEvent.ACTION_MOVE:
                        action = "ACTION_MOVE";
                        //remove long press
                        if(pressed_state & (dx>10 || dy>10)) {
                            pressed_state = false;
                            handler.removeCallbacks(runnable);
                        }
                        break;
                    default:
                        action = "unknown";
                        break;
                }

                //Log.d(TAG, "TouchEvent : " +action+" dx,dy:"+ dx+","+dy);
                return true;
            }
        });

        /*
        // Open with long press, problems on Stratos
        whole_view_calendar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(widget.this.mView.findViewById(R.id.settings_box).getVisibility()!=View.VISIBLE) {
                    widget.this.vibe.vibrate(50);
                    widget.this.mView.findViewById(R.id.settings_box).setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        */

        // Close Settings
        TextView close_settings = (TextView) this.mView.findViewById(R.id.close_settings);
        close_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.mView.findViewById(R.id.settings_box).setVisibility(View.GONE);
                widget.this.vibe.vibrate(10);
            }
        });

        // Change Color
        ((TextView) this.mView.findViewById(R.id.color1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.changeColor(R.id.color1, R.color.basecolor1);
            }
        });
        ((TextView) this.mView.findViewById(R.id.color2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.changeColor(R.id.color2, R.color.basecolor2);
            }
        });
        ((TextView) this.mView.findViewById(R.id.color3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.changeColor(R.id.color3, R.color.basecolor3);
            }
        });
        ((TextView) this.mView.findViewById(R.id.color4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.changeColor(R.id.color4, R.color.basecolor4);
            }
        });
        ((TextView) this.mView.findViewById(R.id.color5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.changeColor(R.id.color5, R.color.basecolor5);
            }
        });
        ((TextView) this.mView.findViewById(R.id.color6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.changeColor(R.id.color6, R.color.basecolor6);
            }
        });

        // Next Translation
        ((TextView) this.mView.findViewById(R.id.language)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.apcalendar.tranlations.nextLang();
                ((TextView) widget.this.mView.findViewById(R.id.language)).setText(widget.this.apcalendar.tranlations.getName());
                widget.this.apcalendar.refresh_days();
                widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
                widget.this.vibe.vibrate(10);
            }
        });

        // Year switch
        CheckBox s = (CheckBox) this.mView.findViewById(R.id.year_switch);
        s.setChecked(this.shown_year);
        if(!s.isChecked()){widget.this.mView.findViewById(R.id.textYear).setVisibility(View.GONE);}
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    widget.this.mView.findViewById(R.id.textYear).setVisibility(View.VISIBLE);
                    widget.this.shown_year = true;
                } else {
                    widget.this.mView.findViewById(R.id.textYear).setVisibility(View.GONE);
                    widget.this.shown_year = false;
                }
                widget.this.vibe.vibrate(10);
            }
        });

        // Monday switch
        s = (CheckBox) this.mView.findViewById(R.id.monday_switch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                widget.this.vibe.vibrate(10);
                widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
                widget.this.apcalendar.refresh_days();
            }
        });

        // Refresh current date
        TextView refresh = (TextView) this.mView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                widget.this.shown_date = now;
                widget.this.apcalendar.refresh(now, widget.this.current_color);

                String date_string = (new SimpleDateFormat("dd/MM/yyyy")).format(now.getTime());
                Toast.makeText(mContext, date_string, Toast.LENGTH_SHORT).show();
                widget.this.vibe.vibrate(10);
            }
        });

        // Prev Month
        TextView up = (TextView) this.mView.findViewById(R.id.arrow_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.shown_date.add(Calendar.MONTH, -1);
                widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
                widget.this.vibe.vibrate(10);
            }
        });

        // Next Month
        TextView down = (TextView) this.mView.findViewById(R.id.arrow_down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widget.this.shown_date.add(Calendar.MONTH, 1);
                widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
                widget.this.vibe.vibrate(10);
            }
        });

        return this.mView;
    }


    public void changeColor(int id, int color) {
        widget.this.current_color_element.setText(" ");
        widget.this.current_color = widget.this.mContext.getResources().getColor(color);
        widget.this.apcalendar.refresh(widget.this.shown_date, widget.this.current_color);
        widget.this.current_color_element = (TextView) widget.this.mView.findViewById(id);
        widget.this.current_color_element.setText("✔");
        widget.this.vibe.vibrate(10);
    }

    //Return the icon for this page, used when the page is disabled in the app list. In this case, the launcher icon is used
    @Override
    public Bitmap getWidgetIcon(Context paramContext) {
        return ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
    }

    //Return the launcher intent for this page. This might be used for the launcher as well when the page is disabled?
    @Override
    public Intent getWidgetIntent() {
        Intent localIntent = new Intent();
        /*localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        localIntent.setAction("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        localIntent.setComponent(new ComponentName(this.mContext.getPackageName(), "com.huami.watch.deskclock.countdown.CountdownListActivity"));*/
        return localIntent;
    }

    //Return the title for this page, used when the page is disabled in the app list. In this case, the app name is used
    @Override
    public String getWidgetTitle(Context paramContext) {
        return this.mContext.getResources().getString(R.string.app_name);
    }

    //Called when the page is shown
    @Override
    public void onActive(Bundle paramBundle) {
        super.onActive(paramBundle);
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    private void refreshView() {
        //Called when the page reloads, check for updates here if you need to
        //Refreshes calendar
        Calendar now = Calendar.getInstance();
        widget.this.shown_date = now;
        widget.this.apcalendar.refresh(now, widget.this.current_color);
    }

    //Returns the springboard host
    public ISpringBoardHostStub getHost() {
        return this.mHost;
    }

    //Called when the page is loading and being bound to the host
    @Override
    public void onBindHost(ISpringBoardHostStub paramISpringBoardHostStub) {
        Log.d(TAG, "onBindHost");
        //Store host
        this.mHost = paramISpringBoardHostStub;
    }

    //Called when the page is destroyed completely (in app mode). Same as the onDestroy method of an activity
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //Called when the page becomes inactive (the user has scrolled away)
    @Override
    public void onInactive(Bundle paramBundle) {
        super.onInactive(paramBundle);
        //Store active state
        this.mHasActive = false;
    }

    //Called when the page is paused (in app mode)
    @Override
    public void onPause() {
        super.onPause();
        this.mHasActive = false;
    }

    //Not sure what this does, can't find it being used anywhere. Best leave it alone
    @Override
    public void onReceiveDataFromProvider(int paramInt, Bundle paramBundle) {
        super.onReceiveDataFromProvider(paramInt, paramBundle);
    }

    //Called when the page is shown again (in app mode)
    @Override
    public void onResume() {
        super.onResume();
        //Check if view already loaded
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            this.mHasActive = true;
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    //Called when the page is stopped (in app mode)
    @Override
    public void onStop() {
        super.onStop();
        this.mHasActive = false;
    }
}
