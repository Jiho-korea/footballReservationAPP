package com.example.footballreservationapp;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://yonamfootball.dothome.co.kr/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String sid, String subject, String name, String password,  String phone , Response.Listener<String> listener) {
    super(Method.POST,URL,listener,null);
    parameters = new HashMap<>();
        parameters.put("sid",sid);
        parameters.put("subject",subject);
        parameters.put("name",name);
        parameters.put("password", password);
        parameters.put("phone", phone);
        parameters.put("manager", 0+"");
    }
    @Override
        public Map<String, String> getParams() {
        return parameters;
        }

}

