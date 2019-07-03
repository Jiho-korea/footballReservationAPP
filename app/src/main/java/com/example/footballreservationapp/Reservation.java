package com.example.footballreservationapp;

public class Reservation {
    int sid;
    String name;
    int people;
    String startTime;
    String endTime;

    public Reservation(int sid, String name, int people, String startTime, String endTime) {
        this.sid = sid;
        this.name = name;
        this.people = people;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
