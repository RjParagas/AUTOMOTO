package com.example.automoto.Model;

public class UserProfile {

    public String name, username, email, phoneNo, image, address, Status;

    public UserProfile() {

    }

    public UserProfile(String name, String email, String phoneNo, String username, String image, String address, String Status) {

        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.username = username;
        this.image = image;
        this.address = address;
        this.Status = Status;
    }
}
