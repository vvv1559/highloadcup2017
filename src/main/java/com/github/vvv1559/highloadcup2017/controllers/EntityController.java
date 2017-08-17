package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.Entity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

interface EntityController<T extends Entity> {

    @GetMapping("{id}")
    T getEntity(int id);

    @PostMapping("new")
    ResponseEntity newEntity(int id, T entity);

    @PostMapping("{id}")
    ResponseEntity updateEntity(int id, T entity);

}
