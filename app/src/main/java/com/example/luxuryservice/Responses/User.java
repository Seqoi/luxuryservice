package com.example.luxuryservice.Responses;

import com.google.gson.annotations.SerializedName;

public class User {

    private int id;
    private String firstname;
    private String lastname;
    private String middlename;
    @SerializedName("bith")
    private String birth;
    private String pol;
    private String image;

    public User(int id, String firstname, String lastname, String middlename, String birth, String pol, String image) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.birth = birth;
        this.pol = pol;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getBirth() {
        return birth;
    }

    public String getPol() {
        return pol;
    }

    public String getImage() {
        return image;
    }
}
