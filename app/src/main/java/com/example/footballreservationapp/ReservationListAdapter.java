package com.example.footballreservationapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
    private Activity parentActivity;

    public ReservationListAdapter(Context context, List<Reservation> reservationList, Activity parentActivity){
        this.context = context;
        this.reservationList = reservationList;
        this.parentActivity = parentActivity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        approveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setMessage("예약 신청을 승인하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(parentActivity.getApplicationContext(),ApproveReservationActivity.class);
                                intent.putExtra("date", reservationList.get(position).getDate());
                                intent.putExtra("sid", reservationList.get(position).getSid());
                                intent.putExtra("starttime", reservationList.get(position).getStartTime());
                                //intent.putExtra("cancellation", reservationList.get(position).getCancellation());
                                parentActivity.startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create()
                        .show();
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setMessage("예약 신청을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(parentActivity.getApplicationContext(),CancleReservationActivity.class);
                                intent.putExtra("date", reservationList.get(position).getDate());
                                intent.putExtra("sid", reservationList.get(position).getSid());
                                intent.putExtra("starttime", reservationList.get(position).getStartTime());
                                //intent.putExtra("cancellation", reservationList.get(position).getCancellation());
                                parentActivity.startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create()
                        .show();
            }
        });

        cancellation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setMessage("예약 리스트에서 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //new ReservationDeleteBackgroundTask(position).execute(sidText.getText().toString(),dateText.getText().toString());
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create()
                        .show();
            }
        });

        Reservation reservation = reservationList.get(position);

        usingStartTime.setText(reservation.getStartTime());
        usingEndTime.setText(reservation.getEndTime());
        person.setText(reservation.getName());
        usingPersonNum.setText(reservation.getPeople() + "");
        phoneTextView.setText(reservation.getPhone());
        subjectTestView.setText(reservation.getSubject());
        sidTextView.setText(reservation.getSid() + "");

        if(reservation.getStatus_code() == 2){
            approveButton.setVisibility(View.GONE);
            approval.setVisibility(View.VISIBLE);
        }else{
            approveButton.setVisibility(View.VISIBLE);
            approval.setVisibility(View.GONE);
        }

        if(reservation.getStatus_code() == 3){
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
