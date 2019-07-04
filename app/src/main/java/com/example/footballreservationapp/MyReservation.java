package com.example.footballreservationapp;

import android.util.Log;

public class MyReservation {
    int sid;
    String date;
    int people;
    String startTime;
    String endTime;
    String reservationday;
    int approval;

    public MyReservation(int sid, String date, int people, String startTime, String endTime, String reservationday, int approval) {
        Log.i("t","MyReservation 생성자 실행");
        this.sid = sid;
        this.date = date;
        this.people = people;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationday = reservationday;
        this.approval = approval;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReservationday() {
        return reservationday;
    }

    public void setReservationday(String reservationday) {
        this.reservationday = reservationday;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }
}
