package com.example.exploresg;

import android.content.Context;

public class Reviewitem {

    private String authorImage;
    private String authorName;
    private double rating;
    private String review;
    private Context context;

    public Reviewitem(String authorImage, String authorName, double rating, String review, Context context) {
        this.authorImage = authorImage;
        this.authorName = authorName;
        this.rating = rating;
        this.review = review;
        this.context = context;

    }

    public String getAuthorImage() { return this.authorImage; }

    public String getAuthorName() { return this.authorName; }

    public double getRating() { return this.rating; }

    public String getReview() { return this.review; }

    public Context getContext() { return this.context; }

}
