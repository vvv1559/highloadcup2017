package com.github.vvv1559.highloadcup2017.dao;

import com.github.vvv1559.highloadcup2017.dao.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        return getStorage(entityType).get(id);
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

    public List<Visit> getUserVisits(int userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException();
        }

        return visits.values().stream().filter(visit -> visit.getUser() == userId).collect(Collectors.toList());
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
}
