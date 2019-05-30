package com.example.footballreservationapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReservationPage extends AppCompatActivity {
    ListView studentList;
    DatabaseHelper mHelper;

    private String today;
    private String month;
    private String day;
    private TextView tvDate;
    private GridAdapter gridAdapter;
    private RelativeLayout listlayout;

    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    private  SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationpage);

        mHelper = new DatabaseHelper(this);
        db = mHelper.getWritableDatabase();

        tvDate = (TextView)findViewById(R.id.tv_date);
        gridView =(GridView)findViewById(R.id.gridview);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        final String reservationDay = curDayFormat.format(now);

        month = curMonthFormat.format(date);
        tvDate.setText(curYearFormat.format(date)+ "/" + curMonthFormat.format(date));

        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date))-1,0);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        for(int i = 0; i<dayNum; i++){
            dayList.add("");
        }
       setCalendarDate(mCal.get(Calendar.DAY_OF_MONTH)+1);
        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);
        // WHERE DATE=" + "'" + today +"'




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                try{
                    String rmonth;
                    day = parent.getItemAtPosition(position).toString();
                    if(month.contains("0")){
                        rmonth = month.replace("0","");
                        setToday(rmonth+"/"+day);

                    }else{
                        setToday(month+"/"+day);
                    }

                    final Cursor cursor = db.rawQuery("SELECT * FROM registrants WHERE DATE= " +"'" + today + "'",null);

                    Toast.makeText(ReservationPage.this, day + "일 선택", Toast.LENGTH_SHORT).show();
                    listlayout = (RelativeLayout)findViewById(R.id.list);
                    RelativeLayout rel = (RelativeLayout)inflater.inflate(R.layout.list_registrant,null);
                    listlayout.removeAllViews();
                    listlayout.addView(rel);
                    studentList = rel.findViewById(R.id.studentList);



                    SimpleCursorAdapter adapter = null;
                    adapter = new SimpleCursorAdapter(ReservationPage.this, R.layout.reservationinthatday,
                            cursor,new String[]{"STARTTIME","ENDTIME","PEOPLE","NAME"},
                            new int[]{R.id.usingstarttime, R.id.usingendtime, R.id.usingpersonnumber, R.id.personwhoreserve});

                    studentList.setAdapter(adapter);



                    rel.findViewById(R.id.reserve).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),RequestPage.class);

                            intent.putExtra("Month",month);
                            intent.putExtra("Date", day);
                            intent.putExtra("Today", today);
                            intent.putExtra("ReservationDay", reservationDay);
                            startActivity(intent);
                        }
                    });

                }catch(NumberFormatException e){

                }

            }
        });
    }

    private void setCalendarDate(int month){
        mCal.set(Calendar.MONTH, month-1);
        for(int i = 0 ; i<mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            dayList.add(""+ (i+1));
        }
    }

    private class GridAdapter extends BaseAdapter {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();
                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));
            mCal = Calendar.getInstance();
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) {
                holder.tvItemGridView.setTextColor(Color.RED);
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
            startActivity(intent);
        }
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}



