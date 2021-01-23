package com.example.exploresg;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExploreSG_DB";
    private static final String TABLE_SAVED = "saved";
    private static final String TABLE_HISTORY = "history";
    private static final String KEY_ITEM_ID = "itemId";
    private static final String KEY_PLACE_ID = "placeId";
    private static final String KEY_TIMESTAMP = "timeStamp";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);

        // Create tables again
        onCreate(db);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SAVED_TABLE = "CREATE TABLE " + TABLE_SAVED + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PLACE_ID + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")";
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PLACE_ID + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_SAVED_TABLE);
        Log.d("Database","Created Saved Table");
        db.execSQL(CREATE_HISTORY_TABLE);
        Log.d("Database","Created History Table");
    }

    //Creates
    public void addSavedItem(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Timestamp generation, in milliseconds
        long timeStamp = Calendar.getInstance().getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, placeId);
        values.put(KEY_TIMESTAMP, timeStamp);

        db.insert(TABLE_SAVED, null, values);
        Log.d("Database","Inserted Saved Item With PlaceId of "+ placeId);
        db.close();
    }

    public void addHistoryItem(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Timestamp generation, in milliseconds
        long timeStamp = Calendar.getInstance().getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, placeId);
        values.put(KEY_TIMESTAMP, timeStamp);

        db.insert(TABLE_HISTORY, null, values);
        Log.d("Database","Inserted History Item With PlaceId of "+ placeId);
        db.close();
    }

    public void addHistoryItemTesting(String placeId, String timeStamp) {//DO NOT USE THIS, STRICTLY FOR TESTING
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_ID, placeId);
        values.put(KEY_TIMESTAMP, timeStamp);

        db.insert(TABLE_HISTORY, null, values);
        Log.d("Database","Inserted History Item With PlaceId of "+ placeId);

        db.close();
    }

    //Reads
    public ArrayList<ArrayList<HistoryItem>> getAllHistoryItems(){//Sorted by dates
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ArrayList<HistoryItem>> returnedArray = new ArrayList<>();
        //public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = db.query( TABLE_HISTORY,
                new String[] {KEY_ITEM_ID,KEY_PLACE_ID,KEY_TIMESTAMP},
                null,
                null,
                null,
                null,
                KEY_TIMESTAMP + " DESC",
                null );
        Log.d("Database","Retrieved HistoryItems");
        if(cursor != null){
            returnedArray = sortHistoryItemIntoArray(cursor);
            printHistoryResults(returnedArray);
            Log.d("Database","Retrieved Here");
            return returnedArray;
        }else{
            return null;
        }
    }

    public ArrayList<SavedItem> getAllSavedItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<SavedItem> returnedArray = new ArrayList<>();
        //public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor cursor = db.query( TABLE_SAVED,
                new String[] {KEY_ITEM_ID,KEY_PLACE_ID,KEY_TIMESTAMP},
                null,
                null,
                null,
                null,
                KEY_TIMESTAMP + " DESC",
                null );
        Log.d("Database","Retrieved SavedItems");

        if(cursor.getCount() != 0 ){

            cursor.moveToFirst();
            do{
                SavedItem savedItem = new SavedItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                returnedArray.add(savedItem);
            }while(cursor.moveToNext());

            return returnedArray;
        }else{
            return null;
        }
    }

//    public ArrayList<ArrayList<HistoryItem>> getTenHistoryItems(int pagination){//Sorted by dates
//        return null;
//    }
//
//    public ArrayList<SavedItem> getTenSavedItems(int pagination){
//        return null;
//    }

    //Deletes
    public void removeSavedItemByPlaceId(String placeId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED, KEY_PLACE_ID + "= ?", new String[] {placeId});
        db.close();
    }

    public void removeSavedItemByItemId(int itemId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED, KEY_ITEM_ID + "= ?", new String[] {String.valueOf(itemId)});
        db.close();
    }

    public void removeHistoryItemByPlaceId(String placeId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_PLACE_ID + "= ?", new String[] {placeId});
        db.close();
    }

    public void removeHistoryItemByItemId(int itemId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_ITEM_ID + "= ?", new String[] {String.valueOf(itemId)});
        db.close();
    }

    public boolean compareSameDate(long firstDate, long secondDate) {
        DateFormat df = new SimpleDateFormat("dd:MM:yyyy");
        Log.d("Database", "compareSameDate: "+ df.format(firstDate) +"vs"+df.format(secondDate)+ (df.format(firstDate).equals(df.format(secondDate))));
        return df.format(firstDate).equals(df.format(secondDate));
    }

    public ArrayList<ArrayList<HistoryItem>> sortHistoryItemIntoArray(Cursor dbResults){

        Log.d("Database","Storing Here");
        ArrayList<ArrayList<HistoryItem>> talliedArray = new ArrayList<ArrayList<HistoryItem>>();//Sorted array

        dbResults.moveToFirst();
        long currentDate = dbResults.getLong(2);
        do{
            ArrayList<HistoryItem> secondaryArray = new ArrayList<HistoryItem>();

            //While loop will always run at least once,
            while(dbResults.getPosition() < dbResults.getCount() && compareSameDate(currentDate, dbResults.getLong(2))){
                    secondaryArray.add(new HistoryItem(dbResults.getInt(0), dbResults.getString(1), dbResults.getString(2)));
                    dbResults.moveToNext();
            }
            talliedArray.add(secondaryArray);
            if(dbResults.getPosition() < dbResults.getCount()){//Makes sure that previous result wasn't the last one.
                currentDate = dbResults.getLong(2);
            }
        }while(dbResults.getPosition() < dbResults.getCount());
        return talliedArray;
    }

    public void printAllResults(Cursor dbResults){
        dbResults.moveToFirst();
        do{
            Log.d("Results", "ItemId: " + dbResults.getInt(0) + ", PlaceId: " + dbResults.getString(1) + ", TimeStamp: " + dbResults.getLong(2));
        }while(dbResults.moveToNext());
    }

    public void printHistoryResults(ArrayList<ArrayList<HistoryItem>> printingArr){
        DateFormat df = new SimpleDateFormat("dd:MM:yyyy");
        Log.d("Results","Number of dates: " + printingArr.size());
        for(int i = 0; i < printingArr.size(); i++){
            ArrayList<HistoryItem> recordsForADate = printingArr.get(i);
            Log.d("Results","New date - " + df.format(Long.parseLong(recordsForADate.get(0).getTimeStamp())));
            Log.d("Results", "Number of records in current date: " + recordsForADate.size());
            for(int e = 0; e < recordsForADate.size(); e++){
                Log.d("Results", "ItemId: " + recordsForADate.get(e).itemId + ", PlaceId: " + recordsForADate.get(e).placeId + ", TimeStamp: " + recordsForADate.get(e).timeStamp);
            }
        }
    }
}
