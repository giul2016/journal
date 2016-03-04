package fr.upem.journal.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import fr.upem.journal.receiver.AlarmReceiver;

public class NotificationService extends IntentService {

    private int nextNotificationId = 1;

    private final List<Integer> notificationHours = new ArrayList<>(Arrays.asList(8, 12, 20));

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

        Calendar nextFiringCalendar = getNextFiringCalendar();
        alarmManager.set(AlarmManager.RTC, nextFiringCalendar.getTimeInMillis(), pendingIntent);

        Log.d("ALARM", "Next alarm set to "+nextFiringCalendar.get(Calendar.HOUR)+"h ("+nextFiringCalendar.getTimeInMillis()+")" );
    }

    private Calendar getNextFiringCalendar() {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Log.d("ALARM", "current hour : "+currentCalendar.get(Calendar.HOUR)+":"+currentCalendar.get(Calendar.MINUTE)+":"+currentCalendar.get(Calendar.SECOND));

        Collections.sort(notificationHours);

        for(int hour : notificationHours) {
            Log.d("ALARM", "hour : "+hour);
            calendar.set(Calendar.HOUR, hour);
            Log.d("ALARM", calendar.getTimeInMillis()+" > "+currentCalendar.getTimeInMillis());
            if(calendar.getTimeInMillis() > currentCalendar.getTimeInMillis()) {
                Log.d("ALARM", "next hour : " + calendar.get(Calendar.HOUR) + " ("+hour+" - "+calendar.getTimeInMillis()+")");
                return calendar;
            }
        }

        // if no next hour found, calendar is set to first hour on the next day
        currentCalendar.set(Calendar.HOUR, notificationHours.get(0));
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        Log.d("ALARM", "no next hour found, set to : " + currentCalendar.get(Calendar.HOUR)+ " ("+currentCalendar.getTimeInMillis()+")");
        return currentCalendar;
    }
}
