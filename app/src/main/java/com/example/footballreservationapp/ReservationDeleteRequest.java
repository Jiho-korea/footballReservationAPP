package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReservationDeleteRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/ReservationDelete.php";
    private Map<String, String> parameters;

    public ReservationDeleteRequest(String sid, String date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("sid", sid);
        parameters.put("date", date);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    } {
    }
}
