package com.example.footballreservationapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReservationPage extends AppCompatActivity {
    int sid;
    String password;
    String subject;
    String name;
    String phone;
    int manager;

    ListView studentList;
    private RelativeLayout rel; // 날짜 클릭시 하단에 채워지는 렐러티브레이아웃(list_registrant.xml) .. 리스트뷰와 예약신청 버튼을 가지고있음
    private String today; //today 필드는 오늘 날짜를 "월/일" 형태의 문자열로 갖고있다. 코드는 밑에서 나오고 사용자가 예약신청을 눌렀을때 날짜 던져주기 위함임
    private String year;
    private String month; // 월(한자리 달일경우 0포함되있는)
    private int day; // 일
    private String todayDate; // yyyy-mm-dd 형식의 date 입력 폼
    private TextView tvDate; // 좌상단  "연/월" 표시해주는 텍스트뷰
    private GridAdapter gridAdapter; // 그리드 뷰에 항목정보를 제공해주는 그리드어댑터
    private RelativeLayout listlayout; // 달력하단 빈 렐러비트 레이아웃 이안이 rel 렐러티브

    List<Reservation> reservationList;
    ReservationListAdapter adapter;



    private ArrayList<String> dayList; // 달력,날짜 정보를 가지고있는 ArrayList 컬렉션
    private GridView gridView;  // 달력 나타내는 그리드뷰
    private Calendar mCal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationpage);

        reservationList = new ArrayList<Reservation>();
        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", 0);
        password = intent.getStringExtra("password");
        subject = intent.getStringExtra("subject");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        manager = intent.getIntExtra("manager",0);


        tvDate = (TextView)findViewById(R.id.tv_date);
        gridView =(GridView)findViewById(R.id.gridview);  // 필드가 위젯을 가르키도록 findViewById 사용

        long now = System.currentTimeMillis();
        final Date date = new Date(now); // 현재 날짜얻음

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA); //현재날짜를 포맷팅해서 사용하도록 포맷정하는중
        final String reservationDay = curDayFormat.format(now);
        year = curYearFormat.format(date);
        month = curMonthFormat.format(date);
        tvDate.setText(year+ "-" + month);

        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");
    // 여기까지 달력상단 요일을 표시할 정보를 나타내도록 List 컬렉션에 요일정보를 넣은거임 ㅇㅇ
        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date))-1,0);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        for(int i = 0; i<dayNum; i++){
            dayList.add("");   // 달력 상단 봐보세요 빈날짜가있죠?? 그렇게 나타나도록 "" 빈문자열 넣는겁니다. 위 문장들은 얼마나 넣어야하는지 정보 얻는겁니다
        }
       setCalendarDate(mCal.get(Calendar.DAY_OF_MONTH)+1); // 이메소드 하단에 있습니다 확인해보세요
        gridAdapter = new GridAdapter(getApplicationContext(), dayList); // 그리드 어댑터 생성  밑에 그리드 어댑터 클래스 있음
        gridView.setAdapter(gridAdapter); //  어댑터 달아주기

/*      스레드 미사용 시
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getJSONArray("response") != null){
                        reservationList.clear();
                    //  Toast.makeText(ReservationPage.this,todayDate, Toast.LENGTH_SHORT).show();
                        try{
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            int count = 0;
                            int sid, people;
                            String name, startTime, endTime;
                            while(count < jsonArray.length()){
                                JSONObject object = jsonArray.getJSONObject(count);
                                sid = object.getInt("sid");
                                name = object.getString("name");
                                people = object.getInt("people");
                                startTime = object.getString("startTime");
                                endTime = object.getString("endTime");
                                Reservation reservation = new Reservation(sid, name, people, startTime, endTime);
                                reservationList.add(reservation);
                                count++;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
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
*/


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // 항목(날짜) 클릭시 항 행동 정의하는 메소드

                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                try{
                    String rmonth;
                    day = Integer.parseInt(parent.getItemAtPosition(position).toString()); // 클릭한 날짜 얻어냄
                    setTodayDate(year + "-" + month + "-" + day+"");
                    listlayout = (RelativeLayout)findViewById(R.id.list);  // 빈 레이아웃 얻음

                    if(month.contains("0")){ // 이 if 문은 이번 달에 0 포함 되어있을시 없애고 today 필드에 "월/일" 형식으로 저장하는 문장
                        rmonth = month.replace("0","");
                        setToday(rmonth+"/"+day);

                    }else{
                        setToday(month+"/"+day);
                    }

                  //  Toast.makeText(ReservationPage.this, day + "일 선택", Toast.LENGTH_SHORT).show(); // 선택 날짜 출력 있으나 마나입니다. 그냥 넣어봤습니다
                    rel = (RelativeLayout)inflater.inflate(R.layout.list_registrant,null); // 빈레이아웃을 R.layout.list_registrant 로 채웁니다.
                    studentList = rel.findViewById(R.id.studentList);

                     //////////////////////////////////////
                    //여기를 내가 만든 어댑터붙여 주고!
                    adapter = null;

                    adapter = new ReservationListAdapter(getApplicationContext(), reservationList);
                    studentList.setAdapter(adapter);

                    /* 스레드 미사용 시
                    ReservationRequest reservationRequest = new ReservationRequest(todayDate ,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ReservationPage.this);
                    queue.add(reservationRequest);
                    */
                    new ReservationBackgroundTask().execute(todayDate);

                    listlayout.removeAllViews(); // 레이아웃이 덮혀써지지 않도록 이미 만들어진 레이아웃 제거 하는겁니다.
                    // 요기서 꺼냄

                    rel.findViewById(R.id.reserve).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {  // 예약 하기 버튼을 눌렀을 때 할 행동을 정의합니다. 추가정보로 월,일,오늘날짜 전달합니다.
                            Intent intent = new Intent(getApplicationContext(),RequestPage.class);
                            intent.putExtra("sid", sid);
                            intent.putExtra("password", password);
                            intent.putExtra("subject", subject);
                            intent.putExtra("name",name);
                            intent.putExtra("phone",phone);
                            intent.putExtra("manager",manager);
                            intent.putExtra("Month",month);
                            intent.putExtra("Date", day+"");
                            intent.putExtra("Today", today);
                            intent.putExtra("todayDate", todayDate);
                            intent.putExtra("ReservationDay", reservationDay);
                            startActivity(intent);
                        }
                    });

                }catch(NumberFormatException e){
                    view.setBackground(new ColorDrawable(Color.WHITE));

                }

            }
        });
    }

    private void setCalendarDate(int month){ // 이메소드드는  dayList 컬렉션에 날짜 같은 숫자를 집어 넣습니다.
        mCal.set(Calendar.MONTH, month-1);
        for(int i = 0 ; i<mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){  // 달의 일 갯수가 다를 땐 여기를 수정 하세요
            dayList.add(""+ (i+1)); // 1일부터 이번달 끝 날짜 만큼 dayList 컬렉션에 문자열 형식으로 넣습니다.
        }
    }

    private class GridAdapter extends BaseAdapter { // 그리드 어댑터 클래스 입니다 BaseAdapter를 재정의 해주고 getCount,getView 등을 재정의 해줍니다.
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { // 제일 중요한 메소드, 그리드 뷰의 각 항목에 출력한 날짜정보 를 만듭니다.
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();
                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));  // 이메소드가 각 항목당 날짜를 표시하는 메소드 이메소드 때문에 달력처럼 보이게됨
            mCal = Calendar.getInstance();
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) {
                holder.tvItemGridView.setTextColor(Color.RED); // 오늘 날짜 빨간색으로 보이게되는 메소드
            }

            return convertView;
        }

    }

    private class ViewHolder{
        TextView tvItemGridView;
    }

    public void reservationCheck(View v){
        if(v.getId() == R.id.check){
            Intent intent = new Intent(this,ReservationCheckPage.class);
            intent.putExtra("sid", sid);
            intent.putExtra("password", password);
            intent.putExtra("subject", subject);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("manager",manager);
            startActivity(intent); // 예약 확인 버튼 클릭시 ReservationCheckPage 뜨게함
        }
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
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
        if(listlayout != null){
            listlayout.removeAllViews();
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
                            //  Toast.makeText(ReservationPage.this,todayDate, Toast.LENGTH_SHORT).show();
                            try{
                                JSONArray jsonArray = jsonObject.getJSONArray("response");
                                int count = 0;
                                int sid, people;
                                String name, startTime, endTime;
                                while(count < jsonArray.length()){
                                    JSONObject object = jsonArray.getJSONObject(count);
                                    sid = object.getInt("sid");
                                    name = object.getString("name");
                                    people = object.getInt("people");
                                    startTime = object.getString("startTime");
                                    endTime = object.getString("endTime");
                                    Reservation reservation = new Reservation(sid, name, people, startTime, endTime);
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

    }

}


