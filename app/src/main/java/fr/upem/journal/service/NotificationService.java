package fr.upem.journal.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationService extends IntentService {

    private int nextNotificationId = 1;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("Journal")
                .setContentText("You have news to read !");

        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(nextNotificationId++, builder.build());

    }
}
