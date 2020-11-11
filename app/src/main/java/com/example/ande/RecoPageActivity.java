package com.example.ande;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class RecoPageActivity extends AppCompatActivity implements LocationListener{
    //Fetch API
    private RequestQueue requestQueue;
    //Recycler items
    private RecyclerView subRecyclerView;
    private ArrayList<SubRecycleritem> locationItem = new ArrayList<>();
    //get location
    private FusedLocationProviderClient client;
    private double latitude = 0;
    private double longitude = 0;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recopage_activity);
        //GET location
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();

        }else {

        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, RecoPageActivity.this);

        }catch (Exception e){
            e.printStackTrace();
            Log.e("loc", e.toString());
        }

    }
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        //API
        requestQueue= Volley.newRequestQueue(this);
        //Parse to RecyclerView
        jsonParseReco();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void jsonParseReco(){
        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=1500&type=restaurant&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570";
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject results = jsonArray.getJSONObject(i);

                                String name = results.getString("name");;
                                String vicinity = results.getString("vicinity");
                                boolean open_now = false;
                                Double rating = 0.0;
                                if(results.has("open_now")){
                                    open_now = results.getBoolean("open_now");;
                                }
                                if(results.has("rating")){
                                    rating = results.getDouble("rating");;
                                }

                                locationItem.add(new SubRecycleritem(R.drawable.overeasy,name, rating, vicinity, open_now));
                            }
                            //Reference of RecyclerView
                            subRecyclerView = findViewById(R.id.recoLocationItems);
                            //Linear Layout Manager
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecoPageActivity.this, RecyclerView.VERTICAL, false);
                            //Set Layout Manager to RecyclerView
                            subRecyclerView.setLayoutManager(linearLayoutManager);

                            //Create adapter
                            SubRecycleritemArrayAdapter myRecyclerViewAdapter = new SubRecycleritemArrayAdapter(locationItem, new SubRecycleritemArrayAdapter.MyRecyclerViewItemClickListener()
                            {
                                //Handling clicks
                                @Override
                                public void onItemClicked(SubRecycleritem locationItem)
                                {
                                    Toast.makeText(RecoPageActivity.this, locationItem.getName(), Toast.LENGTH_SHORT).show();
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
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse (VolleyError error){
                        Toast.makeText(RecoPageActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        requestQueue.add(objectRequest);

    }
}
