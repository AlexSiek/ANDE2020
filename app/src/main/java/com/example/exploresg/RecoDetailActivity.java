package com.example.exploresg;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecoDetailActivity extends AppCompatActivity {

    private RecyclerView reviewRecyclerView;
    private ArrayList<Reviewitem> reviewItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recodetail_activity);
        //RecyclerView
        bindItemData();
        setUIRef();


        // To be called after fetching data - Set Selected Reco Detail Info
        setRecoDetailData();
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

    private void bindItemData()
    {
        reviewItem.add(new Reviewitem(R.drawable.test_profile, "Tester", 5, "Very good food"));
        reviewItem.add(new Reviewitem(R.drawable.test_profile, "Tester", 5, "Very good food"));
        reviewItem.add(new Reviewitem(R.drawable.test_profile, "Tester", 5, "Very good food"));
        reviewItem.add(new Reviewitem(R.drawable.test_profile, "Tester", 5, "Very good food"));
    }

    private void setRecoDetailData() {
        // Declaration
        ImageView image = findViewById(R.id.restaurantImage);
        TextView name = findViewById(R.id.name);
        RatingBar rating = findViewById(R.id.rating);
        TextView ratingNumber = findViewById(R.id.ratingNumber);
        TextView vicinity = findViewById(R.id.location);
        TextView postalCode = findViewById(R.id.postalCode);

        // Assign Values Here
        image.setImageResource(R.drawable.test_overeasy_rect);
        name.setText("Overeasy");
        rating.setRating((float) 4.5);
        ratingNumber.setText("4.5");
        vicinity.setText("1 Fullterton Rd, #01-06, Singapore");
        postalCode.setText("049213");
    }

}
