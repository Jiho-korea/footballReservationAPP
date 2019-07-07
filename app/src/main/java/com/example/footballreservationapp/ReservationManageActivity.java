package com.example.footballreservationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReservationManageActivity extends AppCompatActivity {
    private ListView listView;
    private ManageReservationListAdapter adapter;
    private List<ManageReservation> manageReservationList;
    private List<ManageReservation> saveManageReservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_manage);
        Intent intent = getIntent();
        listView = (ListView)findViewById(R.id.reservationlistView);
        manageReservationList = new ArrayList<ManageReservation>();
        saveManageReservationList = new ArrayList<ManageReservation>();
        adapter = new ManageReservationListAdapter(getApplicationContext(),manageReservationList, this, saveManageReservationList);
        listView.setAdapter(adapter);

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
                saveManageReservationList.add(manageReservation);

                adapter.notifyDataSetChanged();
                count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        EditText search = (EditText)findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchReservation(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void searchReservation(String search){
        manageReservationList.clear();
        for(int i = 0 ; i<saveManageReservationList.size(); i++) {
            if(saveManageReservationList.get(i).getDate().contains(search)){
                manageReservationList.add(saveManageReservationList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

}
