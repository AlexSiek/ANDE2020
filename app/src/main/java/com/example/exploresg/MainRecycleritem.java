package com.example.exploresg;

public class MainRecycleritem {
    private int imageBtn;

    private String category;

    public MainRecycleritem(int imageBtn, String category)
    {
        this.category = category;
        this.imageBtn = imageBtn;

    }

    public int getImageBtn()
    {
        return imageBtn;
    }

    public String getCategory()
    {
        return category;
    }

}
