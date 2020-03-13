package ru.otus.core.dao;

import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface Dao<T> {

    Optional<T> findById(long id);

    long save(T object);

    SessionManager getSessionManager();
}
