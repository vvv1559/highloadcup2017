package com.github.vvv1559.highloadcup2017.dao;

import com.github.vvv1559.highloadcup2017.dao.model.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MetaDao {
    private Map<Integer, User> users = new ConcurrentHashMap<>();
    private Map<Integer, Location> locations = new ConcurrentHashMap<>();
    private Map<Integer, Visit> visits = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private <T extends Entity> Map<Integer, T> getStorage(EntityType entityType) {
        Map<Integer, ? extends Entity> storage;
        switch (entityType) {
            case users:
                storage = users;
                break;
            case locations:
                storage = locations;
                break;
            case visits:
                storage = visits;
                break;
            default:
                throw new NoSuchElementException();
        }

        return (Map<Integer, T>) storage;
    }

    public Entity getEntity(EntityType entityType, int id) {
        Entity entity = getStorage(entityType).get(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    public <T extends Entity> void newEntity(EntityType entityType, T entity) {
        if (!entity.allFieldsFilled()) {
            throw new ValidationException();
        }

        getStorage(entityType).put(entity.getId(), entity);
    }

    public void updateEntity(EntityType entityType, int id, Entity entity) {
        Map<Integer, ? extends Entity> storage = getStorage(entityType);

        Entity prevEntityState = storage.get(id);
        if (prevEntityState == null) {
            throw new EntityNotFoundException();
        }

        prevEntityState.update(entity);
    }

    public List<VisitInfo> getUserVisits(int userId, Integer fromDate, Integer toDate, String country, Integer toDistance) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException();
        }

        if (fromDate != null && toDate != null && fromDate > toDate) {
            throw new ValidationException();
        }

        Set<Integer> locationsFilter;
        if (country == null) {
            locationsFilter = null;
        } else {
            locationsFilter = locations.values().stream()
                .filter(l -> l.getCountry().equals(country))
                .map(Location::getId)
                .collect(Collectors.toSet());
        }

        Stream<Visit> visitStream = visits.values().stream().filter(visit -> visit.getUser() == userId);

        if (fromDate != null) visitStream = visitStream.filter(v -> v.getVisitedAtTimestamp() > fromDate);
        if (toDate != null) visitStream = visitStream.filter(v -> v.getVisitedAtTimestamp() < toDate);
        if (locationsFilter != null) visitStream = visitStream.filter(v -> locationsFilter.contains(v.getId()));
        if (toDistance != null) visitStream = visitStream.filter(v -> v.getLocation() < toDistance);

        return visitStream
            .sorted(Comparator.comparingInt(Visit::getVisitedAtTimestamp))
            .map(visit -> new VisitInfo(visit, locations.get(visit.getLocation()).getPlace()))
            .collect(Collectors.toList());
    }

    public double getAllLocationsAvg(int locationId, Integer fromDate, Integer toDate,
                                     Integer fromAge, Integer toAge, Character gender) {

        if (!locations.containsKey(locationId)) {
            throw new EntityNotFoundException();
        }

        Stream<Visit> visitStream = visits.values().stream()
            .filter(visit -> visit.getLocation() == locationId);

        if (fromDate != null) visitStream = visitStream.filter(v -> v.getVisitedAtTimestamp() > fromDate);
        if (toDate != null) visitStream = visitStream.filter(v -> v.getVisitedAtTimestamp() < toDate);
        final Set<Integer> usersFilter;
        if (fromAge == null && toAge == null && gender == null) {
            usersFilter = null;
        } else {
            Stream<User> userStream = users.values().stream();
            if (fromAge != null) {
                userStream = userStream.filter(u -> getAge(u) > fromAge);
            }

            if (toAge != null) {
                userStream = userStream.filter(u -> getAge(u) < toAge);
            }

            if (gender != null) {
                userStream = userStream.filter(u -> gender.equals(u.getGender()));
            }

            usersFilter = userStream.map(User::getId).collect(Collectors.toSet());
        }

        if (usersFilter != null) {
            visitStream = visitStream.filter(v -> usersFilter.contains(v.getUser()));
        }

        return visitStream
            .map(Visit::getMark)
            .mapToDouble(Integer::floatValue)
            .average()
            .orElse(0.0d);
    }

    private int getAge(User u) {
        LocalDate birthDay = LocalDate.from(new Timestamp(u.getBirthDateTimestamp()).toLocalDateTime());
        LocalDate today = LocalDate.now();
        return Period.between(birthDay, today).getYears();

    }

    public static class VisitInfo {
        int mark;
        int visited_at;
        String place;

        private VisitInfo(Visit visit, String place) {
            this.mark = visit.getMark();
            this.visited_at = visit.getVisitedAtTimestamp();
            this.place = place;
        }
    }
}
