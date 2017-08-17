package com.github.vvv1559.highloadcup2017.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class User implements Entity {

    private int id;

    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private char gender;

    @SerializedName("birth_date")
    private int birthDateTimestamp;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    @JsonProperty("birth_date")
    public int getBirthDateTimestamp() {
        return birthDateTimestamp;
    }

    @JsonProperty("birth_date")
    public void setBirthDateTimestamp(int birthDateTimestamp) {
        this.birthDateTimestamp = birthDateTimestamp;
    }
}
