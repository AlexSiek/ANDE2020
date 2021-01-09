package com.example.exploresg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SavedLocationsActivity extends AppCompatActivity {

    private RecyclerView subRecyclerView;
    private ArrayList<SubRecycleritem> locationItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocations_activity);
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
                        Intent i = new Intent(SavedLocationsActivity.this, SavedLocationsActivity.class);
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
        subRecyclerView = findViewById(R.id.locationItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SavedLocationsActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        subRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(SubRecycleritem locationItem)
            {
                Toast.makeText(SavedLocationsActivity.this, locationItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        subRecyclerView.setAdapter(myRecyclerViewAdapter);
        //subRecyclerView.setNestedScrollingEnabled(false);
    }

    private void bindItemData()
    {
//        locationItem.add(new SubRecycleritem(R.drawable.test_overeasy,"OverEasy", 4.3, "In One Fullerton", true));
//        locationItem.add(new SubRecycleritem(R.drawable.test_overeasy,"teddddddddddddddddddddddddddddst", 4.3, "In One Fulleasdasdasdasdasdasrton", false));
//        locationItem.add(new SubRecycleritem(R.drawable.test_overeasy,"teddddddddddddddddddddddddddddst", 4.3, "In One Fulleasdasdasdasdasdasrton", false));
//        locationItem.add(new SubRecycleritem(R.drawable.test_overeasy,"teddddddddddddddddddddddddddddst", 4.3, "In One Fulleasdasdasdasdasdasrton", false));
//        locationItem.add(new SubRecycleritem(R.drawable.test_overeasy,"teddddddddddddddddddddddddddddst", 4.3, "In One Fulleasdasdasdasdasdasrton", false));

    }
}
