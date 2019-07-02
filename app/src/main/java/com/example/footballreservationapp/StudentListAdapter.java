package com.example.footballreservationapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    private Context context;
    private List<Student> studentList;

    public StudentListAdapter(Context context, List<Student> studentList){
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.student, null);
        TextView num = (TextView)v.findViewById(R.id.num);
        TextView sid = (TextView)v.findViewById(R.id.sid);
        TextView name = (TextView)v.findViewById(R.id.name);
        TextView phone = (TextView)v.findViewById(R.id.phone);
        TextView subject = (TextView)v.findViewById(R.id.subject);

        num.setText((position+1) + "");
        sid.setText(studentList.get(position).getSid() + "");
        name.setText(studentList.get(position).getName());
        phone.setText(studentList.get(position).getPhone());
        subject.setText(studentList.get(position).getSubject());

        v.setTag(studentList.get(position).getSid());

        return v;
    }
}
