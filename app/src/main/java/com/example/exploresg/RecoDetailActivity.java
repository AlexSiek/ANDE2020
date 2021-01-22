package com.example.exploresg;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class RecoDetailActivity extends AppCompatActivity {
    //Display
    private RecyclerView reviewRecyclerView;
    private ArrayList<Reviewitem> reviewItem = new ArrayList<>();
    private String placeId;
    //Fetch API
    private RequestQueue requestQueue;
    //Saved Item
    private ArrayList<SavedItem> savedItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recodetail_activity);
        Intent intent = getIntent();
        placeId = Objects.requireNonNull(intent.getExtras()).getString("placeID");
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.getAllSavedItems() != null)
            savedItem = db.getAllSavedItems();

        //RecyclerView
        requestQueue= Volley.newRequestQueue(this);

        //RecyclerView



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
                    "https://maps.googleapis.com/maps/api/place/details/json?place_id="+placeId+"&fields=photos,name,rating,formatted_address,reviews&key=AIzaSyADxiKqfRs0ttZ71BUc5HJ_3dZBTw2B570",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject results = response.getJSONObject("result");
                                    //Main declaration
                                    String photo_ref;
                                    String name = results.getString("name");
                                    double rating = 0.0;
                                    String ratingText = "";
                                    String vicinity = results.getString("formatted_address");
                                    String ImgUrl = "https://user-images.githubusercontent.com/24848110/33519396-7e56363c-d79d-11e7-969b-09782f5ccbab.png";
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
                                    vicinityView.setText(vicinity);

                                //Review declaration
                                if (results.has("reviews")) {
                                    String reviewProfilePic;
                                    String authorName;
                                    double reviewRating;
                                    String reviewText= "";

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

    private void setUIRef()
    {
        //Reference of RecyclerView
        reviewRecyclerView = findViewById(R.id.reviewItems);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecoDetailActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        reviewRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        ReviewRecycleritemArrayAdapter myRecyclerViewAdapter = new ReviewRecycleritemArrayAdapter(reviewItem, new ReviewRecycleritemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(Reviewitem reviewItem)
            {
                Toast.makeText(RecoDetailActivity.this, reviewItem.getAuthorName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        reviewRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
    private void ErrorPopup(String message){
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("ok", (dialog, which) -> {
                    Intent i = new Intent(RecoDetailActivity.this, MainActivity.class);
                    startActivity(i);
                })
                .create().show();
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.getAllSavedItems() != null)
            savedItem = db.getAllSavedItems();

        if (v.getId() == R.id.saveBtn) {
            if((db.getAllSavedItems() != null)  && savedItem.size() != 0) {
                for (int i = 0; i < savedItem.size(); i++) {
                    if (savedItem.get(i).getPlaceId().equals(placeId)) {
                        Toast.makeText(RecoDetailActivity.this, "Location removed", Toast.LENGTH_SHORT).show();
                        db.removeSavedItemByPlaceId(placeId);
                        return;
                    }
                }
                        Toast.makeText(RecoDetailActivity.this, "Location saved", Toast.LENGTH_SHORT).show();
                        db.addSavedItem(placeId);

            }else{
                Toast.makeText(RecoDetailActivity.this, "Location saved", Toast.LENGTH_SHORT).show();
                db.addSavedItem(placeId);
            }
        }
    }

}
