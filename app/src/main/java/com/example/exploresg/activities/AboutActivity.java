package com.example.exploresg.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exploresg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        try {
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                Intent i;

                @SuppressLint("NonConstantResourceId")
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            i = new Intent(AboutActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            break;
                        case R.id.history:
                            i = new Intent(AboutActivity.this, HistoryActivity.class);
                            startActivity(i);
                            finish();
                            break;
                        case R.id.saved:
                            i = new Intent(AboutActivity.this, SavedLocationsActivity.class);
                            startActivity(i);
                            finish();
                            break;
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            ErrorPopup();
        }
    }
    private void ErrorPopup() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("An error has occurred. Please try again.")
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(AboutActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }

}