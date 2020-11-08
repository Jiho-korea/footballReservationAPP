package com.example.footballreservationapp;


import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

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

    // 이미지 업로드를 위한 필드들
    static final int getimagesetting=1001;//for request intent
    static Context mContext;
    ImageView image;
    String imgPath;

    Button get; // ,send,reflash;
    //ListView bloblist;
    String temp=""; // utf-8 로 인코딩된 Base64 스트링



    private static final int REQUEST_CAMERA = 1;

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

        // 이미지 업로드를 위한 메소드 호출
        init();
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
                }else if(imgPath == null){  // 수정 ("".equals(temp)){
                    circle_bar.setVisibility(View.GONE);
                    Toast.makeText(RequestPage.this, "학생증 이미지를 등록해주세요.", Toast.LENGTH_SHORT).show();
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
                ///////////////////////////////////////////////////////////////////////////////////////////// 스레드 시작
                new CheckDateBackgroundTask().execute();
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

    //예약을 하려는 날짜가 서버날짜와 동일한지 체크하는 스레드
    class CheckDateBackgroundTask extends AsyncTask<String, Void, Void> {
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
                        // success 가 ture 면 오늘 날짜와 동일하다는 뜻
                        if(success){
                            // CheckReservationOverlapRequest -> 다른사람이 예약한 시간에 예약을 할려는지 체크
                            new ReservationValidateBackgroundTask().execute();
                        }else{
                            circle_bar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                            builder.setMessage("날짜가 일치하지 않습니다.")
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
            CheckDateRequest checkDateRequest = new CheckDateRequest(trueTodayDate,validateListener);
            RequestQueue vqueue = Volley.newRequestQueue(RequestPage.this);
            vqueue.add(checkDateRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    //예약을 두번 하는지 체크 하는 스레드
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
                                            Log.e("progress2", "progress2");
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

    // 이미지 업로드를 위한 필드 초기화
    private void init() {
        image=(ImageView)findViewById(R.id.image);
        get=(Button) findViewById(R.id.get);
        //send=(Button)findViewById(R.id.send);
        //reflash=(Button)findViewById(R.id.reflash);
        //bloblist=(ListView)findViewById(R.id.bloblist);
        //list=new ListView(this);
        //send.setOnClickListener(this);
        //reflash.setOnClickListener(this);
        //bloblist.setAdapter(list);
        mContext=this;
        //list_cnt=0;
        permission_init();
    }

    // 권한 검사
    void permission_init(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {	//권한 거절
            // Request missing location permission.
            // Check Permissions Now

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                // 이전에 권한거절
                // Toast.makeText(this,getString(R.string.limit),Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }

        } else {	//권한승인
            //Log.e("onConnected","else");
            // mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

    }

    // 업로드 버튼 클릭시 동작
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.get:
                addImage();
                break;
//            case R.id.send:
//                if(temp.length()>0)
//                    insert_blob();
//                else
//                    Toast.makeText(this,"이미지가 없습니다.",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.reflash:
//
//                reflash_list();
//                break;
        }
    }

    void addImage(){
        Intent intent=new Intent(getApplicationContext(),SetImageActivity.class);

        startActivityForResult(intent, getimagesetting);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==getimagesetting){	//if image change

            if(resultCode==RESULT_OK){
                //수정 (주석)
                //Bitmap selPhoto = null;
                //selPhoto=(Bitmap) data.getParcelableExtra("bitmap");
                //image.setImageBitmap(selPhoto);//썸네일
                //Log.e("selPhoto", selPhoto.toString());

                // 수정
                Uri uri = data.getParcelableExtra("uri");
                image.setImageURI(uri);
                //갤러리앱에서 관리하는 DB정보가 있는데, 그것이 나온다 [실제 파일 경로가 아님!!]
                //얻어온 Uri는 Gallery앱의 DB번호임. (content://-----/2854)
                //업로드를 하려면 이미지의 절대경로(실제 경로: file:// -------/aaa.png 이런식)가 필요함
                //Uri -->절대경로(String)로 변환
                imgPath= getRealPathFromUri(uri);   //임의로 만든 메소드 (절대경로를 가져오는 메소드)
                if(imgPath == null){
                    Toast.makeText(this, "갤러리에서 이미지를 선택해주세요.(클라우드X)", Toast.LENGTH_SHORT).show();
                }
                //이미지 경로 uri 확인해보기
                new AlertDialog.Builder(this).setMessage(uri.toString()+"\n\n"+imgPath).create().show();


                // 수정
                //BitMapToString(selPhoto);

            }else{
                Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * bitmap을 string으로
     * @param
     * @return
     */
    /* 수정
    public void BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);	//bitmap compress
        byte [] arr=baos.toByteArray();
        Log.e("arr", arr.toString());
        String image= Base64.encodeToString(arr, Base64.DEFAULT);
        Log.e("사진DB에 저장시 UTF인코딩전 image",image);

        try{
            temp= "&imagedevice="+ URLEncoder.encode(image,"utf-8");
            //temp = temp.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
            //temp = temp.substring(temp.indexOf(",") + 1);

            Log.e("temp",temp);
        }catch (Exception e){
            Log.e("exception",e.toString());
        }

    }
    */

    String getRealPathFromUri(Uri uri){

        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
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
                        boolean success = jsonResponse.getBoolean("success");
                        Log.e("success", success + "");

                        // 수정
                        //Log.e("image", jsonResponse.getString("image"));
                        //Log.e("data", jsonResponse.getString("data"));

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


            // 수정

            String serverUrl="http://yonamfootball.dothome.co.kr/Reserve.php";

            //Volley plus Library를 이용해서
            //파일 전송하도록..
            //Volley+는 AndroidStudio에서 검색이 안됨 [google 검색 이용]
            //파일 전송 요청 객체 생성[결과를 String으로 받음]
            SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String success_name = jsonResponse.getString("name");
                        Log.e("success", success + " " + success_name);
                        //String message = jsonResponse.getString("message");
                        //String imageName = jsonResponse.getString("imageName");
                        //new AlertDialog.Builder(RequestPage.this).setMessage(message+"\n\n"+imageName).create().show();
                        // 수정
                        //Log.e("image", jsonResponse.getString("image"));
                        //Log.e("data", jsonResponse.getString("data"));

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


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RequestPage.this, "ERROR", Toast.LENGTH_SHORT).show();
                }

            });
            
            //요청 객체에 보낼 데이터를 추가
            smpr.addStringParam("sid",sid+"");
            smpr.addStringParam("date",trueTodayDate);
            smpr.addStringParam("people",people + "");
            smpr.addStringParam("startTime", startTime);
            smpr.addStringParam("endTime", endTime);
            smpr.addStringParam("name", name);
            smpr.addStringParam("phone", phone);
            smpr.addStringParam("subject", subject);
            smpr.addStringParam("password", password);

            //이미지 파일 추가
            //smpr.addFile("img", imgPath);

            // 여기까지 수정

                                                                                                                                             // 수정 (temp -> imgPath)
            //ReserveRequest reserveRequest = new ReserveRequest(sid, trueTodayDate, people, startTime, endTime, name, phone, subject, password, responseListener);


            RequestQueue queue = Volley.newRequestQueue(RequestPage.this);
            //queue.add(reserveRequest);
            queue.add(smpr);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
