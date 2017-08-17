package com.github.vvv1559.highloadcup2017.dao.model;

public interface Entity {
    Integer getId();

    void update(Entity entity);

    boolean allFieldsFilled();
}
