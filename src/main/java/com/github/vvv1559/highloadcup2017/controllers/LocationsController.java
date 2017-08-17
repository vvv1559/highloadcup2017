package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.Location;
import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@RestController
@RequestMapping(value = "/locations", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationsController implements EntityController<Location> {

    private final MetaDao metaDao;
    private final ThreadLocal<DecimalFormat> formatThreadLocal;

    @Autowired
    public LocationsController(MetaDao metaDao) {
        this.metaDao = metaDao;
        this.formatThreadLocal = ThreadLocal.withInitial(() -> new DecimalFormat("0.0####"));
    }

    @Override
    public String getEntity(@PathVariable int id) {
        return GSON.toJson(metaDao.getLocation(id));
    }

    @Override
    public String newEntity(@RequestBody Location location) {
        metaDao.newLocation(location);
        return EntityController.EMPTY_RESPONSE;
    }

    @Override
    public String updateEntity(@PathVariable int id, @RequestBody Location location) {
        metaDao.updateLocation(id, location);
        return EntityController.EMPTY_RESPONSE;
    }

    @GetMapping(value = "{id}/avg")
    public String locationAvg(@PathVariable int id) {
        return "{\"avg\":" + formatThreadLocal.get().format(metaDao.getAllLocationsAvg(id)) + "}";
    }
}
