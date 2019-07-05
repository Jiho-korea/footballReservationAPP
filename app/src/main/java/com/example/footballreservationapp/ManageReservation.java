package com.example.footballreservationapp;

public class ManageReservation {
    int sid;
    String name;
    String date;
    String subject;
    String phone;
    String starttime;
    String endtime;
    int people;
    String reservationday;
    int approval;

    public ManageReservation(int sid, String name, String date, String subject, String phone, String starttime, String endtime, int people, String reservationday, int approval) {
        this.sid = sid;
        this.name = name;
        this.date = date;
        this.subject = subject;
        this.phone = phone;
        this.starttime = starttime;
        this.endtime = endtime;
        this.people = people;
        this.reservationday = reservationday;
        this.approval = approval;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
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
