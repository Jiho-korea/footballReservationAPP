package com.example.footballreservationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservationManageActivity extends AppCompatActivity {
    private ListView listView;
    private ManageReservationListAdapter adapter;
    private List<ManageReservation> manageReservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_manage);
        Intent intent = getIntent();
        Log.i("t", "onCreate; 실행됨 ");
        listView = (ListView)findViewById(R.id.reservationlistView);
        manageReservationList = new ArrayList<ManageReservation>();
        adapter = new ManageReservationListAdapter(getApplicationContext(),manageReservationList, this);
        listView.setAdapter(adapter);
        Log.i("t", "setAdapter; 실행됨 ");


        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("reservationList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            int sid, people, approval;
            String name, date, phone, subject, starttime, endtime, reservationday;
            while(count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count);
                sid = object.getInt("sid");
                people = object.getInt("people");
                date = object.getString("date");
                name = object.getString("name");
                phone = object.getString("phone");
                subject = object.getString("subject");
                starttime = object.getString("starttime");
                endtime = object.getString("endtime");
                reservationday = object.getString("reservationday");
                approval = object.getInt("approval");
                ManageReservation manageReservation = new ManageReservation(sid,name,date,subject,phone,starttime,endtime,people,reservationday,approval);

                manageReservationList.add(manageReservation);

                adapter.notifyDataSetChanged();
                Log.i("t", "notifyDataSetChanged; 실행됨 ");
                count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
