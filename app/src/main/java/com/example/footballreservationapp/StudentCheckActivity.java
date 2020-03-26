package com.example.footballreservationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class StudentCheckActivity extends AppCompatActivity {
    private ProgressBar circle_bar;

    int serial_number;
    String date;
    String starttime;
    String c_password;
    int status_code;
    private EditText c_passwordText;
    ImageView load_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check);

        Intent intent = getIntent();
        serial_number = intent.getIntExtra("serial_number", 0);
        date = intent.getStringExtra("date");
        starttime = intent.getStringExtra("starttime");
        status_code = intent.getIntExtra("status_code", 0);

        circle_bar = (ProgressBar)findViewById(R.id.progressBar);
        circle_bar.setVisibility(View.GONE);

        c_passwordText = (EditText)findViewById(R.id.c_passwordText);

        load_image = (ImageView) findViewById(R.id.load_image);
    }

    // 예약 취소 작업
    public void mCancle(View v) {
        if (v.getId() == R.id.checkStudentCard) {
            checkNetWork();
            circle_bar.setVisibility(View.VISIBLE);
            if(!"".equals(c_passwordText.getText().toString())){
                c_password = c_passwordText.getText().toString();
            }else{
                circle_bar.setVisibility(View.GONE);
                Toast.makeText(StudentCheckActivity.this, "예약비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                return;
            }

            new StudentCheckBackgroundTask().execute();
        }
    }

    class StudentCheckBackgroundTask extends AsyncTask<Void, Void, Void> {
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
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){

                            // 이곳에 비밀번호 일치 할시 수행할 코드 작성
                            String image = jsonResponse.getString("image");
                            Log.e("DB에서 Base64로 인코딩된 문자열", image);
//                            Log.e("test1", jsonResponse.getString("test1"));
//                            Log.e("test2", jsonResponse.getString("test2"));
                            String newImage = null;
                            try{
                                newImage = URLDecoder.decode(image, "UTF-8");
                                Log.e("UTF-8디코딩된 문자열", newImage);
                            }catch(Exception e){

                            }

                            Bitmap bm = StringToBitMap(newImage); // 이 bm 이 현재 null
                            Log.e("bm", bm.toString());
                            bm=resize(bm);
                            Log.e("resizebm", bm.toString());
                            load_image.setImageBitmap(bm);

                            circle_bar.setVisibility(View.GONE);
                            Toast.makeText(StudentCheckActivity.this,"학생증 조회 성공",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            circle_bar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentCheckActivity.this);
                            builder.setMessage("비밀번호를 다시 확인해 주세요.")
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
            StudentCheckRequest studentCheckRequest = new StudentCheckRequest(serial_number,date,starttime,status_code,c_password,responseListener);
            RequestQueue queue = Volley.newRequestQueue(StudentCheckActivity.this);
            queue.add(studentCheckRequest);

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

    // 비트맵 resize 해주는 메소드
    private Bitmap resize(Bitmap orgbm){
        Configuration config=getResources().getConfiguration();
        Log.e("orgbm",orgbm.getWidth() + "");
        Bitmap newbm;
        if(config.smallestScreenWidthDp>=800)
            newbm = Bitmap.createScaledBitmap(orgbm, 400, 240, true);
        else if(config.smallestScreenWidthDp>=600)
            newbm = Bitmap.createScaledBitmap(orgbm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            newbm = Bitmap.createScaledBitmap(orgbm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            newbm = Bitmap.createScaledBitmap(orgbm, 180, 108, true);
        else
            newbm = Bitmap.createScaledBitmap(orgbm, 160, 96, true);

        Log.e("newbm",newbm.getWidth() + "");
        return newbm;
    }


    // String 을 BitMap 으로 변경해주는 메소드
    public static Bitmap StringToBitMap(String image){
        Log.e("StringToBitMap","StringToBitMap");
        try{
            String cleanImage = image.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
            cleanImage = cleanImage.substring(cleanImage.indexOf(",") + 1);

            byte [] encodeByte= Base64.decode(cleanImage,Base64.DEFAULT);
            Log.e("encodeByte",encodeByte + "");
            //byte[] encodeByte2 =  test.getBytes("UTF-8");
            //Log.e("encodeByte2",encodeByte2 + "");

            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); // 여기서 null 이 생긴다


            Log.e("StringToBitMap",bitmap+"");



            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    // 네트워크 연결 유무 확인 후 대화상자를 띄우는 메소드
    private void checkNetWork(){
        if(netWork() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentCheckActivity.this);
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
                            ActivityCompat.finishAffinity(StudentCheckActivity.this);
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
