package com.github.vvv1559.highloadcup2017.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.vvv1559.highloadcup2017.dao.model.Entity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

interface EntityController<T extends Entity> {

    @GetMapping("{id}")
    String getEntity(int id);

    @PostMapping("new")
    String newEntity(T entity);

    @PostMapping("{id}")
    String updateEntity(int id, T entity);

    static ResponseEntity emptyJsonResponse() {
        return new ResponseEntity<>(new EmptyJsonResponse(), HttpStatus.OK);
    }

    @JsonSerialize
    class EmptyJsonResponse {
    }
}
