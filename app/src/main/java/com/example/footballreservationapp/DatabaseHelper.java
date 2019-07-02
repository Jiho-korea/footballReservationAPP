package com.example.footballreservationapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_NAME = "registrants";
    /* DatabaseHelper 의 생성자이다 중요한건 매개변수로 주는 version 이다 상승 시키면서 db를 업그레이드 가능하다 업그레이드시동작을
    onUpgrage 에서 정의하는데 주로 테이블 삭제후 재생성임 ㅇㅇ*/
    public DatabaseHelper(Context context) {
        super(context, "studentDB.db", null, 7);
        Log.d(TAG, "DataBaseHelper 생성자 호출");
    }
// onCreate 에선 테이블을 만드는 코드를 넣어줘야한다. 매개변수로 준 sqLiteDatabase 의 execSQL 메소드로 테이블을 만드는 쿼리를 주며 테이블생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Table onCreate");
        String createQuery = "CREATE TABLE " + TABLE_NAME +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "SID INTEGER NOT NULL, " +
                "SUBJECT TEXT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PHONE INTEGER," +
                "DATE TIME NOT NULL," +
                "PEOPLE TEXT NOT NULL, " +
                "STARTTIME TIME NOT NULL, " +
                "ENDTIME TIME NOT NULL, " +
                "RESERVATIONDAY STRING NOT NULL);";
        sqLiteDatabase.execSQL(createQuery);
    }

    // 위에서 말했듯이 테이블 삭제후 onCreate 를 호출해 재생성하는 동작을 한다. 버전업시 동작함
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "Table onUpgrade");
        String createQuery = "DROP TABLE IF EXISTS " + TABLE_NAME ;
        sqLiteDatabase.execSQL(createQuery);
        onCreate(sqLiteDatabase);
    }
}
