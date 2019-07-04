package com.example.footballreservationapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReservationListAdapter extends BaseAdapter {
    private Context context;
    private List<Reservation> reservationList;

    public ReservationListAdapter(Context context, List<Reservation> reservationList){
        this.context = context;
        this.reservationList = reservationList;
    }

    @Override
    public int getCount() {
        return reservationList.size();
    }

    @Override
    public Object getItem(int position) {
        return reservationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("t", "ReservationListAdapter getView 실행 됨");
        View v = View.inflate(context, R.layout.reservationinthatday, null);
        TextView usingStartTime = (TextView)v.findViewById(R.id.usingstarttime);
        TextView usingEndTime = (TextView)v.findViewById(R.id.usingendtime);
        TextView person = (TextView)v.findViewById(R.id.personwhoreserve);
        TextView usingPersonNum = (TextView)v.findViewById(R.id.usingpersonnumber);

        usingStartTime.setText(reservationList.get(position).getStartTime());
        usingEndTime.setText(reservationList.get(position).getEndTime());
        person.setText(reservationList.get(position).getName());
        usingPersonNum.setText(reservationList.get(position).getPeople() + "");

        v.setTag(reservationList.get(position).getSid());

        return v;
    }
}
