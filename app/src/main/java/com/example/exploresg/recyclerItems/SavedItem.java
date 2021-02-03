package com.example.exploresg.recyclerItems;

import com.example.exploresg.recyclerItems.SubRecycleritem;

public class SavedItem {
    public String placeId, timeStamp;
    public int itemId;
    public SubRecycleritem subRecycleritem;

    public SavedItem(){

    }

    public SavedItem(int itemId, String placeId, String timestamp){
        this.itemId = itemId;
        this.placeId = placeId;
        this.timeStamp = timestamp;
    }

    public SavedItem(int itemId, String placeId, String timestamp, SubRecycleritem subRecycleritem){
        this.itemId = itemId;
        this.placeId = placeId;
        this.timeStamp = timestamp;
        this.subRecycleritem = subRecycleritem;
    }

    public int getItemId() {
        return this.itemId;
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public SubRecycleritem getSubRecycleritem() { return this.subRecycleritem; }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setSubRecycleritem(SubRecycleritem subRecycleritem) { this.subRecycleritem = subRecycleritem; }
}
