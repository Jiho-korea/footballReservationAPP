package com.example.footballreservationapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ManageReservationListAdapter extends BaseAdapter {
    private Context context;
    private List<ManageReservation> manageReservationList;
    private Activity parentActivity;

    public ManageReservationListAdapter(Context context, List<ManageReservation> manageReservationList, Activity parentActivity){
        this.context = context;
        this.manageReservationList = manageReservationList;
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return manageReservationList.size();
    }

    @Override
    public Object getItem(int position) {
        return manageReservationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.reservation, null);
        final TextView dateText = (TextView)v.findViewById(R.id.dateText);
        TextView starttimeText = (TextView)v.findViewById(R.id.starttimeText);
        TextView endtimeText = (TextView)v.findViewById(R.id.endtimeText);
        TextView peopleText = (TextView)v.findViewById(R.id.peopleText);
        TextView nameText = (TextView)v.findViewById(R.id.nameText);
        final TextView sidText = (TextView)v.findViewById(R.id.sidText);
        TextView subjectText = (TextView)v.findViewById(R.id.subjectText);
        TextView phoneText = (TextView)v.findViewById(R.id.phoneText);
        TextView reservationdayText = (TextView)v.findViewById(R.id.reservationdayText);
        Button approveButton = (Button)v.findViewById(R.id.approve);
        Button completeButton = (Button)v.findViewById(R.id.complete);
        Button denyButton = (Button)v.findViewById(R.id.deny);


        dateText.setText(manageReservationList.get(position).getDate());
        starttimeText.setText(manageReservationList.get(position).getStarttime());
        endtimeText.setText(manageReservationList.get(position).getEndtime());
        peopleText.setText(manageReservationList.get(position).getPeople() + "");
        nameText.setText(manageReservationList.get(position).getName());
        sidText.setText(manageReservationList.get(position).getSid() + "");
        subjectText.setText(manageReservationList.get(position).getSubject());
        phoneText.setText(manageReservationList.get(position).getPhone());
        reservationdayText.setText(manageReservationList.get(position).getReservationday());


        approveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setMessage("예약신청을 승인 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new ApproveReservationBackgroundTask().execute(sidText.getText().toString(),dateText.getText().toString());
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create()
                        .show();
            }
        });

        if(manageReservationList.get(position).getApproval() == 0){
            completeButton.setVisibility(View.INVISIBLE);
            approveButton.setVisibility(View.VISIBLE);
        }else{
            approveButton.setVisibility(View.INVISIBLE);
            completeButton.setVisibility(View.VISIBLE);
        }


        denyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setMessage("예약신청을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new ReservationDeleteBackgroundTask(position).execute(sidText.getText().toString(),dateText.getText().toString());
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create()
                        .show();
            }
        });

        v.setTag(manageReservationList.get(position).getSid());

        return v;
    }

    class ReservationDeleteBackgroundTask extends AsyncTask<String, Void, Void> {
        String sidClone;
        String dateClone;
        int positionClone;
        @Override
        protected void onPreExecute() {

        }

        public ReservationDeleteBackgroundTask(int position){
            this.positionClone = position;
        }

        @Override
        protected Void doInBackground(String... args) {
            sidClone = args[0];
            dateClone = args[1];

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success")   ;
                        if(success){
                            manageReservationList.remove(positionClone);
                            notifyDataSetChanged();
                            Toast.makeText(parentActivity,"예약 취소 성공", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            ReservationDeleteRequest reservationDeleteRequest = new ReservationDeleteRequest(sidClone, dateClone,responseListener);
            RequestQueue queue = Volley.newRequestQueue(parentActivity);
            queue.add(reservationDeleteRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    class ApproveReservationBackgroundTask extends AsyncTask<String, Void, Void> {
        String sidClone;
        String dateClone;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            sidClone = args[0];
            dateClone = args[1];

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success")   ;
                        if(success){
                            Toast.makeText(parentActivity,"예약 승인 완료", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            ApproveReservationRequest approveReservationRequest = new ApproveReservationRequest(sidClone, dateClone,responseListener);
            RequestQueue queue = Volley.newRequestQueue(parentActivity);
            queue.add(approveReservationRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
