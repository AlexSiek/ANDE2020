package com.example.exploresg;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class databaseTestActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_test_activity);
    }

    public void OnClick(View v){
        DatabaseHandler db = new DatabaseHandler(this);
        switch(v.getId()){
            case R.id.addDataButton:
                db.addHistoryItemTesting("ElMxNjIsIExhbmUgTnVtYmVyIDcsIEJsb2NrIEgsIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRpYQ", "1616112000200");
                db.addHistoryItemTesting("ElMxNjIsIExhbmUgTnVtYmVyIDcsIEJsb2NrIEgsIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRsdf", "1616112003000");
                db.addHistoryItemTesting("ElMxNjIsIExhbmUgTnVtYmVyIDcsIEJsb2NrIEgsIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkasdfwMDYyLCBJbmRpYQ", "1616112004000");
                db.addHistoryItemTesting("ElMxNjIsIExhbmUgTnVtYmVyIDcsIEJsb2sdfeesIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRpYQ", "1616025600000");
                db.addHistoryItemTesting("ElMxNjIsIExhbmUgTnVtYmVyIDcsIEJsasdsdfssIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRpYQ", "1615593600000");
                db.addHistoryItemTesting("ElMxNjIsIExhasdgTnVtYmVyIDcsIEJsb2NrIEgsIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRpYQ", "1615334400000");
                db.addHistoryItemTesting("ElMxNjIsIExhasdgTnVtYmVyIDcsIEJsb2NrIEgsIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRpYQ", "1615334405000");
                db.addHistoryItemTesting("ElMxNjIsIExhasdgTnVtYmVyIDcsIEJsb2NrIEgsIE5lYiBTYXJhaSwgU2FpbmlrIEZhcm0sIE5ldyBEZWxoaSwgRGVsaGkgMTEwMDYyLCBJbmRpYQ", "1615248000000");
                break;
            case R.id.fetchDataButton:
                db.getAllHistoryItems();
                break;
        }
    }
}
