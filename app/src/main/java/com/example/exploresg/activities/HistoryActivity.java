package com.example.exploresg.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.example.exploresg.recyclerItems.HistoryItem;
import com.example.exploresg.R;
import com.example.exploresg.recyclerItems.SubRecycleritem;
import com.example.exploresg.recyclerItems.SubRecycleritemArrayAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView subRecyclerView;
    private ArrayList<SubRecycleritem> locationItem = new ArrayList<>();
    private ArrayList<ArrayList<HistoryItem>> historyItems = new ArrayList<>();
    private RequestQueue requestQueue;
    private final ArrayList<String> historyPlaceID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        requestQueue = Volley.newRequestQueue(this);

        //load saved items from DB
        DatabaseHandler db = new DatabaseHandler(this);
        if (db.getAllHistoryItems() != null)
            historyItems = db.getAllHistoryItems();

        setHistoryItems();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.home:
                        i = new Intent(HistoryActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.history:
                        i = new Intent(HistoryActivity.this, HistoryActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.saved:
                        i = new Intent(HistoryActivity.this, SavedLocationsActivity.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void setUIRef() {
        //Reference of RecyclerView
        subRecyclerView = findViewById(R.id.locationItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        subRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, HistoryActivity.this, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(SubRecycleritem locationItem) {
                Intent i1 = new Intent(HistoryActivity.this, RecoDetailActivity.class);
                i1.putExtra("placeID", locationItem.getPlaceId());
                startActivity(i1);
            }
        });

        //Set adapter to RecyclerView
        subRecyclerView.setAdapter(myRecyclerViewAdapter);
        //subRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setHistoryItems() {
        Log.d("History Activity", "Running setHistoryItems()");
        for (int i = 0; i < historyItems.size(); i++) {
            for (int a = 0; a < historyItems.get(i).size(); a++) {
                Log.d("HISTORY ITEM SIZE", String.valueOf(historyItems.get(0).size()));
                int finalI = i;
                Log.d("Fetched History Item from HistoryActivity", historyItems.get(i).get(a).getPlaceId());
                int finalA = a;
                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + historyItems.get(i).get(a).getPlaceId() + "&fields=photos,name,rating,vicinity,reviews&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570",
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

                                    locationItem.add(new SubRecycleritem(ImgUrl, name, rating, vicinity, open_now, HistoryActivity.this, historyItems.get(finalI).get(finalA).getPlaceId()));
                                    // Client Side Validation for History Dates
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
    }

    private void ErrorPopup(String message) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }

    protected void onRestart() {
        super.onRestart();
        locationItem.clear();
        historyItems.clear();
        setContentView(R.layout.history_activity);
        requestQueue = Volley.newRequestQueue(this);

        //load saved items from DB
        DatabaseHandler db = new DatabaseHandler(this);
        if (db.getAllHistoryItems() != null)
            historyItems = db.getAllHistoryItems();

        setHistoryItems();


    }

    public void onClick(View view) {
        DatabaseHandler db = new DatabaseHandler(this);
        String placeId;
        switch (view.getId()) {
            case R.id.clearAll:

                if(db.getAllHistoryItems() != null) {
                    //History item
                    for (int i = 0; i < historyItems.size(); i++) {
                        for(int k = 0 ; k < historyItems.get(i).size(); k++){
                            db.removeHistoryItemByPlaceId(historyItems.get(i).get(k).getPlaceId());

                        }
                    }
                    locationItem.clear();
                    historyItems.clear();
                    setContentView(R.layout.history_activity);
                    requestQueue = Volley.newRequestQueue(this);

                    //load saved items from DB
                    if (db.getAllHistoryItems() != null)
                        historyItems = db.getAllHistoryItems();

                    setHistoryItems();
                }
            break;
        }
    }
}
