package com.example.footballreservationapp;

public class Reservation {
    int sid;
    String name;
    int people;
    String startTime;
    String endTime;
    String subject;
    String phone;
    String date;
    int approval;
    int cancellation;

    public Reservation(int sid, String date, String name, int people, String startTime, String endTime, String subject, String phone, int approval, int cancellation) {
        this.sid = sid;
        this.name = name;
        this.people = people;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.phone = phone;
        this.date = date;
        this.approval = approval;
        this.cancellation = cancellation;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }

    public int getCancellation() {
        return cancellation;
    }

    public void setCancellation(int cancellation) {
        this.cancellation = cancellation;
    }
}
