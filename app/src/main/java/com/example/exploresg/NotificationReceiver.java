package com.example.exploresg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    //

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification","NotificationReceiver has been triggered");
        Intent intent1 = new Intent(context, NotificationService.class);
//        intent1.putExtra() can be used to pass data
        NotificationService.enqueueWork(context, intent1);
//        context.startService(intent1);
        Log.d("Notification","Notification intent has been started");
    }
}
