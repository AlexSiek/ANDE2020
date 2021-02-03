package com.example.exploresg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploresg.R;
import com.example.exploresg.recyclerItems.SubRecycleritem;
import com.example.exploresg.recyclerItems.SubRecycleritemArrayAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView subRecyclerView;
    private ArrayList<SubRecycleritem> locationItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        //RecyclerView
        bindItemData();
        setUIRef();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.history:
                        break;
                    case R.id.saved:
                        Intent i = new Intent(NotificationActivity.this, SavedLocationsActivity.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });
    }

    private void setUIRef()
    {
        //Reference of RecyclerView
        subRecyclerView = findViewById(R.id.notificationItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        subRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, NotificationActivity.this, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(SubRecycleritem locationItem)
            {
                Toast.makeText(NotificationActivity.this, locationItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        subRecyclerView.setAdapter(myRecyclerViewAdapter);
        //subRecyclerView.setNestedScrollingEnabled(false);
    }

    private void bindItemData()
    {
        TextView distance = findViewById(R.id.distanceText);
        distance.setText("Within 5km");
        //locationItem.add(new SubRecycleritem("https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png","OverEasy", 4.3, "In One Fullerton", true, NotificationActivity.this));
        //locationItem.add(new SubRecycleritem("https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png","teddddddddddddddddddddddddddddst", 4.3, "In One Fulleasdasdasdasdasdasrton", false, NotificationActivity.this));

    }

}
