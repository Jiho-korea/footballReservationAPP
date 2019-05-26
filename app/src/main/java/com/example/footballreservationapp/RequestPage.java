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

public class RequestPage extends AppCompatActivity {

    SQLiteDatabase db;

    Intent intent;
    private String today;
    private Button submitBtn;
    private EditText sidEdit;
    private EditText subjectEdit;
    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText startTimehourEdit;
    private EditText startTimeminuteEdit;
    private EditText endTimehourEdit;
    private EditText endTimeminuteEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        DatabaseHelper databaseHelper = new DatabaseHelper(this,"studentDB",null,1);
        db = databaseHelper.getWritableDatabase();

        intent = getIntent();

        submitBtn = (Button)findViewById(R.id.submit);

        sidEdit = (EditText)findViewById(R.id.sid);
        subjectEdit = (EditText)findViewById(R.id.subject);
        nameEdit = (EditText)findViewById(R.id.name);
        phoneEdit = (EditText)findViewById(R.id.phone);
        startTimehourEdit = (EditText)findViewById(R.id.starttimehour);
        startTimeminuteEdit = (EditText)findViewById(R.id.starttimeminute);
        endTimehourEdit = (EditText)findViewById(R.id.endtimehour);
        endTimeminuteEdit = (EditText)findViewById(R.id.endtimeminute);

        TextView date = (TextView)findViewById(R.id.date);
        String month = intent.getStringExtra("Month");
        String rmonth;
        int day = intent.getIntExtra("Date",00);
        if(month.contains("0")){
            rmonth = month.replace("0","");
            today = rmonth+day;
            date.setText(rmonth + " / " + day);
        }else{
            today = month+day;
            date.setText(month + " / " + day);
        }

    }

    public void mSubmit(View v){
        if(v.getId() == R.id.submit){
            /*
            int sid = Integer.parseInt(sidEdit.getText().toString());
            String subject = subjectEdit.getText().toString();
            String name = nameEdit.getText().toString();
            int phone = Integer.parseInt(phoneEdit.getText().toString());
            String startTime = startTimehourEdit.getText().toString() + startTimeminuteEdit.getText().toString();
            String endTime = endTimehourEdit.getText().toString() + endTimeminuteEdit.getText().toString();

            dbInsert("STUDENTS", sid,subject,name,phone,today,startTime,endTime);

            db.close();
            */
            Toast.makeText(this,"신청완료",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void dbInsert(String tableName, Integer sid, String subject , String name, Integer phone, String date ,String startTime, String endTime) {
        Log.d("student data input", "Insert Data " + name);

        ContentValues contentValues = new ContentValues();
        contentValues.put("SID", sid);
        contentValues.put("SUBJECT", subject);
        contentValues.put("NAME", name);
        contentValues.put("PHONE", phone);
        contentValues.put("DATE", date);
        contentValues.put("STARTTIME", startTime);
        contentValues.put("ENDTIME", endTime);
        // 리턴값: 생성된 데이터의 id
        long id = db.insert(tableName, null, contentValues);

        Log.d("student data input", "id: " + id);
    }


}
