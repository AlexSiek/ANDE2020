package com.example.exploresg;

public class Reviewitem {

    private int authorImage;
    private String authorName;
    private double rating;
    private String review;

    public Reviewitem(int authorImage, String authorName, double rating, String review) {
        this.authorImage = authorImage;
        this.authorName = authorName;
        this.rating = rating;
        this.review = review;
    }

    public int getAuthorImage() { return this.authorImage; }

    public String getAuthorName() { return this.authorName; }

    public double getRating() { return this.rating; }

    public String getReview() { return this.review; }
}
