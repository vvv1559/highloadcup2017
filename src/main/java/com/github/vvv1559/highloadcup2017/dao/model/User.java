package com.github.vvv1559.highloadcup2017.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class User implements Entity {

    private Integer id;

    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private Character gender;

    @SerializedName("birth_date")
    private Integer birthDateTimestamp;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void update(Entity entity) {
        if (!entity.getClass().isAssignableFrom(User.class)) {
            throw new IllegalArgumentException();
        }

        User patch = (User) entity;

        if (patch.email != null) {
            email = patch.email;
        }

        if (patch.firstName != null) {
            firstName = patch.firstName;
        }

        if (patch.lastName != null) {
            lastName = patch.lastName;
        }

        if (patch.gender != null) {
            gender = patch.gender;
        }

        if (patch.birthDateTimestamp != null) {
            birthDateTimestamp = patch.birthDateTimestamp;
        }
    }

    @Override
    public boolean allFieldsFilled() {
        return email != null && firstName != null && lastName != null && gender != null && birthDateTimestamp != null;
    }

    public void setId(Integer id) {
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

    public Character getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    @JsonProperty("birth_date")
    public Integer getBirthDateTimestamp() {
        return birthDateTimestamp;
    }

    @JsonProperty("birth_date")
    public void setBirthDateTimestamp(Integer birthDateTimestamp) {
        this.birthDateTimestamp = birthDateTimestamp;
    }
}
