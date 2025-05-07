package com.example.contactbook;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private String phone;
    private String email;

    public Contact( String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @NonNull
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
