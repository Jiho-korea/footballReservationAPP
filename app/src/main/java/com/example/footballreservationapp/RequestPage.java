package com.example.footballreservationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class RequestPage extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        intent = getIntent();
        TextView date = (TextView)findViewById(R.id.date);
        String month = intent.getStringExtra("Month");
        String rmonth;
        int day = intent.getIntExtra("Date",00);
        if(month.contains("0")){
            rmonth = month.replace("0","");
            date.setText(rmonth + " / " + day);
        }else{
            date.setText(month + " / " + day);
        }

    }
}
