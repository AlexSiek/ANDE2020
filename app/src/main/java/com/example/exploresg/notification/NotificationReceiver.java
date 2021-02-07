package com.example.exploresg.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {
    //

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification","NotificationReceiver has been triggered");
        Intent intent1 = new Intent(context, NotificationService.class);
        NotificationService.enqueueWork(context, intent1);
        Log.d("Notification","Notification intent hxas been started");
    }
}
