package com.example.footballreservationapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
// 이 클래스는 activity_reservatoion_check_page 레이아웃의 리스트뷰에 본인이한 예약현황을 띄워 주는 역할 합니다. 학번을 조건으로 검색합니다.
public class ReservationCheckPage extends AppCompatActivity {
    ListView list;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check_page);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM registrants WHERE SID='21660003'",null); // 학번을 조건으로 검색 여기선 그냥 줬지만 나중에 로그인 기능이 생긴다면 자동으로 되야 겠지요ㅈ

        list = findViewById(R.id.myReservationList);
        //여기
        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(this, R.layout.myreservation, // 이레이아웃의
                cursor,new String[]{"DATE","STARTTIME","ENDTIME","PEOPLE","RESERVATIONDAY"}, // 이런 속성 정보들을
                new int[]{R.id.reservationday ,R.id.reservationstarttime,R.id.reservationendtime ,R.id.reservationpeople ,R.id.dayreserved}); // 이런이름의 레이아웃에 띄우는 겁니다.

        list.setAdapter(adapter); // 어댑터 달기
    }


}
