package com.example.exploresg;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.example.exploresg.SettingActivity.MyPREFERNCES;
import static com.example.exploresg.SettingActivity.ULocation;
import static com.example.exploresg.SettingActivity.UNotification;


public class RecoPageActivity extends AppCompatActivity{
    //Fetch API
    private RequestQueue requestQueue;
    //Recycler items
    private RecyclerView subRecyclerView;
    private ArrayList<SubRecycleritem> locationItem = new ArrayList<>();
    //Location Settings
    private static final int REQUEST_CODE_PERMISSION = 2;
    // LocationTracker class
    LocationTracker gps;
    double latitude;
    double longitude;
    //Category
    private ArrayList<String> placeTypes = new ArrayList<>();
    private String category;
    private ArrayList<String> APIList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recopage_activity);
        Intent intent = getIntent();
        category = intent.getExtras().getString("CATEGORY");

        getLocation();
    }

    private void getLocation(){
        try {
            // LOCATION PERMISSION

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // create class object
                    gps = new LocationTracker(RecoPageActivity.this);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        if(latitude == 0){
                            LocationError();
                        }
                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                                + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        requestQueue= Volley.newRequestQueue(this);

                        jsonParseReco();
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
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RecoPageActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //if permission is denied
                            dialog.dismiss();
                            Intent i = new Intent(RecoPageActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_PERMISSION);
        }
    }

    private void placeTypes(String category){
        if(category.equals("Diners")){
            placeTypes.add("bar");
            placeTypes.add("restaurant");
            placeTypes.add("cafe");
        }else if(category.equals("Clothing")){
            placeTypes.add("clothing_store");
            placeTypes.add("department_store");
        }else if(category.equals("Scenery")){
            placeTypes.add("aquarium");
            placeTypes.add("art_gallery");
        }else if(category.equals("Adventure")){
            placeTypes.add("amusement_park");
            placeTypes.add("tourist_attraction");
            placeTypes.add("bowling_alley");
            placeTypes.add("shopping_mall");
            placeTypes.add("museum");
            placeTypes.add("zoo");
            placeTypes.add("art_gallery");
        }

    }

    private void jsonParseReco(){

        SharedPreferences prefs = getSharedPreferences(MyPREFERNCES, MODE_PRIVATE);
        int gLocation = prefs.getInt(ULocation,50);// defValue is used to set value if pref doesn't exist. 50km is longest radius of Singapore
        int meters = gLocation * 1000;
        placeTypes(category);
        String type ="";
        for(int i = 0; i < placeTypes.size(); i++){
            type = placeTypes.get(i);
            APIList.add("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius="+String.valueOf(meters)+"&type="+type+"&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570");
        }

        for(int i = 0; i < APIList.size(); i++) {
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    APIList.get(i),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject results = jsonArray.getJSONObject(i);

                                    String name = results.getString("name");
                                    ;
                                    String vicinity = results.getString("vicinity");
                                    boolean open_now = false;
                                    Double rating = 0.0;
                                    String ImgUrl = "https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png";
                                    String photo_ref = "";
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
                                        ;
                                    }
                                    locationItem.add(new SubRecycleritem(ImgUrl, name, rating, vicinity, open_now, RecoPageActivity.this));
                                }
                                //Reference of RecyclerView
                                subRecyclerView = findViewById(R.id.recoLocationItems);
                                //Linear Layout Manager
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecoPageActivity.this, RecyclerView.VERTICAL, false);
                                //Set Layout Manager to RecyclerView
                                subRecyclerView.setLayoutManager(linearLayoutManager);
                                //Create adapter
                                SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener() {
                                    //Handling clicks
                                    @Override
                                    public void onItemClicked(SubRecycleritem locationItem) {
                                        Toast.makeText(RecoPageActivity.this, locationItem.getName(), Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RecoPageActivity.this, RecoDetailActivity.class);
                                        startActivity(i);
                                    }
                                });

                                //Set adapter to RecyclerView
                                subRecyclerView.setAdapter(myRecyclerViewAdapter);
                                myRecyclerViewAdapter.notifyItemInserted(1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RecoPageActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    }
            );
            requestQueue.add(objectRequest);

        }

    }
    private void LocationError(){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There is a problem getting your location. Please try again.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(RecoPageActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .create().show();
    }
}
