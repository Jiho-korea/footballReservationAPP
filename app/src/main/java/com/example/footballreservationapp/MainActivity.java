package com.example.footballreservationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

// 이클래스는 R.layout.activity_main 레이아웃으로 화면을 채워 주의사항, 이용시간등을 보여준다.
public class MainActivity extends AppCompatActivity {
    private TextView information;

    ImageView campusTint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNetWork();

        TextView welcomeText = (TextView)findViewById(R.id.welcome);

        Intent intent = getIntent();

        campusTint = (ImageView)findViewById(R.id.campusTint);
        Drawable alpha = campusTint.getDrawable();
        alpha.setAlpha(50);

        information = (TextView)findViewById(R.id.information);
        information.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InformationPopupActivity.class));
            }
        });
        welcomeText.setText("안녕하세요! 반갑습니다.");
    }

    public void mClick(View v){
        if(v.getId() == R.id.reservationbtn) {
            Intent intent = new Intent(MainActivity.this, ReservationPage.class);
            startActivity(intent);
        }
    }

    // 네트워크 연결 유무 확인 후 대화상자를 띄우는 메소드
    private void checkNetWork(){
        if(netWork() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("네트워크 연결이 끊어져 있습니다.\nwifi 또는 모바일 데이터 네트워크\n연결 상태를 확인해주세요.\n재시도 하려면 확인을 터치 해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(netWork() == true){

                            }else{
                                checkNetWork();
                            }
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.finishAffinity(MainActivity.this);
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
