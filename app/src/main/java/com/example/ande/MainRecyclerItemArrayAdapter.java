package com.example.ande;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainRecyclerItemArrayAdapter extends RecyclerView.Adapter<MainRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<MainRecycleritem> imageCategories;
    private MyRecyclerViewItemClickListener mItemClickListener;
    public MainRecyclerItemArrayAdapter(ArrayList<MainRecycleritem> categories, MyRecyclerViewItemClickListener itemClickListener) {
        this.imageCategories = categories;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycleritem, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(imageCategories.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int id =imageCategories.get(position).getImageBtn();
        //Set Image
        holder.imageViewBtn.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return imageCategories.size();
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
        private ImageView imageViewBtn;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewBtn = itemView.findViewById(R.id.mainImageBtn);

        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(MainRecycleritem categories);
    }
}
