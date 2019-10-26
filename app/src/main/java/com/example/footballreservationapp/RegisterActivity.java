package com.example.footballreservationapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private boolean sidValidation = false;
    private EditText sidText;
    private EditText passwordText;
    private EditText passwordCheckText; // 비밀번호 확인용 TextView
    private EditText nameText;
    private EditText phoneText;
    private Spinner spinner;
    private Button validateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sidText =(EditText) findViewById(R.id.sidText);
        passwordText =(EditText) findViewById(R.id.passwordText);
        passwordCheckText =(EditText) findViewById(R.id.passwordCheckText);
        nameText =(EditText) findViewById(R.id.nameText);

        phoneText =(EditText) findViewById(R.id.phoneText);
        spinner = (Spinner)findViewById(R.id.spinner);

        validateButton = (Button)findViewById(R.id.sidValidateButton);
        validateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String sid = sidText.getText().toString();
                if(sidValidation){
                    return;
                }
                if(sid.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("학번은 빈칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    return;
                }

                new ValidationBackgroundTask().execute(sid);
            }
        });



        Button registerButton = (Button) findViewById(R.id.registerButton);
        final ArrayList<String> list = new ArrayList<>();
        list.add("학과를 선택해 주세요.");
        list.add("스마트소프트웨어");
        list.add("스마트전기전자공학");
        list.add("조선자동차항공기계");
        list.add("전자전기");
        list.add("기계공학");
        list.add("산업정보디자인");
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,list);
        spinner.setAdapter(spinnerAdapter);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                try{
                    int sid = Integer.parseInt(sidText.getText().toString());
                    String password = passwordText.getText().toString();
                    String passwordCheck = passwordCheckText.getText().toString();
                    String name = nameText.getText().toString();
                    String subject = spinner.getSelectedItem().toString();
                    String phone = phoneText.getText().toString();

                    if(!sidValidation){
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("먼저 학번 중복체크를 해주세요.")
                                .setNegativeButton("확인", null)
                                .create()
                                .show();

                    }else if(password.trim().equals("")) {
                        Toast.makeText(RegisterActivity.this, "비밀번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    }else if(!password.equals(passwordCheck)){
                        Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else if(name.trim().equals("")){
                        Toast.makeText(RegisterActivity.this, "이름를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    }else if(subject.trim().equals("학과를 선택해 주세요.")){
                        Toast.makeText(RegisterActivity.this, "학과를 선택하여 주십시오.", Toast.LENGTH_SHORT).show();
                    }else if(phone.trim().equals("")){
                        Toast.makeText(RegisterActivity.this, "전화번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                    }else{
                        new RegisterBackgroundTask().execute(sid+"",subject,name,password,phone);
                    }
                }catch(NumberFormatException e){
                    Toast.makeText(RegisterActivity.this, "학번을 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class RegisterBackgroundTask extends AsyncTask<String, Void, Void> {
        String sidclone;
        String subjectclone;
        String nameclone;
        String passwordclone;
        String phoneclone;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            sidclone = args[0];
            subjectclone = args[1];
            nameclone = args[2];
            passwordclone = args[3];
            phoneclone = args[4];
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success")   ;
                        if(success){

                            finish();
                            Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("회원등록에 실패하셨습니다")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();

                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            RegisterRequest registerRequest = new RegisterRequest(sidclone,subjectclone,nameclone,passwordclone,phoneclone ,responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    class ValidationBackgroundTask extends AsyncTask<String, Void, Void> {
        String sidclone;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            sidclone = args[0];
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("사용할 수 있는 학번입니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                            sidText.setEnabled(false);
                            validateButton.setEnabled(false);
                            sidValidation = true;
                            sidText.setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));
                            validateButton.setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("사용할 수 없는 학번입니다.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();

                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };
            SidValidateRequest sidValidateRequest = new SidValidateRequest(sidclone,responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(sidValidateRequest);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
