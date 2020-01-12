package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
// 같은 날에 예약을 한적이 있는지 검사하는 역할
public class ReservationValidateRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/ReservationValidate.php";
    private Map<String, String> parameters;

    public ReservationValidateRequest(int sid, String date, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("sid",sid+"");
        parameters.put("date",date);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
