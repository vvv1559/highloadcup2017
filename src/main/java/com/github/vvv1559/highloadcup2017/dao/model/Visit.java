package com.github.vvv1559.highloadcup2017.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Visit implements Entity {
    private int id;

    private int location;

    private int user;

    @SerializedName("visited_at")
    private int visitedAtTimestamp;

    private byte mark;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @JsonProperty("visited_at")
    public int getVisitedAtTimestamp() {
        return visitedAtTimestamp;
    }

    @JsonProperty("visited_at")
    public void setVisitedAtTimestamp(int visitedAtTimestamp) {
        this.visitedAtTimestamp = visitedAtTimestamp;
    }

    public byte getMark() {
        return mark;
    }

    public void setMark(byte mark) {
        this.mark = mark;
    }
}
