package com.github.vvv1559.highloadcup2017.dao.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFoundException extends RuntimeException {
}
