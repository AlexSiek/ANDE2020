package com.example.exploresg;

public class HistoryItem {
    public String placeId, timeStamp;
    public int itemId;

    public HistoryItem(){

    }

    public HistoryItem(int itemId,String placeId,String timestamp){
        this.itemId = itemId;
        this.placeId = placeId;
        this.timeStamp = timestamp;
    }

    public int getItemId() {
        return itemId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
