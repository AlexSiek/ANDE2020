package com.example.exploresg.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.exploresg.DatabaseHandler;
import com.example.exploresg.R;
import com.example.exploresg.recyclerItems.HistoryItem;
import com.example.exploresg.recyclerItems.ReviewRecycleritemArrayAdapter;
import com.example.exploresg.recyclerItems.Reviewitem;
import com.example.exploresg.recyclerItems.SavedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class RecoDetailActivity extends AppCompatActivity {
    private final ArrayList<Reviewitem> reviewItem = new ArrayList<>();
    private String placeId;
    //Fetch API
    private RequestQueue requestQueue;
    //Saved Item
    private ArrayList<SavedItem> savedItem = new ArrayList<>();
    private String locationUrl;
    private final ArrayList<String> historyPlaceID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recodetail_activity);
        Intent intent = getIntent();
        placeId = Objects.requireNonNull(intent.getExtras()).getString("placeID");
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.getAllSavedItems() != null) {
            ImageView btn = findViewById(R.id.saveBtn);
            savedItem = db.getAllSavedItems();
            for (int i = 0; i < savedItem.size(); i++) {
                if(savedItem.get(i).getPlaceId().equals(placeId)){
                    btn.setImageResource(R.drawable.ic_baseline_bookmark_24);
                }
            }
        }

        if(db.getAllHistoryItems() != null) {
            //History item
            ArrayList<ArrayList<HistoryItem>> historyItems = db.getAllHistoryItems();
            for (int i = 0; i < historyItems.size(); i++) {
                for(int k = 0 ; k < historyItems.get(i).size(); k++){
                    historyPlaceID.add(historyItems.get(i).get(k).getPlaceId());
                    if(k == historyItems.get(i).size() - 1){
                        if(!historyPlaceID.contains(placeId)){
                            db.addHistoryItem(placeId);
                        }else{
                            db.removeHistoryItemByPlaceId(placeId);
                            db.addHistoryItem(placeId);
                        }
                    }
                }
            }
        }else{
            db.addHistoryItem(placeId);
        }

        //RecyclerView
        requestQueue= Volley.newRequestQueue(this);
        // To be called after fetching data - Set Selected Reco Detail Info
        setRecoDetailData();
    }


    private void setRecoDetailData(){
        // Declaration
        ImageView imageView = findViewById(R.id.restaurantImage);
        TextView nameView = findViewById(R.id.name);
        RatingBar ratingView = findViewById(R.id.rating);
        TextView ratingNumberView = findViewById(R.id.ratingNumber);
        TextView vicinityView = findViewById(R.id.location);

            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://maps.googleapis.com/maps/api/place/details/json?place_id="+placeId+"&fields=photos,name,rating,formatted_address,reviews,url&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570",
                    null,
                    response -> {
                        try {
                            JSONObject results = response.getJSONObject("result");
                                //Main declaration
                                String photo_ref;
                                String name = results.getString("name");
                                double rating = 0.0;
                                String ratingText = "";
                                String vicinity = results.getString("formatted_address");
                            StringBuilder formattedVicinity = new StringBuilder(vicinity);

                            if(vicinity.contains(",")){
                                    int lastComma = vicinity.lastIndexOf(",");
                                    formattedVicinity.setCharAt(lastComma+1, '\0');
                                    formattedVicinity.setCharAt(lastComma, '\n');
                                }


                                String ImgUrl = "https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png";
                                locationUrl = results.getString("url");
                                if (results.has("photos")) {
                                    JSONArray photosArr = results.getJSONArray("photos");
                                    JSONObject PhotoResults = photosArr.getJSONObject(0);
                                    photo_ref = PhotoResults.getString("photo_reference");
                                    ImgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=110&photoreference=" + photo_ref + "&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570";
                                }
                                if (results.has("rating")) {
                                    rating = results.getDouble("rating");
                                    ratingText = Double.toString(rating);
                                }
                                Glide.with(RecoDetailActivity.this)
                                        .load(ImgUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView);
                                nameView.setText(name);
                                ratingView.setRating((float) rating);
                                ratingNumberView.setText(ratingText);
                            if(vicinity.contains(",")){
                                vicinityView.setText(formattedVicinity);

                            }else{
                                vicinityView.setText(vicinity);
                            }

                            //Review declaration
                            if (results.has("reviews")) {
                                String reviewProfilePic;
                                String authorName;
                                double reviewRating;
                                String reviewText;

                                JSONArray reviewsArr = results.getJSONArray("reviews");
                                for (int i = 0; i < reviewsArr.length(); i++) {
                                    JSONObject reviewResults = reviewsArr.getJSONObject(i);
                                    reviewProfilePic = reviewResults.getString("profile_photo_url");
                                    authorName = reviewResults.getString("author_name");
                                    reviewRating = reviewResults.getDouble("rating");
                                    reviewText = reviewResults.getString("text");

                                    reviewItem.add(new Reviewitem(reviewProfilePic, authorName, reviewRating, reviewText, RecoDetailActivity.this));
                                }
                            }

                        } catch (Exception e) {
                           ErrorPopup();
                        }
                        setUIRef();

                    },
                    error -> ErrorPopup()
            );
            requestQueue.add(objectRequest);

        }

    private void setUIRef()
    {
        //Reference of RecyclerView
        //Display
        RecyclerView reviewRecyclerView = findViewById(R.id.reviewItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecoDetailActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        reviewRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        //Handling clicks
        ReviewRecycleritemArrayAdapter myRecyclerViewAdapter = new ReviewRecycleritemArrayAdapter(reviewItem, reviewItem -> Toast.makeText(RecoDetailActivity.this, reviewItem.getAuthorName(), Toast.LENGTH_SHORT).show());

        //Set adapter to RecyclerView
        reviewRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void ErrorPopup(){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("No internet connection. Please try again.")
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(RecoDetailActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        DatabaseHandler db = new DatabaseHandler(this);
        ImageView btn = findViewById(R.id.saveBtn);
        switch (v.getId()) {
            case R.id.saveBtn:
                if(db.getAllSavedItems() != null)
                    savedItem = db.getAllSavedItems();

                if (v.getId() == R.id.saveBtn) {
                    if((db.getAllSavedItems() != null)  && savedItem.size() != 0) {
                        for (int i = 0; i < savedItem.size(); i++) {
                            if (savedItem.get(i).getPlaceId().equals(placeId)) {
                                Toast.makeText(RecoDetailActivity.this, "Location removed", Toast.LENGTH_SHORT).show();
                                btn.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                                db.removeSavedItemByPlaceId(placeId);
                                return;
                            }
                        }
                        Toast.makeText(RecoDetailActivity.this, "Location saved", Toast.LENGTH_SHORT).show();
                        btn.setImageResource(R.drawable.ic_baseline_bookmark_24);
                        db.addSavedItem(placeId);

                    }else{
                        Toast.makeText(RecoDetailActivity.this, "Location saved", Toast.LENGTH_SHORT).show();
                        db.addSavedItem(placeId);
                        btn.setImageResource(R.drawable.ic_baseline_bookmark_24);

                    }
                }
                break;
            case R.id.googlemaps:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
                startActivity(browserIntent);
                break;
        }
    }


}
