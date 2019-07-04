package com.example.footballreservationapp;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyReservationRequest  extends StringRequest {

    final static private String URL = "http://yonamfootball.dothome.co.kr/MyReservationList.php";
    private Map<String, String> parameters;

    public MyReservationRequest(String sid, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        Log.i("t", "MyReservationRequest 생성자 실행");
        parameters = new HashMap<>();
        parameters.put("sid",sid);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}