package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.request.StringRequest;
//import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CancleReservationRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/CancleReservation.php";
    private Map<String, String> parameters;

    public CancleReservationRequest(int serial_number, String date, String starttime, int status_code,String password, String canclereason,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("serial_number", serial_number+"");
        parameters.put("date", date);
        parameters.put("starttime", starttime);
        parameters.put("status_code", status_code+"");
        parameters.put("password", password);
        parameters.put("canclereason", canclereason);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    } {
    }
}
