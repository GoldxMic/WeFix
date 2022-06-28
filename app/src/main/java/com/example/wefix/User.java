package com.example.wefix;

public class User {

    public String fullname, email, phone, address;

    public User(){

    }

    public User(String fullname, String email, String phone, String address){
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }
}
