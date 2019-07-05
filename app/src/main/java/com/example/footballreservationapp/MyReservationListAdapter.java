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


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyReservationListAdapter extends BaseAdapter {
    private Context context;
    private List<MyReservation> myReservationList;
    private Activity parentActivity;

    public MyReservationListAdapter(Context context, List<MyReservation> myReservationList, Activity parentActivity){
        this.context = context;
        this.myReservationList = myReservationList;
        this.parentActivity = parentActivity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.myreservation, null);
        final int sid = myReservationList.get(position).getSid();
        final TextView reservationday = (TextView)v.findViewById(R.id.reservationday);
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
        if(myReservationList.get(position).getApproval() == 0){
            reservationOK.setVisibility(View.INVISIBLE);
            reservationWait.setVisibility(View.VISIBLE);
        }else{
            reservationWait.setVisibility(View.INVISIBLE);
            reservationOK.setVisibility(View.VISIBLE);
        }

        cancleReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                builder.setMessage("예약을 취소하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new MyReservationDeleteBackgroundTask(position).execute(sid+"",reservationday.getText().toString());
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .create()
                        .show();
            }
        });

        v.setTag(myReservationList.get(position).getSid());

        return v;
    }

    class MyReservationDeleteBackgroundTask extends AsyncTask<String, Void, Void> {
        String sidClone;
        String dateClone;
        int positionClone;
        @Override
        protected void onPreExecute() {

        }

        public MyReservationDeleteBackgroundTask(int position){
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
                            myReservationList.remove(positionClone);
                            notifyDataSetChanged();
                            Toast.makeText(parentActivity,"예약 취소 성공", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            MyReservationDeleteRequest myReservationDeleteRequest = new MyReservationDeleteRequest(sidClone+"", myReservationList.get(positionClone).getDate(),responseListener);
            RequestQueue queue = Volley.newRequestQueue(parentActivity);
            queue.add(myReservationDeleteRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
