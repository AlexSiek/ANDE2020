package com.example.exploresg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class SubRecycleritemArrayAdapter extends RecyclerView.Adapter<SubRecycleritemArrayAdapter.MyViewHolder> {

    private final ArrayList<SubRecycleritem> locationItem;
    Context mContext;
    private int lastPosition = -1;

    private final MyRecyclerViewItemClickListener mItemClickListener;
    public SubRecycleritemArrayAdapter(ArrayList<SubRecycleritem> locationItem, Context context, MyRecyclerViewItemClickListener itemClickListener) {
        this.locationItem = locationItem;
        this.mItemClickListener = itemClickListener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recycleritem, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(v -> mItemClickListener.onItemClicked(locationItem.get(myViewHolder.getLayoutPosition())));

        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            ((MyViewHolder)holder).itemView.startAnimation(animation);


            String imgUrl = locationItem.get(position).getImageUrl();
            String name = locationItem.get(position).getName();
            double rating = locationItem.get(position).getRating();
            String vicinity = locationItem.get(position).getVicinity();
            boolean openStatus = locationItem.get(position).getOpenStatus();
            Context context = locationItem.get(position).getContext();

            //Set Image
            //holder.subImage.setImageResource(imageId);
            Glide.with(context)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.subImage);
            holder.name.setText(name);
            holder.rating.setRating((float) rating);
            holder.ratingNumber.setText(String.valueOf(rating));
            holder.vicinity.setText(vicinity);
            if (openStatus) {
                holder.openStatus.setText("Open");
            } else {
                holder.openStatus.setText("Closed");
                holder.openStatus.setTextColor(Color.parseColor("#FF0000"));
            }


            lastPosition = holder.getAdapterPosition();
        }


    }

    @Override
    public int getItemCount() {
        return locationItem.size();
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
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView subImage;
        private final TextView name;
        private final RatingBar rating;
        private final TextView ratingNumber;
        private final TextView vicinity;
        private final TextView openStatus;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subImage = itemView.findViewById(R.id.subImage);
            name = itemView.findViewById(R.id.locationName);
            rating = itemView.findViewById(R.id.rating);
            ratingNumber = itemView.findViewById(R.id.ratingNumber);
            vicinity = itemView.findViewById(R.id.vicinity);
            openStatus = itemView.findViewById(R.id.openStatus);
        }

    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(SubRecycleritem locationItem);
    }
}
