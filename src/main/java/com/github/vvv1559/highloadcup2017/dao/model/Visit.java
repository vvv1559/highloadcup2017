package com.github.vvv1559.highloadcup2017.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Visit implements Entity {
    private Integer id;

    private Integer location;

    private Integer user;

    @SerializedName("visited_at")
    private Integer visitedAtTimestamp;

    private Integer mark;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    @JsonProperty("visited_at")
    public Integer getVisitedAtTimestamp() {
        return visitedAtTimestamp;
    }

    @JsonProperty("visited_at")
    public void setVisitedAtTimestamp(Integer visitedAtTimestamp) {
        this.visitedAtTimestamp = visitedAtTimestamp;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }
}
