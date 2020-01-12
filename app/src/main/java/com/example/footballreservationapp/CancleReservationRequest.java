package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CancleReservationRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/CancleReservation.php";
    private Map<String, String> parameters;

    public CancleReservationRequest(int sid, String date, String starttime, String password, int cancellation,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("sid", sid+"");
        parameters.put("date", date);
        parameters.put("starttime", starttime);
        parameters.put("password", password);
        parameters.put("cancellation", cancellation+"");
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    } {
    }
}
