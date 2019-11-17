package com.example.footballreservationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageMainActivity extends AppCompatActivity {
    int sid;
    String password;
    String subject;
    String name;
    String phone;
    int manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_main);

        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", 0);
        password = intent.getStringExtra("password");
        subject = intent.getStringExtra("subject");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        manager = intent.getIntExtra("manager",0);
        TextView topword = (TextView)findViewById(R.id.topword);
        topword.setText(intent.getStringExtra("name") + " 님 안녕하세요! " + "\n" + "이곳은 관리자 페이지입니다.");

    }
    public void mClick(View v){
        switch (v.getId()){
            case R.id.studentsList:
                new StudentListBackgroundTask().execute();
                break;

            case R.id.reservationsList:
                new ReservationListBackgroundTask().execute();
                break;
            case R.id.reservationincalendar:
                Intent intent = new Intent(ManageMainActivity.this,ReservationPage.class);
                intent.putExtra("sid", sid);
                intent.putExtra("password", password);
                intent.putExtra("subject", subject);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("manager",manager);
                startActivity(intent);
                break;
        }
    }


    class StudentListBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://yonamfootball.dothome.co.kr/List.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(ManageMainActivity.this, StudentsManageActivity.class);
            intent.putExtra("studentList", result);
            ManageMainActivity.this.startActivity(intent);
        }
    }

    class ReservationListBackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://yonamfootball.dothome.co.kr/ManageReservationList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(ManageMainActivity.this, ReservationManageActivity.class);
            intent.putExtra("reservationList", result);
            ManageMainActivity.this.startActivity(intent);
        }
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
                        ActivityCompat.finishAffinity(ManageMainActivity.this);
                        System.exit(0);
                    }

                })
                .setNegativeButton("Cancle", null)
                .show();
    }
}
