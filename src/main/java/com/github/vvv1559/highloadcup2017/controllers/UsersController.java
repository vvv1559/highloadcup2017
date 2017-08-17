package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.User;
import com.github.vvv1559.highloadcup2017.dao.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController implements EntityController<User> {

    private final MetaDao metaDao;

    @Autowired
    public UsersController(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    @Override
    public String getEntity(@PathVariable int id) {
        return GSON.toJson(metaDao.getUser(id));
    }

    @Override
    public String newEntity(@RequestBody User user) {
        metaDao.newUser(user);
        return EntityController.EMPTY_RESPONSE;
    }

    @Override
    public String updateEntity(@PathVariable int id, @RequestBody User user) {
        metaDao.updateUser(id, user);
        return EntityController.EMPTY_RESPONSE;
    }

    @GetMapping("{id}/visits")
    public String getUserVisits(@PathVariable int id) {
        return GSON.toJson(new VisitsResponse(metaDao.getUserVisits(id)));
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
