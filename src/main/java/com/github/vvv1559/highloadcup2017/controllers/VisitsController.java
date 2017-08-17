package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.Visit;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/visits", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitsController implements EntityController<Visit> {

    private final MetaDao metaDao;
    private final Gson gson = new Gson();

    @Autowired
    public VisitsController(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    @Override
    public String getEntity(@PathVariable int id) {
        return gson.toJson(metaDao.getVisit(id));
    }

    @Override
    public String updateEntity(@PathVariable int id, @RequestBody Visit visit) {
        metaDao.updateVisit(id, visit);
        return gson.toJson(EntityController.emptyJsonResponse());
    }

    @Override
    public String newEntity(@RequestBody Visit visit) {
        metaDao.newVisit(visit);
        return gson.toJson(EntityController.emptyJsonResponse());
    }
}
