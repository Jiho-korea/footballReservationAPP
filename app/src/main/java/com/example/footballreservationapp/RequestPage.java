package com.example.footballreservationapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class RequestPage extends AppCompatActivity {

    int sid;
    String password;
    String subject;
    String name;
    String phone;
    int manager;

/*
아래를 보면 필드가 상당히 많은 데 전부
사용자가 입력한 내용을 얻기위해 EditText 필드 선언한겁니다. 이름을 보면 직관적으로 어떤 정보를 입력받는 EditText인지 알 수있습니다.
 */
    private String today;
    private String todayDate;
    private String reservationDay;
    private Button submitBtn;
    int people;
    String startTime;
    String endTime;
    private EditText peopleEdit;
    private EditText startTimehourEdit;
    private EditText startTimeminuteEdit;
    private EditText endTimehourEdit;
    private EditText endTimeminuteEdit;

    private boolean reservationOverlapValidation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", 0);
        password = intent.getStringExtra("password");
        subject = intent.getStringExtra("subject");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        manager = intent.getIntExtra("manager",0);

        intent = getIntent(); // ReservationPage 에서전달 받은 인텐트 얻어줍니다. 날짜정보만 가지고 있습니다.

        submitBtn = (Button)findViewById(R.id.submit);

        todayDate = intent.getStringExtra("todayDate");
        peopleEdit = (EditText)findViewById(R.id.people);
        startTimehourEdit = (EditText)findViewById(R.id.starttimehour);
        startTimeminuteEdit = (EditText)findViewById(R.id.starttimeminute);
        endTimehourEdit = (EditText)findViewById(R.id.endtimehour);
        endTimeminuteEdit = (EditText)findViewById(R.id.endtimeminute);

        TextView date = (TextView)findViewById(R.id.date);
        String month = intent.getStringExtra("Month");
        reservationDay = month + "-" +intent.getStringExtra("ReservationDay");
        String day = intent.getStringExtra("Date");
        today = intent.getStringExtra("Today");
        date.setText(today);

    }

    public void mSubmit(View v){  // 노란색 예약신청 버튼 클릭시 사용자가 입력한 정보를 db에 넣는 작업을 합니다.
        if(v.getId() == R.id.submit){
            try{
                people = Integer.parseInt(peopleEdit.getText().toString());
                startTime = startTimehourEdit.getText().toString() + ":" +startTimeminuteEdit.getText().toString();
                endTime = endTimehourEdit.getText().toString() + ":" + endTimeminuteEdit.getText().toString();


                // 입력한시간의 패턴 검사
                boolean startHourPattern = Pattern.matches("[0-1][0-9]|[2][0-4]", startTimehourEdit.getText().toString());
                boolean startMinutePattern = Pattern.matches("[0-5][0-9]", startTimeminuteEdit.getText().toString());
                boolean endHourPattern = Pattern.matches("[0-1][0-9]|[2][0-4]", endTimehourEdit.getText().toString());
                boolean endMinutePattern = Pattern.matches("[0-5][0-9]", endTimeminuteEdit.getText().toString());

                if(!peopleEdit.getText().toString().equals("")){
                    if(startTime.trim().equals(":")) {
                        Toast.makeText(RequestPage.this, "시작시간를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    }else if(!startHourPattern){
                        Toast.makeText(RequestPage.this, "시작 시간의 시간을 정확하게 입력하여 주십시오(두자리숫자).", Toast.LENGTH_SHORT).show();
                    }else if(!startMinutePattern){
                        Toast.makeText(RequestPage.this, "시작 시간의 분을 정확하게 입력하여 주십시오(두자리숫자).", Toast.LENGTH_SHORT).show();
                    }else if(endTime.trim().equals(":")){
                        Toast.makeText(RequestPage.this, "종료시간를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    }else if(!endHourPattern){
                        Toast.makeText(RequestPage.this, "종료 시간의 시간을 정확하게 입력하여 주십시오(두자리숫자).", Toast.LENGTH_SHORT).show();
                    }else if(!endMinutePattern){
                        Toast.makeText(RequestPage.this, "종료 시간의 분을 정확하게 입력하여 주십시오(두자리숫자).", Toast.LENGTH_SHORT).show();
                    }else{
                        new CheckOverlapBackgroundTask().execute(startTime,endTime,todayDate);

                        String[] todaySplit = today.split("/");

                        Calendar startCal = Calendar.getInstance();
                        startCal.set(0,Integer.parseInt(todaySplit[0]),Integer.parseInt(todaySplit[1]),Integer.parseInt(startTimehourEdit.getText().toString()),Integer.parseInt(startTimeminuteEdit.getText().toString()));

                        Calendar endCal = Calendar.getInstance();
                        endCal.set(0,Integer.parseInt(todaySplit[0]),Integer.parseInt(todaySplit[1]),Integer.parseInt(endTimehourEdit.getText().toString()),Integer.parseInt(endTimeminuteEdit.getText().toString()));

                        int i = endCal.compareTo(startCal);

                        if(i != 1){
                            Toast.makeText(RequestPage.this, "시간 시간과 종료 시간을 올바르게 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        Response.Listener<String> validateListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success")   ;
                                        if(success){
                                            Response.Listener<String> checkOverlapListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try{
                                                        JSONObject jsonResponse = new JSONObject(response);
                                                        boolean success = jsonResponse.getBoolean("success");
                                                        if(success){
                                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    try{
                                                                        JSONObject jsonResponse = new JSONObject(response);
                                                                        boolean success = jsonResponse.getBoolean("success")   ;
                                                                        if(success){
                                                                            Toast.makeText(RequestPage.this,"예약신청완료",Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                        else{
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                                                                            builder.setMessage("예약신청에 실패하셨습니다")
                                                                                    .setNegativeButton("다시 시도", null)
                                                                                    .create()
                                                                                    .show();

                                                                        }
                                                                    }
                                                                    catch (JSONException e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            };
                                                            ReserveRequest reserveRequest = new ReserveRequest(sid,todayDate,people,startTime,endTime, reservationDay ,responseListener);
                                                            RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
                                                            queue.add(reserveRequest);
                                                        }else{
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                                                            builder.setMessage("다른 사람이 예약한 시간에 예약을 할 수 없습니다.")
                                                                    .setNegativeButton("확인", null)
                                                                    .create()
                                                                    .show();
                                                        }
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            CheckReservationOverlapRequest checkReservationOverlapRequest = new CheckReservationOverlapRequest(startTime,endTime,todayDate,checkOverlapListener);
                                            RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
                                            queue.add(checkReservationOverlapRequest);
                                        }else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                                            builder.setMessage("하루에 예약은 한번만 할 수있습니다.")
                                                    .setNegativeButton("확인", null)
                                                    .create()
                                                    .show();

                                        }
                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            ReservationValidateRequest reservationValidateRequest = new ReservationValidateRequest(sid,todayDate,validateListener);
                            RequestQueue vqueue = Volley.newRequestQueue(RequestPage.this);
                            vqueue.add(reservationValidateRequest);

                    }
                }else{
                    Toast.makeText(RequestPage.this, "1명 이상의 인원을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }catch(NumberFormatException e){
                Toast.makeText(RequestPage.this, "모든 입력 사항을 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    class CheckOverlapBackgroundTask extends AsyncTask<String, Void, Void> {
        String startTimeclone;
        String endTimeclone;
        String dateclone;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            startTimeclone = args[0];
            endTimeclone = args[1];
            dateclone = args[2];
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            reservationOverlapValidation = true;
                        }else{
                            reservationOverlapValidation = false;
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            CheckReservationOverlapRequest checkReservationOverlapRequest = new CheckReservationOverlapRequest(startTimeclone,endTimeclone,dateclone,responseListener);
            RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
            queue.add(checkReservationOverlapRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

}
