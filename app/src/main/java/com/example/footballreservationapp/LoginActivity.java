package com.example.footballreservationapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText sidText =(EditText) findViewById(R.id.sidText);
        final EditText passwordText =(EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView registerButton = (TextView) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String sid = sidText.getText().toString();
                final String password = passwordText.getText().toString();

                new LoginBackgroundTask().execute(sid, password);

                    /*  스레드 미사용 시 ㅇㅇㅇㅇ
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                               int sid = jsonResponse.getInt("sid");
                               String password = jsonResponse.getString("password");
                               String subject = jsonResponse.getString("subject");
                               String name = jsonResponse.getString("name");
                               String phone = jsonResponse.getString("phone");
                               int manager = jsonResponse.getInt("manager");

                               if(manager == 0){
                                   Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                   intent.putExtra("sid", sid);
                                   intent.putExtra("password", password);
                                   intent.putExtra("subject", subject);
                                   intent.putExtra("name",name);
                                   intent.putExtra("phone",phone);
                                   intent.putExtra("manager",manager);

                                   LoginActivity.this.startActivity(intent);
                               }else{
                                   Intent intent = new Intent(LoginActivity.this, ManageMainActivity.class);
                                   intent.putExtra("name",name);
                                   LoginActivity.this.startActivity(intent);
                               }

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인에 실패하셨습니다")
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
                LoginRequest loginRequest = new LoginRequest(sid,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
           */
            }
        });
    }
    class LoginBackgroundTask extends AsyncTask<String, Void, Void> {
        String sid;
        String password;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... args) {
            sid = args[0];
            password = args[1];
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            int sid = jsonResponse.getInt("sid");
                            String password = jsonResponse.getString("password");
                            String subject = jsonResponse.getString("subject");
                            String name = jsonResponse.getString("name");
                            String phone = jsonResponse.getString("phone");
                            int manager = jsonResponse.getInt("manager");

                            if(manager == 0){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("sid", sid);
                                intent.putExtra("password", password);
                                intent.putExtra("subject", subject);
                                intent.putExtra("name",name);
                                intent.putExtra("phone",phone);
                                intent.putExtra("manager",manager);

                                LoginActivity.this.startActivity(intent);
                            }else{
                                Intent intent = new Intent(LoginActivity.this, ManageMainActivity.class);
                                intent.putExtra("sid", sid);
                                intent.putExtra("password", password);
                                intent.putExtra("subject", subject);
                                intent.putExtra("name",name);
                                intent.putExtra("phone",phone);
                                intent.putExtra("manager",manager);
                                LoginActivity.this.startActivity(intent);
                            }

                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("로그인에 실패하셨습니다")
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
            LoginRequest loginRequest = new LoginRequest(sid,password,responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}
