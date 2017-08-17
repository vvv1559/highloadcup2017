package com.github.vvv1559.highloadcup2017.controllers;

import com.github.vvv1559.highloadcup2017.dao.model.Entity;

interface EntityController<T extends Entity> {

    T getEntity(int id);

    void newEntity(int id, T entity);

    void updateEntity(int id, T entity);

}
