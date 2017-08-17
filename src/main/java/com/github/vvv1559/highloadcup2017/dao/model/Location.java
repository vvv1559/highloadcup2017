package com.github.vvv1559.highloadcup2017.dao.model;

public class Location implements Entity {
    private Integer id;

    private String place;

    private String country;

    private String city;

    private Integer distance;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void update(Entity entity) {

        if (!entity.getClass().isAssignableFrom(Location.class)) {
            throw new IllegalArgumentException();
        }

        Location patch = (Location) entity;

        if (patch.place != null) {
            place = patch.place;
        }

        if (patch.country != null) {
            country = patch.country;
        }

        if (patch.city != null) {
            city = patch.city;
        }

        if (patch.distance != null) {
            distance = patch.distance;
        }
    }

    @Override
    public boolean allFieldsFilled() {
        return place != null && country != null && city != null && distance != null;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
