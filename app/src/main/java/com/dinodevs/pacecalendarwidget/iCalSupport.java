package com.dinodevs.pacecalendarwidget;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.MapTimeZoneCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

class iCalSupport {

    iCalSupport() { }

    static boolean checkICSFile(Context context, String URL) {

        boolean result = false;
        WifiManager wfm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        String workDir = context.getCacheDir().getAbsolutePath();
        File newFile = new File(workDir + File.separator + "new_calendar.ics");
        File oldFile = new File(context.getFilesDir() + File.separator + "calendar.ics");

        if (wfm != null)
            result = wfm.isWifiEnabled();

        if (result)
            result = false;
        else {
            Log.w(Constants.TAG, "iCalSupport checkICSFile WiFi is off, checking local file...");
            return oldFile.exists();
        }

        String testURL = URL.toLowerCase();

        Log.d(Constants.TAG, "iCalSupport checkICSFile URL: " + URL);

        if (!URL.isEmpty() && (testURL.startsWith("http://") || testURL.startsWith("https://"))
                && (testURL.endsWith("ics"))) {

            try {
                result = new FilesUtil.urlToFile().execute(URL, workDir, "new_calendar.ics").get();
                if (result) {

                    System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());

                    FileInputStream in = new FileInputStream(newFile.getAbsolutePath());
                    CalendarBuilder builder = new CalendarBuilder();
                    net.fortuna.ical4j.model.Calendar calendar = builder.build(in);

                    if (calendar != null) {
                        if (calendar.getProperty("PRODID") != null)
                            Log.d(Constants.TAG, "iCalSupport checkICSFile PRODID: " + calendar.getProperty("PRODID").getValue());
                        if (calendar.getProperty("X-WR-CALDESC") != null)
                            Log.d(Constants.TAG, "iCalSupport checkICSFile CALDESC: " + calendar.getProperty("X-WR-CALDESC").getValue());
                        if (calendar.getProperty("X-WR-CALNAME") != null)
                            Log.d(Constants.TAG, "iCalSupport checkICSFile CALNAME: " + calendar.getProperty("X-WR-CALNAME").getValue());
                        if (calendar.getProperty("X-WR-TIMEZONE") != null)
                            Log.d(Constants.TAG, "iCalSupport checkICSFile TIMEZONE: " + calendar.getProperty("X-WR-TIMEZONE").getValue());

                        result = true;
                        if (oldFile.exists())
                            result = oldFile.delete();

                        if (newFile.exists() && result) {
                            result = newFile.renameTo(oldFile);
                            if (result) {
                                APsettings settings = new APsettings(Constants.TAG, context);
                                settings.set(Constants.PREF_CALENDAR_URL, URL);
                            }
                        } else
                            Log.w(Constants.TAG, "iCalSupport checkICSFile error moving newFile: " + newFile.getAbsolutePath());
                    } else {
                        Log.w(Constants.TAG, "iCalSupport checkICSFile bad ICS file!");
                        if (newFile.exists())
                            newFile.delete();
                    }

                }

            } catch (InterruptedException | ExecutionException | IOException | ParserException e) {
                Log.e(Constants.TAG, e.getLocalizedMessage(), e);
            }
        }

        return result;

    }

    static String getICSCalendarEvents(Context context) {

        int calendar_events_days = 30;
        Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents calendar_events_days: " + calendar_events_days);
        String jsonEvents = "{\"events\":[]}";

        File oldFile = new File(context.getFilesDir() + File.separator + "calendar.ics");
        net.fortuna.ical4j.model.Calendar calendar = null;
        if (oldFile.exists()) {
            try {
                Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents getting ics data");
                System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
                FileInputStream in = new FileInputStream(oldFile.getAbsolutePath());
                CalendarBuilder builder = new CalendarBuilder();
                calendar = builder.build(in);
            } catch (IOException | ParserException e) {
                Log.e(Constants.TAG, e.getLocalizedMessage(), e);
            }
        } else {
            Log.w(Constants.TAG, "iCalSupport getICSCalendarEvents file not found!");
            return null;
        }

        // Run query
        if (calendar != null) {
            Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents listing events");

            // create a period for the filter starting now with a duration of calendar_events_days + 1
            java.util.Calendar today = java.util.Calendar.getInstance();
            today.set(java.util.Calendar.HOUR_OF_DAY, 0);
            today.clear(java.util.Calendar.MINUTE);
            today.clear(java.util.Calendar.SECOND);
            Period period = new Period(new DateTime(today.getTime()), new Dur(calendar_events_days + 1, 0, 0, 0));
            Filter filter = new Filter(new PeriodRule(period));

            ComponentList events = (ComponentList) filter.filter(calendar.getComponents(Component.VEVENT));

            ComponentList eventList = new ComponentList();

            for (Object o : events) {
                VEvent event = (VEvent) o;

                if (event.getProperty("SUMMARY") != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event SU: " + event.getProperty("SUMMARY").getValue());
                if (event.getProperty("DESCRIPTION") != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event DC: " + event.getProperty("DESCRIPTION").getValue());
                if (event.getProperty("DTSTART") != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event DS: " + event.getProperty("DTSTART").getValue());
                Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event SD: " + event.getStartDate().getDate().getTime());
                if (event.getProperty("DTEND") != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event DE: " + event.getProperty("DTEND").getValue());
                if (event.getProperty("LOCATION") != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event LO: " + event.getProperty("LOCATION").getValue());
                //Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event: " + event.getProperty("URL").getValue());

                if (event.getProperty("RRULE") != null) {
                    PeriodList list = event.calculateRecurrenceSet(period);

                    for (Object po : list) {
                        try {
                            VEvent vEvent = (VEvent) event.copy();
                            Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents SU: " + vEvent.getSummary().getValue());
                            Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents RRULE: " + (Period) po
                                    + " \\ " + ((Period) po).getStart() + " \\ " + ((Period) po).getStart().getTime());
                            //Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents RRULE: " + (Period) po + " \\ " + ((Period) po).getStart().toString() + " \\ " + ((Period) po).getStart().getTime() + " \\ " + ((Period) po).getEnd().getTime());
                            vEvent.getStartDate().setDate(new DateTime(((Period) po).getStart()));
                            vEvent.getEndDate().setDate(new DateTime(((Period) po).getEnd()));
                            eventList.add(vEvent);
                            Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents SD: " + vEvent.getStartDate().getDate());
                        } catch (Exception e) {
                            Log.e(Constants.TAG, e.getLocalizedMessage(), e);
                        }
                    }

                } else
                    eventList.add(event);
            }

            Collections.sort(eventList, new Comparator<VEvent>() {
                public int compare(VEvent o1, VEvent o2) {
                    if (o1.getStartDate().getDate() == null || o2.getStartDate().getDate() == null)
                        return 0;
                    return o1.getStartDate().getDate().compareTo(o2.getStartDate().getDate());
                }
            });

            // Start formulating JSON
            jsonEvents = "{\"events\":[";

            // Use the cursor to step through the returned records
            for (Object o: eventList) {
                // Get the field values
                VEvent event = (VEvent) o;

                if (event.getSummary() != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event SU: " + event.getSummary().getValue());
                if (event.getDescription() != null)
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event DC: " + event.getDescription().getValue());
                if (event.getStartDate() != null) {
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event DS: " + event.getStartDate().getValue());
                    Log.d(Constants.TAG, "iCalSupport getICSCalendarEvents event SD: " + event.getStartDate().getDate().getTime());
                }

                String title = event.getSummary().getValue();
                String description = event.getDescription().getValue();
                long start = event.getStartDate().getDate().getTime();
                long end = event.getEndDate().getDate().getTime();
                String location = event.getLocation().getValue();
                String account = "ical4j";

                if (isEventAllDay(event)) {
                    Time timeFormat = new Time();
                    long offset = TimeZone.getDefault().getOffset(start);
                    if (offset < 0)
                        timeFormat.set(start - offset);
                    else
                        timeFormat.set(start + offset);
                    start = timeFormat.toMillis(true);
                    jsonEvents += "[ \"" + title + "\", \"" + description + "\", \"" + start + "\", \"" + null + "\", \"" + location + "\", \"" + account + "\"],";
                } else
                    jsonEvents += "[ \"" + title + "\", \"" + description + "\", \"" + start + "\", \"" + end + "\", \"" + location + "\", \"" + account + "\"],";
            }

            // Remove last "," from JSON
            if (jsonEvents.substring(jsonEvents.length() - 1).equals(",")) {
                jsonEvents = jsonEvents.substring(0, jsonEvents.length() - 1);
            }

            jsonEvents += "]}";


            // Check if there are new data
            String setting = Settings.System.getString(context.getContentResolver(), Constants.CALENDAR_DATA);
            if (jsonEvents.equals(setting)) {
                // No new data, no update
                Log.d(Constants.TAG, "iCalSupport calendar events: no new data");
                return null;
            }
            // Save new events as last send
            Settings.System.putString(context.getContentResolver(), Constants.CALENDAR_DATA, jsonEvents);

            return jsonEvents;

        } else {
            Log.w(Constants.TAG, "iCalSupport getICSCalendarEvents calendar is null");
            return null;
        }
    }

    private static boolean isEventAllDay(VEvent event){
        return event.getStartDate().toString().contains("VALUE=DATE");
    }

}
