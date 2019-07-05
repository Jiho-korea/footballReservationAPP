package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyReservationDeleteRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/MyReservationDelete.php";
    private Map<String, String> parameters;

    public MyReservationDeleteRequest(String sid, String date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("sid", sid);
        parameters.put("date", date);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

