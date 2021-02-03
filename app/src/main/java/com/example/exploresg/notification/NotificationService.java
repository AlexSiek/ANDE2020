package com.example.exploresg.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.exploresg.R;
import com.example.exploresg.activities.NotificationActivity;

public class NotificationService extends JobIntentService {
    //
    private static final String NOTIFICATION_CHANNEL_ID = "404";
    static final int JOB_ID = 1000;
    private NotificationManager notificationManager;

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d("Notification","Intent is being handled");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Daily Random Location");
        builder.setContentText("New day, new locations!");
        builder.setSmallIcon(R.drawable.icon);
        Log.d("Notification","Notification has been built");

        //Sets up the intent to build when notification is clicked
        Intent notifyIntent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("Notification","Intents has been set up");

        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
        Log.d("Notification","Notification has been fired");
    }

}
