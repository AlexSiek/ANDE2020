package com.example.exploresg.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exploresg.DatabaseHandler;
import com.example.exploresg.R;

public class databaseTestActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_test_activity);
    }

    public void OnClick(View v){
        DatabaseHandler db = new DatabaseHandler(this);
        switch(v.getId()){
            case R.id.addDataButton:

                break;
            case R.id.fetchDataButton:
                db.getAllHistoryItems();
                break;
        }
    }
}
