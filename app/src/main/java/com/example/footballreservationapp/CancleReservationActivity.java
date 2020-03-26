package com.example.footballreservationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CancleReservationActivity extends AppCompatActivity {
    private ProgressBar circle_bar;
    private Spinner reasonSpinner;

    int serial_number;
    String date;
    String starttime;
    String c_password;
    private String canclereason;
    int status_code;
    private EditText c_passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancle_reservation);

        Intent intent = getIntent();
        serial_number = intent.getIntExtra("serial_number", 0);
        date = intent.getStringExtra("date");
        starttime = intent.getStringExtra("starttime");
        status_code = intent.getIntExtra("status_code", 0);

        circle_bar = (ProgressBar)findViewById(R.id.progressBar);
        circle_bar.setVisibility(View.GONE);

        reasonSpinner = (Spinner)findViewById(R.id.reasonspinner);
        final ArrayList<String> list = new ArrayList<>();
        list.add("선택");
        list.add("단순변심");
        list.add("행사예정");
        list.add("사용불가시간");
        list.add("공사중");
        list.add("임시폐쇄");
        list.add("기타(경비실문의)");
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,list);
        reasonSpinner.setAdapter(spinnerAdapter);

        c_passwordText = (EditText)findViewById(R.id.c_passwordText);
    }

    // 예약 취소 작업
    public void mCancle(View v) {
        if (v.getId() == R.id.cancle) {
            checkNetWork();
            circle_bar.setVisibility(View.VISIBLE);
            canclereason = reasonSpinner.getSelectedItem().toString();
            if(!"".equals(c_passwordText.getText().toString())){
                c_password = c_passwordText.getText().toString();
            }else{
                circle_bar.setVisibility(View.GONE);
                Toast.makeText(CancleReservationActivity.this, "예약비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                return;
            }

            if("선택".equals(canclereason.trim())){
                circle_bar.setVisibility(View.GONE);
                Toast.makeText(CancleReservationActivity.this, "사유를 선택하여 주십시오.", Toast.LENGTH_SHORT).show();
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
                            circle_bar.setVisibility(View.GONE);
                            Toast.makeText(CancleReservationActivity.this,"예약취소완료",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            circle_bar.setVisibility(View.GONE);
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
            CancleReservationRequest cancleReservationRequest = new CancleReservationRequest(serial_number,date,starttime,status_code,c_password,canclereason,responseListener);
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

    // 네트워크 연결 유무 확인 후 대화상자를 띄우는 메소드
    private void checkNetWork(){
        if(netWork() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(CancleReservationActivity.this);
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
                            ActivityCompat.finishAffinity(CancleReservationActivity.this);
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
