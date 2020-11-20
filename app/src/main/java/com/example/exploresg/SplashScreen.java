package com.example.exploresg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    public static boolean firstTimeSetUp = true;
    public static boolean wantNotification = false;
    public static int location = 50;

    public static final String MyPREFERNCES = "userPref";
    public static final String ULocation = "locationPref";
    public static final String UNotification = "notificationPref";
    public static final String USetUp = "firstTimeSetUp";
    SharedPreferences prefs;
    private static final int NOTIFICATION_REMINDER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.splashscreen_activity);

        //tagline color
        TextView textView = findViewById(R.id.tagline);
        String text = "Take The Time, Explore Singapore";
        SpannableString ss = new SpannableString(text);
        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan green = new ForegroundColorSpan(0xFF008826);
        ss.setSpan(green, 9,13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(red, 23,32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);

        //Check and sets up for on first time
//        prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
//        boolean gFirstTimeSetUp = prefs.getBoolean(USetUp, true);
//        if(gFirstTimeSetUp){
//            firstTimeSetup();
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putInt(ULocation,50);
//            editor.putBoolean(UNotification, true);
//            editor.putBoolean(USetUp,false);
//            editor.commit();
//        }


        Thread splashThread = new Thread() {

            public void run() {
                try {
                    // sleep time in milliseconds (3000 = 3sec)
                    sleep(3000);
                }  catch(InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                } finally
                {
                    // Launch the MainActivity class
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        // To Start the thread
        splashThread.start();
    }

    public void firstTimeSetup(){
        settingUpNotification();
        Log.d("Alarm","ON from Set Up");

    }

    public void settingUpNotification(){
        //Creating a receiver intent
        Intent notifyIntent = new Intent(this,NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_REMINDER, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Alarm are services using the phone's system alarm
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //RTC_WAKEUP wakes up the device when it is off
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  1000 * 60 * 60 * 9,1000 * 60 * 60 * 24, pendingIntent);
        Log.d("Alarm","ON from Set Up");
    }
}
