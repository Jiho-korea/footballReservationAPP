package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.request.StringRequest;
//import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReservationRequest extends StringRequest {

    final static private String URL = "http://yonamfootball.dothome.co.kr/ReservationList.php";
    private Map<String, String> parameters;

    public ReservationRequest(String date, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("date",date);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
