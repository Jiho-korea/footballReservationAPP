package com.example.footballreservationapp;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/Login.php";
    private Map<String, String> parameters;

    public LoginRequest(String sid, String password, Response.Listener<String> listener) {
    super(Method.POST,URL,listener,null);
    parameters = new HashMap<>();
        parameters.put("sid",sid);
        parameters.put("password", password);
    }
    @Override
        public Map<String, String> getParams() {
        return parameters;
        }

}

