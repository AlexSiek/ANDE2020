package com.example.exploresg.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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


        //RecyclerView
        setSavedItems();

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
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, SavedLocationsActivity.this, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(SubRecycleritem locationItem)
            {
                Intent i1 = new Intent(SavedLocationsActivity.this, RecoDetailActivity.class);
                i1.putExtra("placeID", locationItem.getPlaceId());
                ImageView img = findViewById(R.id.subImage);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SavedLocationsActivity.this,  Pair.create(img,"imageTransition"));
                startActivity(i1, options.toBundle());
            }
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
                "https://maps.googleapis.com/maps/api/place/details/json?place_id="+savedItem.get(i).getPlaceId()+"&fields=photos,name,rating,vicinity,reviews&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                                ImgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=110&photoreference=" + photo_ref + "&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570";
                            }
                            if (results.has("rating")) {
                                rating = results.getDouble("rating");
                            }

                            locationItem.add(new SubRecycleritem(ImgUrl, name, rating, vicinity, open_now, SavedLocationsActivity.this, savedItem.get(finalI).getPlaceId()));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setUIRef();

                    }
                },
                error -> ErrorPopup("No internet connection. Please try again.")
        );
        requestQueue.add(objectRequest);

        }
    }

    private void ErrorPopup(String message){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(SavedLocationsActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }


}
