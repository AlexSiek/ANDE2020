package com.example.exploresg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView subRecyclerView;
    private ArrayList<SubRecycleritem> locationItem = new ArrayList<>();
    private ArrayList<SavedItem> savedItem = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocations_activity);
        requestQueue= Volley.newRequestQueue(this);

        //load saved items from DB
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.getAllSavedItems() != null)
            savedItem = db.getAllSavedItems();

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
                        Intent i = new Intent(HistoryActivity.this, SavedLocationsActivity.class);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        subRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(SubRecycleritem locationItem)
            {
                Intent i1 = new Intent(HistoryActivity.this, RecoDetailActivity.class);
                i1.putExtra("placeID", locationItem.getPlaceId());
                startActivity(i1);            }
        });

        //Set adapter to RecyclerView
        subRecyclerView.setAdapter(myRecyclerViewAdapter);
        //subRecyclerView.setNestedScrollingEnabled(false);
    }

}
