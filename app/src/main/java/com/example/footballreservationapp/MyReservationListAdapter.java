package com.example.footballreservationapp;

import android.content.Context;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class MyReservationListAdapter extends BaseAdapter {
    private Context context;
    private List<MyReservation> myReservationList;

    public MyReservationListAdapter(Context context, List<MyReservation> myReservationList){
        this.context = context;
        this.myReservationList = myReservationList;
        Log.i("t", "MyReservationListAdapter 생성자 실행 됨");
    }

    @Override
    public int getCount() {
        return myReservationList.size();
    }

    @Override
    public Object getItem(int position) {
        return myReservationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.myreservation, null);
        TextView reservationday = (TextView)v.findViewById(R.id.reservationday);
        TextView reservationstarttime = (TextView)v.findViewById(R.id.reservationstarttime);
        TextView reservationendtime = (TextView)v.findViewById(R.id.reservationendtime);
        TextView reservationpeople = (TextView)v.findViewById(R.id.reservationpeople);
        TextView dayreserved = (TextView)v.findViewById(R.id.dayreserved);
        Button reservationOK = (Button)v.findViewById(R.id.reservationOK);
        Button reservationWait = (Button)v.findViewById(R.id.reservationWait);
        Button cancleReservation = (Button)v.findViewById(R.id.cancleReservation);

        String[] tokens = myReservationList.get(position).getDate().split("-");
        String date = null;
        if(tokens[1].contains("0")){
            date = tokens[1].replace("0","") +  " / " +tokens[2];
        }else{
            date = tokens[1] + " / " + tokens[2];
        }


        reservationday.setText(date);
        reservationstarttime.setText(myReservationList.get(position).getStartTime());
        reservationendtime.setText(myReservationList.get(position).getEndTime());
        reservationpeople.setText(myReservationList.get(position).getPeople() + "");
        dayreserved.setText(myReservationList.get(position).getReservationday());
        Log.i("t", "MyReservationListAdapter getView 실행 됨");
        if(myReservationList.get(position).getApproval() == 0){
            Log.i("t", "MyReservationListAdapter getView 안 if 문 실행 됨");
            reservationOK.setVisibility(View.INVISIBLE);
            reservationWait.setVisibility(View.VISIBLE);
        }else{
            Log.i("t", "MyReservationListAdapter getView 안 if 문 실행 됨");
            reservationWait.setVisibility(View.INVISIBLE);
            reservationOK.setVisibility(View.VISIBLE);
        }

        cancleReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "예약 취소", Toast.LENGTH_SHORT).show();
            }
        });

        v.setTag(myReservationList.get(position).getSid());

        return v;
    }
}
