package com.example.luxuryservice.Responses;

import com.google.gson.annotations.SerializedName;

public class UpdateUser {
    private String firstname;
    private String lastname;
    private String middlename;
    @SerializedName("bith")
    private String birth;
    private String pol;

    public UpdateUser(String firstname, String lastname, String middlename, String birth, String pol) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.birth = birth;
        this.pol = pol;
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
}
