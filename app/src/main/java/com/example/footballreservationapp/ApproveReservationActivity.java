package com.example.footballreservationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ApproveReservationActivity extends AppCompatActivity {
    int serial_number;
    String date;
    String starttime;
    String m_password;
    private EditText m_passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_reservation);

        Intent intent = getIntent();
        serial_number = intent.getIntExtra("serial_number", 0);
        date = intent.getStringExtra("date");
        starttime = intent.getStringExtra("starttime");

        m_passwordText = (EditText)findViewById(R.id.m_passwordText);
    }

    // 예약 취소 작업
    public void mCancle(View v) {
        if (v.getId() == R.id.cancle) {
            checkNetWork();
            if(!"".equals(m_passwordText.getText().toString())){
                m_password = m_passwordText.getText().toString();
            }else{
                Toast.makeText(ApproveReservationActivity.this, "관리자비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                return;
            }

            new ApproveReservationBackgroundTask().execute();
        }
    }

    class ApproveReservationBackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... args) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success")   ;
                        if(success){
                            Toast.makeText(ApproveReservationActivity.this,"예약승인완료",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ApproveReservationActivity.this);
                            builder.setMessage("예약승인에 실패하셨습니다. 비밀번호를 확인해 주세요.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                            return;
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            ApproveReservationRequest approveReservationRequest = new ApproveReservationRequest(serial_number,date,starttime,m_password,responseListener);
            RequestQueue queue = Volley.newRequestQueue(ApproveReservationActivity.this);
            queue.add(approveReservationRequest);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    // 네트워크 연결 유무 확인 후 대화상자를 띄우는 메소드
    private void checkNetWork(){
        if(netWork() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(ApproveReservationActivity.this);
            builder.setMessage("네트워크 연결이 끊어져 있습니다.\nwifi 또는 모바일 데이터 네트워크\n연결 상태를 확인해주세요.\n재시도 하려면 확인을 터치 해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(netWork() == true){
//                                circle_bar.setVisibility(View.GONE);
                            }else{
                                checkNetWork();
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.finishAffinity(ApproveReservationActivity.this);
                        }
                    })
                    .create()
                    .show();
        }
    }

    // 네트워크 연결 유무를 확인하는 메소드
    private boolean netWork(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        if(ninfo == null){
            return false;
        }else{
            return true;
        }
    }
}
