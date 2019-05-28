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

        Cursor cursor = db.rawQuery("SELECT * FROM registrants",null);

        list = findViewById(R.id.myReservationList);

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,
                cursor,new String[]{"_id","DATE"},
                new int[]{android.R.id.text1, android.R.id.text2});

        list.setAdapter(adapter);
    }
}
