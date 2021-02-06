package com.example.exploresg.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.exploresg.LocationTracker;
import com.example.exploresg.R;
import com.example.exploresg.recyclerItems.SubRecycleritem;
import com.example.exploresg.recyclerItems.SubRecycleritemArrayAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.example.exploresg.activities.SettingActivity.MyPREFERNCES;
import static com.example.exploresg.activities.SettingActivity.ULocation;

public class NotificationActivity extends AppCompatActivity {

    private final ArrayList<SubRecycleritem> locationItem = new ArrayList<>();
    //Fetch API
    private RequestQueue requestQueue;
    //Recycler items
    //Location Settings
    private static final int REQUEST_CODE_PERMISSION = 2;
    // LocationTracker class
    LocationTracker gps;
    double latitude;
    double longitude;
    //Category
    private final ArrayList<String> placeTypes = new ArrayList<>();
    private final ArrayList<String> APIList = new ArrayList<>();
    private int counter = 0;

    public NotificationActivity() {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        //
        try {
            getLocation();

        //RecyclerView
        bindItemData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent i;
            switch (item.getItemId()) {
                case R.id.home:
                    i = new Intent(NotificationActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.history:
                    i = new Intent(NotificationActivity.this, HistoryActivity.class);
                    startActivity(i);
                    finish();
                    break;
                case R.id.saved:
                    i = new Intent(NotificationActivity.this, SavedLocationsActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }
            return true;
        });
        }catch (Exception e){
            ErrorPopup("An error has occurred. Please try again.");
        }
    }

    private void getLocation(){
        try {
            // LOCATION PERMISSION

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // create class object
                gps = new LocationTracker(NotificationActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    if(latitude == 0){
                        Thread.sleep(1000);
                        getLocation();
                        counter++;

                        if(counter == 5)
                        ErrorPopup("There is a problem getting your location. Please try again.");

                    }else{
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                                + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        requestQueue= Volley.newRequestQueue(this);

                        jsonParseReco();
                    }


                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            } else {
                requestLocationPermission();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Location is needed to explore places!")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(NotificationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION))
                    .setNegativeButton("cancel", (dialog, which) -> {
                        //if permission is denied
                        dialog.dismiss();
                        Intent i = new Intent(NotificationActivity.this, MainActivity.class);
                        startActivity(i);
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
        }
    }

    private void placeTypes(String category){
        switch (category) {
            case "Diners":
                placeTypes.add("bar");
                placeTypes.add("restaurant");
                placeTypes.add("cafe");
                break;
            case "Clothing":
                placeTypes.add("clothing_store");
                placeTypes.add("department_store");
                break;
            case "Scenery":
                placeTypes.add("aquarium");
                placeTypes.add("art_gallery");
                break;
            case "Adventure":
                placeTypes.add("amusement_park");
                placeTypes.add("tourist_attraction");
                placeTypes.add("bowling_alley");
                placeTypes.add("shopping_mall");
                placeTypes.add("museum");
                placeTypes.add("zoo");
                placeTypes.add("art_gallery");
                break;
        }

    }


    private void jsonParseReco(){
        //get radius
        SharedPreferences prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
        int gLocation = prefs.getInt(ULocation,50);// defValue is used to set value if pref doesn't exist. 50km is longest radius of Singapore
        int meters = gLocation * 1000;

        // create instance of Random class
        Random rand = new Random();
        String type;

        //Random Diners
        placeTypes("Diners");
            type = placeTypes.get(rand.nextInt((placeTypes.size()-1)));
            APIList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+ meters +"&type="+type+"&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A");
            placeTypes.clear();

        //Random Clothing
        placeTypes("Clothing");
            type = placeTypes.get(rand.nextInt((placeTypes.size()-1)));
            APIList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+ meters +"&type="+type+"&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A");
            placeTypes.clear();

        //Random Scenery
        placeTypes("Scenery");
            type = placeTypes.get(rand.nextInt((placeTypes.size()-1)));
            APIList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+ meters +"&type="+type+"&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A");
            placeTypes.clear();

        //Random Adventure
        placeTypes("Adventure");
            type = placeTypes.get(rand.nextInt((placeTypes.size()-1)));
            APIList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+ meters +"&type="+type+"&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A");
            placeTypes.clear();

            generateLocationObjects();

    }

    private void generateLocationObjects() {

        for(int i = 0; i < APIList.size() ; i++) {

            int finalI = i;
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    APIList.get(i),
                    null,
                    response -> {
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");
                            for (int k = 0; k < resultsArray.length(); k++) {
                                JSONObject results = resultsArray.getJSONObject(k);

                                if (results.has("photos")) {

                                    String businessStatus = "";
                                if(results.has("business_status"))
                                    businessStatus = results.getString("business_status");

                                if((businessStatus.equals("OPERATIONAL") ||businessStatus.equals("CLOSED_TEMPORARILY"))) {
                                    String name = results.getString("name");
                                    String vicinity = results.getString("vicinity");
                                        String placeId = results.getString("place_id");

                                        boolean open_now = false;
                                        double rating = 0.0;
                                        String ImgUrl;
                                        String photo_ref;
                                        if (results.has("opening_hours")) {
                                            JSONObject opening_hours = results.getJSONObject("opening_hours");
                                            open_now = opening_hours.getBoolean("open_now");
                                        }
                                            JSONArray photosArr = results.getJSONArray("photos");
                                            JSONObject PhotoResults = photosArr.getJSONObject(0);
                                            photo_ref = PhotoResults.getString("photo_reference");
                                            ImgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=110&photoreference=" + photo_ref + "&key=AIzaSyCck4O2J1amBwQVr0soFFaQcOmDiYvwY1A";

                                        if (results.has("rating")) {
                                            rating = results.getDouble("rating");
                                        }
                                        locationItem.add(new SubRecycleritem(ImgUrl, name, rating, vicinity, open_now, NotificationActivity.this, placeId));
                                        break;

                                }
                                }
                            }
                            if (finalI == APIList.size() - 1 ) {
                                try {
                                    setUIRef();
                                }catch(Exception e){
                                    ErrorPopup("An error has occurred. Please try again.");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    Intent i = new Intent(NotificationActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }


    private void setUIRef()
    {
        //Reference of RecyclerView
        //Recycler items
        RecyclerView subRecyclerView = findViewById(R.id.notificationItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        subRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        //Handling clicks
        SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, NotificationActivity.this, locationItem -> {
            Intent i1 = new Intent(NotificationActivity.this, RecoDetailActivity.class);
            i1.putExtra("placeID", locationItem.getPlaceId());
            ImageView img = findViewById(R.id.subImage);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NotificationActivity.this,  Pair.create(img,"imageTransition"));
            startActivity(i1,options.toBundle());
        });

        //Set adapter to RecyclerView
        subRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.notifyItemInserted(1);
    }

    @SuppressLint("SetTextI18n")
    private void bindItemData()
    {
        TextView distance = findViewById(R.id.distanceText);
        SharedPreferences prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
        int gLocation = prefs.getInt(ULocation,50);// defValue is used to set value if pref doesn't exist. 50km is longest radius of Singapore
        distance.setText("Within " + gLocation + "km");
    }

    protected void onRestart() {
        super.onRestart();
        APIList.clear();
        locationItem.clear();
        try {
            getLocation();
        }catch (Exception e){
            ErrorPopup("An error has occurred. Please try again.");
        }
    }

}
