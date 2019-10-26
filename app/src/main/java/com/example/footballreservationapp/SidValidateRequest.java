package com.example.footballreservationapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//회원가입시 이미 가입한 학번이 있는지 조사하는 클래스
public class SidValidateRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/SidValidate.php";
    private Map<String, String> parameters;

    public SidValidateRequest(String sid, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("sid",sid);
    }
    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

}
