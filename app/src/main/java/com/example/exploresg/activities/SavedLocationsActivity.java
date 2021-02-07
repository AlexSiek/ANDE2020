package com.example.exploresg.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.exploresg.DatabaseHandler;
import com.example.exploresg.R;
import com.example.exploresg.recyclerItems.SavedItem;
import com.example.exploresg.recyclerItems.SubRecycleritem;
import com.example.exploresg.recyclerItems.SubRecycleritemArrayAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SavedLocationsActivity extends AppCompatActivity {

    private final ArrayList<SubRecycleritem> locationItem = new ArrayList<>();
    private ArrayList<SavedItem> savedItem = new ArrayList<>();
    private RequestQueue requestQueue;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocations_activity);

        try {
            requestQueue = Volley.newRequestQueue(this);

            //load saved items from DB
            DatabaseHandler db = new DatabaseHandler(this);
            if (db.getAllSavedItems() != null)
                savedItem = db.getAllSavedItems();


            //RecyclerView
            setSavedItems();

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.home:
                        i = new Intent(SavedLocationsActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.history:
                        i = new Intent(SavedLocationsActivity.this, HistoryActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.saved:
                        break;
                }
                return true;
            });
        }catch (Exception e){
            ErrorPopup();
        }
    }

    private void setUIRef()
    {
        //Reference of RecyclerView
        RecyclerView subRecyclerView = findViewById(R.id.locationItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SavedLocationsActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        subRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        //Handling clicks
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, SavedLocationsActivity.this, locationItem -> {
            Intent i1 = new Intent(SavedLocationsActivity.this, RecoDetailActivity.class);
            i1.putExtra("placeID", locationItem.getPlaceId());
            startActivity(i1);
        });

        //Set adapter to RecyclerView
        subRecyclerView.setAdapter(myRecyclerViewAdapter);
        //subRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setSavedItems(){

        for(int i = 0 ; i < savedItem.size(); i++){
            int finalI = i;
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://maps.googleapis.com/maps/api/place/details/json?place_id="+savedItem.get(i).getPlaceId()+"&fields=photos,name,rating,vicinity,reviews&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A",
                null,
                    response -> {
                        try {
                            JSONObject results = response.getJSONObject("result");
                            String photo_ref;
                            String name = results.getString("name");
                            double rating = 0.0;
                            String vicinity = results.getString("vicinity");
                            boolean open_now = false;
                            String ImgUrl = "https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png";

                            if (results.has("opening_hours")) {
                                JSONObject opening_hours = results.getJSONObject("opening_hours");
                                open_now = opening_hours.getBoolean("open_now");
                            }
                            if (results.has("photos")) {
                                JSONArray photosArr = results.getJSONArray("photos");
                                JSONObject PhotoResults = photosArr.getJSONObject(0);
                                photo_ref = PhotoResults.getString("photo_reference");
                                ImgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=110&photoreference=" + photo_ref + "&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A";
                            }
                            if (results.has("rating")) {
                                rating = results.getDouble("rating");
                            }

                            locationItem.add(new SubRecycleritem(ImgUrl, name, rating, vicinity, open_now, SavedLocationsActivity.this, savedItem.get(finalI).getPlaceId()));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setUIRef();

                    },
                error -> ErrorPopup()
        );
        requestQueue.add(objectRequest);

        }
    }

    private void ErrorPopup(){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("No internet connection. Please try again.")
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(SavedLocationsActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        savedItem.clear();
        locationItem.clear();
        setContentView(R.layout.savedlocations_activity);
        requestQueue= Volley.newRequestQueue(this);

        //load saved items from DB
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.getAllSavedItems() != null)
            savedItem = db.getAllSavedItems();
        setSavedItems();

    }

}
