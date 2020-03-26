package com.example.footballreservationapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private ArrayList<String> dayList; // 달력,날짜 정보를 가지고있는 ArrayList 컬렉션(이번달)
    private GridView calendarGrid;  // 달력 나타내는 그리드뷰(이번달)
    private GridAdapter gridAdapter; // 그리드 뷰에 항목정보를 제공해주는 그리드어댑터(이번달)
    private Calendar mCal;
    private String date; //yyyy-MM-dd 형식

    private TextView month; // 상단의 월 정보
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.9), (int)(height * 0.6));


        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        month = (TextView)findViewById(R.id.month);
        month.setText(" " + Integer.parseInt(date.split("-")[1]) + "월");

        calendarGrid =(GridView)findViewById(R.id.calendarGrid);  // 첫번째 달력 그리드뷰(필드가 위젯을 가르키도록 findViewById 사용)

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

        mCal.set(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1])-1,1);
        int startday = mCal.get(Calendar.DAY_OF_WEEK)-1;
        for(int i = 0; i<startday; i++){
            dayList.add("");   // 달력 상단 봐보세요 빈날짜가있죠?? 그렇게 나타나도록 "" 빈문자열 넣는겁니다. 위 문장들은 얼마나 넣어야하는지 정보 얻는겁니다
        }

        setCalendarDate(Integer.parseInt(date.split("-")[1])); // 이메소드 하단에 있습니다 확인
        gridAdapter = new GridAdapter(getApplicationContext(), dayList); // 그리드 어댑터 생성  밑에 그리드 어댑터 클래스 있음
        calendarGrid.setAdapter(gridAdapter); //  어댑터 달아주기

        calendarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // 항목(날짜) 클릭시 항 행동 정의하는 메소드
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                try{

                    int day = Integer.parseInt(parent.getItemAtPosition(position).toString()); // 클릭한 날짜 얻어냄

                    String trueDay = day+""; // 날짜(day)가 한자리일경우 앞에 0이 붙어있는 형태

                    if(day < 10){
                        trueDay = "0"+trueDay;
                    }
                    //Toast.makeText(CalendarActivity.this, day + "일 선택" + trueDay, Toast.LENGTH_SHORT).show(); // 선택 날짜 출력 있으나 마나입니다. 그냥 넣어봤습니다

                    Intent intent = new Intent();
                    intent.putExtra("choiceDay", trueDay);
                    CalendarActivity.this.setResult(RESULT_OK , intent);
                    finish();
                }catch(NumberFormatException e){
                    // 빈칸 혹은 요일이름을 클릭했을때의 행동
                    calendarGrid.clearFocus();

                }

            }
        });
    }

    private void setCalendarDate(int month){ // 이메소드는  dayList 컬렉션에 날짜 같은 숫자를 집어 넣습니다. (이번달)
        mCal.set(Calendar.MONTH, month-1);
        for(int i = 1 ; i<=mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){  // 달의 일 갯수가 다를 땐 여기를 수정 하세요
            dayList.add(""+ (i)); // 1일부터 이번달 끝 날짜 만큼 dayList 컬렉션에 문자열 형식으로 넣습니다.
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
            Integer thisMonth = mCal.get(Calendar.MONTH) + 1;
            String sToday = String.valueOf(today);
            //String[] subDate = tvDate.getText().toString().split("-");
            //                              && subDate[1].equals(thisMonth+"")
            if (sToday.equals(getItem(position))) {
                holder.tvItemGridView.setTextColor(Color.RED); // 오늘 날짜 빨간색으로 보이게되는 메소드
            }

            return convertView;
        }

    }

    private class ViewHolder{
        TextView tvItemGridView;
    }
}
