package com.example.ande;

public class SubRecycleritem {

    private int image;
    private String name;
    private double rating;
    private String vicinity;
    private boolean openStatus;

    public SubRecycleritem(int image, String name, double rating, String vicinity, boolean openStatus) {
        this.image = image;
        this.name = name;
        this.rating = rating;
        this.vicinity = vicinity;
        this.openStatus = openStatus;
    }

        public int getImage() { return this.image; }

        public String getName() { return this.name; }

        public double getRating() { return this.rating; }

        public String getVicinity() { return this.vicinity; }

        public boolean getOpenStatus() { return this.openStatus; }
}
