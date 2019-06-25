package com.example.footballreservationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.RelativeSizeSpan;
import android.view.View;
// 이클래스는 R.layout.activity_main 레이아웃으로 화면을 채워 주의사항, 이용시간등을 보여준다. 있으나마나 그래그래
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mClick(View v){
        Intent intent = new Intent(this, ReservationPage.class);
        startActivity(intent);
    }

}
