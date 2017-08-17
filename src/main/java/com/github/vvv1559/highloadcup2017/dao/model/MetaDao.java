package com.github.vvv1559.highloadcup2017.dao.model;

import com.google.common.base.Preconditions;
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

    private static void checkNotNull(Object parameter) {
        if (parameter == null) {
            throw new ValidationException();
        }
    }
    
    private void validateUser(User user) {
        MetaDao.checkNotNull(user.getId());
        MetaDao.checkNotNull(user.getGender());
        MetaDao.checkNotNull(user.getEmail());
        MetaDao.checkNotNull(user.getFirstName());
        MetaDao.checkNotNull(user.getLastName());
        MetaDao.checkNotNull(user.getBirthDateTimestamp());
    }

    private void validateLocation(Location location) {
        MetaDao.checkNotNull(location.getId());
        MetaDao.checkNotNull(location.getCity());
        MetaDao.checkNotNull(location.getCountry());
        MetaDao.checkNotNull(location.getDistance());
        MetaDao.checkNotNull(location.getPlace());

    }

    private void validateVisit(Visit visit) {
        MetaDao.checkNotNull(visit.getId());
        MetaDao.checkNotNull(visit.getLocation());
        MetaDao.checkNotNull(visit.getMark());
        MetaDao.checkNotNull(visit.getUser());
        MetaDao.checkNotNull(visit.getVisitedAtTimestamp());
    }

    private <T> T getEntity(int id, Map<Integer, T> storage) {
        T entity = storage.get(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    private <T extends Entity> void newEntity(T entity, Map<Integer, T> storage) {
        storage.put(entity.getId(), entity);
    }

    private <T> void updateEntity(int id, T entity, Map<Integer, T> storage) {
        if (!storage.containsKey(id)) {
            throw new EntityNotFoundException();
        }

        storage.put(id, entity);
    }

    public User getUser(int userId) {
        return getEntity(userId, users);
    }

    public void newUser(User user) {
        validateUser(user);
        newEntity(user, users);
    }

    public void updateUser(int userId, User user) {
        validateUser(user);
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
            .mapToDouble(Integer::floatValue).average().orElse(0.0d);
    }

    public void newLocation(Location location) {
        validateLocation(location);
        newEntity(location, locations);
    }

    public void updateLocation(int locationId, Location location) {
        validateLocation(location);
        updateEntity(locationId, location, locations);
    }

    public Visit getVisit(int visitId) {
        return getEntity(visitId, visits);
    }

    public void newVisit(Visit visit) {
        validateVisit(visit);
        newEntity(visit, visits);
    }

    public void updateVisit(int visitId, Visit visit) {
        validateVisit(visit);
        updateEntity(visitId, visit, visits);
    }
}
