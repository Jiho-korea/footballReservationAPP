package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//예약 승인해주는 
public class ApproveReservationRequest  extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/ApproveReservation.php";
    private Map<String, String> parameters;

    public ApproveReservationRequest(String sid, String date, Response.Listener<String> listener) {
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
