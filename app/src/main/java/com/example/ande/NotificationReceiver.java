package com.example.ande;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationReceiver","Reached this level");
        Intent intent1 = new Intent(context, NotificationService.class);
        context.startService(intent1);
    }
}
