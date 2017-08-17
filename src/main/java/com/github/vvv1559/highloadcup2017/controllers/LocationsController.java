package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.Location;
import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@RestController
@RequestMapping("/locations")
public class LocationsController implements EntityController<Location> {

    private final MetaDao metaDao;
    private final ThreadLocal<DecimalFormat> formatThreadLocal;

    @Autowired
    public LocationsController(MetaDao metaDao) {
        this.metaDao = metaDao;
        this.formatThreadLocal = ThreadLocal.withInitial(() -> new DecimalFormat("0.0####"));
    }

    @Override
    public Location getEntity(@PathVariable int id) {
        return metaDao.getLocation(id);
    }

    @Override
    public ResponseEntity newEntity(@PathVariable int id, @RequestBody Location location) {
        metaDao.newLocation(id, location);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity updateEntity(@PathVariable int id, @RequestBody Location location) {
        metaDao.updateLocation(id, location);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{id}/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    public String locationAvg(@PathVariable int id) {
        return "{\"avg\":" + formatThreadLocal.get().format(metaDao.getAllLocationsAvg(id)) + "}";
    }
}
