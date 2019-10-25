package com.example.footballreservationapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 이 클래스는 activity_reservatoion_check_page 레이아웃의 리스트뷰에 본인이한 예약현황을 띄워 주는 역할 합니다. 학번을 조건으로 검색합니다.
public class ReservationCheckPage extends AppCompatActivity {
    private ListView listView;
    private MyReservationListAdapter adapter;
    private List<MyReservation> myReservationList;

    int sid;
    String password;
    String subject;
    String name;
    String phone;
    int manager;
    //신청한 예약이 없을 때 출력할 View
    private View empty_my_reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check_page);

        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", 0);
        password = intent.getStringExtra("password");
        subject = intent.getStringExtra("subject");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        manager = intent.getIntExtra("manager",0);

        listView = findViewById(R.id.myReservationList);
        myReservationList = new ArrayList<MyReservation>();

        empty_my_reservation = getLayoutInflater().inflate(R.layout.ampty_mylist_item, null, false);
        addContentView(empty_my_reservation, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        empty_my_reservation.setVisibility(View.GONE);

        listView.setEmptyView(empty_my_reservation);
        adapter = new MyReservationListAdapter(getApplicationContext(),myReservationList, this);


        listView.setAdapter(adapter); // 어댑터 달기

        new MyReservationBackgroundTask().execute(sid+"");


    }



    class MyReservationBackgroundTask extends AsyncTask<String, Void, Void> {
        String sidClone;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            sidClone = args[0];
            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getJSONArray("response") != null){
                            try{
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                int count = 0;
                                int sid, people, approval;
                                String date, startTime, endTime, reservationday;
                                while(count < jsonArray.length()){
                                    JSONObject object = jsonArray.getJSONObject(count);
                                    sid = object.getInt("sid");
                                    date = object.getString("date");
                                    people = object.getInt("people");
                                    startTime = object.getString("startTime");
                                    endTime = object.getString("endTime");
                                    reservationday = object.getString("reservationday");
                                    approval = object.getInt("approval");
                                    MyReservation myReservation = new MyReservation(sid, date, people, startTime, endTime,reservationday, approval);
                                    myReservationList.add(myReservation);
                                    adapter.notifyDataSetChanged();
                                    Log.i("t", "myReservationList.add(myReservation);  실행 됨");
                                    count++;

                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(ReservationCheckPage.this , "나의 예약 리스트 읽기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            MyReservationRequest myReservationRequest = new MyReservationRequest(sidClone,responseListener);
            RequestQueue queue = Volley.newRequestQueue(ReservationCheckPage.this);
            queue.add(myReservationRequest);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

}
