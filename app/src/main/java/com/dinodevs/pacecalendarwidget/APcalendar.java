package com.dinodevs.pacecalendarwidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by GreatApo on 06/04/2018.
 */

public class APcalendar {
    // Calendar variables
    private int color;
    private int year;
    private int month;
    private int day;
    public boolean isMondayFirst;

    // Calendar View objects
    private TextView view_monthName;
    private TextView view_year;
    private TextView[] view_boxes;
    private TextView[] view_boxes_rtl;
    private TextView[] view_colorable;

    // Calendar translations
    private APtranslations tranlations;

    // Constructor
    APcalendar(View view, Context context, Calendar date, int color){
        this.isMondayFirst = false;

        // Create translations instance
        this.tranlations = new APtranslations();

        // Init view objects
        this.initViewObjects(view, context);

        // Refresh calendar
        this.refresh(date, color);
        this.refreshDays();
    }

    // Initialize View objects
    private void initViewObjects(View view, Context context) {
        // Get header objects
        this.view_monthName = (TextView) view.findViewById(R.id.textMonth);
        this.view_year = (TextView) view.findViewById(R.id.textYear);

        // Get color-able view objects
        this.view_colorable = new TextView[]{
            (TextView) view.findViewById(R.id.day1),
            (TextView) view.findViewById(R.id.day2),
            (TextView) view.findViewById(R.id.day3),
            (TextView) view.findViewById(R.id.day4),
            (TextView) view.findViewById(R.id.day5),
            (TextView) view.findViewById(R.id.day6),
            (TextView) view.findViewById(R.id.day7),
            (TextView) view.findViewById(R.id.arrow_down),
            (TextView) view.findViewById(R.id.arrow_up)
        };

        // Get dates boxes
        this.view_boxes = new TextView[]{
            (TextView) view.findViewById(R.id.calbox1),
            (TextView) view.findViewById(R.id.calbox2),
            (TextView) view.findViewById(R.id.calbox3),
            (TextView) view.findViewById(R.id.calbox4),
            (TextView) view.findViewById(R.id.calbox5),
            (TextView) view.findViewById(R.id.calbox6),
            (TextView) view.findViewById(R.id.calbox7),
            (TextView) view.findViewById(R.id.calbox8),
            (TextView) view.findViewById(R.id.calbox9),
            (TextView) view.findViewById(R.id.calbox10),
            (TextView) view.findViewById(R.id.calbox11),
            (TextView) view.findViewById(R.id.calbox12),
            (TextView) view.findViewById(R.id.calbox13),
            (TextView) view.findViewById(R.id.calbox14),
            (TextView) view.findViewById(R.id.calbox15),
            (TextView) view.findViewById(R.id.calbox16),
            (TextView) view.findViewById(R.id.calbox17),
            (TextView) view.findViewById(R.id.calbox18),
            (TextView) view.findViewById(R.id.calbox19),
            (TextView) view.findViewById(R.id.calbox20),
            (TextView) view.findViewById(R.id.calbox21),
            (TextView) view.findViewById(R.id.calbox22),
            (TextView) view.findViewById(R.id.calbox23),
            (TextView) view.findViewById(R.id.calbox24),
            (TextView) view.findViewById(R.id.calbox25),
            (TextView) view.findViewById(R.id.calbox26),
            (TextView) view.findViewById(R.id.calbox27),
            (TextView) view.findViewById(R.id.calbox28),
            (TextView) view.findViewById(R.id.calbox29),
            (TextView) view.findViewById(R.id.calbox30),
            (TextView) view.findViewById(R.id.calbox31),
            (TextView) view.findViewById(R.id.calbox32),
            (TextView) view.findViewById(R.id.calbox33),
            (TextView) view.findViewById(R.id.calbox34),
            (TextView) view.findViewById(R.id.calbox35),
            (TextView) view.findViewById(R.id.calbox36),
            (TextView) view.findViewById(R.id.calbox37),
            (TextView) view.findViewById(R.id.calbox38),
            (TextView) view.findViewById(R.id.calbox39),
            (TextView) view.findViewById(R.id.calbox40),
            (TextView) view.findViewById(R.id.calbox41),
            (TextView) view.findViewById(R.id.calbox42)
        };

        // Get date boxes for rtl
        this.view_boxes_rtl = new TextView[]{
                (TextView) view.findViewById(R.id.calbox7),
                (TextView) view.findViewById(R.id.calbox6),
                (TextView) view.findViewById(R.id.calbox5),
                (TextView) view.findViewById(R.id.calbox4),
                (TextView) view.findViewById(R.id.calbox3),
                (TextView) view.findViewById(R.id.calbox2),
                (TextView) view.findViewById(R.id.calbox1),
                (TextView) view.findViewById(R.id.calbox14),
                (TextView) view.findViewById(R.id.calbox13),
                (TextView) view.findViewById(R.id.calbox12),
                (TextView) view.findViewById(R.id.calbox11),
                (TextView) view.findViewById(R.id.calbox10),
                (TextView) view.findViewById(R.id.calbox9),
                (TextView) view.findViewById(R.id.calbox8),
                (TextView) view.findViewById(R.id.calbox21),
                (TextView) view.findViewById(R.id.calbox20),
                (TextView) view.findViewById(R.id.calbox19),
                (TextView) view.findViewById(R.id.calbox18),
                (TextView) view.findViewById(R.id.calbox17),
                (TextView) view.findViewById(R.id.calbox16),
                (TextView) view.findViewById(R.id.calbox15),
                (TextView) view.findViewById(R.id.calbox28),
                (TextView) view.findViewById(R.id.calbox27),
                (TextView) view.findViewById(R.id.calbox26),
                (TextView) view.findViewById(R.id.calbox25),
                (TextView) view.findViewById(R.id.calbox24),
                (TextView) view.findViewById(R.id.calbox23),
                (TextView) view.findViewById(R.id.calbox22),
                (TextView) view.findViewById(R.id.calbox35),
                (TextView) view.findViewById(R.id.calbox34),
                (TextView) view.findViewById(R.id.calbox33),
                (TextView) view.findViewById(R.id.calbox32),
                (TextView) view.findViewById(R.id.calbox31),
                (TextView) view.findViewById(R.id.calbox30),
                (TextView) view.findViewById(R.id.calbox29),
                (TextView) view.findViewById(R.id.calbox42),
                (TextView) view.findViewById(R.id.calbox41),
                (TextView) view.findViewById(R.id.calbox40),
                (TextView) view.findViewById(R.id.calbox39),
                (TextView) view.findViewById(R.id.calbox38),
                (TextView) view.findViewById(R.id.calbox37),
                (TextView) view.findViewById(R.id.calbox36)
        };

        /*
        // Alternative way but not tested
        this.view_boxes = new TextView[42];
        for (int i = 0; i < 42; i++) {
            this.view_boxes[i] = (TextView) view.findViewById(
                context.getResources().getIdentifier("calbox" + (i + 1), "id", context.getPackageName())
            );
        }
        */
    }

    // Set calendar color
    private void setColor(int color) {
        // Save new color
        this.color = color;

        // Update Colors
        for(int i = this.view_colorable.length - 1; i >= 0; i--) {
            this.view_colorable[i].setTextColor(this.color);
        }
    }

    // Set date to show
    private void setDate(Calendar date) {
        this.year = date.get(Calendar.YEAR);
        this.month = date.get(Calendar.MONTH);

        Calendar now = Calendar.getInstance();
        this.day = -1;
        if (year == now.get(Calendar.YEAR) && month == now.get(Calendar.MONTH)) {
            this.day = now.get(Calendar.DAY_OF_MONTH);
        }
    }

    public void refresh(Calendar date, int color) {
        // Update colors
        this.setColor(color);
        // Refresh calendar
        this.refresh(date);
    }

    public void refresh(Calendar date) {
        // Update date
        this.setDate(date);
        // Refresh calendar
        this.refresh();
    }

    public void refresh() {
        // Change month name
        this.refreshMonthName();
        // Set year
        this.view_year.setText(String.valueOf(this.year));

        // Number to fill the boxes
        GregorianCalendar first = new GregorianCalendar(this.year, this.month, 1);
        int monthStart = first.get(Calendar.DAY_OF_WEEK) - 1;
        if(this.isMondayFirst){
            monthStart = (monthStart - 1 < 0) ? 6 : monthStart - 1;
        }
        int monthDays = first.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Previous month
        Calendar previous = new GregorianCalendar(this.year, this.month, 1);
        previous.add(Calendar.MONTH, -1);
        int previousMonthDays = previous.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Change rotation for right to left languages
        TextView[] temp_view_boxes = (this.tranlations.isRtL()) ? this.view_boxes_rtl.clone() : this.view_boxes.clone();

        for (int i = monthStart - 1; i >= 0; i--) {
            temp_view_boxes[i].setText(String.valueOf(previousMonthDays - monthStart + i + 1));
            temp_view_boxes[i].setTextColor(Color.parseColor("#505050"));
            temp_view_boxes[i].setBackgroundResource(android.R.color.transparent);
        }
        for (int i = 0; i < monthDays; i++) {
            temp_view_boxes[monthStart + i].setText(String.valueOf(i + 1));
            if (i + 1 == this.day) {
                temp_view_boxes[monthStart + i].setTextColor(Color.parseColor("#000000"));
                temp_view_boxes[monthStart + i].setBackgroundResource(R.drawable.round_bg);
                ((GradientDrawable) temp_view_boxes[monthStart + i].getBackground()).setColor(this.color);
            }
            else {
                temp_view_boxes[monthStart + i].setTextColor(Color.parseColor("#FFFFFF"));
                temp_view_boxes[monthStart + i].setBackgroundResource(android.R.color.transparent);
            }
        }
        for (int i = monthStart + monthDays; i < 42; i++) {
            temp_view_boxes[i].setText(String.valueOf(i - monthStart - monthDays + 1));
            temp_view_boxes[i].setTextColor(Color.parseColor("#505050"));
            temp_view_boxes[i].setBackgroundResource(android.R.color.transparent);
        }

    }

    public void refreshMonthName() {
        // Set month name
        this.view_monthName.setText(this.tranlations.getMonths()[this.month]);
    }

    public void refreshDays() {
        // Get days translations
        String[] days = this.tranlations.getDays();
        int n = days.length;
        int offset = (this.isMondayFirst)? 1 : 0;
        // Change days names
        for (int i = 0 ; i < 7; i++) {
            this.view_colorable[i].setText(   (days[(offset + i) % n].length()>3) ? days[(offset + i) % n].substring(0, 3) : days[(offset + i) % n] );
        }
    }

    public APtranslations getTranlations() {
        return this.tranlations;
    }
}
