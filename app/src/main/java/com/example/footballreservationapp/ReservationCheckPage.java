package com.example.footballreservationapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ReservationCheckPage extends AppCompatActivity {
    ListView list;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check_page);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM registrants WHERE SID='21660003'",null);

        list = findViewById(R.id.myReservationList);

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(this, R.layout.myreservation,
                cursor,new String[]{"DATE","STARTTIME","ENDTIME","PEOPLE","RESERVATIONDAY"},
                new int[]{R.id.reservationday ,R.id.reservationstarttime,R.id.reservationendtime ,R.id.reservationpeople ,R.id.dayreserved});

        list.setAdapter(adapter);
    }


}
