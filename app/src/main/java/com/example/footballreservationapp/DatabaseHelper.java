package com.example.footballreservationapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_NAME = "REGISTRANTS";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "DataBaseHelper 생성자 호출");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Table onCreate");
        String createQuery = "CREATE TABLE " + TABLE_NAME +
                "( SID INTEGER PRIMARY KEY , "+
                "SUBJECT TEXT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PHONE INTEGER NOT NULL," +
                "DATE TEXT PRIMARY KEY," +
                "PEOPLE INTEGER PRIMARY KEY," +
                "STARTTIME TIME NOT NULL," +
                "ENDTIME TIME NOT NULL);";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "Table onUpgrade");
        String createQuery = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(createQuery);
    }
}
