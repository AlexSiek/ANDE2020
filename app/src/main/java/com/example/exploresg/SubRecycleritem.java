package com.example.exploresg;

import android.content.Context;

public class SubRecycleritem  {

    private String ImgUrl;
    private String name;
    private double rating;
    private String vicinity;
    private boolean openStatus;
    private Context context;

    public SubRecycleritem(String ImgUrl, String name, double rating, String vicinity, boolean openStatus, Context context) {
        this.ImgUrl = ImgUrl;
        this.name = name;
        this.rating = rating;
        this.vicinity = vicinity;
        this.openStatus = openStatus;
        this.context = context;

    }

        public String getImageUrl() { return this.ImgUrl; }

        public String getName() { return this.name; }

        public double getRating() { return this.rating; }

        public String getVicinity() { return this.vicinity; }

        public boolean getOpenStatus() { return this.openStatus; }

        public Context getContext() { return this.context; }

}
