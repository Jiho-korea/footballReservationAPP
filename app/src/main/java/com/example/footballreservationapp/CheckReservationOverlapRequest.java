package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckReservationOverlapRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/CheckReservationOverlap.php";
    private Map<String, String> parameters;

    public CheckReservationOverlapRequest(String startTime, String endTime , String date,  Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("startTime", startTime);
        parameters.put("endTime", endTime);
        parameters.put("date",date);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
