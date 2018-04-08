package com.dinodevs.pacecalendarwidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Apostolos on 06/04/2018.
 */

public class APcalendar {

    // Months in english
    private static String[] locale_months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    // Instance's view objects
    private TextView view_monthName;
    private TextView view_year;
    private TextView[] view_boxes;
    private TextView[] need_color;
    private Context context;

    APcalendar(View view, Context context){
        this.getViewObjects(view, context);
    }

    APcalendar(View view, Context context, Calendar date, int current_color){
        this.getViewObjects(view, context);
        this.refresh(date, current_color);
    }

    private void getViewObjects(View view, Context context) {
        this.view_monthName = (TextView) view.findViewById(R.id.textMonth);
        this.view_year = (TextView) view.findViewById(R.id.textYear);
        this.context = context;


        this.view_boxes = new TextView[42];
        for (int i = 0; i < 42; i++) {
            this.view_boxes[i] = (TextView) view.findViewById(
                context.getResources().getIdentifier("calbox" + (i + 1), "id", context.getPackageName())
            );
        }

        this.need_color = new TextView[]{
                (TextView) view.findViewById(R.id.day1),
                (TextView) view.findViewById(R.id.day2),
                (TextView) view.findViewById(R.id.day3),
                (TextView) view.findViewById(R.id.day4),
                (TextView) view.findViewById(R.id.day5),
                (TextView) view.findViewById(R.id.day6),
                (TextView) view.findViewById(R.id.day7),
                (TextView) view.findViewById(R.id.arrow_down),
                (TextView) view.findViewById(R.id.arrow_up),
                (TextView) view.findViewById(R.id.close_settings)
        };

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
    }


    public void refresh(Calendar date, int current_color) {

        // Get Settings
        //SharedPreferences data = this.context.getApplicationContext().getSharedPreferences("Calendar_Data", 0);
        //String color = data.getString("color", "#efb171");

        // Coloring
        for (int i = 0 ; i < 9; i++) {
            this.need_color[i].setTextColor(current_color);
        }
        GradientDrawable BackgroundShape = (GradientDrawable)this.need_color[9].getBackground();
        BackgroundShape.setColor(current_color);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);

        Calendar now = Calendar.getInstance();
        int current_day = -1;
        if (year == now.get(Calendar.YEAR) && month == now.get(Calendar.MONTH)) {
            current_day = now.get(Calendar.DAY_OF_MONTH);
        }

        // Set month name
        this.view_monthName.setText( APcalendar.locale_months[month] );

        // Set year
        this.view_year.setText( year + "" );//or %100

        // Number to fill the boxes
        GregorianCalendar first = new GregorianCalendar(year, month, 1);
        int monthStart = first.get(Calendar.DAY_OF_WEEK) - 1;
        int monthDays = first.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Previous month
        Calendar previous = new GregorianCalendar(year, month, 1);
        previous.add(Calendar.MONTH, -1);
        int previousMonthDays = previous.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = monthStart - 1; i >= 0; i--) {
            this.view_boxes[i].setText(previousMonthDays - monthStart + i + 1 + "");
            this.view_boxes[i].setTextColor(Color.parseColor("#505050"));
            this.view_boxes[i].setBackgroundResource(android.R.color.transparent);
        }
        for (int i = 0; i < monthDays; i++) {
            this.view_boxes[monthStart + i].setText((i + 1) + "");
            if (i + 1 == current_day) {
                this.view_boxes[monthStart + i].setTextColor(Color.parseColor("#000000"));
                this.view_boxes[monthStart + i].setBackgroundResource(R.drawable.round_bg);

                BackgroundShape = (GradientDrawable)this.view_boxes[monthStart + i].getBackground();
                BackgroundShape.setColor(current_color);
            }
            else {
                this.view_boxes[monthStart + i].setTextColor(Color.parseColor("#FFFFFF"));
                this.view_boxes[monthStart + i].setBackgroundResource(android.R.color.transparent);
            }
        }
        for (int i = monthStart + monthDays; i < 42; i++) {
            this.view_boxes[i].setText(i - monthStart - monthDays + 1 + "");
            this.view_boxes[i].setTextColor(Color.parseColor("#505050"));
            this.view_boxes[i].setBackgroundResource(android.R.color.transparent);
        }

    }
}
