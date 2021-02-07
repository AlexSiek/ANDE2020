package com.example.exploresg.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploresg.LocationTracker;
import com.example.exploresg.recyclerItems.MainRecyclerItemArrayAdapter;
import com.example.exploresg.recyclerItems.MainRecycleritem;
import com.example.exploresg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //RecyclerView
    private RecyclerView mRecyclerView;
    private final ArrayList<MainRecycleritem> imageCategories = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSION = 2;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            //RecyclerView
            bindCategoryData();
            setUIRef();
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            //
            ImageButton btn = (ImageButton) findViewById(R.id.popup_menu);

            btn.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.popup_menu);
                popup.show();
            });

            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.history:
                        i = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.saved:
                        i = new Intent(MainActivity.this, SavedLocationsActivity.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            });
        }catch (Exception e){
            ErrorPopup();
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.setting_item:
                // do your code
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.about_item:
                // do your code
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    private void setUIRef()
    {
        //Reference of RecyclerView
        mRecyclerView = findViewById(R.id.mainPageButtons);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        //Handling clicks
        MainRecyclerItemArrayAdapter myRecyclerViewAdapter = new MainRecyclerItemArrayAdapter(imageCategories, category -> {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // create class object
                LocationTracker gps = new LocationTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    //intent
                    Intent i = new Intent(MainActivity.this, RecoPageActivity.class);
                    i.putExtra("CATEGORY", category.getCategory());
                    startActivity(i);
                } else {
                    gps.showSettingsAlert();
                }
            }else {
                requestLocationPermission();
            }

        });

        //Set adapter to RecyclerView
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void bindCategoryData()
    {
        imageCategories.add(new MainRecycleritem(R.drawable.img_diner,"Diners"));
        imageCategories.add(new MainRecycleritem(R.drawable.img_clothing,"Clothing"));
        imageCategories.add(new MainRecycleritem(R.drawable.img_scenery,"Scenery"));
        imageCategories.add(new MainRecycleritem(R.drawable.img_adventure,"Adventure"));

    }

    //Location Permission
    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Location is needed to explore places!")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION))
                    .setNegativeButton("cancel", (dialog, which) -> {
                        //if permission is denied
                        dialog.dismiss();
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
        }
    }

    private void ErrorPopup() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("An error has occurred. Please try again.")
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }
}