package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visits")
public class VisitsController implements EntityController<Visit> {

    private final MetaDao metaDao;

    @Autowired
    public VisitsController(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    @Override
    public Visit getEntity(@PathVariable int id) {
        return metaDao.getVisit(id);
    }

    @Override
    public ResponseEntity updateEntity(@PathVariable int id, @RequestBody Visit visit) {
        metaDao.updateVisit(id, visit);
        return EntityController.emptyJsonResponse();
    }

    @Override
    public ResponseEntity newEntity(@RequestBody Visit visit) {
        metaDao.newVisit(visit);
        return EntityController.emptyJsonResponse();
    }
}
