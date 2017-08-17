package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visits/{id}")
public class VisitsController implements EntityController<Visit> {

    private final MetaDao metaDao;

    @Autowired
    public VisitsController(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    @Override
    @GetMapping
    @ResponseStatus
    public Visit getEntity(@PathVariable int id) {
        return metaDao.getVisit(id);
    }

    @Override
    @PostMapping
    public void newEntity(@PathVariable int id, @RequestBody Visit visit) {
        metaDao.newVisit(id, visit);
    }

    @Override
    @PostMapping("new")
    public void updateEntity(@PathVariable int id, @RequestBody Visit visit) {
        metaDao.updateVisit(id, visit);
    }
}
