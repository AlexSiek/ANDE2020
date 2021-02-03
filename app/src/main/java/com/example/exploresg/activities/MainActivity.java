package com.example.exploresg.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
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
    private ArrayList<MainRecycleritem> imageCategories = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RecyclerView
        bindCategoryData();
        setUIRef();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //
        ImageButton btn = (ImageButton) findViewById(R.id.popup_menu);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.popup_menu);
                popup.show();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.home:
//                        i = new Intent(MainActivity.this, HistoryActivity.class);
//                        startActivity(i);
                        break;
                    case R.id.history:
                        i = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(i);
                        break;
                    case R.id.saved:
                        i = new Intent(MainActivity.this, SavedLocationsActivity.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });
    }

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
            case R.id.database_test:
                // do your code
                Intent intent2 = new Intent(this, NotificationActivity.class);
                startActivity(intent2);
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
        MainRecyclerItemArrayAdapter myRecyclerViewAdapter = new MainRecyclerItemArrayAdapter(imageCategories, new MainRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(MainRecycleritem category)
            {

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

    //Loaction Permission
    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Location is needed to explore places!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if permission is denied
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
        }
    }
}