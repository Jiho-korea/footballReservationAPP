package com.example.footballreservationapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
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


        TextView approvalText = (TextView)v.findViewById(R.id.approvalText);
        TextView approvalReception = (TextView)v.findViewById(R.id.approvalReception);

        Button cancleButton = (Button)v.findViewById(R.id.cancleButton);
        Button cancellation = (Button)v.findViewById(R.id.cancellation);

        TextView studentCard = (TextView)v.findViewById(R.id.studentcard);

//        approveButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//                builder.setMessage("예약 신청을 승인하시겠습니까?")
//                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(parentActivity.getApplicationContext(),ApproveReservationActivity.class);
//                                intent.putExtra("date", reservationList.get(position).getDate());
//                                intent.putExtra("serial_number", reservationList.get(position).getSerial_number());
//                                intent.putExtra("starttime", reservationList.get(position).getStartTime());
//                                parentActivity.startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("아니오", null)
//                        .create()
//                        .show();
//            }
//        });

//        approvalReception.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//                builder.setMessage("예약 신청을 승인하시겠습니까?")
//                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(parentActivity.getApplicationContext(),ApproveReservationActivity.class);
//                                intent.putExtra("date", reservationList.get(position).getDate());
//                                intent.putExtra("serial_number", reservationList.get(position).getSerial_number());
//                                intent.putExtra("starttime", reservationList.get(position).getStartTime());
//                                parentActivity.startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("아니오", null)
//                        .create()
//                        .show();
//            }
//        });

        studentCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity.getApplicationContext(),StudentCheckActivity.class);
                intent.putExtra("date", reservationList.get(position).getDate());
                intent.putExtra("serial_number", reservationList.get(position).getSerial_number());
                intent.putExtra("starttime", reservationList.get(position).getStartTime());
                intent.putExtra("status_code", reservationList.get(position).getStatus_code());
                parentActivity.startActivity(intent);
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
                                intent.putExtra("serial_number", reservationList.get(position).getSerial_number());
                                intent.putExtra("starttime", reservationList.get(position).getStartTime());
                                intent.putExtra("status_code", reservationList.get(position).getStatus_code());
                                parentActivity.startActivity(intent);
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

        if(reservation.getStatus_code() == 1){
            approvalText.setVisibility(View.VISIBLE);
            cancleButton.setVisibility(View.VISIBLE);
            approvalReception.setVisibility(View.GONE);
            cancellation.setVisibility(View.GONE);
        }else if(reservation.getStatus_code() == 4){
            approvalReception.setVisibility(View.VISIBLE);
            cancleButton.setVisibility(View.VISIBLE);
            approvalText.setVisibility(View.GONE);
            cancellation.setVisibility(View.GONE);
        }else if(reservation.getStatus_code() == 3){
            cancellation.setVisibility(View.VISIBLE);
            cancleButton.setVisibility(View.GONE);
            approvalText.setVisibility(View.GONE);
            approvalReception.setVisibility(View.GONE);
            studentCard.setVisibility(View.GONE);
        }

        v.setTag(reservation.getSid());

        return v;
    }
}
