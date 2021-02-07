package com.example.exploresg.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exploresg.notification.NotificationReceiver;
import com.example.exploresg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    public static boolean wantNotification = false;
    public static int location = 50;

    public static final String MyPREFERNCES = "userPref";
    public static final String ULocation = "locationPref";
    public static final String UNotification = "notificationPref";
    private static final int NOTIFICATION_REMINDER = 1;
    SharedPreferences prefs;
    SeekBar locationSeekBar;
    Switch notificationSwitch;
    TextView rangeValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        try {
            notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
            locationSeekBar = (SeekBar) findViewById(R.id.locationSeekBar);
            rangeValue = (TextView) findViewById(R.id.rangeValue);

            //Setting seekBar boundaries
            locationSeekBar.setMax(50);
            locationSeekBar.setMin(1);

            // Loading preferences
            prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
            int gLocation = prefs.getInt(ULocation, 50);// defValue is used to set value if pref doesn't exist. 50km is longest radius of Singapore
            boolean gNotification = prefs.getBoolean(UNotification, true);

            //  If notification is true set it up
            if (gNotification){
                switchNotification(true);
            }

            // Set loaded last set preferences
            String textValue = "";
            notificationSwitch.setChecked(gNotification);
            locationSeekBar.setProgress(gLocation);
            if (gLocation < 50) {
                textValue = "Within " + Integer.toString(gLocation) + "Km";
            } else {
                textValue = "Entire Singapore";
            }
            rangeValue.setText(textValue);

            locationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChangedValue = 0;
                String textValue = "";

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChangedValue = progress;
                    if (progressChangedValue < 50) {
                        textValue = "Within " + Integer.toString(progressChangedValue) + "Km";
                    } else {
                        textValue = "Entire Singapore";
                    }
                    rangeValue.setText(textValue);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(SettingActivity.this, "New location range set", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(ULocation, progressChangedValue);
                    editor.commit();
                }
            });

            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent i;
                    switch (item.getItemId()) {
                        case R.id.home:
                            i = new Intent(SettingActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            break;
                        case R.id.history:
                            i = new Intent(SettingActivity.this, HistoryActivity.class);
                            startActivity(i);
                            finish();
                            break;
                        case R.id.saved:
                            i = new Intent(SettingActivity.this, SavedLocationsActivity.class);
                            startActivity(i);
                            finish();
                            break;
                    }
                    return true;
                }
            });
        }catch(Exception e){
            ErrorPopup();
        }
    }
    private void ErrorPopup(){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("No internet connection. Please try again.")
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }


    public void onCheck(View v) {
        switch(v.getId()) {
            case R.id.notificationSwitch:
                settingNotification(notificationSwitch.isChecked());
                SharedPreferences.Editor editor = prefs.edit();
                String onOrOff = "off";
                if(notificationSwitch.isChecked()){
                    onOrOff = "on";
                }
                Toast.makeText(this, "Notification are turned "+ onOrOff, Toast.LENGTH_SHORT).show();
                editor.putBoolean(UNotification,notificationSwitch.isChecked());
                editor.commit();
                break;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        int gLocation = prefs.getInt(ULocation,50);// defValue is used to set value if pref doesn't exist. 50km is longest radius of Singapore
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ULocation,gLocation);
        editor.putBoolean(UNotification,notificationSwitch.isChecked());
        editor.commit();
    }

    public void settingNotification(boolean switchedOn){
        Log.d("Notification","Switch is triggered");
        switchNotification(switchedOn);
        if(switchedOn){
            Log.d("Notification","Alarm has been turned on");
        }else {
            Log.d("Notification","Alarm has been turned off");
        }
    }

    public void switchNotification(boolean isOn) {// Handle switching on and off of notification
        //Setting intent time/notification time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Log.d("Notification","Notification calendar time has been set");

        //Creating a receiver intent
        Intent notifyIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_REMINDER, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("Notification","Pending intent has been set");

        //Alarm are services using the phone's system alarm
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Log.d("Notification","Alarm manager has been set");

        if(isOn){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("Notification","Alarm has been turn on");
        }else{
            alarmManager.cancel(pendingIntent);
            Log.d("Notification","Alarm has been turn off");
        }
    }

}
