package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.request.StringRequest;
//import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
//예약 승인해주는
public class ApproveReservationRequest  extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/ApproveReservation.php";
    private Map<String, String> parameters;

    public ApproveReservationRequest(int serial_number, String date, String starttime, String password, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("serial_number", serial_number+"");
        parameters.put("date", date);
        parameters.put("starttime", starttime);
        parameters.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
