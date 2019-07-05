package com.example.footballreservationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentsManageActivity extends AppCompatActivity {
    private ListView listView;
    private StudentListAdapter adapter;
    private List<Student> studentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_manage);
        Intent intent = getIntent();

        listView = (ListView)findViewById(R.id.studentslistView);
        studentList = new ArrayList<Student>();
        adapter = new StudentListAdapter(getApplicationContext(),studentList);
        listView.setAdapter(adapter);

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("studentList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            int sid;
            String name, phone, subject;
            while(count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count);
                sid = object.getInt("sid");
                name = object.getString("name");
                phone = object.getString("phone");
                subject = object.getString("subject");
                Student student = new Student(sid, name, phone, subject);
                if(sid != 1111){
                    studentList.add(student);
                }
                adapter.notifyDataSetChanged();
                count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
