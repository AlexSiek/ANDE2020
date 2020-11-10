package com.example.ande;

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

public class SubRecycleritemArrayAdapter extends RecyclerView.Adapter<SubRecycleritemArrayAdapter.MyViewHolder> {

    private ArrayList<SubRecycleritem> locationItem;
    private MyRecyclerViewItemClickListener mItemClickListener;
    public SubRecycleritemArrayAdapter(ArrayList<SubRecycleritem> locationItem, MyRecyclerViewItemClickListener itemClickListener) {
        this.locationItem = locationItem;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recycleritem, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(locationItem.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int imageId = locationItem.get(position).getImage();
        String name = locationItem.get(position).getName();
        float rating = locationItem.get(position).getRating();
        String vicinity = locationItem.get(position).getVicinity();
        boolean openStatus = locationItem.get(position).getOpenStatus();
        //Set Image
        holder.subImage.setImageResource(imageId);
        holder.name.setText(name);
        holder.rating.setRating(rating);
        holder.ratingNumber.setText(String.valueOf(rating));
        holder.vicinity.setText(vicinity);
        if (openStatus) {
            holder.openStatus.setText("Open");
        } else {
            holder.openStatus.setText("Closed");
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
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView subImage;
        private TextView name;
        private RatingBar rating;
        private TextView ratingNumber;
        private TextView vicinity;
        private TextView openStatus;

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
