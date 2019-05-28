package com.example.footballreservationapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_NAME = "registrants";

    public DatabaseHelper(Context context) {
        super(context, "studentDB.db", null, 4);
        Log.d(TAG, "DataBaseHelper 생성자 호출");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Table onCreate");
        String createQuery = "CREATE TABLE " + TABLE_NAME +
                "(_id INTEGER PRIMARY KEY, "+
                "SUBJECT TEXT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PHONE INTEGER NOT NULL," +
                "DATE TEXT NOT NULL," +
                "PEOPLE INTEGER NOT NULL, " +
                "STARTTIME TIME NOT NULL, " +
                "ENDTIME TIME NOT NULL);";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "Table onUpgrade");
        String createQuery = "DROP TABLE IF EXISTS " + TABLE_NAME ;
        sqLiteDatabase.execSQL(createQuery);
        onCreate(sqLiteDatabase);
    }
}
