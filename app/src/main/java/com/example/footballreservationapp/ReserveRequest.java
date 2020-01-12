package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReserveRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/Reserve.php";
    private Map<String, String> parameters;

    public ReserveRequest(int sid, String date, int people, String startTime, String endTime, String name, String phone, String subject, String password,Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("sid",sid+"");
        parameters.put("date",date);
        parameters.put("people",people + "");
        parameters.put("startTime", startTime);
        parameters.put("endTime", endTime);
        parameters.put("name", name);
        parameters.put("phone", phone);
        parameters.put("subject", subject);
        parameters.put("password", password);
        parameters.put("approval", 0+"");
        parameters.put("cancellation", 0+"");
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
