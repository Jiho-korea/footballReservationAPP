package com.example.footballreservationapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestPage extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
/*
아래를 보면 필드가 상당히 많은 데 전부
사용자가 입력한 내용을 얻기위해 EditText 필드 선언한겁니다. 이름을 보면 직관적으로 어떤 정보를 입력받는 EditText인지 알 수있습니다.
 */
    Intent intent;
    private String today;
    private String reservationDay;
    private Button submitBtn;
    private EditText sidEdit;
    private EditText subjectEdit;
    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText peopleEdit;
    private EditText startTimehourEdit;
    private EditText startTimeminuteEdit;
    private EditText endTimehourEdit;
    private EditText endTimeminuteEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        intent = getIntent(); // ReservationPage 에서전달 받은 인텐트 얻어줍니다. 날짜정보만 가지고 있습니다.

        submitBtn = (Button)findViewById(R.id.submit);

        sidEdit = (EditText)findViewById(R.id.sid);
        subjectEdit = (EditText)findViewById(R.id.subject);
        nameEdit = (EditText)findViewById(R.id.name);
        phoneEdit = (EditText)findViewById(R.id.phone);
        peopleEdit = (EditText)findViewById(R.id.people);
        startTimehourEdit = (EditText)findViewById(R.id.starttimehour);
        startTimeminuteEdit = (EditText)findViewById(R.id.starttimeminute);
        endTimehourEdit = (EditText)findViewById(R.id.endtimehour);
        endTimeminuteEdit = (EditText)findViewById(R.id.endtimeminute);

        TextView date = (TextView)findViewById(R.id.date);
        String month = intent.getStringExtra("Month");
        reservationDay = month + "/" +intent.getStringExtra("ReservationDay");
        int day = intent.getIntExtra("Date",00);
        today = intent.getStringExtra("Today");
        date.setText(today);
    }

    public void mSubmit(View v){  // 노란색 예약신청 버튼 클릭시 사용자가 입력한 정보를 db에 넣는 작업을 합니다. ㅎㅎ
        if(v.getId() == R.id.submit){
            // 학번 학과 이름 전화번호 인원 시작시간 끝시간 을 db에 넣어주네요
            //수정해야할 부분은 시간과 관련된 부분입니다.
            int sid = Integer.parseInt(sidEdit.getText().toString());
            String subject = subjectEdit.getText().toString();
            String name = nameEdit.getText().toString();
            int phone = Integer.parseInt(phoneEdit.getText().toString());
            int people = Integer.parseInt(peopleEdit.getText().toString());
            String startTime = startTimehourEdit.getText().toString() + ":" +startTimeminuteEdit.getText().toString();
            String endTime = endTimehourEdit.getText().toString() + ":" + endTimeminuteEdit.getText().toString();

            dbInsert("registrants", sid,subject,name,phone,people,today,startTime,endTime,reservationDay);

            db.close();

            Toast.makeText(this,"신청완료",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void dbInsert(String tableName, Integer sid, String subject , String name, Integer phone, Integer people ,String date ,String startTime, String endTime,String reservationDay) {
        Log.d("student data input", "Insert Data " + name);

        ContentValues contentValues = new ContentValues();
        contentValues.put("SID", sid);
        contentValues.put("SUBJECT", subject);
        contentValues.put("NAME", name);
        contentValues.put("PHONE", phone);
        contentValues.put("PEOPLE", people);
        contentValues.put("DATE", date);
        contentValues.put("STARTTIME", startTime);
        contentValues.put("ENDTIME", endTime);
        contentValues.put("RESERVATIONDAY", reservationDay);
        // 리턴값: 생성된 데이터의 id
        long id = db.insert(tableName, null, contentValues);

        Log.d("student data input", "id: " + id);
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }
}
