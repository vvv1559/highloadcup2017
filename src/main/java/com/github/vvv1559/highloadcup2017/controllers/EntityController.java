package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.Entity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

interface EntityController<T extends Entity> {

    Gson GSON = new Gson();
    String EMPTY_RESPONSE = GSON.toJson(new Object());

    @GetMapping("{id}")
    String getEntity(int id);

    @PostMapping("new")
    String newEntity(T entity);

    @PostMapping("{id}")
    String updateEntity(int id, T entity);
}
