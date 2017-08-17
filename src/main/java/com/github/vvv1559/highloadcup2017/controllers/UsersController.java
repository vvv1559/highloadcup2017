package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.MetaDao;
import com.github.vvv1559.highloadcup2017.dao.model.User;
import com.github.vvv1559.highloadcup2017.dao.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController implements EntityController<User> {

    private final MetaDao metaDao;

    @Autowired
    public UsersController(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    @Override
    public User getEntity(@PathVariable int id) {
        return metaDao.getUser(id);
    }

    @Override
    public ResponseEntity newEntity(@RequestBody User user) {
        metaDao.newUser(user);
        return EntityController.emptyJsonResponse();
    }

    @Override
    public ResponseEntity updateEntity(@PathVariable int id, @RequestBody User user) {
        metaDao.updateUser(id, user);
        return EntityController.emptyJsonResponse();
    }

    @GetMapping("{id}/visits")
    public VisitsResponse getUserVisits(@PathVariable int id) {
        return new VisitsResponse(metaDao.getUserVisits(id));
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
