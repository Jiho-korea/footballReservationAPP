package com.example.footballreservationapp;

public class Student {
    int sid;
    String name;
    String phone;
    String subject;


    public Student(int sid, String name, String phone, String subject) {
        this.sid = sid;
        this.name = name;
        this.phone = phone;
        this.subject = subject;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
