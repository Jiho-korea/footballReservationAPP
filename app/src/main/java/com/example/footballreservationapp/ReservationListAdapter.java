package com.example.footballreservationapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        View v = View.inflate(context, R.layout.reservationinthatday, null);

        TextView usingStartTime = (TextView)v.findViewById(R.id.usingstarttime);
        TextView usingEndTime = (TextView)v.findViewById(R.id.usingendtime);
        TextView person = (TextView)v.findViewById(R.id.personwhoreserve);
        TextView usingPersonNum = (TextView)v.findViewById(R.id.usingpersonnumber);
        TextView phoneTextView = (TextView)v.findViewById(R.id.phoneText);
        TextView subjectTestView = (TextView)v.findViewById(R.id.subjectText);
        TextView sidTextView = (TextView)v.findViewById(R.id.sidText);

        Button approveButton = (Button)v.findViewById(R.id.approveButton);
        Button cancleButton = (Button)v.findViewById(R.id.cancleButton);
        Button approval = (Button)v.findViewById(R.id.approval);
        Button cancellation = (Button)v.findViewById(R.id.cancellation);


        Reservation reservation = reservationList.get(position);
        usingStartTime.setText(reservation.getStartTime());
        usingEndTime.setText(reservation.getEndTime());
        person.setText(reservation.getName());
        usingPersonNum.setText(reservation.getPeople() + "");
        phoneTextView.setText(reservation.getPhone());
        subjectTestView.setText(reservation.getSubject());
        sidTextView.setText(reservation.getSid() + "");

        if(reservation.getApproval() == 1){
            approveButton.setVisibility(View.GONE);
            approval.setVisibility(View.VISIBLE);
        }else{
            approveButton.setVisibility(View.VISIBLE);
            approval.setVisibility(View.GONE);
        }

        if(reservation.getCancellation() == 1){
            cancleButton.setVisibility(View.GONE);
            approveButton.setVisibility(View.GONE);
            approval.setVisibility(View.GONE);
            cancellation.setVisibility(View.VISIBLE);
        }else{
            cancleButton.setVisibility(View.VISIBLE);
            cancellation.setVisibility(View.GONE);
        }

        v.setTag(reservation.getSid());

        return v;
    }
}
