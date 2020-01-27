package com.example.footballreservationapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class CancleReservationActivity extends AppCompatActivity {
    int serial_number;
    String date;
    String starttime;
    String c_password;
    private EditText c_passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_reservation);

        Intent intent = getIntent();
        serial_number = intent.getIntExtra("serial_number", 0);
        date = intent.getStringExtra("date");
        starttime = intent.getStringExtra("starttime");

        c_passwordText = (EditText)findViewById(R.id.c_passwordText);
    }

    // 예약 취소 작업
    public void mCancle(View v) {
        if (v.getId() == R.id.cancle) {
            if(!"".equals(c_passwordText.getText().toString())){
                c_password = c_passwordText.getText().toString();
            }else{
                Toast.makeText(CancleReservationActivity.this, "예약비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                return;
            }

            new CancleReservationBackgroundTask().execute();
        }
    }

    class CancleReservationBackgroundTask extends AsyncTask<Void, Void, Void> {
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
                            Toast.makeText(CancleReservationActivity.this,"예약취소완료",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(CancleReservationActivity.this);
                            builder.setMessage("예약취소에 실패하셨습니다. 비밀번호를 확인해 주세요.")
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
            CancleReservationRequest cancleReservationRequest = new CancleReservationRequest(serial_number,date,starttime,c_password,responseListener);
            RequestQueue queue = Volley.newRequestQueue(CancleReservationActivity.this);
            queue.add(cancleReservationRequest);

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
