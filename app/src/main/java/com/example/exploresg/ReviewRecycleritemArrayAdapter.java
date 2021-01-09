package com.example.exploresg;

import android.graphics.Color;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReviewRecycleritemArrayAdapter extends RecyclerView.Adapter<ReviewRecycleritemArrayAdapter.MyViewHolder> {

    private ArrayList<Reviewitem> reviewItem;
    private ReviewRecycleritemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;
    public ReviewRecycleritemArrayAdapter(ArrayList<Reviewitem> reviewItem, ReviewRecycleritemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.reviewItem = reviewItem;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ReviewRecycleritemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycleritem, parent, false);

        //Create View Holder
        final ReviewRecycleritemArrayAdapter.MyViewHolder myViewHolder = new ReviewRecycleritemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(reviewItem.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecycleritemArrayAdapter.MyViewHolder holder, int position) {

        int profilePicture = reviewItem.get(position).getAuthorImage();
        String authorName = reviewItem.get(position).getAuthorName();
        double rating = reviewItem.get(position).getRating();
        String review = reviewItem.get(position).getReview();
        //Set Image
        holder.profilePicture.setImageResource(profilePicture);
        holder.authorName.setText(authorName);
        holder.rating.setRating((float) rating);
        holder.review.setText(review);
    }

    @Override
    public int getItemCount() {
        return reviewItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePicture;
        private TextView authorName;
        private RatingBar rating;
        private TextView review;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            authorName = itemView.findViewById(R.id.authorName);
            rating = itemView.findViewById(R.id.reviewRating);
            review = itemView.findViewById(R.id.review);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Reviewitem reviewitem);
    }

}
