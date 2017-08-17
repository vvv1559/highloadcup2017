package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.User;
import com.github.vvv1559.highloadcup2017.dao.model.Visit;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController implements EntityController<User> {

    private final MetaDao metaDao;
    private final Gson gson = new Gson();


    @Autowired
    public UsersController(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    @Override
    public String getEntity(@PathVariable int id) {
        return gson.toJson(metaDao.getUser(id));
    }

    @Override
    public String newEntity(@RequestBody User user) {
        metaDao.newUser(user);
        return gson.toJson(EntityController.emptyJsonResponse());
    }

    @Override
    public String updateEntity(@PathVariable int id, @RequestBody User user) {
        metaDao.updateUser(id, user);
        return gson.toJson(EntityController.emptyJsonResponse());
    }

    @GetMapping("{id}/visits")
    public String getUserVisits(@PathVariable int id) {
        return gson.toJson(new VisitsResponse(metaDao.getUserVisits(id)));
    }

    private class VisitsResponse {
        private final List<Visit> visits;

        private VisitsResponse(List<Visit> visits) {
            this.visits = visits;
        }

        public List<Visit> getVisits() {
            return visits;
        }
    }
}
