package com.example.ande;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        subRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
    }

    private void bindItemData()
    {
        locationItem.add(new SubRecycleritem(R.drawable.overeasy,"OverEasy", 3, "In One Fullerton", true));
    }
}
