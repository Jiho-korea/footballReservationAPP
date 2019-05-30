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

        intent = getIntent();

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

    public void mSubmit(View v){
        if(v.getId() == R.id.submit){

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
