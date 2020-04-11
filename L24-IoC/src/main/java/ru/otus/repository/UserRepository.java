package ru.otus.repository;

import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    <T> T insert(User user);

    Optional<User> findByLogin(String login);

    Optional<List<User>> findAll();
}
