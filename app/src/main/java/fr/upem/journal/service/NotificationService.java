package fr.upem.journal.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.upem.journal.R;
import fr.upem.journal.receiver.AlarmReceiver;
import fr.upem.journal.utils.ObjectSerializer;

public class NotificationService extends IntentService {

    private int nextNotificationId = 1;

    //private final List<Integer> notificationHours = new ArrayList<>(Arrays.asList(8, 12, 20));

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent alarmIntent = new Intent(NotificationService.this, AlarmReceiver.class);
        intent.putExtra("id", nextNotificationId++);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationService.this, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // cancel the previous alarm set
        alarmManager.cancel(pendingIntent);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(preferences.getBoolean(getResources().getString(R.string.prefNotificationActiveKey), true)) {
            try {
                Calendar nextFiringCalendar = getNextFiringCalendar();
                alarmManager.set(AlarmManager.RTC, nextFiringCalendar.getTimeInMillis(), pendingIntent);

                Log.d("ALARM", "Next alarm set to " + nextFiringCalendar.get(Calendar.HOUR_OF_DAY) + "h (" + nextFiringCalendar.getTimeInMillis() + ")");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("ALARM", "notification not active");
        }
    }

    private Calendar getNextFiringCalendar() {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Log.d("ALARM", "current hour : " + currentCalendar.get(Calendar.HOUR_OF_DAY) + ":" + currentCalendar.get(Calendar.MINUTE) + ":" + currentCalendar.get(Calendar.SECOND));

        ArrayList<Integer> notificationHours = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> hours = preferences.getStringSet(getResources().getString(R.string.prefNotificationHoursKey), new HashSet<>(Arrays.asList("8", "12", "20")));

        /*try {
            notificationHours = (ArrayList<Integer>) ObjectSerializer.deserialize(
                    preferences.getString(
                            "notification_hours",
                            ObjectSerializer.serialize(new ArrayList<>(Arrays.asList(8, 12, 20)))
                    )
            );
        } catch (Exception e) {
            notificationHours = new ArrayList<>(Arrays.asList(8, 12, 20));
        }*/
        for(String hour : hours) {
            notificationHours.add(Integer.parseInt(hour));
        }
        Collections.sort(notificationHours);

        for(int hour : notificationHours) {
            Log.d("ALARM", "hour : "+hour);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            Log.d("ALARM", calendar.getTimeInMillis()+" > "+currentCalendar.getTimeInMillis());
            if(calendar.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                Log.d("ALARM", "next hour : " + calendar.get(Calendar.HOUR_OF_DAY) + " ("+calendar.getTimeInMillis()+")");
                return calendar;
            }
        }
        if(notificationHours.isEmpty()) {
            throw new IllegalStateException("Notification hours list is empty");
        }

        // if no next hour found, calendar is set to first hour on the next day
        currentCalendar.set(Calendar.HOUR_OF_DAY, notificationHours.get(0));
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        Log.d("ALARM", "no next hour found, set to : " + currentCalendar.get(Calendar.HOUR_OF_DAY)+ " ("+currentCalendar.getTimeInMillis()+")");
        return currentCalendar;
    }
}
