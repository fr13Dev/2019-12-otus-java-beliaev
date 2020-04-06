package ru.otus.dao;

import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    <T> T insert(User user);

    Optional<User> findByLogin(String login);

    Optional<List<User>> findAll();
}
