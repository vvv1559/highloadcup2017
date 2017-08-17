package com.github.vvv1559.highloadcup2017.dao.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MetaDao {
    private Map<Integer, User> users = new ConcurrentHashMap<>();
    private Map<Integer, Location> locations = new ConcurrentHashMap<>();
    private Map<Integer, Visit> visits = new ConcurrentHashMap<>();

    private <T> T getEntity(int id, Map<Integer, T> storage) {
        T entity = storage.get(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    private <T> void newEntity(int id, T entity, Map<Integer, T> storage) {
        storage.put(id, entity);
    }

    private <T> void updateEntity(int id, T entity, Map<Integer, T> storage) {
        if (!storage.containsKey(id)) {
            throw new EntityNotFoundException();
        }

        storage.get(id);
    }

    public User getUser(int userId) {
        return getEntity(userId, users);
    }

    public void newUser(int userId, User user) {
        newEntity(userId, user, users);
    }

    public void updateUser(int userId, User user) {
        updateEntity(userId, user, users);
    }

    public List<Visit> getUserVisits(int userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException();
        }

        return visits.values().stream().filter(visit -> visit.getUser() == userId).collect(Collectors.toList());
    }

    public Location getLocation(int locationId) {
        return getEntity(locationId, locations);
    }

    public double getAllLocationsAvg(int locationId) {
        if (!locations.containsKey(locationId)) {
            throw new EntityNotFoundException();
        }
        return visits.values().stream()
            .filter(visit -> visit.getLocation() == locationId)
            .map(Visit::getMark)
            .mapToDouble(Byte::floatValue).average().orElse(0.0d);
    }

    public void newLocation(int locationId, Location location) {
        newEntity(locationId, location, locations);
    }

    public void updateLocation(int locationId, Location location) {
        updateEntity(locationId, location, locations);
    }

    public Visit getVisit(int visitId) {
        return getEntity(visitId, visits);
    }

    public void newVisit(int visitId, Visit visit) {
        newEntity(visitId, visit, visits);
    }

    public void updateVisit(int visitId, Visit visit) {
        updateEntity(visitId, visit, visits);
    }
}
