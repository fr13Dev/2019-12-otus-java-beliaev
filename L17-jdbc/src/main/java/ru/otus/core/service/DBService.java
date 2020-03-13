package ru.otus.core.service;

import java.util.Optional;

public interface DBService<T> {

    long save(T user);

    Optional<T> getById(long id);
}
