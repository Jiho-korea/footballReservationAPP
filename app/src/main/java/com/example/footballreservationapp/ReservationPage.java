package com.example.footballreservationapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// 당일의 예약을 보여주는 액티비티 이다.
public class ReservationPage extends AppCompatActivity {
    private ListView studentList;
    private RelativeLayout rel; // 날짜 클릭시 하단에 채워지는 렐러티브레이아웃(list_registrant.xml) .. 리스트뷰와 예약신청 버튼을 가지고있음
    private String year; // 년
    private String month; // 월(한자리 달일경우 0포함되있는)
    private String rmonth;
    private int day; // 일
    private String todayDate; // yyyy-mm-dd 형식의 date 입력 폼
    private TextView tvDate; // 좌상단  "연/월" 표시해주는 텍스트뷰
    private RelativeLayout listlayout; // 달력하단 빈 렐러비트 레이아웃 이안이 rel 렐러티브
    private List<Reservation> reservationList;
    private ReservationListAdapter adapter;
    //예약이 없을 때 출력할 텍스트
    private View empty_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationpage);

        reservationList = new ArrayList<Reservation>();
        Intent intent = getIntent();

        tvDate = (TextView)findViewById(R.id.tv_date);

        empty_word = getLayoutInflater().inflate(R.layout.empty_list_item, null, false);
        addContentView(empty_word, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        empty_word.setVisibility(View.GONE);

        long now = System.currentTimeMillis();
        final Date date = new Date(now); // 현재 날짜얻음
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA); //현재날짜를 포맷팅해서 사용하도록 포맷정하는중

        year = curYearFormat.format(date); // 이번 년도
        month = curMonthFormat.format(date); // 이번 달
        day = Integer.parseInt(curDayFormat.format(date)); // 오늘
        if(month.equals("10")){ // 이 if 문은 이번 달에 0 포함 되어있을시 없애고 today 필드에 "월/일" 형식으로 저장하는 문장
            rmonth = month;
        }else{
            rmonth = month.replace("0","");
        }
        tvDate.setText(year+ "-" + rmonth + "-" + day);
        // 예약리스트를 보여주는 코드
        listReservation();
        /*
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        try{
            listlayout = (RelativeLayout)findViewById(R.id.list);  // 빈 레이아웃 얻음
            String[] subDate = tvDate.getText().toString().split("-");

            String trueDay = day+"";
            //String trueMonth = subDate[1];
            if(day < 10){
                trueDay = "0"+trueDay;
            }

            //if(Integer.parseInt(subDate[1]) < 10){
            //    trueMonth = "0"+trueMonth;
            //}

            setTodayDate(year + "-" + month + "-" + trueDay);

            //  Toast.makeText(ReservationPage.this, day + "일 선택", Toast.LENGTH_SHORT).show(); // 선택 날짜 출력 있으나 마나입니다. 그냥 넣어봤습니다
            rel = (RelativeLayout)inflater.inflate(R.layout.list_registrant,null); // 빈레이아웃을 R.layout.list_registrant 로 채웁니다.

            studentList = rel.findViewById(R.id.studentList);
            studentList.setEmptyView(empty_word);
            //////////////////////////////////////
            //여기를 내가 만든 어댑터붙여 주고!

            adapter = null;

            adapter = new ReservationListAdapter(getApplicationContext(), reservationList, this);

            studentList.setAdapter(adapter);

            new ReservationBackgroundTask().execute(getTodayDate());

            listlayout.removeAllViews(); // 레이아웃이 덮혀써지지 않도록 이미 만들어진 레이아웃 제거 하는겁니다.
            // 요기서 꺼냄

            rel.findViewById(R.id.reserve).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {  // 예약 하기 버튼을 눌렀을 때 할 행동을 정의합니다. 추가정보로 월,일,오늘날짜 전달합니다.
                    Intent intent = new Intent(getApplicationContext(),RequestPage.class);
                    intent.putExtra("todayDate", todayDate);
                    startActivity(intent);
                }
            });
        }catch(NumberFormatException e){
            if(listlayout != null){
                listlayout.removeAllViews();
                empty_word.setVisibility(View.GONE);
            }
        }
        */
    }
    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listReservation();
        /*
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        try{
            listlayout = (RelativeLayout)findViewById(R.id.list);  // 빈 레이아웃 얻음
            String[] subDate = tvDate.getText().toString().split("-");

            String trueDay = day+"";

            if(day < 10){
                trueDay = "0"+trueDay;
            }
            setTodayDate(year + "-" + month + "-" + trueDay);

            rel = (RelativeLayout)inflater.inflate(R.layout.list_registrant,null); // 빈레이아웃을 R.layout.list_registrant 로 채웁니다.

            studentList = rel.findViewById(R.id.studentList);
            studentList.setEmptyView(empty_word);

            adapter = null;

            adapter = new ReservationListAdapter(getApplicationContext(), reservationList, this);

            studentList.setAdapter(adapter);

            new ReservationBackgroundTask().execute(getTodayDate());

            listlayout.removeAllViews();

            rel.findViewById(R.id.reserve).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {  // 예약 하기 버튼을 눌렀을 때 할 행동을 정의합니다. 추가정보로 월,일,오늘날짜 전달합니다.
                    Intent intent = new Intent(getApplicationContext(),RequestPage.class);
                    intent.putExtra("todayDate", todayDate);
                    startActivity(intent);
                }
            });
        }catch(NumberFormatException e){
            if(listlayout != null){
                listlayout.removeAllViews();
                empty_word.setVisibility(View.GONE);
            }
        }
         */
    }

    private void listReservation(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        try{
            listlayout = (RelativeLayout)findViewById(R.id.list);  // 빈 레이아웃 얻음
            String[] subDate = tvDate.getText().toString().split("-");

            String trueDay = day+"";

            if(day < 10){
                trueDay = "0"+trueDay;
            }
            setTodayDate(year + "-" + month + "-" + trueDay);

            rel = (RelativeLayout)inflater.inflate(R.layout.list_registrant,null); // 빈레이아웃을 R.layout.list_registrant 로 채웁니다.

            studentList = rel.findViewById(R.id.studentList);
            studentList.setEmptyView(empty_word);

            adapter = null;

            adapter = new ReservationListAdapter(getApplicationContext(), reservationList, this);

            studentList.setAdapter(adapter);

            new ReservationBackgroundTask().execute(getTodayDate());

            listlayout.removeAllViews();

            rel.findViewById(R.id.reserve).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {  // 예약 하기 버튼을 눌렀을 때 할 행동을 정의합니다. 추가정보로 월,일,오늘날짜 전달합니다.
                    Intent intent = new Intent(getApplicationContext(),RequestPage.class);
                    intent.putExtra("todayDate", todayDate);
                    startActivity(intent);
                }
            });
        }catch(NumberFormatException e){
            if(listlayout != null){
                listlayout.removeAllViews();
                empty_word.setVisibility(View.GONE);
            }
        }
    }

    class ReservationBackgroundTask extends AsyncTask<String, Void, Void> {
        String todayDateclone;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            todayDateclone = args[0];
            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getJSONArray("response") != null){
                            reservationList.clear();

                            try{
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                int count = 0;
                                int sid, people, approval, cancellation;
                                String name, date, startTime, endTime, subject, phone;
                                while(count < jsonArray.length()){
                                    JSONObject object = jsonArray.getJSONObject(count);
                                    sid = object.getInt("sid");
                                    date = object.getString("date");
                                    name = object.getString("name");
                                    people = object.getInt("people");
                                    startTime = object.getString("startTime");
                                    endTime = object.getString("endTime");
                                    subject = object.getString("subject");
                                    phone = object.getString("phone");
                                    approval = object.getInt("approval");
                                    cancellation = object.getInt("cancellation");
                                    Reservation reservation = new Reservation(sid, date,name, people, startTime, endTime,subject, phone, approval , cancellation);
                                    reservationList.add(reservation);
                                    adapter.notifyDataSetChanged();
                                    count++;
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            listlayout.removeAllViews();
                            listlayout.addView(rel);
                        }
                        else{
                            Toast.makeText(ReservationPage.this , "예약 목록 읽기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            ReservationRequest reservationRequest = new ReservationRequest(todayDateclone ,responseListener);
            RequestQueue queue = Volley.newRequestQueue(ReservationPage.this);
            queue.add(reservationRequest);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

}


