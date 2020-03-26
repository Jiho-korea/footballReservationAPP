package com.example.footballreservationapp;

public class Reservation {
    int serial_number;
    int sid;
    String name;
    int people;
    String startTime;
    String endTime;
    String subject;
    String phone;
    String date;
    int status_code;
    String canclereason;

    public Reservation(int serial_number, int sid, String name, int people, String startTime, String endTime, String subject, String phone, String date, int status_code, String canclereason) {
        this.serial_number = serial_number;
        this.sid = sid;
        this.name = name;
        this.people = people;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.phone = phone;
        this.date = date;
        this.status_code = status_code;
        this.canclereason = canclereason;
    }

    public Reservation() {
    }

    public int getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(int serial_number) {
        this.serial_number = serial_number;
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

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getCanclereason() {
        return canclereason;
    }

    public void setCanclereason(String canclereason) {
        this.canclereason = canclereason;
    }
}
