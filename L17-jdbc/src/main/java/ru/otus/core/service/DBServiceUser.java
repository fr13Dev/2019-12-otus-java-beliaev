package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long save(User user);

    Optional<User> getById(long id);
}
