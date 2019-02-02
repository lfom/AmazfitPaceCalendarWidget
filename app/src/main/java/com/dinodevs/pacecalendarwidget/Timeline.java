package com.dinodevs.pacecalendarwidget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Timeline extends Activity {

    // Version
    private String version = Constants.VERSION;

    // Activity variables
    private boolean isActive = false;
    private Context mContext;
    private View mView;

    private ListView lv;
    private ArrayList<HashMap<String, String>> eventsList;

    private long next_event;
    private String calendarEvents;
    private String header_pattern;
    private String time_pattern;

    private static final String TITLE = "title";
    private static final String SUBTITLE = "subtitle";
    private static final String DOT = "dot";

    private static boolean is24h;


    // Set up the widget's layout
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save Activity variables
        this.mContext = this;
        setContentView(R.layout.widget_timeline);
        this.mView = this.findViewById(android.R.id.content);

        // Initialize variables
        Log.d(Constants.TAG, "Timeline: Starting...");
        this.init();

        // Attach event listeners
        Log.d(Constants.TAG, "Timeline: Attaching listeners...");
        this.initListeners();

        // Finish
        Log.d(Constants.TAG, "Timeline: Done...");
    }

    // Initialize widget
    private void init() {
        // Get widget version number
        try {
            PackageInfo pInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
            this.version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Constants.TAG, e.getLocalizedMessage(), e);
        }

        is24h = Settings.System.getString(mContext.getContentResolver(), "time_12_24").equals("24");
        Log.d(Constants.TAG, "Timeline init is24h: " + is24h);

        if (is24h) {
            header_pattern = Constants.HEADER_PATTERN_24H;
            time_pattern = Constants.TIME_PATTERN_24H;
        } else {
            header_pattern = Constants.HEADER_PATTERN_12H;
            time_pattern = Constants.TIME_PATTERN_12H;
        }

        // Show Time/Date
        refresh_time();

        // Calendar Events Data
        eventsList = new ArrayList<>();
        lv = this.mView.findViewById(R.id.list);

        loadCalendarEvents();
    }

    // Attach listeners
    @SuppressLint("ClickableViewAccessibility")
    private void initListeners(){
        // About button event
        TextView time = this.mView.findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_time();
                Timeline.this.toast("Calendar Widget v" + Timeline.this.version + " by GreatApo, LFOM & DarkThanos");
            }
        });
        // Refresh events
        time.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                refresh_time();
                loadCalendarEvents();
                Timeline.this.toast("Events were refreshed...");
                return true;
            }
        });
        // Scroll to top
        TextView top = this.mView.findViewById(R.id.backToTop);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView list = Timeline.this.mView.findViewById(R.id.list);
                list.setSelectionAfterHeaderView();
            }
        });
    }

    private void refresh_time(){
        TextView time = this.mView.findViewById(R.id.time);
        time.setText(dateToString(Calendar.getInstance(), header_pattern));
    }

    private void loadCalendarEvents() {
        eventsList = new ArrayList<>();
        next_event = 0;

        // Load data
        calendarEvents = Settings.System.getString(mContext.getContentResolver(), Constants.CALENDAR_DATA);

        if(calendarEvents !=null && !calendarEvents.isEmpty() ) {
            try {
                // Check if correct form of JSON
                JSONObject json_data = new JSONObject(calendarEvents);

                // If there are events
                if (json_data.has(Constants.EVENTS_DATA)) {
                    int event_number = json_data.getJSONArray(Constants.EVENTS_DATA).length();

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE, -10); // Show only future events + 10 minutes old
                    long current_time = calendar.getTimeInMillis();
                    String current_loop_date = "";

                    // Get data
                    for (int i = 0; i < event_number; i++) {
                        JSONArray data = json_data.getJSONArray(Constants.EVENTS_DATA).getJSONArray(i);
                        HashMap<String, String> event = new HashMap<>();

                        // adding each child node to HashMap key => value
                        event.put(TITLE, data.getString(0));
                        //event.put("description", data.getString(1));
                        //event.put("start", data.getString(2));
                        //event.put("end", data.getString(3));
                        //event.put("location", data.getString(4));
                        //event.put("account", data.getString(5));

                        String start;
                        String end = "";
                        String location = "";

                        if (!data.getString(2).equals("") && !data.getString(2).equals("null")) {
                            calendar.setTimeInMillis(Long.parseLong(data.getString(2)));

                            if (current_time > calendar.getTimeInMillis()) {
                                // Event expired, go to next
                                continue;
                            }
                            if (next_event == 0) // Hence this is the next event
                                next_event = calendar.getTimeInMillis();

                            start = dateToString(calendar, time_pattern);

                            // Insert day separator, or not :P
                            if (!current_loop_date.equals(dateToString(calendar, Constants.ELEMENT_PATTERN))) {
                                current_loop_date = dateToString(calendar, Constants.ELEMENT_PATTERN);
                                // Is it today?
                                if (current_loop_date.equals(dateToString(Calendar.getInstance(), Constants.ELEMENT_PATTERN))) {
                                    current_loop_date = getString(R.string.today);
                                }
                                HashMap<String, String> date_elem = new HashMap<>();
                                date_elem.put(TITLE, "");
                                date_elem.put(SUBTITLE, current_loop_date);
                                date_elem.put(DOT, "");
                                eventsList.add(date_elem);
                            }
                        } else {
                            // Event has no date, go to next
                            continue;
                        }

                        if (!data.getString(3).equals("") && !data.getString(3).equals("null")) {
                            calendar.setTimeInMillis(Long.parseLong(data.getString(3)));
                            end = " - " + dateToString(calendar, time_pattern);
                        }

                        //All day events
                        if ((start.startsWith("00") || start.startsWith("12")) && data.getString(3).equals("null")) {
                            start = getString(R.string.all_day);
                            end = "";
                        }

                        if (!data.getString(4).equals("") && !data.getString(4).equals("null")) {
                            location = "\n@ " + data.getString(4);
                        }
                        event.put(SUBTITLE, start + end + location);
                        event.put(DOT, mContext.getResources().getString(R.string.bull));
                        // adding events to events list
                        eventsList.add(event);
                    }
                } else {
                    HashMap<String, String> event = new HashMap<>();
                    event.put("title", getString(R.string.no_events));
                    //event.put("description", "-");
                    //event.put("start", "-");
                    //event.put("end", "-");
                    //event.put("location", "-");
                    //event.put("account", "-");
                    event.put(SUBTITLE, "-");
                    event.put(DOT, "");
                    eventsList.add(event);
                }
            } catch (JSONException e) {
                //default
                HashMap<String, String> event = new HashMap<>();
                event.put(TITLE, getString(R.string.no_events));
                //event.put("description", "-");
                //event.put("start", "-");
                //event.put("end", "-");
                //event.put("location", "-");
                //event.put("account", "-");
                event.put(SUBTITLE, "-");
                event.put(DOT, "");
                eventsList.add(event);
            }
        }

        if(eventsList.isEmpty()){
            HashMap<String, String> elem = new HashMap<>();
            elem.put("title", "\n"+mContext.getResources().getString(R.string.no_events));
            elem.put("subtitle", mContext.getResources().getString(R.string.no_events_description));
            elem.put("dot", "" );
            eventsList.add(elem);
        }

        ListAdapter adapter = new SimpleAdapter(mContext, eventsList, R.layout.list_item, new String[]{"title", "subtitle", "dot"}, new int[]{R.id.title, R.id.description, R.id.dot});
        lv.setAdapter(adapter);
    }


    // Toast wrapper
    private void toast (String message) {
        Toast toast = Toast.makeText(this.mContext, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    // Convert a date to format
    private String dateToString (Calendar date) {
        return (new SimpleDateFormat(Constants.DATE_PATTERN, Locale.US)).format(date.getTime());
    }
    private String dateToString (Calendar date, String pattern) {
        return (new SimpleDateFormat(pattern, Locale.US)).format(date.getTime());
    }

    private void onShow() {
        // If view loaded (and was inactive)
        if (this.mView != null && !this.isActive) {
            refresh_time();

            // If an event expired OR new events
            if ( next_event+10*1000 < Calendar.getInstance().getTimeInMillis()
                    || !calendarEvents.equals(Settings.System.getString(mContext.getContentResolver(), "CustomCalendarData")) ) {
                // Refresh timeline
                loadCalendarEvents();
            }
        }
        // Save state
        this.isActive = true;
    }

    private void onHide() {
        // Save state
        this.isActive = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.onHide();
    }
    @Override
    public void onStop() {
        super.onStop();
        this.onHide();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.onShow();
    }

}

