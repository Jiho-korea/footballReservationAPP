package com.example.footballreservationapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

// 이클래스는 R.layout.activity_main 레이아웃으로 화면을 채워 주의사항, 이용시간등을 보여준다. 있으나마나 그래그래
public class MainActivity extends AppCompatActivity {
    int sid;
    String password;
    String subject;
    String name;
    String phone;
    int manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeText = (TextView)findViewById(R.id.welcome);

        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", 0);
        password = intent.getStringExtra("password");
        subject = intent.getStringExtra("subject");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        manager = intent.getIntExtra("manager",0);


        welcomeText.setText("안녕하세요 " + name + " 님!");
    }

    public void mClick(View v){
        Intent intent = new Intent(this, ReservationPage.class);
        intent.putExtra("sid", sid);
        intent.putExtra("password", password);
        intent.putExtra("subject", subject);
        intent.putExtra("name",name);
        intent.putExtra("phone",phone);
        intent.putExtra("manager",manager);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("연암공대 풋살장 예약 종료")
                .setMessage("애플리케이션을 종료하시겠습니까?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(MainActivity.this);
                        System.exit(0);
                    }

                })
                .setNegativeButton("Cancle", null)
                .show();
    }
}
