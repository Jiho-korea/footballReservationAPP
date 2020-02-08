package com.example.footballreservationapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class RequestPage extends AppCompatActivity {
    private Spinner starttimehourSpinner;
    private Spinner endtimehourSpinner;
    private Spinner subjectSpinner;
    private ProgressBar circle_bar;

    private String todayDate;
    private String trueTodayDate; // yyyy-MM-dd 형태의 날짜 (todayDate의 날짜를 포맷 )
    private int sid;
    private String password;
    private String subject;
    private String name;
    private String phone;
    private int people;
    private String startTime;
    private String endTime;

    private EditText sidText;
    private EditText nameText;
    private EditText phoneText;
    private EditText passwordText;
    private EditText peopleEdit;
    private EditText startTimeminuteEdit;
    private EditText endTimeminuteEdit;
    private TextView privacyTermText;
    private CheckBox privacyTermCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        Intent intent = getIntent(); // ReservationPage 에서전달 받은 인텐트 얻어줍니다. 날짜정보만 가지고 있습니다

        subjectSpinner = (Spinner)findViewById(R.id.spinner);
        final ArrayList<String> list = new ArrayList<>();
        list.add("선택");
        list.add("스마트소프트웨어");
        list.add("스마트전기전자공학");
        list.add("조선자동차항공기계");
        list.add("전자전기");
        list.add("기계공학");
        list.add("산업정보디자인");
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,list);
        subjectSpinner.setAdapter(spinnerAdapter);

        circle_bar = (ProgressBar)findViewById(R.id.progressBar);
        circle_bar.setVisibility(View.GONE);

        todayDate = intent.getStringExtra("todayDate");

        String[] dateSplit = todayDate.split("-");
        TextView date = (TextView)findViewById(R.id.date);
        date.setText(Integer.parseInt(dateSplit[1]) + "/" +  Integer.parseInt(dateSplit[2]));

        trueTodayDate = dateSplit[0] + "-" +dateSplit[1] + "-" +  dateSplit[2];

        // 입력 TestView 참조 얻기
        sidText = (EditText)findViewById(R.id.sidText);
        nameText = (EditText)findViewById(R.id.nameText);
        phoneText = (EditText)findViewById(R.id.phoneText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        peopleEdit = (EditText)findViewById(R.id.people);
        startTimeminuteEdit = (EditText)findViewById(R.id.starttimeminute);
        endTimeminuteEdit = (EditText)findViewById(R.id.endtimeminute);
        privacyTermText = (TextView)findViewById(R.id.privacyTermText);
        privacyTermText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestPage.this, PrivacyTermPopupActivity.class));
            }
        });
        privacyTermCheckBox = (CheckBox)findViewById(R.id.checkbox_privacyTerm);

        starttimehourSpinner = (Spinner)findViewById(R.id.starttimehourSpinner);
        endtimehourSpinner = (Spinner)findViewById(R.id.endtimehourSpinner);

        startTimeminuteEdit.setEnabled(false);
        endTimeminuteEdit.setEnabled(false);

        final ArrayList<String> starttimeList = new ArrayList<>();
        starttimeList.add("09");
        starttimeList.add("10");
        starttimeList.add("11");
        starttimeList.add("12");
        starttimeList.add("13");
        starttimeList.add("14");
        starttimeList.add("15");
        starttimeList.add("16");
        starttimeList.add("17");
        starttimeList.add("18");
        starttimeList.add("19");
        starttimeList.add("20");
        starttimeList.add("21");
        ArrayAdapter spinnerAdapter1;
        spinnerAdapter1 = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,starttimeList);
        starttimehourSpinner.setAdapter(spinnerAdapter1);

        final ArrayList<String> endtimeList = new ArrayList<>();
        endtimeList.add("10");
        endtimeList.add("11");
        endtimeList.add("12");
        endtimeList.add("13");
        endtimeList.add("14");
        endtimeList.add("15");
        endtimeList.add("16");
        endtimeList.add("17");
        endtimeList.add("18");
        endtimeList.add("19");
        endtimeList.add("20");
        endtimeList.add("21");
        endtimeList.add("22");
        ArrayAdapter spinnerAdapter2;
        spinnerAdapter2 = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,endtimeList);
        endtimehourSpinner.setAdapter(spinnerAdapter2);

    }




    public void mSubmit(View v){  // 노란색 예약신청 버튼 클릭시 사용자가 입력한 정보를 db에 넣는 작업을 합니다.
        if(v.getId() == R.id.submit){

            checkNetWork();

            try{
                circle_bar.setVisibility(View.VISIBLE);
                if(!"".equals(sidText.getText().toString())){
                    sid = Integer.parseInt(sidText.getText().toString());
                }else{
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "학번을 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                name = nameText.getText().toString();
                phone = phoneText.getText().toString();
                password = passwordText.getText().toString();
                subject = subjectSpinner.getSelectedItem().toString();
                if(!"".equals(peopleEdit.getText().toString())){
                    people = Integer.parseInt(peopleEdit.getText().toString());
                }else{
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "인원을 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                startTime = starttimehourSpinner.getSelectedItem().toString()+ ":" +startTimeminuteEdit.getText().toString();
                endTime =  endtimehourSpinner.getSelectedItem().toString() + ":" + endTimeminuteEdit.getText().toString();

                boolean namePattern = Pattern.matches("[가-힣]*", name);
                boolean phonePattern = Pattern.matches("01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}", phone);

                if("".equals(name.trim())){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "이름을 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if("".equals(phone.trim())){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "전화번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if("".equals(password.trim())){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "예약 비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if("선택".equals(subject.trim())){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "학과를 선택하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!namePattern){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "이름을 정확하게 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!phonePattern){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "전화번호를 정확하게 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(startTime.equals(endTime)){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "시간 시간과 종료 시간을 올바르게 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!privacyTermCheckBox.isChecked()){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "개인 정보 처리 방침을 확인하시고 약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 입력한시간의 패턴 검사
                boolean startHourPattern = Pattern.matches("[0-1][0-9]|[2][0-1]", starttimehourSpinner.getSelectedItem().toString());
                boolean startMinutePattern = Pattern.matches("[0][0]", startTimeminuteEdit.getText().toString());
                boolean endHourPattern = Pattern.matches("[0-1][0-9]|[2][0-2]",endtimehourSpinner.getSelectedItem().toString());
                boolean endMinutePattern = Pattern.matches("[0][0]", endTimeminuteEdit.getText().toString());

                String[] todaySplit = trueTodayDate.split("-");

                Calendar startCal = Calendar.getInstance();
                startCal.set(0,Integer.parseInt(todaySplit[1]),Integer.parseInt(todaySplit[2]),Integer.parseInt(starttimehourSpinner.getSelectedItem().toString()),Integer.parseInt(startTimeminuteEdit.getText().toString()));
                Calendar endCal = Calendar.getInstance();
                endCal.set(0,Integer.parseInt(todaySplit[1]),Integer.parseInt(todaySplit[2]),Integer.parseInt(endtimehourSpinner.getSelectedItem().toString()),Integer.parseInt(endTimeminuteEdit.getText().toString()));
                int i = endCal.compareTo(startCal);
                if(i != 1){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "시간 시간과 종료 시간을 올바르게 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 한사람이 한시간 이상 예약하는 것은 방지하는 코드
                int starttimeint = Integer.parseInt(starttimehourSpinner.getSelectedItem().toString() + startTimeminuteEdit.getText().toString());
                int endtimeint = Integer.parseInt(endtimehourSpinner.getSelectedItem().toString() + endTimeminuteEdit.getText().toString());
                if((endtimeint - starttimeint) > 100){
                    circle_bar.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                    builder.setMessage("한 시간 이상 예약할 수 없습니다.")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                    return;
                }


                new ReservationValidateBackgroundTask().execute();
                // ReservationValidateRequest -> 같은 날에 예약을 한적이 있는 지 확인
//                Response.Listener<String> validateListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try{
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//                            if(success){
//                                // CheckReservationOverlapRequest -> 다른사람이 예약한 시간에 예약을 할려는지 체크
//                                Response.Listener<String> checkOverlapListener = new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        try{
//                                            JSONObject jsonResponse = new JSONObject(response);
//                                            boolean success = jsonResponse.getBoolean("success");
//                                            if(success){
//                                                // ReserveRequest -> 사용자가 입력한 정보를 갖고 예약을 신청
//                                                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                                                    @Override
//                                                    public void onResponse(String response) {
//                                                        try{
//                                                            JSONObject jsonResponse = new JSONObject(response);
//                                                            boolean success = jsonResponse.getBoolean("success")   ;
//                                                            if(success){
//                                                                circle_bar.setVisibility(View.GONE);
//                                                                Toast.makeText(RequestPage.this,"예약신청완료",Toast.LENGTH_SHORT).show();
//                                                                finish();
//                                                            }
//                                                            else{
//                                                                circle_bar.setVisibility(View.GONE);
//                                                                AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
//                                                                builder.setMessage("예약신청에 실패하셨습니다")
//                                                                        .setNegativeButton("다시 시도", null)
//                                                                        .create()
//                                                                        .show();
//                                                                return;
//                                                            }
//                                                        }
//                                                        catch (JSONException e){
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                };
//                                                ReserveRequest reserveRequest = new ReserveRequest(sid,trueTodayDate,people,startTime,endTime,name,phone,subject,password,responseListener);
//                                                RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
//                                                queue.add(reserveRequest);
//                                            }else{
//                                                circle_bar.setVisibility(View.GONE);
//                                                AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
//                                                builder.setMessage("다른 사람이 예약한 시간에 예약을 할 수 없습니다.\n해당 신청자의 취소를 기다리시겠습니까?")
//                                                        .setPositiveButton("예", new DialogInterface.OnClickListener(){
//                                                            @Override
//                                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                                                                    @Override
//                                                                    public void onResponse(String response) {
//                                                                        try{
//                                                                            JSONObject jsonResponse = new JSONObject(response);
//                                                                            boolean success = jsonResponse.getBoolean("success")   ;
//                                                                            if(success){
//                                                                                circle_bar.setVisibility(View.GONE);
//                                                                                Toast.makeText(RequestPage.this,"예약신청완료",Toast.LENGTH_SHORT).show();
//                                                                                finish();
//                                                                            }
//                                                                            else{
//                                                                                circle_bar.setVisibility(View.GONE);
//                                                                                AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
//                                                                                builder.setMessage("예약신청에 실패하셨습니다")
//                                                                                        .setNegativeButton("다시 시도", null)
//                                                                                        .create()
//                                                                                        .show();
//                                                                                return;
//                                                                            }
//                                                                        }
//                                                                        catch (JSONException e){
//                                                                            e.printStackTrace();
//                                                                        }
//                                                                    }
//                                                                };
//                                                                ReserveRequest reserveRequest = new ReserveRequest(sid,trueTodayDate,people,startTime,endTime,name,phone,subject,password,responseListener);
//                                                                RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
//                                                                queue.add(reserveRequest);
//                                                            }
//                                                        })
//                                                        .setNegativeButton("아니오", null)
//                                                        .create()
//                                                        .show();
//                                                return;
//                                            }
//                                        }
//                                        catch (JSONException e){
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                };
//                                CheckReservationOverlapRequest checkReservationOverlapRequest = new CheckReservationOverlapRequest(startTime,endTime,trueTodayDate,checkOverlapListener);
//                                RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
//                                queue.add(checkReservationOverlapRequest);
//                            }else{
//                                circle_bar.setVisibility(View.GONE);
//                                AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
//                                builder.setMessage("하루에 예약은 한번만 할 수있습니다.")
//                                        .setNegativeButton("확인", null)
//                                        .create()
//                                        .show();
//                                return;
//                            }
//                        }
//                        catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                ReservationValidateRequest reservationValidateRequest = new ReservationValidateRequest(sid,trueTodayDate,validateListener);
//                RequestQueue vqueue = Volley.newRequestQueue(RequestPage.this);
//                vqueue.add(reservationValidateRequest);
//
            }catch(Exception e){
                circle_bar.setVisibility(View.GONE);
                Toast.makeText(RequestPage.this, "모든 입력 사항을 올바르게 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 네트워크 연결 유무 확인 후 대화상자를 띄우는 메소드
    private void checkNetWork(){
        if(netWork() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
            builder.setMessage("네트워크 연결이 끊어져 있습니다.\nwifi 또는 모바일 데이터 네트워크\n연결 상태를 확인해주세요.\n재시도 하려면 확인을 터치 해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(netWork() == true){
                                circle_bar.setVisibility(View.GONE);
                            }else{
                                checkNetWork();
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.finishAffinity(RequestPage.this);
                        }
                    })
                    .create()
                    .show();
        }
    }

    // 네트워크 연결 유무를 확인하는 메소드
    private boolean netWork(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        if(ninfo == null){
            return false;
        }else{
            return true;
        }
    }

    class ReservationValidateBackgroundTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            Response.Listener<String> validateListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            // CheckReservationOverlapRequest -> 다른사람이 예약한 시간에 예약을 할려는지 체크
                            new CheckReservationOverlapBackgroundTask().execute();
                        }else{
                            circle_bar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                            builder.setMessage("하루에 예약은 한번만 할 수있습니다.")
                                    .setNegativeButton("확인", null)
                                    .create()
                                    .show();
                            return;
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            ReservationValidateRequest reservationValidateRequest = new ReservationValidateRequest(sid,trueTodayDate,validateListener);
            RequestQueue vqueue = Volley.newRequestQueue(RequestPage.this);
            vqueue.add(reservationValidateRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    class CheckReservationOverlapBackgroundTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            Response.Listener<String> checkOverlapListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            // ReserveRequest -> 사용자가 입력한 정보를 갖고 예약을 신청
                            new ReserveBackgroundTask().execute();
                        }else{
                            circle_bar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                            builder.setMessage("다른 사람이 예약한 시간에 예약을 할 수 없습니다.\n해당 신청자의 취소를 기다리시겠습니까?")
                                    .setPositiveButton("예", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            new ReserveBackgroundTask().execute();
                                        }
                                    })
                                    .setNegativeButton("아니오", null)
                                    .create()
                                    .show();
                            return;
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            CheckReservationOverlapRequest checkReservationOverlapRequest = new CheckReservationOverlapRequest(startTime,endTime,trueTodayDate,checkOverlapListener);
            RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
            queue.add(checkReservationOverlapRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


    class ReserveBackgroundTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success")   ;
                        if(success){
                            circle_bar.setVisibility(View.GONE);
                            Toast.makeText(RequestPage.this,"예약신청완료",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            circle_bar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                            builder.setMessage("예약신청에 실패하셨습니다")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                            return;
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            ReserveRequest reserveRequest = new ReserveRequest(sid,trueTodayDate,people,startTime,endTime,name,phone,subject,password,responseListener);
            RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
            queue.add(reserveRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
