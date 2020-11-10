package com.example.ande;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    public static final boolean wantNotification = false;
    public static final int location = 50;

    public static final String MyPREFERNCES = "userPref";
    public static final String ULocation = "locationPref";
    public static final String UNotification = "notificationPref";
    SharedPreferences prefs;
    SeekBar locationSeekBar;
    Switch notificationSwitch;
    TextView rangeValue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        notificationSwitch = (Switch)findViewById(R.id.notificationSwitch);
        locationSeekBar = (SeekBar)findViewById(R.id.locationSeekBar);
        rangeValue = (TextView)findViewById(R.id.rangeValue);

        //Setting seekBar boundaries
        locationSeekBar.setMax(50);
        locationSeekBar.setMin(1);

        // Loading preferences
        prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
        int gLocation = prefs.getInt(ULocation,50);// defValue is used to set value if pref doesn't exist. 50km is longest radius of Singapore
        boolean gNotification = prefs.getBoolean(UNotification,false);

        // Set loaded last set preferences
        String textValue = "";
        notificationSwitch.setChecked(gNotification);
        locationSeekBar.setProgress(gLocation);
        if(gLocation < 50){
            textValue = "Within "+Integer.toString(gLocation)+"Km";
        }else{
            textValue = "Entire Singapore";
        }
        rangeValue.setText(textValue);

        locationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            String textValue = "";

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                if(progressChangedValue < 50){
                    textValue = "Within "+Integer.toString(progressChangedValue)+"Km";
                }else{
                    textValue = "Entire Singapore";
                }
                rangeValue.setText(textValue);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(SettingActivity.this, "New location range set",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(ULocation,progressChangedValue);
                editor.commit();
            }
        });
    }

    public void onCheck(View v) {
        switch(v.getId()) {
            case R.id.notificationSwitch:
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

}
