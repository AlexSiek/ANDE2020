package com.example.ande;

public class SubRecycleritem {

    private int image;
    private String name;
    private float rating;
    private String vicinity;
    private boolean openStatus;

    public SubRecycleritem(int image, String name, float rating, String vicinity, boolean openStatus) {
        this.image = image;
        this.name = name;
        this.rating = rating;
        this.vicinity = vicinity;
        this.openStatus = openStatus;
    }

        public int getImage() { return this.image; }

        public String getName() { return this.name; }

        public float getRating() { return this.rating; }

        public String getVicinity() { return this.vicinity; }

        public boolean getOpenStatus() { return this.openStatus; }
}
