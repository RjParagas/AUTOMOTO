package com.example.automoto.Model;

public class User {

    public String name, username, email,address, phoneNo, latitude, longitude, date, week, month, year, Status;

    public User(){

    }

    public User(String name, String email, String address, String phoneNo, String username, String latitude, String longitude, String date, String week,
                String month, String year, String Status){

        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNo = phoneNo;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.week = week;
        this.month = month;
        this.year = year;
        this.Status = Status;
    }



}
