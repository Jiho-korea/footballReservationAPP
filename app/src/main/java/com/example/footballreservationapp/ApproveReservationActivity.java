package com.example.footballreservationapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    String date;
    int sid;
    String starttime;
    String m_password;
    int cancellation;
    private EditText m_passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_reservation);

        Intent intent = getIntent();
        sid = intent.getIntExtra("sid", 0);
        date = intent.getStringExtra("date");
        starttime = intent.getStringExtra("starttime");
        cancellation = intent.getIntExtra("cancellation",0);

        m_passwordText = (EditText)findViewById(R.id.m_passwordText);
    }

    // 예약 취소 작업
    public void mCancle(View v) {
        if (v.getId() == R.id.cancle) {
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
            ApproveReservationRequest approveReservationRequest = new ApproveReservationRequest(sid,date,starttime,m_password,cancellation,responseListener);
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
}
